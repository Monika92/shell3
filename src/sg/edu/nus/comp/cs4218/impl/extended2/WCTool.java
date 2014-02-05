package sg.edu.nus.comp.cs4218.impl.extended2;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import sg.edu.nus.comp.cs4218.extended2.IWcTool;
import sg.edu.nus.comp.cs4218.impl.ATool;
import sg.edu.nus.comp.cs4218.impl.ArgumentObject;
import sg.edu.nus.comp.cs4218.impl.ArgumentObjectParser;


public class WCTool extends ATool implements IWcTool{

	String helpOutput, command;
	public WCTool(String[] arguments) {
		super(arguments);
		// TODO Auto-generated constructor stub
		helpOutput = "wc [OPTIONS] [FILE]" + "\n" +
				"FILE : Name of the file" + "\n" +
				"OPTIONS : -m : Print only the number of characters \n" +
				"          -w : Print only the number of words \n" +
				"          -l : Print only the number of lines \n" +
				"          -help : Brief information about supported options." ;
		
		command = "wc";
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
		return helpOutput;
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
		
	
	public String implementWC(String file, ArrayList<String> options)
	{
		String outputString = "";
		if(options.isEmpty())
		{
			outputString += file +" : -m  "+  getCharacterCount(readFile(file, StandardCharsets.UTF_8))
					        + " , -w  " +  getWordCount(readFile(file, StandardCharsets.UTF_8))
					        + " , -l " +  getNewLineCount(readFile(file, StandardCharsets.UTF_8)) + "\n";
		}
		else
		{
			outputString += file + " : ";
			
			if(options.contains("-help"))
			{
				outputString += "\n" + getHelp() + "\n";
			}
			else
			{
				if(options.contains("-m"))
				{
					outputString += " -m  "+  getCharacterCount(readFile(file, StandardCharsets.UTF_8));
				}
				if(options.contains("-w"))
				{
					outputString += " -w  "+  getWordCount(readFile(file, StandardCharsets.UTF_8));
				}
				else if(options.contains("-l"))
				{
					outputString += " -l  "+  getNewLineCount(readFile(file, StandardCharsets.UTF_8));
				}	
			}
					        
			outputString += "\n";
		}
		return outputString;
	}
	
	public String getFilePath(String fileName, File dir)
	{		
		if((new File(fileName)).isAbsolute())
	 		return fileName;
		else
			return dir + File.separator +  fileName;
	}
	
	
	@Override
	public String execute(File workingDir, String stdin) {
		
		String outputString = "";
		ArgumentObjectParser argumentObjectParser = new ArgumentObjectParser();
		ArgumentObject argumentObject = argumentObjectParser.parse(args, command);
		ArrayList<String> fileList = argumentObject.getFileList();
		ArrayList<String> options = argumentObject.getOptions();
		
		for(int i=0; i<fileList.size(); i++)
		{
			File filePath = new File(getFilePath(fileList.get(i) , workingDir));
			if(filePath.isFile())
			{
				outputString += implementWC(filePath.getAbsolutePath(),options);
			}
			else
			{
				outputString += fileList.get(i) + " error - Invalid Input. \n";
			}
		}
		
		if(stdin == null)
		{}
		else
		{
			File filePath = new File(getFilePath(stdin , workingDir));
			if(filePath.isFile())
			{
			outputString += implementWC(filePath.getAbsolutePath(),options);
			}
			else
			{
				outputString += stdin + "Error - Invalid Input. \n";
			}
		}
		return outputString;
	}
}