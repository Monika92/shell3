/**
 * 
 */
package sg.edu.nus.comp.cs4218.impl.extended1;

import java.awt.List;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import sg.edu.nus.comp.cs4218.extended1.IGrepTool;
import sg.edu.nus.comp.cs4218.impl.ATool;
import sg.edu.nus.comp.cs4218.impl.ArgumentObject;
import sg.edu.nus.comp.cs4218.impl.ArgumentObjectParser;

/**
 * @author Admin
 *
 */
public class GREPTool extends ATool implements IGrepTool {

	protected static String GREP_FILE_ERR_MSG ;
	protected static int GREP_ERR_CODE ;
	String helpOutput,input,output,command;
	Boolean stdInFlag = false;
	Boolean fileInFlag = false;

	/**
	 * @param arguments
	 */
	public GREPTool(String[] arguments) {
		super(arguments);

		helpOutput = "Usage: grep [OPTIONS] PATTERN [FILE]\n"
				+ "Search for PATTERN in each FILE or standard input.\n"
				+ "PATTERN - This specifies a regular expression pattern that describes a set of strings.\n"
				+ "FILE - Name of the file, when no file is present (denoted by \"-\") uses standard input.\n"
				+ "OPTIONS\n"
				+ "	-A NUM : Print NUM lines of trailing context after matching lines\n"
				+ "	-B NUM : Print NUM lines of leading context before matching lines\n"
				+ "	-C NUM : Print NUM lines of output context\n"
				+ "	-c : Suppress normal output. Instead print a count of matching lines for each input file\n"
				+ "	-v : Select non-matching (instead of matching) lines\n";
		command = "grep";
	}

	/* */
	@Override
	public int getCountOfMatchingLines(String pattern, String input) {
		// TODO Auto-generated method stub
		int output = 0;
		if(pattern == null || input == null)
			setStatusCode(-1);
		else
		{
			if(!input.isEmpty())
			{
				String[] inputLines = input.split("\n");
				for(int i =0;i<inputLines.length;i++)
				{
					Pattern p = Pattern.compile(pattern);
					Matcher m = p.matcher(inputLines[i]);
					if(m.find()) 
					{
						output++;
					}
				}
			}
		}
		return output;
	}

	/* 
	 */
	@Override
	public String getOnlyMatchingLines(String pattern, String input) {
		// TODO Auto-generated method stub

		StringBuilder matchingLines = new StringBuilder();
		String output="";
		String ls = "\n";
		if(pattern == null || input == null)
			setStatusCode(-1);
		else
		{
			if(!input.isEmpty())
			{
				String[] inputLines = input.split("\n");
				for(int i =0;i<inputLines.length;i++)
				{
					Pattern p = Pattern.compile(pattern);
					Matcher m = p.matcher(inputLines[i]);
					if(m.find()) 
					{
						matchingLines.append(inputLines[i]);
						matchingLines.append(ls);
					}
				}
				output += matchingLines.toString();
			}
			//dont we need to set status code here
		}
		return output;
	}

	/*  */
	@Override
	public String getMatchingLinesWithTrailingContext(int option_A,
			String pattern, String input) {
		// TODO Auto-generated method stub
		Boolean matchFound = false;
		StringBuilder matchingLines = new StringBuilder();
		String output="";
		String ls = "\n";
		if(pattern == null || input == null || option_A < 0)
			setStatusCode(-1);
		else
		{
			if(!input.isEmpty())
			{
				String[] inputLines = input.split("\n");
				for(int i =0;i<inputLines.length;i++)
				{
					Pattern p = Pattern.compile(pattern);
					Matcher m = p.matcher(inputLines[i]);
					
					if(m.find()) 
					{
						
						matchFound = true;
						matchingLines.append(inputLines[i]);
						matchingLines.append(ls);
					}
					if(matchFound)
					{
						for(int j = 1; j<= option_A ;j++)
						{
							if(i+j <= inputLines.length-1)
							{
								matchingLines.append(inputLines[i+j]);
								matchingLines.append(ls);
							}
						}
						i+=option_A;
						matchFound = false;
					}
				}
				output += matchingLines.toString();
			}
		}
		return output;
	}

	/*  */
	@Override
	public String getMatchingLinesWithLeadingContext(int option_B,
			String pattern, String input) {
		// TODO Auto-generated method stub
		int start,tracker=0;
		StringBuilder matchingLines = new StringBuilder();
		String output="";
		String ls = "\n";
		if(pattern == null || input == null || option_B < 0)
			setStatusCode(-1);
		else
		{
			if(!input.isEmpty())
			{
				String[] inputLines = input.split("\n");
				for(int i =0;i<inputLines.length;i++)
				{
					Pattern p = Pattern.compile(pattern);
					Matcher m = p.matcher(inputLines[i]);
					if(m.find()) 
					{
						start = i-option_B;
						if(start<0)
							start = 0;
						if(start<tracker)
							start = tracker;
						for(int j = start ; j<= i ;j++)
						{
							matchingLines.append(inputLines[j]);
							matchingLines.append(ls);
						}
						tracker=i;
					}
				}
				output += matchingLines.toString();
			}
		}
		return output;
	}

	/**/
	@Override
	public String getMatchingLinesWithOutputContext(int option_C, String pattern, String input) {
		// TODO Auto-generated method stub
		/*
		output = getMatchingLinesWithLeadingContext(option_C,pattern,input);
		if(output!="")
		{
			output = output.substring(0, output.length()-1); //remove the last last newline
			//find if there is any other newline in the string
			if(output.contains("\n"))
			{
				//remove the last sentence ie the one with the match
				output = output.substring(output.lastIndexOf("\n")+1,output.length());
			}
			*/
				
		int start=0,tracker=0;
		StringBuilder matchingLines = new StringBuilder();
		String output="";
		String ls = "\n";
		if(pattern == null || input == null || option_C < 0)
			setStatusCode(-1);
		else
		{
			if(!input.isEmpty())
			{
				String[] inputLines = input.split("\n");
				for(int i =0;i<inputLines.length;i++)
				{
					Pattern p = Pattern.compile(pattern);
					Matcher m = p.matcher(inputLines[i]);
					if(m.find()) 
					{
						//leading lines
						
						start = i-option_C;
						if(start<0)
							start = 0;
						if(start<tracker)
							start = tracker;
						
						for(int j = start ; j<= i ;j++)
						{
							matchingLines.append(inputLines[j]);
							matchingLines.append(ls);
						}
						tracker=i;
					
						//trailing lines
						for(int k = 1; k<= option_C ;k++)
						{
							if(i+k <= inputLines.length-1)
							{
								matchingLines.append(inputLines[i+k]);
								matchingLines.append(ls);
							}
						}
						i+=option_C;
												
					}
				}
				output += matchingLines.toString();
			}
		}
		return output;
	}

	/* */
	@Override
	public String getMatchingLinesOnlyMatchingPart(String pattern, String input) {
		// TODO Auto-generated method stub
		StringBuilder matchingLines = new StringBuilder();
		String output="";
		String ls = "\n";
		if(pattern == null || input == null)
			setStatusCode(-1);
		else
		{
			String[] inputLines = input.split("\n");
			//System.out.println(inputLines.);
			for(int i =0;i<inputLines.length;i++)
			{
				Pattern p = Pattern.compile(pattern);
				Matcher m = p.matcher(inputLines[i]);
				int count = 1;
		   		while(m.find()) 
				{	
		   			matchingLines.append(m.group());
					matchingLines.append(ls);
				}
			}
			output += matchingLines.toString();	
		}
		return output;
	}

	/*  */
	@Override
	public String getNonMatchingLines(String pattern, String input) {
		// TODO Auto-generated method stub
		StringBuilder matchingLines = new StringBuilder();
		String output="";
		String ls = "\n";
		if(pattern == null || input == null)
			setStatusCode(-1);
		else
		{
			if(!input.isEmpty())
			{
				String[] inputLines = input.split("\n");
				for(int i =0;i<inputLines.length;i++)
				{
					Pattern p = Pattern.compile(pattern);
					Matcher m = p.matcher(inputLines[i]);
					if(!m.find()) 
					{
						matchingLines.append(inputLines[i]);
						matchingLines.append(ls);
					}
				}
				output += matchingLines.toString();	
			}

		}
		return output;
	}

	/*  */
	@Override
	public String getHelp() {
		// TODO Auto-generated method stub
		return helpOutput;
	}

	/* */
	@Override
	public String execute(File workingDir, String stdin) {
		// TODO Auto-generated method stub
		input = "";
		output = "";
		ArgumentObjectParser argumentObjectParser = new ArgumentObjectParser();
		ArgumentObject argumentObject = argumentObjectParser.parse(args,command);
		ArrayList<String> fileList = argumentObject.getFileList();
		ArrayList<String> options = argumentObject.getOptions();
		ArrayList<String> optionArguments = argumentObject.getOptionArguments();
		String pattern = argumentObject.getPattern();

		if (stdin != null) {
			input += stdin + "\n";
			stdInFlag = true;
		}
		if (fileList != null)
		{	
			for (String fileName : fileList) 
			{
				if (fileName != null && workingDir!=null) {
					if (fileName.startsWith(File.separator))
					{	
						// Do nothing
					}
					else
					{
						fileName = workingDir.toString() + File.separator
								+ fileName;
					}
					File file = new File(fileName);
					try {
						input += readFile(file) + "\n";
						fileInFlag = true;
					} catch (Exception e) {
						output += "File not found";
						setStatusCode(-1);
						return output;
					}
				}
			}
		}
		for (int i = 0; i < options.size(); ) {
			if (options.get(i).equalsIgnoreCase("-help"))
			{
				output = getHelp();
				i++;
			}
			else
			{
				if (options.get(i).equalsIgnoreCase("-v"))
				{
				output += getNonMatchingLines(pattern, input);
				i++;
				}	
				else if (options.get(i).equalsIgnoreCase("-o"))
				{
				output += getMatchingLinesOnlyMatchingPart(pattern, input);
				i++;
				}
				else if (options.get(i).equals("-c"))
				{
				output += getCountOfMatchingLines(pattern, input);
				i++;
				}
				else if (options.get(i).equals("-C"))
				{
				output += getMatchingLinesWithOutputContext(Integer.parseInt(optionArguments.get(i)), pattern, input);
				i++;
				}
				else if (options.get(i).equalsIgnoreCase("-A"))
				{
				output += getMatchingLinesWithTrailingContext(Integer.parseInt(optionArguments.get(i)), pattern, input);
				i++;
				}
				else if (options.get(i).equalsIgnoreCase("-B"))
				{
				output += getMatchingLinesWithLeadingContext(Integer.parseInt(optionArguments.get(i)), pattern, input);
				i++;
				}
			}	
		}
		return output;
	}

	/*Reads from the file specified and returns the contents of the file*/
	private String readFile(File file) throws Exception {
		// TODO Auto-generated method stub

		String line = null;
		BufferedReader reader = new BufferedReader(new FileReader(file));
		StringBuilder stringBuilder = new StringBuilder();
		String ls = "\n";
		while ((line = reader.readLine()) != null) {
			stringBuilder.append(line);
			stringBuilder.append(ls);
		}
		reader.close();
		return stringBuilder.toString();
	}

}
