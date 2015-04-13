/* Cymbal Compiler Visitor.
 * 	File: LengthMapVisitor.java
	Authors: Harry Bartlett, Terrence Tan, Tyler Harley
	Date: 4/27/2014
	
	Visitor to traverse the Tree of Cymbal's grammar
	as outlined in the design document
  
*/

package visitor;

import ast.*;

import java.util.HashMap;
import java.util.Iterator;

import codegenmips.MipsSupport;

/**
 * Abstract visitor class for traversing the AST
 */
public  class LengthMapVisitor extends Visitor {
    
	private HashMap<String,Integer> lengthMap;
	
	/**The file to be traversed*/
	private File file;
	
	public LengthMapVisitor(File file){
		this.file = file;
	}
	
	public HashMap<String,Integer> genLengthMap(File file, HashMap<String,Integer> lengthMap){
		this.lengthMap = lengthMap;
		file.accept(this);	
		return lengthMap;
	}
    /**
     * Visit a File node
     *
     * @param node the file node
     * @return result of the visit
     */
    public Object visit(File node) {
        node.getSong().accept(this);
        return null;
    }

   

    /**
     * Visit a song node
     *
     * @param node the song node
     * @return result of the visit
     */
    public Object visit(Song node) {
        int tempo = node.getTempo();
        //set length of quarter note in milliseconds
        tempo = 60000/tempo;
        
        //milisecond lengths for each length
        lengthMap.put("Eighth",tempo/2);
        lengthMap.put("Quarter",tempo);
        lengthMap.put("Half",tempo*2);
        lengthMap.put("Whole",tempo*4);
        
        return null;
    }


}
