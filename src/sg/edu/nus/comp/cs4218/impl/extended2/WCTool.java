package sg.edu.nus.comp.cs4218.impl.extended2;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import sg.edu.nus.comp.cs4218.extended2.IWcTool;
import sg.edu.nus.comp.cs4218.impl.ATool;

/**
 * Do not modify this file
 */
/*
 * 
 * wc : Prints the number of bytes, words, and lines in given file
 *
 * Command Format - wc [OPTIONS] [FILE]
 * FILE - Name of the file, when no file is present (denoted by "-") use standard input
 * OPTIONS
 *		-m : Print only the character counts
 *      -w : Print only the word counts
 *      -l : Print only the newline counts
 *		-help : Brief information about supported options
*/

public class WCTool extends ATool implements IWcTool{

	public WCTool(String[] arguments) {
		super(arguments);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String getCharacterCount(String input) {
		// TODO Auto-generated method stub
		input = input.replaceAll("\\s","");
		int length = input.length();
		return Integer.toString(length);
	}

	@Override
	public String getWordCount(String input) {
		// TODO Auto-generated method stub
		String trimmed = input.trim();
		int words = trimmed.isEmpty() ? 0 : trimmed.split("\\s+").length;
		return Integer.toString(words);
	}

	@Override
	public String getNewLineCount(String input) {
		// TODO Auto-generated method stub
		 String[] lines = input.split("\r\n|\r|\n");
		 return  Integer.toString(lines.length);
	}

	@Override
	public String getHelp() {
		// TODO Auto-generated method stub
		return null;
	}

	static String readFile(String path, Charset encoding)
	{
		//"The character count of " + input + " is " + 
		byte[] encoded = null;
	    try {
	    	encoded = Files.readAllBytes(Paths.get(path));
	    } 
	    catch (IOException e) {
	    	e.printStackTrace();
	    }
	    return encoding.decode(ByteBuffer.wrap(encoded)).toString();
	
	}
		
	
	@Override
	public String execute(File workingDir, String stdin) {
		
		File filePath = new File( args[0] );
		if(filePath.isFile())
		{
		return getNewLineCount(readFile( args[0], StandardCharsets.UTF_8));
		}
		return "Invalid input!";
	}
}