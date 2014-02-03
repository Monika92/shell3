package sg.edu.nus.comp.cs4218.impl.extended2;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import sg.edu.nus.comp.cs4218.extended2.ICutTool;
import sg.edu.nus.comp.cs4218.impl.ATool;
import sg.edu.nus.comp.cs4218.impl.ArgumentObject;
import sg.edu.nus.comp.cs4218.impl.ArgumentObjectParser;
/**
 * Do not modify this file
 */
/*
 * 
 * cut : prints a substring that is specified in a certain range
 *
 *	Command Format - cut [OPTIONS] [FILE]
 *		FILE - Name of the file, when no file is present (denoted by "-") use standard input OPTIONS
 *			-c LIST: Use LIST as the list of characters to cut out. Items within the list may be
 *					separated by commas, and ranges of characters can be separated with dashes.
 *					For example, list ‘1-5,10,12,18-30’ specifies characters 1 through 5, 10,12 and
 *					18 through 30.
 *			-d DELIM: Use DELIM as the field-separator character instead of the TAB character
 *			-help : Brief information about supported options
 */
public class CUTTool extends ATool implements ICutTool{

	File filename;
	String options,list,helpOutput,input,delim,output;
	int noOfArguments;
	String command;
	Boolean stdinFlag = false;
	
	public CUTTool(String[] arguments) {
		super(arguments);

		helpOutput = "usage: cut [OPTIONS] [FILE]" + "\n"
				+ "FILE : Name of the file, when no file is present" + "\n"
				+ "OPTIONS : -c LIST : Use LIST as the list of characters to cut out. Items within "
				+ "the list may be separated by commas, "
				+ "and ranges of characters can be separated with dashes. "
				+ "For example, list Ô1-5,10,12,18-30Õ specifies characters "
				+ "1 through 5, 10,12 and 18 through 30" + "\n"
				+ "-d DELIM: Use DELIM as the field-separator character instead of"
				+ "the TAB character" + "\n" 
				+ "-help : Brief information about supported options";
		
		command = "cut";
		// TODO Auto-generated constructor stub
	}

	@Override
	public String cutSpecfiedCharacters(String list, String input) {
		// TODO Auto-generated method stub
		
		if(list.equals(null) || list.isEmpty() || input.equals(null) || input.isEmpty())
			return input;
		else 
		{
		int fromNumber = 0,toNumber=0;
		StringBuilder  stringBuilder = new StringBuilder();
		String[] listNumbers = list.split(",");
		for(String listNumber : listNumbers)
		{
			if(listNumber.startsWith("-"))
			{
				fromNumber = 0;
				toNumber = Character.getNumericValue(listNumber.charAt(1));
			}
			else if(listNumber.endsWith("-"))
			{
				fromNumber = Character.getNumericValue(listNumber.charAt(0));
				toNumber = input.length();
			}
			else
			{ 
				String[] rangeNumbers = listNumber.split("-");
				if(rangeNumbers.length==2)
				{
					fromNumber = Integer.parseInt(rangeNumbers[0]);
					toNumber = Integer.parseInt(rangeNumbers[1]);
				}
				else if(rangeNumbers.length==1)
				{
					fromNumber = Integer.parseInt(rangeNumbers[0]);
					toNumber = Integer.parseInt(rangeNumbers[0]);
				}
			}
			if(toNumber > input.length())
				toNumber= input.length();
			for(int i = fromNumber; i<= toNumber; i++)
			{
					if(i > 0)
					stringBuilder.append(input.charAt(i-1));
			}
		}
		return stringBuilder.toString();
		}
	}

	@Override
	public String cutSpecifiedCharactersUseDelimiter(String list, String delim,
			String input) {
		StringBuilder  stringBuilder = new StringBuilder();
		if(input.contains(delim)==false)
		{
			stringBuilder.append(input);
		}
		else
		{
		int fromNumber = 0,toNumber=0;
		String[] words = input.split(delim);
		String[] listNumbers = list.split(",");
		for(String listNumber : listNumbers)
		{
			if(listNumber.startsWith("-"))
			{
				fromNumber = 0;
				toNumber = Character.getNumericValue(listNumber.charAt(1));
			}
			else if(listNumber.endsWith("-"))
			{
				fromNumber = Character.getNumericValue(listNumber.charAt(0));
				toNumber = words.length;
			}
			else
			{ 
				String[] rangeNumbers = listNumber.split("-");
				if(rangeNumbers.length==2)
				{
					fromNumber = Integer.parseInt(rangeNumbers[0]);
					toNumber = Integer.parseInt(rangeNumbers[1]);
				}
				else if(rangeNumbers.length==1)
				{
					fromNumber = Integer.parseInt(rangeNumbers[0]);
					toNumber = Integer.parseInt(rangeNumbers[0]);
				}
			}
			
			if(toNumber > words.length)
				toNumber= words.length;
			
			for(int i = fromNumber; i<= toNumber; i++)
			{
				if(i>0 && i< toNumber)
				stringBuilder.append(words[i-1]+delim);
				else if(i==toNumber)
				stringBuilder.append(words[i-1]);
			}
		}
		}
		return stringBuilder.toString();
	}

	@Override
	public String getHelp() {
		// TODO Auto-generated method stub
		return helpOutput;
	}

	@Override
	public String execute(File workingDir, String stdin) 
	{
		input = "";
		output= "";
		ArgumentObjectParser argumentObjectParser = new ArgumentObjectParser();
		ArgumentObject argumentObject = argumentObjectParser.parse(args, command);
		ArrayList<String> fileList = argumentObject.getFileList();
		ArrayList<String> options = argumentObject.getOptions();
		ArrayList<String> optionArguments = argumentObject.getOptionArguments();
		
		if(fileList.get(0)!=null)
		{
			if(fileList.get(0).equalsIgnoreCase("-"))
			{
				input = stdin;
				stdinFlag = true;
			}
			else
			{
				for (String fileName : fileList)
				{
					File file = new File(fileName);
					try {
						input += readFile(file) + "\n";
					} catch (Exception e) {
						output = "File not found";
						setStatusCode(-1);
						return output;
					} 
				}
			}			
		}
		for( int i = 0; i< options.size() ; i++)
		{
			if(options.get(i).equalsIgnoreCase("-help"))
				output = getHelp();
			else if(options.get(i).equalsIgnoreCase("-c"))
			{
				list = optionArguments.get(i);
				if(stdinFlag)
				{
					output += cutSpecfiedCharacters(list, input);
				}
				else
				{
					StringBuilder  stringBuilder = new StringBuilder();
					String         ls = System.getProperty("line.separator");
					String[] inputLines = input.split(System.getProperty("line.separator"));
					for(String inputLine : inputLines)
					{
						stringBuilder.append( cutSpecfiedCharacters(list, inputLine) );
				        stringBuilder.append( ls );	
					}
					output += stringBuilder.toString();
				}
				
			}
			else if(options.get(i).equalsIgnoreCase("-d"))
			{
				if(options.get(i+1).equalsIgnoreCase("-f"))
				{
					list = optionArguments.get(i+1);
					delim = optionArguments.get(i).replace("\"","");
					if(stdinFlag)
					{
						output += cutSpecifiedCharactersUseDelimiter(list, delim, input);
					}
					else
					{
						StringBuilder  stringBuilder = new StringBuilder();
						String         ls = System.getProperty("line.separator");
						String[] inputLines = input.split(System.getProperty("line.separator"));
						for(String inputLine : inputLines)
						{
							stringBuilder.append( cutSpecifiedCharactersUseDelimiter(list, delim, inputLine) );
					        stringBuilder.append( ls );	
						}
						output += stringBuilder.toString();						
					}
					i++;
				}
			}
			else if(options.get(i).equalsIgnoreCase("-f"))
			{
				if(options.get(i+1).equalsIgnoreCase("-d"))
				{
					list = optionArguments.get(i);
					delim = optionArguments.get(i+1).replace("\"","");
					if(stdinFlag)
					{
						output += cutSpecifiedCharactersUseDelimiter(list, delim, input);
					}
					else
					{
						StringBuilder  stringBuilder = new StringBuilder();
						String         ls = System.getProperty("line.separator");
						String[] inputLines = input.split(System.getProperty("line.separator"));
						for(String inputLine : inputLines)
						{
							stringBuilder.append( cutSpecifiedCharactersUseDelimiter(list, delim, inputLine) );
					        stringBuilder.append( ls );	
						}
						output += stringBuilder.toString();						
					}
					i++;
				}
			}
		}
		return output;
	}

	private String readFile(File file) throws Exception {
		// TODO Auto-generated method stub
		String         line = null;
		BufferedReader reader = new BufferedReader( new FileReader (file));
		StringBuilder  stringBuilder = new StringBuilder();
		String         ls = System.getProperty("line.separator");
		while((line = reader.readLine() ) != null )
		{
			stringBuilder.append( line );
	        stringBuilder.append( ls );
		}
		reader.close();
		return stringBuilder.toString();
	}
}
