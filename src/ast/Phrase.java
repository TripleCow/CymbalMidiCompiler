/* Cymbal Compiler 
   Phrase.java
   Modified by: Harry Bartlett, Terrence Tan, Tyler Harley
   Modified: 4/27/2014 
*/
package ast;

import visitor.Visitor;

/**
 * The <tt>Phrase</tt> class a series of notes to be played at a later time
 * similar to a method in Java
 *
 * @see ASTNode
 */
public class Phrase extends ASTNode {
    /**
     * The name of this phrase
     */
    protected String phraseName;


    /**
     * List of the class members
     */
    protected ExprList exprList;

    /**
     * Phrase constructor
     *
     * @param lineNum    source line number corresponding to this AST node
     * @param phraseName the name of this phrase
     * @param exprList   a list of the notes to be played
     */
    public Phrase(int lineNum, String phraseName, ExprList exprList) {
        super(lineNum);
        this.phraseName = phraseName;
        this.exprList = exprList;
    }


    /**
     * Get the name of this phrase
     *
     * @return phrase name
     */
    public String getPhraseName() {
        return phraseName;
    }


    /**
     * Get list of expressions the phrase contains
     *
     * @return list of expressions
     */
    public ExprList getExprList() {
        return exprList;
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
