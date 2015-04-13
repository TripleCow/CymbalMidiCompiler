/* Bantam Java Compiler and Language Toolset.

   Copyright (C) 2009 by Marc Corliss (corliss@hws.edu) and 
                         David Furcy (furcyd@uwosh.edu) and
                         E Christopher Lewis (lewis@vmware.com).
   
   ALL RIGHTS RESERVED.

   The Bantam Java toolset is distributed under the following 
   conditions:

     You may make copies of the toolset for your own use and 
     modify those copies.

     All copies of the toolset must retain the author names and 
     copyright notice.

     You may not sell the toolset or distribute it in 
     conjunction with a commerical product or service without 
     the expressed written consent of the authors.

   THIS SOFTWARE IS PROVIDED ``AS IS'' AND WITHOUT ANY EXPRESS 
   OR IMPLIED WARRANTIES, INCLUDING, WITHOUT LIMITATION, THE 
   IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A 
   PARTICULAR PURPOSE. 
   
   Modified by: Harry Bartlett, Terrence Tan, Tyler Harley
   Modified: 4/27/2014
   
   Removed Bantam Specific methods and functions
   
   Added MIPS code gen capability for Cymbal language
*/

package codegenmips;

import ast.*;
import ast.File;
import semant.*;
import util.*;
import visitor.CodeGenVisitor;
import visitor.LengthMapVisitor;
import visitor.PhraseCodeGenVisitor;

import java.io.*;
import java.util.*;

/** The <tt>MipsCodeGenerator</tt> class generates mips assembly code
  * targeted for the SPIM emulator.  Note: this code will only run 
  * under SPIM.
  * 
  * This class is incomplete and will need to be implemented by the student. 
  * */
public class MipsCodeGenerator {
    /** Root of the class hierarchy tree */
    private File file;

    /** Print stream for output assembly file */
    private PrintStream out;

    /** Assembly support object (using Mips assembly support) */
    private MipsSupport assemblySupport;
    
    //contains the notes and their appropriate values
    private HashMap<String,Integer> noteMap = new HashMap<String,Integer>();
    
    //contains the lengths and their appropriate values
    private HashMap<String,Integer> lengthMap = new HashMap<String,Integer>();

    /** Boolean indicating whether garbage collection is enabled */
    private boolean gc = false;

    /** Boolean indicating whether optimization is enabled */
    private boolean opt = false;

    /** Boolean indicating whether debugging is enabled */
    private boolean debug = false;
    
    private int stringIndex;

    /** MipsCodeGenerator constructor
      * @param root root of the class hierarchy tree
      * @param outFile filename of the assembly output file
      * @param gc boolean indicating whether garbage collection is enabled
      * @param opt boolean indicating whether optimization is enabled
      * @param debug boolean indicating whether debugging is enabled
      * */
    public MipsCodeGenerator(File file, String outFile, 
			     boolean gc, boolean opt, boolean debug) {
	this.file = file;
	this.gc = gc;
	this.opt = opt;
	this.debug = debug;

	try {
	    out = new PrintStream(new FileOutputStream(outFile));
	    assemblySupport = new MipsSupport(out);
	}
	catch(IOException e) {
	    // if don't have permission to write to file then report an error and exit
	    System.err.println("Error: don't have "
	    					+ "permission to write to file '" + outFile + "'");
	    System.exit(1);
	}
    }

    /** Generate assembly file 
      * 
      * In particular, will need to do the following:
      *   1 - generate a map of notes and their associated MIPS values
      *   2 - generate a map of lengths and their associate MIPS values
      * */
    public void generate() {
    //Offsets for each note into chromatic scale
    noteMap.put("A",0);
    noteMap.put("B",2);
    noteMap.put("C",3);
    noteMap.put("D",5);
    noteMap.put("E",7);
    noteMap.put("F",8);
    noteMap.put("G",10);
   
    
    //0 - Generate the length of notes to be played for passed tempo
    genLengths();
    
    // 1 - start the data section
    dataStart();
    
    // 2 - generate data for the garbage collector
    assemblySupport.genLabel("gc_flag");
    if(gc){
    	assemblySupport.genWord("1");
    }
    else{
    	assemblySupport.genWord("0");
    }
    
    // 3 - start the text section
    textStart();
    
    //4 - Generate the notes
    noteGen();
	// comment out
	//throw new RuntimeException("MIPS code generator unimplemented");

    }
    
   


	/**
     * Generate the start of the MIPS file, containing
     * comments and beginning globalizations
     */
	private void dataStart() {
		assemblySupport.genComment("Authors: Tyler Harley, "
								+ "Harry Bartlett, Terrence Tan");
		assemblySupport.genComment("Date: " + new Date());
		//TODO
		//add filename Support
		//assemblySupport.genComment("Files: " + "myFile" + "\n");
		assemblySupport.genDataStart();
	}
    
   
    /**
     * Generates the start of the text section.
     */
    public void textStart() {
    	assemblySupport.genTextStart();
    }
    
    private void genLengths(){
		LengthMapVisitor lmv = new LengthMapVisitor(file);
    	lengthMap = lmv.genLengthMap(file, lengthMap);
    }
    
    private void noteGen() {
    	//enables syscall of MIDI out
    	assemblySupport.genLoadImm("$v0", 33 );
    	
    	//load volume default
        assemblySupport.genLoadImm("$a3",75);
        
        //load tempo default
        assemblySupport.genLoadImm("$a1",120);
        
        //load instrument default (in 1.0 it is unchangeable)
  	 	assemblySupport.genLoadImm("$a2", 0);
        
		assemblySupport.genComment("Notes Start");
		//run the note generation visitor
		CodeGenVisitor cgv = new CodeGenVisitor(file,assemblySupport);
		cgv.genCode(file, noteMap, lengthMap);
		
		PhraseCodeGenVisitor pcgv = new PhraseCodeGenVisitor(file,assemblySupport);
		pcgv.genPhrases(file, noteMap, lengthMap);
		
	}
   
}
