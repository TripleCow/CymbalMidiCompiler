/* Cymbal Compiler Visitor.
 * 	File: CodeGenVisitor.java
	Authors: Harry Bartlett, Terrence Tan, Tyler Harley
	Date: 4/27/2014
	
	Visitor to traverse the Tree of Cymbal's grammar
	as outlined in the design document
	
	Traverses the AST to generate the mips code for desired notes
  
*/

package visitor;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Stack;

import visitor.*;
import ast.*;
import codegenmips.MipsSupport;

/**
 *  visitor class for traversing the AST
 *  @author Harry Bartlett, Terrence Tan, Tyler Harley
 *  Traverses the AST to generate the mips code for desired notes
 */
public class CodeGenVisitor extends Visitor {
	

	
	/**Field to hold the current numerical value of the note to be played */
	private int currNoteInt;
	
	/**The file to be traversed*/
	private File file;
	
	/**Map of lengths for given tempo*/
	private HashMap<String,Integer> lengthMap;
	
	/**Map of offsets into a chromatic scale for each principle note */
	private HashMap<String,Integer> noteMap;
	
	
	private MipsSupport assemblySupport;
	
	private String[] instumentList = InstrumentValidatorVisitor.INSTRUMENTNAMES;
	
	//stacks to keep track of needed values
	private Stack<Integer> volumeStack = new Stack<Integer>();
	private Stack<Integer> octaveStack = new Stack<Integer>();
	private Stack<Integer> instrumentStack = new Stack<Integer>();
	private Stack<Integer> tempoStack = new Stack<Integer>();
	
    /**
     * Visit an AST node (should never be called)
     *
     * @param node the AST node
     * @return result of the visit
     */
	//constructor
		public CodeGenVisitor(File file, MipsSupport assemblySupport){
			this.file = file;
			this.assemblySupport = assemblySupport;
		}
		
		public Object genCode(File file, HashMap<String,Integer> noteMap,
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
    	node.getSong().accept(this);
        
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
        node.getExprList().accept(this);
        assemblySupport.genRetn();
        return null;
    }


    /**
     * Visit a song node
     *
     * @param node the song node
     * @return result of the visit
     */
    public Object visit(Song node) {
    	octaveStack.push(3);
    	volumeStack.push(75);
    	instrumentStack.push(0);
    	tempoStack.push(500);
    	assemblySupport.genComment("Song starts here");
        node.getExprList().accept(this);
        //end program
      	assemblySupport.genLoadImm("$v0", 10 );
      	//load a unique no side effect syscall
      	assemblySupport.genSyscall(999);
        return null;
    }


    /**
     * Visit a list node of expressions
     *
     * @param node the expression list node
     * @return result of the visit
     */
    public Object visit(ExprList node) {
        for (Iterator it = node.iterator(); it.hasNext(); )
            ((Expr) it.next()).accept(this);
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
    	
    	
    	//Static Tempo Map version
        //get the value in miliseconds of the length string
    	int time = lengthMap.get(node.getLength());
    	//load length in miliseconds
        assemblySupport.genLoadImm("$a1", time);
    	
        //Dyanmic tempo Block version
//        switch(node.getLength()){
//    	case "Eighth":
//    		assemblySupport.genLoadImm("$a1", tempoStack.peek()/2);
//    		break;
//    	case "Quarter":
//    		assemblySupport.genLoadImm("$a1", tempoStack.peek());
//    		break;
//    	case "Half":
//    		assemblySupport.genLoadImm("$a1", 2*tempoStack.peek());
//    		break;
//    	case "Whole":
//    		assemblySupport.genLoadImm("$a1", 4*tempoStack.peek());
//    		break;
//    	default:
//    		System.out.println("this should never execute (main code gen)");
//    	
//    	}
        //get the string of the note to be played
    	String note = (String) node.getNote().accept(this);

    	
    	
    	//get the octave of the note to be played
    	//if in an octave block with no explicit octave
    	//it will use the octave block value
    	int octave;
        if (node.getOctave() != null){
        	node.getOctave().accept(this);
        	octave = (int) octaveStack.pop(); 	
        }
        else{
        	octave = (int) octaveStack.peek(); 
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
        	assemblySupport.genComment("Playing " +  
        								node.getLength() + " " + 
        								note + 
        								" in octave " + octave);
        	
        	//convert the note and its octave to the pitch
        	//desired by MARS
        	currNoteInt = 9 + (12*octave) + noteMap.get(note);
        	
        	//if there is a sharp or flat, the pitch is adjusted accordingly
        	if(node.getAccidental() != null){
        		node.getAccidental().accept(this);
        	}
      	 	
      	 	//load pitch
      	 	assemblySupport.genLoadImm("$a0", currNoteInt);
      	 	
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
    	//Push the current tempo onto the stack
    	assemblySupport.genComment("Load tempo/instrument/octave/volume for phrase");
    	assemblySupport.genAdd("$sp", "$sp", -4);
    	assemblySupport.genLoadImm("$t0", tempoStack.peek());
    	assemblySupport.genStoreWord("$t0", 4, "$sp");
    	
    	//push instrument onto the stack
    	assemblySupport.genAdd("$sp", "$sp", -4);
    	assemblySupport.genLoadImm("$t0", instrumentStack.peek());
    	assemblySupport.genStoreWord("$t0", 4, "$sp");
    	
    	//Push the correct value onto the mips stack
    	assemblySupport.genAdd("$sp", "$sp", -4);
    	assemblySupport.genLoadImm("$t0", octaveStack.peek());
    	assemblySupport.genStoreWord("$t0", 4, "$sp");
    	
    	//push volume onto the stack
    	assemblySupport.genAdd("$sp", "$sp", -4);
    	assemblySupport.genLoadImm("$t0", volumeStack.peek());
    	assemblySupport.genStoreWord("$t0", 4, "$sp");
    	//get label name from String Const Expression
    	
    	assemblySupport.genComment("PhraseCall");
    	String phraseLabel = (String) node.getPhraseName().accept(this);
 
    	assemblySupport.genDirCall(phraseLabel);
    	
    	//pop off stack
    	assemblySupport.genAdd("$sp", "$sp", 16);
    	assemblySupport.genComment("Phrase Dispatch End");
        return null;
    }
    
    /**
     * Visit a InstrumentBlock
     *
     * @param node the InstrumentBlock node
     * @return result of the visit
     */
    public Object visit(InstrumentBlock node) {
    	assemblySupport.genComment("Instrument Block Start");
    	String instrument = (String)node.getInstrument().accept(this);
    	for(int i=0;i<instumentList.length; i++){
    		if(instumentList[i].equals(instrument)){
    			instrumentStack.push(i);
    		}
    	}
    	assemblySupport.genLoadImm("$a2", instrumentStack.peek());
    	if (node.getExprList() != null){
    		node.getExprList().accept(this);
    	}
    	instrumentStack.pop();
    	assemblySupport.genLoadImm("$a2", instrumentStack.peek());
    	assemblySupport.genComment("Instrument Block End");
        return null;
    }
    
//    /**
//     * Visit a TempoBlock
//     *
//     * @param node the TempoBlock node
//     * @return result of the visit
//     */
//    public Object visit(TempoBlock node) {
//    	int tempo = node.getTempo();
//    	tempo = (60000/tempo);
//    	tempoStack.push(tempo);
//    	if (node.getExprList() != null){
//    		node.getExprList().accept(this);
//    	}
//    	tempoStack.pop();
//        return null;
//    }
    
    /**
     * Visit a OcatveBlock
     *
     * @param node the OcatveBlock node
     * @return result of the visit
     */
    public Object visit(OctaveBlock node) {
    	assemblySupport.genComment("Octave Block Start");
    	node.getOctaveValue().accept(this);
    	
    	if (node.getExprList() != null){
    		node.getExprList().accept(this);
    	
    	}
    	octaveStack.pop();
    	assemblySupport.genComment("Octave Block End");
        return null;
    }
    
    /**
     * Visit a VolumeBlock
     *
     * @param node the VolumeBlock node
     * @return result of the visit
     */
    public Object visit(VolumeBlock node) {
    	assemblySupport.genComment("Volume Block Start");
    	node.getDynamic().accept(this);
    	assemblySupport.genLoadImm("$a3", volumeStack.peek());
    	if (node.getExprList() != null){
    		node.getExprList().accept(this);
    	}
    	volumeStack.pop();
    	assemblySupport.genLoadImm("$a3", volumeStack.peek());
    	assemblySupport.genComment("Volume Block End");
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
    	case "forte": volumeStack.push(125);
    				  break;
    	case "mesoforte": volumeStack.push(100);
		  					break;
    	case "default": volumeStack.push(75);
    						break;
    	case "mesopiano": volumeStack.push(50);
    						break;
    	case "piano": volumeStack.push(25);
    					break; 
		  
    	}
    	
    	
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
     * Visit a OctaveIntConst
     *
     * @param node the OctaveIntConst node
     * @return result of the visit
     */
    public Object visit(OctaveIntConst node) {
    	octaveStack.push(node.getIntConstant());
    	return null;
    }
    
    /**
     * Visit a OctaveModVal
     *
     * @param node the OctaveModVal node
     * @return result of the visit
     */
    public Object visit(OctaveModVal node) {
    	if (node.getModVal().equals("+")){
    		octaveStack.push(octaveStack.peek()+1);
    	}
    	else{
    		octaveStack.push(octaveStack.peek()-1);
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
    		currNoteInt++;
    		assemblySupport.genComment("with sharp accidental");
    	}
    	else{
    		currNoteInt--;
    		assemblySupport.genComment("with flat accidental");
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
}
