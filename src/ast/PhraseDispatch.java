/* Cymbal Compiler 
   PhraseDispatch.java
   Modified by: Harry Bartlett, Terrence Tan, Tyler Harley
   Modified: 4/27/2014 
*/
package ast;

import visitor.Visitor;

/**
 * The <tt>PhraseDispatch</tt> class represents a dispatch expression
 * to a desired phrase
 *
 * @see ASTNode
 * @see Expr
 */
public class PhraseDispatch extends NoteExpr {
    /**
     * The name of the phrase
     */
    protected ConstStringExpr phraseName;
    
    

    /**
     * PhraseDispatch constructor
     *
     * @param lineNum    source line number corresponding to this AST node
     * @param phraseName the name of the phrase
     */
    public PhraseDispatch(int lineNum, ConstStringExpr phraseName) {
        super(lineNum, null, null);
        this.phraseName = phraseName;
    }

    /**
     * Get the phrase name
     *
     * @return phrase name
     * 
     */
    public ConstStringExpr getPhraseName() {
        return phraseName;
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
