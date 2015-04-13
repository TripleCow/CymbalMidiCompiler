/* Cymbal Compiler 
   TempoBlock.java
   Modified by: Harry Bartlett, Terrence Tan, Tyler Harley
   Modified: 4/27/2014 
*/

package ast;

import visitor.Visitor;

/**
 * The <tt>OctaveBlock</tt> class represents a block statement, which
 * contains a list of NoteExprs to be modified by an octave value
 *
 * @see ASTNode
 * @see Stmt
 */
public class TempoBlock extends BlockExpr {
    /**
     * A list of notes
     */
    protected ExprList exprList;
    
    /**
     * The number of the octave
     */
    protected int tempo;

    
    /**
     * OctaveBlock constructor
     *
     * @param lineNum  source line number corresponding to this AST node
     * @param OctaveValue the value of the octave
     * @param NoteList a list of NoteExpr
     */
    public TempoBlock(int lineNum, String tempo, ExprList exprList) {
        super(lineNum);
        this.tempo = Integer.parseInt(tempo);
        this.exprList = exprList;
    }

    public int getTempo(){
    	return this.tempo;
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
