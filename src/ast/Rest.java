/* Cymbal Compiler 
   Rest.java
   Modified by: Harry Bartlett, Terrence Tan, Tyler Harley
   Modified: 5/5/2014 
*/
package ast;

import visitor.Visitor;
/**
 * The <tt>Rest</tt> class is a note where no sound is played
 *
 * @see Note
 */
public class Rest extends Note{
	
	String rest;
    /**
     * Rest constructor
     *
     * @param lineNum  source line number corresponding to this AST node
     * @param constant constant value (as a String)
     */
    public Rest(int lineNum, String rest) {
        super(lineNum,null);
        this.rest = rest;

        
    }
    
    /**
     * Get the rest 
     *
     * @return rest
     */
    public String getRest() {
        return rest;
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
