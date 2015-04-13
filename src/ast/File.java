/* Cymbal Compiler 
   File.java
   Modified by: Harry Bartlett, Terrence Tan, Tyler Harley
   Modified: 5/1/2014 
*/
package ast;

import visitor.Visitor;


/**
 * The <tt>File</tt> class holds the phraseList and song
 * of the program loaded
 *
 * @see ASTNode
 * @see ClassList
 */
public class File extends ASTNode {
    /**
     * List of phrase declarations that comprise the program
     */
    protected PhraseList phraseList;
    /**
     * List of phrase declarations that comprise the program
     */
    protected Song song;

    /**
     * Note constructor
     *
     * @param lineNum   source line number corresponding to this AST node
     * @param phraseList list of phrase declarations
     * @param song song to be played
     */
    public File(int lineNum, PhraseList phraseList, Song song) {
        super(lineNum);
        this.phraseList = phraseList;
        this.song = song;
    }

    /**
     * Get list of phrases that comprise the program
     *
     * @return list of phrases declarations
     */
    public PhraseList getPhraseList() {
        return phraseList;
    }
    
    /**
     * Get the song in the file
     *
     * @return song
     */
    public Song getSong() {
        return song;
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
