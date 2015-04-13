/* Cymbal Compiler 
   OctaveModVal.java
   Modified by: Harry Bartlett, Terrence Tan, Tyler Harley
   Modified: 4/27/2014 
*/
package ast;

import visitor.Visitor;


/**
 * The <tt>OctaveModVal</tt> class holds the modifiers + or - to
 * play notes more effectively while in an OctaveBlock
 *
 * @see ASTNode
 * @see OctaveValue
 */
public class OctaveModVal extends OctaveValue {
    /**
     * The constant value represented as an int
     */
    private String modConstant;

    /**
     * OctaveModVal constructor
     *
     * @param lineNum  source line number corresponding to this AST node
     * @param constant constant value (as a String)
     */
    public OctaveModVal(int lineNum, String constant) {
        super(lineNum);
        this.modConstant = constant;
    }

    /**
     * Get the constant value
     *
     * @return the constant value
     */
    public String getModVal() {
        return modConstant;
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
