

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
   
   Added Semantic Analysis capability for Cymbal
   
 */

package semant;

import visitor.*;
import ast.*;
import util.ErrorHandler;


/** The <tt>SemanticAnalyzer</tt> class performs semantic analysis.
 * In particular this class is able to perform (via the <tt>analyze()</tt>
 * method) the following tests and analyses: (1) legal inheritence
 * hierarchy (all classes have existing parent, no cycles), (2) 
 * legal class member declaration, (3) there is a correct Main class
 * and main() method, and (4) each class member is correctly typed.
 * 
 * This class is incomplete and will need to be implemented by the student. 
 */
public class SemanticAnalyzer {
	/** Root of the AST */
	private File file;


	/** Object for error handling */
	private ErrorHandler errorHandler = new ErrorHandler();

	/** Boolean indicating whether debugging is enabled */
	@SuppressWarnings("unused")
	private boolean debug = false;

//	/** Maximum number of inherited and non-inherited 
//	 * fields that can be defined for any one class */
//	private final int MAX_NUM_FIELDS = 1500; 
	

	/** SemanticAnalyzer constructor
	 * @param program root of the AST
	 * @param debug boolean indicating whether debugging is enabled
	 */
	public SemanticAnalyzer(File file, boolean debug) {
		this.file = file;
		this.debug = debug;
	}

	/** 
	 * Analyze the AST checking for semantic errors and annotating the tree
	 * Also builds an auxiliary class hierarchy tree 
	 * @return root of the file 
	 */
	public File analyze() {
		
		//check to make sure octave blocks are declared with valid values
		this.checkOctaveBlocks();
		
		//check to make sure octave values given to notes are valid
		this.checkOctaveMods();
		
		//check to make sure octave values do not go past max/min values with mods
		this.checkMinMaxOctave();
		
		//check to make sure phrase names are not duplicated or undeclared
		this.validatePhraseNames();
		
		//check to make sure all instruments are valid
		this.validateInstrumentNames();
		
		// Give back the semantically correct root
		errorHandler.checkErrors();
		
		
		return file;
	}
	/**
	 * check to make sure octave blocks are declared with valid values
	 */
	private void checkOctaveBlocks(){
		OctaveCheckVisitor tcv = new OctaveCheckVisitor(
				this.file, this.errorHandler);
		errorHandler = tcv.checkOctaveBlocks(this.file);
	}
	/**
	 * check to make sure octave values given to notes are valid
	 */
	private void checkOctaveMods(){
		InvalidOctaveModVisitor iom = new InvalidOctaveModVisitor(
				this.file, this.errorHandler);
		errorHandler = iom.checkOctaveMods(this.file);
	}
	/**
	 * check to make sure octave values do not go past max/min values with mods
	 */
	private void checkMinMaxOctave(){
		OctaveValueCheckVisitor iom = new OctaveValueCheckVisitor(
				this.file, this.errorHandler);
		errorHandler = iom.checkOctaveValues(this.file);
	}
	/**
	 * check to make sure phrase names are not duplicated or undeclared
	 */
	private void validatePhraseNames(){
		PhraseValidatorVisitor pvv = new PhraseValidatorVisitor(
				this.file, this.errorHandler);
		errorHandler = pvv.validatePhraseNames(this.file);
	}
	/**
	 * check to make sure instrument names are valid instruments
	 */
	private void validateInstrumentNames(){
		InstrumentValidatorVisitor ivv = new InstrumentValidatorVisitor(
				this.file, this.errorHandler);
		errorHandler = ivv.validateInstrumentNames(this.file);
	}

}

