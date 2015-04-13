/* Cymbal Compiler Visitor.
 *  File: Visitor.java
	Authors: Harry Bartlett, Terrence Tan, Tyler Harley
	Date: 4/27/2014
	
	Visitor to traverse the Tree of Cymbal's grammar
	as outlined in the design document
  
*/

package visitor;

import ast.*;

import java.util.Iterator;

/**
 * Abstract visitor class for traversing the AST
 */
public abstract class Visitor {
    /**
     * Visit an AST node (should never be called)
     *
     * @param node the AST node
     * @return result of the visit
     */
    public Object visit(ASTNode node) {
        throw new RuntimeException("This visitor method should not be called (node is abstract)");
    }

    /**
     * Visit a list node (should never be called)
     *
     * @param node the list node
     * @return result of the visit
     */
    public Object visit(ListNode node) {
        throw new RuntimeException("This visitor method should not be called (node is abstract)");
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
        node.getExprList().accept(this);
        return null;
    }


    /**
     * Visit a song node
     *
     * @param node the song node
     * @return result of the visit
     */
    public Object visit(Song node) {
        node.getExprList().accept(this);
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
     * Visit a NoteLiteral
     *
     * @param node the NoteLiteral node
     * @return result of the visit
     */
    public Object visit(NoteLiteral node) {
    	node.getNote().accept(this);
       	if(node.getAccidental() != null){
    		node.getAccidental().accept(this);
    	}
        if (node.getOctave() != null){
        	node.getOctave().accept(this);
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
    	node.getPhraseName().accept(this);
        return null;
    }
    /**
     * Visit a BlockExpr
     *
     * @param node the BlockExpr node
     * @return result of the visit
     */
    public Object visit(BlockExpr node) {
    	switch(node.getExprType()){
    	case "OctaveBlock": 
    		((OctaveBlock) node).accept(this);
    		break;
    	case "RepeatBlock": 
    		((RepeatBlock) node).accept(this);
    		break;
    	case "VolumeBlock": 
    		((VolumeBlock) node).accept(this);
    		break; 
    	case "TempoBlock": 
    		((TempoBlock) node).accept(this);
    		break;  
    	default:
    		System.out.println("this should never execute");
    		break;
    	}
    	return null;
    }
    
    /**
     * Visit a OcatveBlock
     *
     * @param node the OcatveBlock node
     * @return result of the visit
     */
    public Object visit(OctaveBlock node) {
    	node.getOctaveValue().accept(this);
    	if (node.getExprList() != null){
    		node.getExprList().accept(this);
    	}
    	
        return null;
    }
    
    /**
     * Visit a InstrumentBlock
     *
     * @param node the InstrumentBlock node
     * @return result of the visit
     */
    public Object visit(InstrumentBlock node) {
    	node.getInstrument().accept(this);
    	if (node.getExprList() != null){
    		node.getExprList().accept(this);
    	}
    	
        return null;
    }
    
    /**
     * Visit a VolumeBlock
     *
     * @param node the VolumeBlock node
     * @return result of the visit
     */
    public Object visit(VolumeBlock node) {
    	node.getDynamic().accept(this);
    	if (node.getExprList() != null){
    		node.getExprList().accept(this);
    	}
    	
        return null;
    }
    
    /**
     * Visit a TempoBlock
     *
     * @param node the TempoBlock node
     * @return result of the visit
     */
    public Object visit(TempoBlock node) {
    	if (node.getExprList() != null){
    		node.getExprList().accept(this);
    	}
    	
        return null;
    }
    
    /**
     * Visit a VolumeWord
     *
     * @param node the VolumeWord node
     * @return result of the visit
     */
    public Object visit(VolumeWord node) {
    	
    	
        return null;
    }

    
    /**
     * Visit a RepeatBlock
     *
     * @param node the RepeatBlock node
     * @return result of the visit
     */
    public Object visit(RepeatBlock node) {
    	if (node.getExprList() != null){
    		node.getExprList().accept(this);
    	}
    	
        return null;
    }
    /**
     * Visit a OctaveValue
     *
     * @param node the OctaveValue node
     * @return result of the visit
     */
    public Object visit(OctaveValue node) {
    	throw new RuntimeException("This visitor method should not be called (node is abstract)");
    }
    
    /**
     * Visit a OctaveIntConst
     *
     * @param node the OctaveIntConst node
     * @return result of the visit
     */
    public Object visit(OctaveIntConst node) {
    	return null;
    }
    
    /**
     * Visit a OctaveModVal
     *
     * @param node the OctaveModVal node
     * @return result of the visit
     */
    public Object visit(OctaveModVal node) {
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
 
    		
    	return null;
    }
    
    /**
     * Visit a Rest
     *
     * @param node the Rest node
     * @return result of the visit
     */
    public Object visit(Rest node) {
    	return null;
    }
    
    /**
     * Visit a Accidental
     *
     * @param node the Accidental node
     * @return result of the visit
     */
    public Object visit(Accidental node) {
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
        return null;
    }
}
