/* Cymbal Compiler Visitor.
	Authors: Harry Bartlett, Terrence Tan, Tyler Harley
	Date: 4/27/2014
	
	Visitor to traverse the Tree of Cymbal's grammar
	as outlined in the design document
  
*/

package visitor;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Stack;

import codegenmips.MipsSupport;
import ast.*;

public class PhraseCodeGenVisitor extends Visitor {
	
	
	/**The file to be traversed*/
	private File file;
	
	/**Map of lengths for given tempo*/
	private HashMap<String,Integer> lengthMap;
	
	/**Map of offsets into a chromatic scale for each principle note */
	private HashMap<String,Integer> noteMap;
	
	private String[] instumentList = InstrumentValidatorVisitor.INSTRUMENTNAMES;
	
	private MipsSupport assemblySupport;
	
	
	public PhraseCodeGenVisitor(File file, MipsSupport assemblySupport){
		this.file = file;
		this.assemblySupport = assemblySupport;

	}
	
	public Object genPhrases(File file, HashMap<String,Integer> noteMap,
							HashMap<String,Integer> lengthMap){
		
		this.noteMap = noteMap;
		this.lengthMap = lengthMap;
		file.accept(this);	
		return null;
	}
	
	
    /**
     * Visit a File node
     *
     * @param node the file node
     * @return result of the visit
     */
    public Object visit(File node) {
    	
    	if (node.getPhraseList() != null){
    		node.getPhraseList().accept(this);
        }
        
        return null;
    }

    /**
     * Visit a list node of phrases
     *
     * @param node the phrase list node
     * @return result of the visit
     */
    public Object visit(PhraseList node) {
        for (ASTNode aNode : node)
            aNode.accept(this);
        return null;
    }
    
    /**
     * Visit a phrase node
     *
     * @param node the phrase node
     * @return result of the visit
     */
    public Object visit(Phrase node) {
    	assemblySupport.genLabel(node.getPhraseName());
    	//load in specified default value for volume
        node.getExprList().accept(this);
        assemblySupport.genRetn();
        return null;
    }

    /**
     * Visit a RepeatBlock
     *
     * @param node the RepeatBlock node
     * @return result of the visit
     */
    public Object visit(RepeatBlock node) {
    	assemblySupport.genComment("starting repeat");
    	if (node.getExprList() != null){
    		node.getExprList().accept(this);
    		node.getExprList().accept(this);
    	}
    	assemblySupport.genComment("end repeat");
        return null;
    }


    /**
     * Visit a list node of expressions
     *
     * @param node the expression list node
     * @return result of the visit
     */
    public Object visit(ExprList node) {
        for (Iterator it = node.iterator(); it.hasNext(); ){
            ((Expr) it.next()).accept(this);
        }
        return null;
    }

    /**
     * Visit an expression node (should never be called)
     *
     * @param node the expression node
     * @return result of the visit
     */
    public Object visit(Expr node) {
        throw new RuntimeException("This visitor method should not be called (node is abstract)");
    }
    
    
    /**
     * Visit a Note Expression
     *
     * @param node the expression node
     * @return result of the visit
     */
    public Object visit(NoteExpr node) {
        if (node.getPhrase() != null){
        	node.getPhrase().accept(this);
        }
        else if(node.getNoteLiteral() != null){
        	node.getNoteLiteral().accept(this);
        }
        return null;
    }
    
    /**
     * Visit a Note literal
     *
     * @param node the literal node
     * @return result of the visit
     */
    public Object visit(NoteLiteral node) {
    	
    	//get the value in miliseconds of the length string
    	int time = lengthMap.get(node.getLength());
    	//load length in miliseconds
        assemblySupport.genLoadImm("$a1", time);
 
//    	assemblySupport.genLoadWord("$a1", 16, "$sp");
//    	switch(node.getLength()){
//    	case "Eighth":
//    		assemblySupport.genDiv("$a1", "$a1", 2);
//    		break;
//    	case "Quarter":
//    		break;
//    	case "Half":
//    		assemblySupport.genMul("$a1", "$a1", 2);
//    		break;
//    	case "Whole":
//    		assemblySupport.genMul("$a1", "$a1", 4);
//    		break;
//    	default:
//    		System.out.println("this should never execute");
//    	
//    	}
//    	
    		
        //get the string of the note to be played
    	String note = (String) node.getNote().accept(this);

    	
    	
    	//get the octave of the note to be played
    	//if in an octave block with no explicit octave
    	//it will use the octave block value
        if (node.getOctave() != null){
        	//explicit octave
        	node.getOctave().accept(this); 	
        }
        else{
         	assemblySupport.genLoadWord("$t0", 8, "$sp"); 
        }
        
        //If a rest is loaded, Mars sleeps for the required length
        if(note.equals("Rest")){
        	assemblySupport.genComment(node.getLength() + " Rest");
        	assemblySupport.genMove("$a0", "$a1");
        	assemblySupport.genLoadImm("$v0",32);
        	assemblySupport.genSyscall(999);
        	assemblySupport.genLoadImm("$v0",33);
        }
        else{
        
        	
        	//convert the note and its octave to the pitch
        	//desired by MARS
        	assemblySupport.genMul("$t0", "$t0", 12);
        	assemblySupport.genAdd("$t0", "$t0", noteMap.get(note));
        	assemblySupport.genAdd("$a0", "$t0", 9);
        	
        	//if there is a sharp or flat, the pitch is adjusted accordingly
        	if(node.getAccidental() != null){
        		node.getAccidental().accept(this);
        	}
     
        	assemblySupport.genComment("Playing " + note);

      	 	//load a unique no side effect syscall to play the note
        	assemblySupport.genSyscall(999);
        }
        
        return null;
    }
    
    /**
     * Visit a PhraseDispatch
     *
     * @param node the PhraseDispatch node
     * @return result of the visit
     */
    public Object visit(PhraseDispatch node) {
    	//get label name from String Const Expression
    	assemblySupport.genComment("PhraseCall");
    	String phraseLabel = (String) node.getPhraseName().accept(this);
 
    	assemblySupport.genDirCall(phraseLabel);
        return null;
    }
    
    /**
     * Visit a OcatveBlock
     *
     * @param node the OcatveBlock node
     * @return result of the visit
     */
    public Object visit(OctaveBlock node) {
    	blockExprFramePush();
    	assemblySupport.genComment("Octave Block Start ");
    	node.getOctaveValue().accept(this);
    	//overwrite old value in current frame
    	assemblySupport.genStoreWord("$t0", 8, "$sp");
    	
    	
    	//blockOctave = currOctave;
    	if (node.getExprList() != null){
    		node.getExprList().accept(this);
    	
    	}
    	assemblySupport.genComment("Octave Block End");
    	blockExprFramePop();
        return null;
    }
    
    
//    /**
//     * Visit a TempoBlock
//     *
//     * @param node the TempoBlock node
//     * @return result of the visit
//     */
//    public Object visit(TempoBlock node) {
//    	blockExprFramePush();
//    	int tempo = node.getTempo();
//    	tempo = (60000/tempo);
//    	assemblySupport.genLoadImm("$t4", tempo);
//    	assemblySupport.genStoreWord("$t4", 16, "$sp");
//    	
//    	
//    	if (node.getExprList() != null){
//    		node.getExprList().accept(this);
//    	}
//    	blockExprFramePop();
//        return null;
//    }
    
    /**
     * Visit a InstrumentBlock
     *
     * @param node the InstrumentBlock node
     * @return result of the visit
     */
    public Object visit(InstrumentBlock node) {  	
    	blockExprFramePush();
    	assemblySupport.genComment("Instrument Block Start");
    	String instrument = (String)node.getInstrument().accept(this);
    	for(int i=0;i<instumentList.length; i++){
    		if(instumentList[i].equals(instrument)){
    			//load instrument value into register
    			assemblySupport.genLoadImm("$a2",i);
    		}
    	}
    	
    	//overwrite old value in current frame
    	assemblySupport.genStoreWord("$a2", 12, "$sp");
    	if (node.getExprList() != null){
    		node.getExprList().accept(this);
    	}
    	assemblySupport.genComment("Instrument Block End");
    	blockExprFramePop();
        return null;
    }
    
    /**
     * Visit a VolumeBlock
     *
     * @param node the VolumeBlock node
     * @return result of the visit
     */
    public Object visit(VolumeBlock node) {
    	blockExprFramePush();
    	assemblySupport.genComment("Volume Block Start");
    	node.getDynamic().accept(this);
    	
    	//overwrite old value
    	assemblySupport.genStoreWord("$a3", 4, "$sp");
    	
    	
    	if (node.getExprList() != null){
    		node.getExprList().accept(this);
    	}
    	assemblySupport.genComment("Volume Block End");
    	blockExprFramePop();
        return null;
    }
    
    /**
     * Visit a VolumeWord
     *
     * @param node the VolumeWord node
     * @return result of the visit
     */
    public Object visit(VolumeWord node) {
    	
    	switch(node.getDynamic()){
    	case "forte": assemblySupport.genLoadImm("$a3",125);
    				  break;
    	case "mesoforte": assemblySupport.genLoadImm("$a3",100);
		  					break;
    	case "default": assemblySupport.genLoadImm("$a3",75);
    						break;
    	case "mesopiano": assemblySupport.genLoadImm("$a3",50);
    						break;
    	case "piano": assemblySupport.genLoadImm("$a3",25);
    					break; 
		  
    	}
        return null;
    }
    
    /**
     * Visit a OctaveIntConst
     *
     * @param node the OctaveIntConst node
     * @return result of the visit
     */
    public Object visit(OctaveIntConst node) {
    	//octaveStack.push(node.getIntConstant());
    	assemblySupport.genLoadImm("$t0", node.getIntConstant());
    	return null;
    }
    
    /**
     * Visit a OctaveModVal
     *
     * @param node the OctaveModVal node
     * @return result of the visit
     */
    public Object visit(OctaveModVal node) {
    	assemblySupport.genLoadWord("$t0", 8, "$sp");
    	if (node.getModVal().equals("+")){
        	assemblySupport.genAdd("$t0", "$t0", 1);
    	}
    	else{
    		assemblySupport.genAdd("$t0", "$t0", -1);
    	}
    	return null;
    }
    
    /**
     * Visit a Note 
     *
     * @param node the Note node
     * @return result of the visit
     */
    public Object visit(Note node) {
    	
    	
    	if(node.getNote().equals("Rest")){
    		//traverse to rest node
    		((Rest)node).accept(this);
    	}
    		
    	return node.getNote();
    }
    
    /**
     * Visit a Rest
     *
     * @param node the Rest node
     * @return result of the visit
     */
    public Object visit(Rest node) {
    	return node.getRest();
    }
    
    /**
     * Visit a Accidental
     *
     * @param node the Accidental node
     * @return result of the visit
     */
    public Object visit(Accidental node) {
    	if (node.getAccidental().equals("#")){
    		assemblySupport.genAdd("$a0", "$a0", 1);
    	}
    	else{
    		assemblySupport.genAdd("$a0", "$a0", -1);
    	}
    	return null;
    }
    
    
    
    

    /**
     * Visit a constant expression node (should never be called)
     *
     * @param node the constant expression node
     * @return result of the visit
     */
    public Object visit(ConstExpr node) {
        throw new RuntimeException("This visitor method should not be called (node is abstract)");
    }

    /**
     * Visit a string constant expression node
     *
     * @param node the string constant expression node
     * @return result of the visit
     */
    public Object visit(ConstStringExpr node) {
        return node.getConstant();
    }
    
    private void blockExprFramePush(){
    	assemblySupport.genComment("Start of block statement");
    	assemblySupport.genComment("Push on old values ");
    	//push on space for Instrument octave and volume respectively
    	//temp0
    	assemblySupport.genLoadWord("$t4", 16, "$sp");
    	//Instrument
    	assemblySupport.genLoadWord("$t1", 12, "$sp");
    	//Octave
    	assemblySupport.genLoadWord("$t2", 8, "$sp");
    	//Volume
    	assemblySupport.genLoadWord("$t3", 4, "$sp");
    	assemblySupport.genAdd("$sp", "$sp", -16);
    	//store old values
    	assemblySupport.genStoreWord("$t4", 16, "$sp");
    	assemblySupport.genStoreWord("$t1", 12, "$sp");
    	assemblySupport.genStoreWord("$t2", 8, "$sp");
    	assemblySupport.genStoreWord("$t3", 4, "$sp");
    	assemblySupport.genComment("Begin Block Code");
    }
    
    private void blockExprFramePop(){
    	assemblySupport.genComment("End Block Code");
    	//pop off frame
    	assemblySupport.genAdd("$sp", "$sp", 16);
    	//reload Instrument
    	assemblySupport.genLoadWord("$a2",12,"$sp");
    	//reload volume
    	assemblySupport.genLoadWord("$a3",4,"$sp");
    	//reload tempo
    	assemblySupport.genLoadWord("$a1", 16, "$sp");
    	assemblySupport.genComment("reload old values");
  	
    }

}
