/* Cymbal Compiler 
   VolumeWord.java
   Modified by: Harry Bartlett, Terrence Tan, Tyler Harley
   Modified: 4/27/2014 
*/
package ast;

import visitor.Visitor;


/**
 * The <tt>VolumeWord</tt> class contains the String value of an dynamic
 *
 * @see ASTNode
 * @see OctaveValue
 */
public class VolumeWord extends BlockExpr {
    /**
     * The constant value represented as an int
     */
    private String dynamic;

    /**
     * OctaveIntConst constructor
     *
     * @param lineNum  source line number corresponding to this AST node
     * @param dynamic constant string value
     */
    public VolumeWord(int lineNum, String dynamic) {
        super(lineNum);
        this.dynamic = dynamic;
    }

    /**
     * Get the string constant value of the dynamic
     *
     * @return the constant value
     */
    public String getDynamic() {
        return this.dynamic;
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
