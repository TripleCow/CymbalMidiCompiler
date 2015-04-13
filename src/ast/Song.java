/* Cymbal Compiler 
   Song.java
   Modified by: Harry Bartlett, Terrence Tan, Tyler Harley
   Modified: 4/27/2014 
*/
package ast;

import visitor.Visitor;

/**
 * The <tt>Song</tt> class represents a song to be played
 *
 * @see ASTNode
 */
public class Song extends ASTNode {

    /**
     * The name of this song
     */
    protected String name;

    /**
     * The list of notes to be played
     */
    protected ExprList exprList;
    
    private int tempo;


    /**
     * Song constructor
     *
     * @param lineNum    source line number corresponding to this AST node
     * @param name       the name of this class
     * @param exprList 	 a list of the noteExpressions 
     */
    public Song(int lineNum, String name, String tempo, ExprList exprList) {
        super(lineNum);
        this.name = name;
        this.tempo = Integer.parseInt(tempo);
        this.exprList = exprList;

    }


    /**
     * Get the name of this song
     *
     * @return song name
     */
    public String getName() {
        return name;
    }

    /**
     * Get list of expressions that this song contains
     *
     * @return list of expressions
     */
    public ExprList getExprList() {
        return this.exprList;
    }

    /**
     * Get the tempo associated with this song
     *
     * @return list of expressions
     */
    public int getTempo() {
        return this.tempo;
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
