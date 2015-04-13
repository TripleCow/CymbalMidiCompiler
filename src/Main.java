/* Bantam Java Compiler and Language Toolset.

   Copyright (C) 2007 by Marc Corliss (corliss@hws.edu) and 
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

   ----------------------------

   Changes were added by DJS to permit drawing of the AST using
   the drawer package.
*/

/*
authors: Harry Bartlett, Terrence Tan, Tyler Harley
file: main.java
date:4/27/2014

changes:
1.)Removed many Bantam Java specific features
2.)Optimized for Midi Synthesizer
*/

import java_cup.runtime.Symbol;
import lexer.Lexer;
import codegenmips.*;
import parser.Parser;
import semant.SemanticAnalyzer;
import ast.File;

/**
 * Main class that runs the Bantam compiler
 * Constructs and runs each phase of the compiler
 * (lexing, parsing, semantic analysis, and code generation).
 */
public class Main {
    /**
     * Array for holding each input file name
     */
    private static String[] inFiles;
    /**
     * String that holds the output file name - "out.s" by default
     */
    private static String outFile = "out.s";
    /**
     * Boolean flag indicating whether garbage collection is enabled - disabled by default
     */
    private static boolean gcEnabled = false;
    /**
     * Boolean flags that indicate whether we should stop after
     * a particular phase.  If turned on Main prints out an
     * intermediate representation of the phase before exiting
     */
    private static boolean stopAfterLexing, stopAfterParsing, stopAfterSemant, stopAfterOpt;
    /**
     * Debugging flags for each phase of the compiler
     */
    private static boolean debugLexer, debugParser, debugSemant, debugInt, debugOpt, debugCodeGen;
    /**
     * Optimization level (0 means off)
     */
    private static int opt = 0;
    /**
     * Interpreter mode (false means compiler mode)
     */
    private static boolean intMode = false;
    /**
     * Integer indicating target (0=mips, 1=x86, 2=jvm -- mips by default)
     */
    private static int targetType = 0;

    /**
     * Constant for MIPS target
     */
    private static final int TARG_MIPS = 0;


    /**
     * Prints out a usage message to the screen
     * Modified by DJS to include [-dt]
     */
    private static void showHelp() {
        System.err.println("Usage: Cymbal [-h] [-o <output_file>] [-t <architecture>]");
        System.err.println("               [-gc] [-int] [-opt <num>] [-dl] [-dp] [-ds]");
        System.err.println("               [-di] [-do] [-dc] [-sl] [-ss] [-so] <input_files>");
        System.err.println("man Cymbal for more details");
        System.exit(1);
    }

    /**
     * Get target name
     * Converts targetType (global) into the target name
     *
     * @return name of target
     */
    private static String getTargName() {
        if (targetType == TARG_MIPS) {
            return "mips";
        }
        else {
            throw new RuntimeException("Internal error: bad target type (" +
                    targetType + ") in Main.getTargName");
        }
    }

    /**
     * Processes the commandline flags, setting appropriate variables
     *
     * @param args list of commandline arguments
     */
    private static void processFlags(String[] args) {
        // initialize inFiles to size of args, will probably be smaller, but args length
        // gives upper bound
        inFiles = new String[args.length];
        // cnt represents the number of input files found - initialize to 0
        int cnt = 0;

        // if no arguments then call showHelp (which eventually exits)
        if (args.length == 0) {
            showHelp();
        }

        // otherwise inspect the arguments
        for (int i = 0; i < args.length; i++) {
            // if '-h' or help then call showHelp (which eventually exits)
            if (args[i].equals("-h")) {
                showHelp();
            }

            // if -gc is set then enable garbage collection
            else if (args[i].equals("-gc")) {
                gcEnabled = true;
            }

            // if -dl, -dp, -ds, -di, -do, -dc then set corresponding boolean to enable
            // debugging for that phase
            else if (args[i].equals("-dl")) {
                debugLexer = true;
            }
            else if (args[i].equals("-dp")) {
                debugParser = true;
            }
            else if (args[i].equals("-ds")) {
                debugSemant = true;
            }
            else if (args[i].equals("-di")) {
                debugInt = true;
            }
            else if (args[i].equals("-do")) {
                debugOpt = true;
            }
            else if (args[i].equals("-dc")) {
                debugCodeGen = true;
            }

            // if -sl, -sp, -ss, -so then set corresponding boolean to stop compilation
            // after that phase
            else if (args[i].equals("-sl")) {
                stopAfterLexing = true;
            }
            else if (args[i].equals("-sp")) {
                stopAfterParsing = true;
            }
            else if (args[i].equals("-ss")) {
                stopAfterSemant = true;
            }
            else if (args[i].equals("-so")) {
                stopAfterOpt = true;
            }        

            // if -int turn on interpreter mode
            else if (args[i].equals("-int")) {
                intMode = true;
            }
            // if -t is set then user is specifying the target architecture
            else if (args[i].equals("-t")) {
                // check if no further arguments
                if (i == args.length - 1) {
                    // if not, then print error message and call showHelp() (which eventually exits)
                    System.err.println("Usage error: must specify a target architecture with -t");
                    showHelp();
                }
                i++;
                // check if architecture is "mips", if so, set targetType to mips
                if (args[i].equals("mips")) {
                    targetType = TARG_MIPS;
                }
                else {
                    // if not, then print error message and call showHelp() (which eventually exits)
                    System.err.println("Usage error: bad target architecture: " + args[i]);
                    System.err.println("             must be 'mips', 'x86', or 'jvm'");
                    showHelp();
                }
            }

            // if -o is set then user is specifying the output file
            else if (args[i].equals("-o")) {
                // check if no further arguments
                if (i == args.length - 1) {
                    // if not, then print error message and call showHelp() (which eventually exits)
                    System.err.println("Usage error: must specify an output file with -o");
                    showHelp();
                }
                i++;
                // otherwise set output file
                outFile = args[i];
       
            }

            // any other arguments must be input files

            // check if argument ends in .cym
            else if (args[i].length() >= 5 && args[i].substring(args[i].length() - 4).equals(".cym")) {
                // if so then set next entry in inFiles
                inFiles[cnt++] = args[i];
            }

            else {
                // if we get to here then we have an illegal argument
                // (we treat this as a bad input file name)
                System.err.println("Usage error: bad input file name: " + args[i]);
                System.err.println("             file names must end with '.cym'");
                showHelp();
            }
        }

        // make sure at least one input file was specified
        if (cnt == 0) {
            System.err.println("Usage error: must specify some input files");
            showHelp();
        }

        // resize inFiles to the number of specified input files (i.e., cnt)
        String[] tmp = inFiles;
        inFiles = new String[cnt];
        for (int i = 0; i < cnt; i++)
            inFiles[i] = tmp[i];
    }

    /**
     * Main method, which drives compilation
     * builds and runs each phase of the compiler
     *
     * @param args list of commandline arguments
     */
    public static void main(String[] args) {
        // process flags
        processFlags(args);

        try {
            // lexing
            Lexer lexer = new Lexer(inFiles, debugLexer);
            if (stopAfterLexing) {
                // if stopAfterLexing==true, then print tokens and exit
                lexer.printTokens();
                System.exit(0);
            }

            // parsing
            Parser parser = new Parser(lexer);
            Symbol result = null;
            if (debugParser) {
                result = parser.debug_parse();
            }
            else {
                result = parser.parse();
            }

            // semantic analysis
            SemanticAnalyzer semanticAnalyzer =
                    new SemanticAnalyzer((File) result.value, debugSemant);
            semanticAnalyzer.analyze();
            if (stopAfterSemant) {
                System.exit(0);
            }


            // code generation
            if (targetType == TARG_MIPS) {
                MipsCodeGenerator codeGenerator = new MipsCodeGenerator((File) result.value, outFile,
                        gcEnabled, (opt > 0),
                        debugCodeGen);
                codeGenerator.generate();
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Internal error within compiler: stopping compilation");
            System.exit(1);
        }
    }
}
