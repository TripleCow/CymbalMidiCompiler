/* Cymbal Compiler 
   NoteExpr.java
   Modified by: Harry Bartlett, Terrence Tan, Tyler Harley
   Modified: 4/27/2014 
*/
package ast;

import visitor.Visitor;


/**
 * The <tt>NoteExpr</tt> holds the two possible expressions to play notes:
 * either a phrase dispatch to call a particular phrase or a literal note
 *
 * @see ASTNode
 * @see Expr
 */
public class NoteExpr extends Expr {
	 /**
     * Expression(if applicable)
     */
    protected NoteLiteral note;

	
	/**
     * Note value
     */
    protected PhraseDispatch phrase;

    /**
     * NoteExpr constructor
     *
     * @param lineNum  source line number corresponding to this AST node
     */
    public NoteExpr(int lineNum, NoteLiteral note,PhraseDispatch phrase) {
        super(lineNum);
        this.note = note;
        this.phrase = phrase;
    }

    /**
     * Get the noteLiteral 
     *
     * @return noteLiteral value
     */
    public NoteLiteral getNoteLiteral() {
        return note;
    }
    /**
     * Get the phraseDispatch 
     *
     * @return phraseDispatch value
     */
    public PhraseDispatch getPhrase() {
        return phrase;
    }

    /**
     * Visitor method
     *
     * @param v visitor object
     * @return result of visiting this node
     * @see visitor.Visitor
     */
     public Object accept(Visitor v){
    	 return v.visit(this);
     }
}
