/* Cymbal Compiler 
   NoteLiteral.java
   Modified by: Harry Bartlett, Terrence Tan, Tyler Harley
   Modified: 4/27/2014 
*/

package ast;

import visitor.Visitor;


/**
 * The  <tt>NoteLiteral</tt> represents a literal note to be played
 * by the midi synthesizer in MARS
 *
 * @see ASTNode
 * @see Expr
 */
public class NoteLiteral extends NoteExpr {
	 /**
     * length value
     */
    protected String length;

	
	/**
     * Note value
     */
    protected Note note;
    
    /**
     * Accidental value
     */
    protected Accidental accidental;
    
    
    /**
     * Octave value
     */
    protected OctaveValue octave;

    /**
     * NoteLiteral constructor
     *
     * @param lineNum  source line number corresponding to this AST node
     * @param constant length of the note
     * @param Note pitch of the note
     * @param Accidental modification of the note
     * @param OctaveValue octave of the note
     */
    public NoteLiteral(int lineNum, String length,Note note, 
    					Accidental accidental, OctaveValue octave) {
    	super(lineNum, null, null);
        this.note = note;
        this.accidental = accidental;
        this.length = length;
        this.octave = octave;
    }

    /**
     * Get the length value
     *
     * @return length value
     */
    public String getLength() {
        return length;
    }
    /**
     * Get the note value
     *
     * @return note value
     */
    public Note getNote() {
        return note;
    }
    
    /**
     * Get the accidental value
     *
     * @return accidental value
     */
    public Accidental getAccidental() {
        return accidental;
    }
    /**
     * Get the octave value
     *
     * @return constant value
     */
    public OctaveValue getOctave() {
        return octave;
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
