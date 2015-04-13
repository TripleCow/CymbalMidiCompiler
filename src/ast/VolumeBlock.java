/* Cymbal Compiler 
   VolumeBlock.java
   Modified by: Harry Bartlett, Terrence Tan, Tyler Harley
   Modified: 4/27/2014 
*/

package ast;

import visitor.Visitor;

/**
 * The <tt>VolumeBlock</tt> class represents a block statement, which
 * contains a list of Expressions to be modified by the given dynamic
 *
 * @see ASTNode
 * @see Stmt
 */
public class VolumeBlock extends BlockExpr {
    /**
     * A list of expressions
     */
    protected ExprList exprList;
    
    
    /**
     * the dynamic for this block
     */
    VolumeWord dynamic;
    /**
     * OctaveBlock constructor
     *
     * @param lineNum  source line number corresponding to this AST node
     * @param dynamic contains the value of the current dynamic
     * @param exprList a list of Exprs
     */
    public VolumeBlock(int lineNum, VolumeWord dynamic ,ExprList exprList) {
        super(lineNum);
        this.dynamic = dynamic;
        this.exprList = exprList;
    }

    public VolumeWord getDynamic(){
    	return this.dynamic;
    }
    
    /**
     * Get the expression list
     *
     * @return exprlist
     */
    public ExprList getExprList() {
        return this.exprList;
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
