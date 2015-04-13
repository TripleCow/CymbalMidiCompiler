/* Cymbal Compiler 
   InstrumentBlock.java
   Modified by: Harry Bartlett, Terrence Tan, Tyler Harley
   Modified: 5/8/2014 
*/

package ast;

import visitor.Visitor;

/**
 * The <tt>InstrumentBlock</tt> class represents a block statement, which
 * changes the instrument
 *
 * @see ASTNode
 */
public class InstrumentBlock extends BlockExpr {
    /**
     * A list of notes
     */
    protected ExprList exprList;
    
    /**
     * Instrument to be played
     */
    protected ConstStringExpr instrument;

    
    /**
     * RepeatBlock constructor
     *
     * @param lineNum  source line number corresponding to this AST node
     * @param exprList a list of ExprList
     */
    public InstrumentBlock(int lineNum,ConstStringExpr instrument,ExprList exprList) {
        super(lineNum);
        this.instrument = instrument;
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
     * Get the instrument
     *
     * @return instrument
     */
    public ConstStringExpr getInstrument() {
        return this.instrument;
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
