/* Cymbal Compiler 
   RepeatBlock.java
   Modified by: Harry Bartlett, Terrence Tan, Tyler Harley
   Modified: 5/5/2014 
*/

package ast;

import visitor.Visitor;

/**
 * The <tt>RepeatBlock</tt> class represents a block statement, which
 * repeats the encapsulated expression list
 *
 * @see ASTNode
 * @see Stmt
 */
public class RepeatBlock extends BlockExpr {
    /**
     * A list of notes
     */
    protected ExprList exprList;
    

    
    /**
     * RepeatBlock constructor
     *
     * @param lineNum  source line number corresponding to this AST node
     * @param exprList a list of ExprList
     */
    public RepeatBlock(int lineNum,ExprList exprList) {
        super(lineNum);
        this.exprList = exprList;
    }
    
    /**
     * Get the expression list
     *
     * @return expression list
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
