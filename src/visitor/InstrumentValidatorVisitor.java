/* Cymbal Compiler Visitor.
 * 	File: InstrumentValidatorVisitor.java
	Authors: Harry Bartlett, Terrence Tan, Tyler Harley
	Date: 4/27/2014
	
	Visitor to traverse the Tree of Cymbal's grammar
	as outlined in the design document
  
*/

package visitor;

import ast.*;

import java.util.*;

import util.ErrorHandler;

/**
 * Abstract visitor class for traversing the AST
 */
public class InstrumentValidatorVisitor extends Visitor {
	
	/** The error handler */
	private ErrorHandler errorHandler;
	
	/**Array containing names of all instruments */
	//Static and public so it can be indexed in later sections
	public final static String[] INSTRUMENTNAMES = {
		    "Acoustic Grand Piano",
		    "Bright Acoustic Piano",
		    "Electric Grand Piano",
		    "Honky-tonk Piano",
		    "Electric Piano",
		    "Electric Piano 2",
		    "Harpsichord",
		    "Clavinet",
		    "Celesta",
		    "Glockenspiel",
		    "Music Box",
		    "Vibraphone",
		    "Marimba",
		    "Xylophone",
		    "Tubular Bells",
		    "Dulcimer",
		    "Drawbar Organ",
		    "Percussive Organ",
		    "Rock Organ",
		    "Church Organ",
		    "Reed Organ",
		    "Accordion",
		    "Harmonica",
		    "Tango Accordion",
		    "Nylon Guitar",
		    "Steel Guitar",
		    "Jazz Guitar", 
		    "Clean Guitar", 
		    "Muted Guitar", 
		    "Overdriven Guitar",
		    "Distortion Guitar",
		    "Guitar Harmonics",
		    "Acoustic Bass",
		    "Finger Bass",
		    "Pick Bass", 
		    "Fretless Bass",
		    "Slap Bass 1",
		    "Slap Bass 2",
		    "Synth Bass 1",
		    "Synth Bass 2",
		    "Violin",
		    "Viola",
		    "Cello",
		    "Contrabass",
		    "Tremolo Strings",
		    "Pizzicato Strings",
		    "Orchestral Harp",
		    "Timpani",
		    "String Ensemble 1",
		    "String Ensemble 2",
		    "Synth Strings 1",
		    "Synth Strings 2",
		    "Choir Aahs",
		    "Voice Oohs",
		    "Synth Choir",
		    "Orchestra Hit",
		    "Trumpet",
		    "Trombone",
		    "Tuba",
		    "Muted Trumpet",
		    "French Horn",
		    "Brass Section",
		    "Synth Brass 1",
		    "Synth Brass 2",
		    "Soprano Sax",
		    "Alto Sax",
		    "Tenor Sax",
		    "Baritone Sax",
		    "Oboe",
		    "English Horn",
		    "Bassoon",
		    "Clarinet",
		    "Piccolo",
		    "Flute",
		    "Recorder",
		    "Pan Flute",
		    "Blown bottle",
		    "Shakuhachi",
		    "Whistle",
		    "Ocarina",
		    "Square Synth",
		    "Sawtooth Synth",
		    "Calliope Synth",
		    "Chiff Synth",
		    "Charang Synth",
		    "Voice Synth",
		    "Fifths Synth",
		    "Bass/Lead Synth",
		    "Synth Pad Warm",
		    "Synth Pad Polysynth",
		    "Synth Pad Choir",
		    "Synth Pad Bowed",
		    "Synth Pad Metallic",
		    "Synth Pad Halo",
		    "Synth Pad Sweep",
		    "Synth Pad Rain",
		    "Rain",
		    "Soundtrack",
		    "Crystal",
		    "Atmosphere",
		    "Brightness",
		    "Brightness",
		    "Goblins",
		    "Echoes",
		    "Sci-Fi",
		    "Sitar",
		    "Banjo",
		    "Shamisen",
		    "Koto",
		    "Kalimba",
		    "Bagpipe",
		    "Fiddle",
		    "Shanai",
		    "Tinkle Bell",
		    "Agogo",
		    "Steel Drums",
		    "Woodblock",
		    "Taiko Drum",
		    "Melodic Tom",
		    "Synth Drum",
		    "Reverse Cymbal",
		    "Guitar Fret Noise",
		    "Breath Noise",
		    "Seashore",
		    "Bird Tweet",
		    "Telephone Ring",
		    "Helicopter",
		    "Applause",
		    "Gunshot"
};
	
	/**current file */
	private File file;
	
	//constructor
		public InstrumentValidatorVisitor(File file, ErrorHandler errorHandler){
			this.errorHandler = errorHandler;
			this.file = file;
		}
		
		public ErrorHandler validateInstrumentNames(File file){
			file.accept(this);	
			return errorHandler;
		}
	
		/**
	     * Visit a PhraseDispatch
	     *
	     * @param node the PhraseDispatch node
	     * @return result of the visit
	     */
	    public Object visit(PhraseDispatch node) {
	        return null;
	    }


   
    
    /**
     * Visit a InstrumentBlock
     *
     * @param node the InstrumentBlock node
     * @return result of the visit
     */
    public Object visit(InstrumentBlock node) {
    	node.getInstrument().accept(this);
    	
        return null;
    }

    /**
     * Visit a string constant expression node
     *
     * @param node the string constant expression node
     * @return result of the visit
     */
    public Object visit(ConstStringExpr node) {
    	int index = -1;
    	for(int i=0;i<INSTRUMENTNAMES.length; i++){
    		if(INSTRUMENTNAMES[i].equals(node.getConstant())){
    			index = i;
    		}
    		
    	}
    	if(index == -1){
    		errorHandler.register(
        			errorHandler.SEMANT_ERROR, 
    				null, 
    				node.getLineNum(), 
    				"Insturment: " + node.getConstant() + " is invalid");
    	}
        return null;
    }
}
