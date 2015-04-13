/* Cymbal Compiler 
   Accidental.java
   Modified by: Harry Bartlett, Terrence Tan, Tyler Harley
   Modified: 4/27/2014 
*/
package ast;

import visitor.Visitor;


/**
 * The <tt>Note</tt> class holds the literal string of the note
 * to be played
 *
 * @see ASTNode
 * @see ConstExpr
 */
public class Note extends Expr {


	
	/**
     * Constant value
     */
    protected String note;
    /**
     * Note constructor
     *
     * @param lineNum  source line number corresponding to this AST node
     * @param constant note value (as a String)
     */
    public Note(int lineNum, String note) {
        super(lineNum);
        this.note = note;

        
    }

    
    /**
     * Get the note value
     *
     * @return note value
     */
    public String getNote() {
        return note;
    }
    /**
     * Visitor method
     *
     * @param v visitor object
     * @return result of visiting this node
     * @see visitor.Visitor
     */
    public Object accept(Visitor v) {
        return v.visit(this);
    }
}
