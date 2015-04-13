/* Cymbal Compiler Visitor.
 *  File: OctaveCheckVisitor.java
	Authors: Harry Bartlett, Terrence Tan, Tyler Harley
	Date: 4/27/2014
	
	Visitor to traverse the Tree of Cymbal's grammar
	as outlined in the design document
	Traverses the AST to make sure that octaveBlocks are
 	declared with appropriate values
  
*/
package visitor;

import ast.*;
import util.*;

import java.util.Iterator;

/**
 *  visitor class for traversing the AST
 *  @author Harry Bartlett, Terrence Tan, Tyler Harley
 *  Traverses the AST to make sure that octaveBlocks are
 *  declared with appropriate values
 */
public class OctaveCheckVisitor extends Visitor {
	
	/** The error handler */
	private ErrorHandler errorHandler;
	
	boolean block = false;
	
	/**current file */
	private File file;
	
	//constructor
	public OctaveCheckVisitor(File file, ErrorHandler errorHandler){
		this.errorHandler = errorHandler;
		this.file = file;
	}
	
	public ErrorHandler checkOctaveBlocks(File file){
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
		block = true;
    	node.getOctaveValue().accept(this);
    	block = false;

    	
        return null;
    }
	/**
     * Stop traversing at a NoteExpr node
     *
     * @param node the NoteExpr node
     * @return result of the visit
     */
	 public Object visit(NoteExpr node) {
	    	return null;
	    }
	
	 /**
	     * Visit a OctaveModVal
	     *
	     * @param node the OctaveModVal node
	     * @return result of the visit
	     */
    public Object visit(OctaveModVal node) {
    	if(block == true){
    	errorHandler.register(
    			errorHandler.SEMANT_ERROR, 
				null, 
				node.getLineNum(), 
				"invalid Octave Mod value in Octave Block Declaration");
    	}
    	return null;
    }
	
}
