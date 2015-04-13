/* Cymbal Compiler 
   OctaveIntConst.java
   Modified by: Harry Bartlett, Terrence Tan, Tyler Harley
   Modified: 4/28/2014 
*/
package ast;

import visitor.Visitor;


/**
 * The <tt>OctaveIntConst</tt> class contains the integer value of an octave
 * both from NoteLiterals and OctaveBlocks
 *
 * @see ASTNode
 * @see OctaveValue
 */
public class OctaveIntConst extends OctaveValue {
    /**
     * The constant value represented as an int
     */
    private int intConstant;

    /**
     * OctaveIntConst constructor
     *
     * @param lineNum  source line number corresponding to this AST node
     * @param constant constant value (as a String)
     */
    public OctaveIntConst(int lineNum, String constant) {
        super(lineNum);
        intConstant = Integer.parseInt(constant);
    }

    /**
     * Get the constant value represented as an int
     *
     * @return the constant value
     */
    public int getIntConstant() {
        return intConstant;
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
