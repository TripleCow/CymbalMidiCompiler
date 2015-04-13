/* Cymbal Compiler 
   PhraseList.java
   Modified by: Harry Bartlett, Terrence Tan, Tyler Harley
   Modified: 4/27/2014 
*/
package ast;

import visitor.Visitor;


/**
 * The <tt>PhraseList</tt> class represents a list of phrases.
 *
 * @see ListNode
 * @see Member
 */
public class PhraseList extends ListNode {
    /**
     * Member list constructor
     *
     * @param lineNum source line number corresponding to this AST node
     */
    public PhraseList(int lineNum) {
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
