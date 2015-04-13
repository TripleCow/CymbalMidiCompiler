/* Cymbal Compiler 
   OctaveValue.java
   Modified by: Harry Bartlett, Terrence Tan, Tyler Harley
   Modified: 4/27/2014 
*/
package ast;

import visitor.Visitor;


/**
 * The abstract <tt>OctaveValue</tt> class holds the value of an octave or
 * an OctaveModVal
 *
 * @see ASTNode
 * @see ConstExpr
 */
public abstract class OctaveValue extends ASTNode {


    /**
     * OctaveValue constructor
     *
     * @param lineNum  source line number corresponding to this AST node
     */
    public OctaveValue(int lineNum) {
        super(lineNum);
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
