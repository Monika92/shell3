package sg.edu.nus.comp.cs4218.impl.extended2;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import sg.edu.nus.comp.cs4218.extended2.ICutTool;
import sg.edu.nus.comp.cs4218.impl.ATool;
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

	public CUTTool(String[] arguments) {
		super(arguments);

		helpOutput = "usage: cut -b list [-n] [file ...] \n  "
				+ "cut -c list [file ...] \n "
				+ "cut -f list [-s] [-d delim] [file ...]";
		// TODO Auto-generated constructor stub
	}

	@Override
	public String cutSpecfiedCharacters(String list, String input) {
		// TODO Auto-generated method stub
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
		String output = helpOutput;
		if(args!=null)
		{
			noOfArguments = args.length;
		}
		
		for(int i=0 ; i< noOfArguments ;i++)
		{
			
			if(args[i].equalsIgnoreCase("-help"))
			output = getHelp();

			else if(args[i].equalsIgnoreCase("-c"))
			{
				list = args[i+1];
				if(args[i+2].equalsIgnoreCase("-"))
				{
					input = stdin;
					output = cutSpecfiedCharacters(list,input);
				}
				else
				{
					File file = new File(args[i+2]);
					try 
					{
							input = readFile(file);
							StringBuilder  stringBuilder = new StringBuilder();
							String         ls = System.getProperty("line.separator");
							String[] inputLines = input.split(System.getProperty("line.separator"));
							for(String inputLine : inputLines)
							{
								stringBuilder.append( cutSpecfiedCharacters(list, inputLine) );
						        stringBuilder.append( ls );									
							}
							output = stringBuilder.toString();
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						output = "File not found";
					} catch (IOException e) {
						// TODO Auto-generated catch block
						output = "Invalid file";
					}							
				}
				i+=2;
			}
			else if(args[i].equalsIgnoreCase("-d"))
			{
				delim = args[i+1].replace("\"","");
				if(args[i+2].equalsIgnoreCase("-f"))
				{
					list = args[i+3];
					if(args[i+4].equalsIgnoreCase("-"))
					{
						input = stdin;
						output = cutSpecifiedCharactersUseDelimiter(list, delim, input);
					}
					else
					{
						File file = new File(args[i+4]);
						try {
							input = readFile(file);
							StringBuilder  stringBuilder = new StringBuilder();
							String         ls = System.getProperty("line.separator");
							String[] inputLines = input.split(System.getProperty("line.separator"));
							for(String inputLine : inputLines)
							{
								stringBuilder.append( cutSpecifiedCharactersUseDelimiter(list, delim, inputLine) );
						        stringBuilder.append( ls );									
							}
							output = stringBuilder.toString();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							output = "Invalid file";
						}
					}
				}
				i+=4;
			}
			else if(args[i].equalsIgnoreCase("-f"))
			{
				list = args[i+1];
				if(args[i+2].equalsIgnoreCase("-d"))
				{
					delim = args[i+3];
					if(args[i+4].equalsIgnoreCase("-"))
					{
						input = stdin;
						output = cutSpecifiedCharactersUseDelimiter(list, delim, input);
					}
					else
					{
						File file = new File(args[i+4]);
						try {
							input = readFile(file);
							StringBuilder  stringBuilder = new StringBuilder();
							String         ls = System.getProperty("line.separator");
							String[] inputLines = input.split(System.getProperty("line.separator"));
							for(String inputLine : inputLines)
							{
								stringBuilder.append( cutSpecifiedCharactersUseDelimiter(list, delim, inputLine) );
						        stringBuilder.append( ls );									
							}
							output = stringBuilder.toString();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							output = "Invalid file";
						}
					}
				}
				i+=4;
			}
		}
		return output;
	}

	private String readFile(File file) throws IOException {
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
