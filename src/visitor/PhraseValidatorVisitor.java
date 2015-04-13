/* Cymbal Compiler Visitor.
 * 	File: PhraseValidatorVisitor.java
	Authors: Harry Bartlett, Terrence Tan, Tyler Harley
	Date: 4/27/2014
	
	Visitor to traverse the Tree of Cymbal's grammar
	as outlined in the design document
  
*/

package visitor;

import ast.*;

import java.util.*;

import util.ErrorHandler;

/**
 * Abstract visitor class for traversing the AST
 */
public class PhraseValidatorVisitor extends Visitor {
	
	/** The error handler */
	private ErrorHandler errorHandler;
	
	/**List containing names of all phrases */
	private List<String> phraseNames = new ArrayList<String>();
	
	/**current file */
	private File file;
	

	
	//constructor
		public PhraseValidatorVisitor(File file, ErrorHandler errorHandler){
			this.errorHandler = errorHandler;
			this.file = file;
		}
		
		public ErrorHandler validatePhraseNames(File file){
			file.accept(this);	
			return errorHandler;
		}
	
    
    /**
     * Visit a phrase node
     *
     * @param node the phrase node
     * @return result of the visit
     */
    public Object visit(Phrase node) {
    	if( !phraseNames.contains(node.getPhraseName())){
        	phraseNames.add(node.getPhraseName());
    	}
    	else{
    		errorHandler.register(
        			errorHandler.SEMANT_ERROR, 
    				null, 
    				node.getLineNum(), 
    				"Duplicate Phrase Name: " + node.getPhraseName());
    	}
        node.getExprList().accept(this);
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
     * Visit a InstrumentBlock
     *
     * @param node the InstrumentBlock node
     * @return result of the visit
     */
    public Object visit(InstrumentBlock node) {
    	if (node.getExprList() != null){
    		node.getExprList().accept(this);
    	}
    	
        return null;
    }

    /**
     * Visit a string constant expression node
     *
     * @param node the string constant expression node
     * @return result of the visit
     */
    public Object visit(ConstStringExpr node) {
    	if(!phraseNames.contains(node.getConstant())){
    		errorHandler.register(
        			errorHandler.SEMANT_ERROR, 
    				null, 
    				node.getLineNum(), 
    				"Phrase: " + node.getConstant() + " is undeclared");
    	}
        return null;
    }
}
