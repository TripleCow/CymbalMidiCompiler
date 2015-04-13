/* Cymbal Compiler Visitor.
 * 	File: InvalidOctaveModVisitor.java
	Authors: Harry Bartlett, Terrence Tan, Tyler Harley
	Date: 4/27/2014
	
	Visitor to traverse the Tree of Cymbal's grammar
	as outlined in the design document
	
	Traverses the AST to make sure that octave mods are only used in
    Octave Blocks
  
*/
package visitor;

import ast.*;
import util.*;

import java.util.Iterator;

/**
 *  visitor class for traversing the AST
 *  @author Harry Bartlett, Terrence Tan, Tyler Harley
 *  Traverses the AST to make sure that octave mods are only used in
 *  Octave Blocks
 */
public class InvalidOctaveModVisitor extends Visitor {
	
	/** The error handler */
	private ErrorHandler errorHandler;
	
	/**current file */
	private File file;
	
	//constructor
	public InvalidOctaveModVisitor(File file, ErrorHandler errorHandler){
		this.errorHandler = errorHandler;
		this.file = file;
	}
	
	public ErrorHandler checkOctaveMods(File file){
		file.accept(this);	
		return errorHandler;
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
     * Stop traversing at an OcatveBlock
     *
     * @param node the OcatveBlock node
     * @return result of the visit
     */
	public Object visit(OctaveBlock node) {
        return null;
    }
	
	/**
     * Visit a InstrumentBlock
     *
     * @param node the InstrumentBlock node
     * @return result of the visit
     */
    public Object visit(InstrumentBlock node) {
    	return null;
    }
	/**
     * Visit a TempoBlock
     *
     * @param node the InstrumentBlock node
     * @return result of the visit
     */
    public Object visit(TempoBlock node) {
    	return null;
    }
	/**
     * Visit a Note Expression
     *
     * @param node the expression node
     * @return result of the visit
     */
	 public Object visit(NoteExpr node) {
	        if(node.getNoteLiteral() != null){
	        	node.getNoteLiteral().accept(this);
	        }
	        return null;
	    }
	/**
	* Visit a OctaveModVal
	*
	* @param node the OctaveModVal node
	* @return result of the visit
	*/
    public Object visit(OctaveModVal node) {
    	errorHandler.register(
    			errorHandler.SEMANT_ERROR, 
				null, 
				node.getLineNum(), 
				"Octave Modifiers can only be used in octave blocks");
    	return null;
    }
	
}
