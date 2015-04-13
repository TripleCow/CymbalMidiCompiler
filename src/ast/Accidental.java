/* Cymbal Compiler 
   Accidental.java
   Modified by: Harry Bartlett, Terrence Tan, Tyler Harley
   Modified: 4/27/2014
   
   
*/

package ast;

import visitor.Visitor;


/**
 * Node to hold the values of accidentals
 *
 * @see ASTNode
 */
public class Accidental extends ASTNode {
	 /**
     * accidental value
     */
    protected String accidental;

    /**
     * Accidental constructor
     *
     * @param lineNum  source line number corresponding to this AST node
     * @param constant constant value (as a String)
     */
    public Accidental(int lineNum,String accidental) {
        super(lineNum);
        this.accidental = accidental;
        
    }
    /**
     * Get the accidental value
     *
     * @return accidental value
     */
    public String getAccidental() {
        return accidental;
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
