package sg.edu.nus.comp.cs4218.impl.extended2;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
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
import sg.edu.nus.comp.cs4218.impl.CommandVerifier;


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

	/*
	 * Reads the lines in the file passed as argument and returns the information as a string
	 */
		public String readFromFile(File toRead){
			FileReader fr;
			String output = "";
			
			try{
				fr = new FileReader(toRead);
			} catch(FileNotFoundException e){
				e.printStackTrace();
				//System.out.println("File not found");
				return "File not found";
			}
			BufferedReader br = new BufferedReader(fr);
			try{
				String line = br.readLine();
				while(line != null){
					//System.out.println(line);
					if(line.equalsIgnoreCase("\n")||line.equalsIgnoreCase(""))
						output+="\n";
					else
						output += line + "\n";
					line = br.readLine();
				}
			} catch(IOException e){
				e.printStackTrace();
				//System.out.println("Unable to read file");
				return "Unable to read file";
			} finally{
				try {
					br.close();
					fr.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return output;
		}
	/*
	 * Find number of characters in a string
	 */
	@Override
	public String getCharacterCount(String input) {
		// TODO Auto-generated method stub
		input = input.replaceAll("\\s","");
		int length = input.length();
		return Integer.toString(length);
	}

	/*
	 * Find number of words in a string and return as int
	 */
	@Override
	public String getWordCount(String input) {
		// TODO Auto-generated method stub
		String trimmed = input.trim();
		int words = trimmed.isEmpty() ? 0 : trimmed.split("\\s+").length;
		return Integer.toString(words);
	}

	/*
	 * Find number of lines in a string and return as int. A string = " " is considered to have 0 lines
	 */
	@Override
	public String getNewLineCount(String input) {
		// TODO Auto-generated method stub
		 String[] lines = input.split("\r\n|\r|\n");
		 if(input.replaceAll("\\s","").isEmpty() == true) return "0";
		 else return  Integer.toString(lines.length);
	}

	/*return help string
	 */
	@Override
	public String getHelp() {
		// TODO Auto-generated method stub
		return helpOutput;
	}

	/*
	 * reading contents of the string
	 */
	/*static String readFile(String path, Charset encoding)
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
	
	}*/
		
	/*
	 * Implementation of wc
	 * 1. wc file1 : shows -m , -l and -w in the file1
	 * 2. wc -m -l -w : or any other combination of options, display respective numbers for the file
	 * 3. wc -m -l -w file1 file2 : shows the respective numbers for the valid files in the argument list
	 * 4. wc -m - -l file1 : ignores the - in the middle and executes normally
	 * 5. wc -m -l -w file1 - : executes numbers for file1 and takes stdin
	 */
	public String implementWC(String fileName, String fileContent, ArrayList<String> options)
	{
		String outputString = "";
		if(options.isEmpty())
		{
			outputString += fileName +" : -m  "+  getCharacterCount(fileContent)
					        + " , -w  " +  getWordCount(fileContent)
					        + " , -l " +  getNewLineCount(fileContent) ;
		}
		else
		{
			outputString += fileName + " : ";
			
			if(options.contains("-help"))
			{
				outputString += getHelp();
			}
			else
			{
				if(options.contains("-m"))
				{
					outputString += " -m  "+  getCharacterCount(fileContent);
				}
				if(options.contains("-w"))
				{
					outputString += " -w  "+  getWordCount(fileContent);
				}
				if(options.contains("-l"))
				{
					outputString += " -l  "+  getNewLineCount(fileContent);
				}	
			}
					        
		}
		return outputString + "\n";
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
		CommandVerifier cv = new CommandVerifier();
		int validCode = cv.verifyCommand("wc", super.args);
		if(validCode == -1)
		{
		setStatusCode(-1);
		return "";
		}
		if(workingDir == null)
		{
			setStatusCode(-1);
			return "";
		}	
		if(!workingDir.exists()){
			setStatusCode(-1);
			return "";
		}
		String outputString = "";
		ArgumentObjectParser argumentObjectParser = new ArgumentObjectParser();
		ArgumentObject argumentObject = argumentObjectParser.parse(args, command);
		ArrayList<String> fileList = argumentObject.getFileList();
		ArrayList<String> options = argumentObject.getOptions();
		
		if(options.contains("-help"))
		{
			outputString += getHelp();
		}
		else
		{
			for(int i=0; i<fileList.size(); i++)
			{
				File filePath = new File(getFilePath(fileList.get(i) , workingDir));
				if(filePath.isFile())
				{
					//outputString += implementWC(filePath.getAbsolutePath(),readFile(filePath.getAbsolutePath(), StandardCharsets.UTF_8),options);
					outputString += implementWC(filePath.getAbsolutePath(),readFromFile(new File(filePath.toString())),options);
				}
				else
				{
					outputString += fileList.get(i) + " : error - Invalid Input. \n";
					setStatusCode(-1);
				}
			}
			
			if(stdin == null)
			{
				if(outputString == "")
				{
					outputString += "No standard input given. \n";
					setStatusCode(-1);
				}
			}
			else
			{
				outputString += implementWC("Stdin",stdin,options);
				
			}
		}
		return outputString;
	}
}