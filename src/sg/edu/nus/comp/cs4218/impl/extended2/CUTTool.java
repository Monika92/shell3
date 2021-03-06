package sg.edu.nus.comp.cs4218.impl.extended2;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import sg.edu.nus.comp.cs4218.extended2.ICutTool;
import sg.edu.nus.comp.cs4218.impl.ATool;
import sg.edu.nus.comp.cs4218.impl.ArgumentObject;
import sg.edu.nus.comp.cs4218.impl.ArgumentObjectParser;
import sg.edu.nus.comp.cs4218.impl.CommandVerifier;

/**
 * Do not modify this file
 */
public class CUTTool extends ATool implements ICutTool {

	File filename;
	String options, list, helpOutput, input, delim, output;
	int noOfArguments;
	String command;
	Boolean stdInFlag = false;
	Boolean fileInFlag = false;

	public CUTTool(String[] arguments) {
		super(arguments);

		helpOutput = "usage: cut [OPTIONS] [FILE]"
				+ "\n"
				+ "FILE : Name of the file, when no file is present"
				+ "\n"
				+ "OPTIONS : -c LIST : Use LIST as the list of characters to cut out. Items within "
				+ "the list may be separated by commas, "
				+ "and ranges of characters can be separated with dashes. "
				+ "For example, list 1-5,10,12,18-30 specifies characters "
				+ "1 through 5, 10,12 and 18 through 30"
				+ "\n"
				+ "-d DELIM: Use DELIM as the field-separator character instead of"
				+ "the TAB character" + "\n"
				+ "-help : Brief information about supported options";

		command = "cut";
		// TODO Auto-generated constructor stub
	}

	/* This method returns modified input after removing the characters mentioned in the list */
	@Override
	public String cutSpecfiedCharacters(String list, String input) {
		// TODO Auto-generated method stub
		try
		{
			if (list==null || input==null)
			{
				setStatusCode(-1);
				return "";	
			}
			else if (list.isEmpty() || input.isEmpty())
				return input;
			else {
				int fromNumber = 0, toNumber = 0;
				StringBuilder stringBuilder = new StringBuilder();
				String[] listNumbers = list.split(",");
				for (String listNumber : listNumbers) {
					if (listNumber.startsWith("-")) {
						fromNumber = 0;
						toNumber = Character.getNumericValue(listNumber.charAt(1));
					} else if (listNumber.endsWith("-")) {
						fromNumber = Character
								.getNumericValue(listNumber.charAt(0));
						toNumber = input.length();
					} else {
						String[] rangeNumbers = listNumber.split("-");
						if (rangeNumbers.length == 2) {
							fromNumber = Integer.parseInt(rangeNumbers[0]);
							toNumber = Integer.parseInt(rangeNumbers[1]);
						} else if (rangeNumbers.length == 1) {
							fromNumber = Integer.parseInt(rangeNumbers[0]);
							toNumber = Integer.parseInt(rangeNumbers[0]);
						}
					}
					if (toNumber > input.length())
						toNumber = input.length();
					for (int i = fromNumber; i <= toNumber; i++) {
						if (i > 0)
							stringBuilder.append(input.charAt(i - 1));
					}
				}
				return stringBuilder.toString();
			}
		}
		catch(Exception e)
		{
			setStatusCode(-1);
			return "";	
		}
	}
	
	/**
	 * This method cuts the specified characters from the input using delimiter
	 * @param Range of characters to cut, delimiter parameter,input
	 * @return the cut characters of input
	 */
	@Override
	public String cutSpecifiedCharactersUseDelimiter(String list, String delim,
			String input) {
		try{
			StringBuilder stringBuilder = new StringBuilder();	
			if(list==null || delim==null || input==null)
			{
				setStatusCode(-1);
				return  "";
			}
			else if (input.contains(delim) == false) {
				stringBuilder.append(input);
			} else {
				int fromNumber = 0, toNumber = 0;
				String[] words = input.split(delim);
				String[] listNumbers = list.split(",");
				for (String listNumber : listNumbers) {
					if (listNumber.startsWith("-")) {
						fromNumber = 0;
						toNumber = Character.getNumericValue(listNumber.charAt(1));
					} else if (listNumber.endsWith("-")) {
						fromNumber = Character
								.getNumericValue(listNumber.charAt(0));
						toNumber = words.length;
					} else {
						String[] rangeNumbers = listNumber.split("-");
						if (rangeNumbers.length == 2) {
							fromNumber = Integer.parseInt(rangeNumbers[0]);
							toNumber = Integer.parseInt(rangeNumbers[1]);
						} else if (rangeNumbers.length == 1) {
							fromNumber = Integer.parseInt(rangeNumbers[0]);
							toNumber = Integer.parseInt(rangeNumbers[0]);
						}
					}

					if (toNumber > words.length)
						toNumber = words.length;

					for (int i = fromNumber; i <= toNumber; i++) {
						if (i > 0 && i < toNumber)
							stringBuilder.append(words[i - 1] + delim);
						else if (i == toNumber)
							stringBuilder.append(words[i - 1]);
					}
				}
			}
			return stringBuilder.toString();
		}
		catch(Exception e)
		{
			setStatusCode(-1);
			return "";	
		}
	}

	/**
	 * This method returns the help output of cut
	 * @return This method returns the help output
	 */
	@Override
	public String getHelp() {
		// TODO Auto-generated method stub
		return helpOutput;
	}
	
	/**
	 * This method executes the cut command
	 * @param Working Directory and standard input
	 * @return Returns the executed output 
	 */
	@Override
	public String execute(File workingDir, String stdin) {
		
		CommandVerifier cv = new CommandVerifier();
		int validCode = cv.verifyCommand("cut", super.args);

		if(validCode == -1){
		setStatusCode(-1);
		return "";
		}
		
		try{
			input = ""; 
			output = "";
			ArgumentObjectParser argumentObjectParser = new ArgumentObjectParser();
			ArgumentObject argumentObject = argumentObjectParser.parse(args,
					command);
			ArrayList<String> fileList = argumentObject.getFileList();
			ArrayList<String> options = argumentObject.getOptions();
			ArrayList<String> optionArguments = argumentObject.getOptionArguments();

			// assign input value (std input or input from file)
			if(workingDir!=null){
				
				if(!workingDir.exists()){
					setStatusCode(-1);
					return "";

				}
			if (stdin != null) {
				input += stdin + "\n";
				stdInFlag = true;
			}
			if (fileList != null) {
				for (String fileName : fileList) {

					if (fileName != null) {
						if (fileName.startsWith(File.separator)) {
							// Do nothing
						} else {
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
				else if (options.get(i).equalsIgnoreCase("-c")) {
					list = optionArguments.get(i);
					StringBuilder stringBuilder = new StringBuilder();
					String ls = "\n";
					String[] inputLines = input.split("\n");
					for (String inputLine : inputLines) {
						stringBuilder
						.append(cutSpecfiedCharacters(list, inputLine));
						stringBuilder.append(ls);
					}
					output += stringBuilder.toString() ;
					i++;
				}
				else if (options.get(i).equalsIgnoreCase("-d")) {
					if (i + 1 < options.size()) {
						if (options.get(i + 1).equalsIgnoreCase("-f")) {
							list = optionArguments.get(i + 1);
							delim = optionArguments.get(i).replace("\"", "");
							StringBuilder stringBuilder = new StringBuilder();
							String ls = "\n";
							String[] inputLines = input.split("\n");
							for (String inputLine : inputLines) {
								stringBuilder
								.append(cutSpecifiedCharactersUseDelimiter(
										list, delim, inputLine));
								stringBuilder.append(ls);
							}
							output += stringBuilder.toString() ;
						}
					}
					i++;
				}
				else if (options.get(i).equalsIgnoreCase("-f")) {
					if (i + 1 < options.size()) {
						if (options.get(i + 1).equalsIgnoreCase("-d")) {
							list = optionArguments.get(i);
							delim = optionArguments.get(i + 1).replace("\"", "");
							StringBuilder stringBuilder = new StringBuilder();
							String ls = "\n";
							String[] inputLines = input.split("\n");
							for (String inputLine : inputLines) {
								stringBuilder
								.append(cutSpecifiedCharactersUseDelimiter(
										list, delim, inputLine));
								stringBuilder.append(ls);
							}
							output += stringBuilder.toString() ;
						}
					}
					i++;
				}
			}
			}
			return output;
		}
		catch(Exception e)
		{
			setStatusCode(-1);
			return "";		
		}
	}

	/**
	 * This method reads the file and returns its contents
	 * @param file
	 * @return contents of the file in String
	 * @throws Exception
	 */
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