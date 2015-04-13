/* Cymbal Compiler Visitor.
	Authors: Harry Bartlett, Terrence Tan, Tyler Harley
	Date: 4/27/2014
	
	Visitor to traverse the Tree of Cymbal's grammar
	as outlined in the design document
  
*/
package visitor;

import ast.*;
import util.*;

import java.util.Iterator;

/**
 *  visitor class for traversing the AST
 *  @author Harry Bartlett, Terrence Tan, Tyler Harley
 *  Traverses the AST to make sure that octave mods do not
 *  try to extend past the max/min defined octaves
 */
public class OctaveValueCheckVisitor extends Visitor {
	
	/** The error handler */
	private ErrorHandler errorHandler;
	
	/**current file */
	private File file;
	
	/**current octave value */
	private int currOctave = 4;
	
	/**current block octave value */
	private int blockOctave = 4;
	
	//constructor
	public OctaveValueCheckVisitor(File file, ErrorHandler errorHandler){
		this.errorHandler = errorHandler;
		this.file = file;
	}
	
	/**
	 * Returns the error handler with approriate errors registered
	 */
	
	public ErrorHandler checkOctaveValues(File file){
		file.accept(this);	
		return errorHandler;
	}
	
	 /**
     * Visit a OcatveBlock
     *
     * @param node the OcatveBlock node
     * @return result of the visit
     */
	public Object visit(OctaveBlock node) {
    	node.getOctaveValue().accept(this);
    	blockOctave = currOctave;
    	if (node.getExprList() != null){
    		node.getExprList().accept(this);
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
    	currOctave = node.getIntConstant();
    	return null;
    }
	
    /**
     * Visit a OctaveModVal
     *
     * @param node the OctaveModVal node
     * @return result of the visit
     */
    public Object visit(OctaveModVal node) {
    	if (blockOctave == 7 && node.getModVal().equals("+"))
    	errorHandler.register(
    			errorHandler.SEMANT_ERROR, 
				null, 
				node.getLineNum(), 
				"Cannot go above 7th Octave");
    	else if (blockOctave == 0 && node.getModVal().equals("-")){
    		errorHandler.register(
        			errorHandler.SEMANT_ERROR, 
    				null, 
    				node.getLineNum(), 
    				"Cannot go below 0th Octave");
    	}
    	return null;
    }
	
    

	
}
