package sg.edu.nus.comp.cs4218.impl.extended2;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import sg.edu.nus.comp.cs4218.IShell;
import sg.edu.nus.comp.cs4218.extended2.ISortTool;
import sg.edu.nus.comp.cs4218.impl.ATool;
import sg.edu.nus.comp.cs4218.impl.ArgumentObject;
import sg.edu.nus.comp.cs4218.impl.ArgumentObjectParser;

/**
 * Do not modify this file
 */
/*
 * 
 * sort : sort lines of text file
 *
 * Command Format - sort [OPTIONS] [FILE]
 *	FILE - Name of the file
 *	OPTIONS
 *		-c : Check whether the given file is already sorted, if it is not all sorted, print a
 *           diagnostic containing the first line that is out of order
 *	    -help : Brief information about supported options
 */

public class SORTTool extends ATool implements ISortTool{

	File filename;
	String options,helpOutput,output,input,command;
	int noOfArguments;
	
	public SORTTool(String[] arguments) {
		super(arguments);
		
		helpOutput = "sort [OPTIONS] [FILE]" + "\n" +
				"FILE : Name of the file" + "\n" +
				"OPTIONS : -c : Check whether the given file is already sorted, " +
				"if it is not all sorted, print a diagnostic containing the first " +
				"line that is out of order" + "\n" +
				"-help : Brief information about supported options" ;
		
		command = "sort";
		// TODO Auto-generated constructor stub
	}

	@Override
	public String sortFile(String input) {
		// TODO Auto-generated method stub
		String[] inputLines = input.split("\n");
		Arrays.sort(inputLines);
		StringBuilder builder = new StringBuilder();
		for(String s : inputLines) {
		    builder.append(s);
		    builder.append("\n");
		}
		return builder.toString();
	}

	@Override
	public String checkIfSorted(String input) {
		String[] inputLines = input.split("\n");
		
		int n=inputLines.length;

        for (int i=0;i< n-1;++i)
        	if (inputLines[i].compareTo(inputLines[i+1]) > 0)
                return inputLines[i] + " is out of order";

        return "Already sorted";
	}

	@Override
	public String getHelp() {
		// TODO Auto-generated method stub
		return helpOutput;
	}

	@Override
	public String execute(File workingDir, String stdin, IShell shell) {
		// TODO Auto-generated method stub
		input = "";
		output = "";
		ArgumentObjectParser argumentObjectParser = new ArgumentObjectParser();
		ArgumentObject argumentObject = argumentObjectParser.parse(args, command);
		ArrayList<String> fileList = argumentObject.getFileList();
		ArrayList<String> options = argumentObject.getOptions();
		
		if(!options.isEmpty())
		{
			for( int i = 0; i< options.size() ; i++)
			{
			if(options.get(i).equalsIgnoreCase("-help"))
			{
				output = getHelp();
				return output;
			}
			else if(options.get(i).equalsIgnoreCase("-c"))
			{
				for (String fileName : fileList)
				{
					File file = new File(fileName);
					try {
						input += readFile(file) + "\n";
						output = checkIfSorted(input);
					} catch (Exception e) {
						output = "File not found";
						setStatusCode(-1);
						return output;
					} 
					input = "";
				}					
			}
			else
			{
				output = "Invalid option";
				setStatusCode(-1);
				return output;
			}
			}
		}
		else	
		{
			for (String fileName : fileList)
			{
				File file = new File(fileName);
				try {
					input = readFile(file);
					String sortedFile = sortFile(input);
					writeFile(file,sortedFile);
					output = "sorted";
				} catch (Exception e) {
					output = "File not found";
					setStatusCode(-1);
					return output;
				} 
			}
		}
			
		
		return output;
	}
	
	private void writeFile(File file, String sortedFile) throws Exception {
		// TODO Auto-generated method stub
		BufferedWriter writer = new BufferedWriter( new FileWriter (file));
		writer.write(sortedFile);
		writer.close();
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
