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
import sg.edu.nus.comp.cs4218.impl.CommandVerifier;

/**
 * @author Admin
 * 
 */
public class GREPTool extends ATool implements IGrepTool {

	protected static String GREP_FILE_ERR_MSG;
	protected static int GREP_ERR_CODE;
	String helpOutput, input, output, command;
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

	/**
	 * This method returns the number of lines that contain a match to a given pattern
	 */
	@Override
	public int getCountOfMatchingLines(String pattern, String input) {
		// TODO Auto-generated method stub
		int output = 0;
		if (pattern == null || input == null)
			setStatusCode(-1);
		else {
			if (!input.isEmpty()) {
				String[] inputLines = input.split("\n");
				for (int i = 0; i < inputLines.length; i++) {
					Pattern p = Pattern.compile(pattern);
					Matcher m = p.matcher(inputLines[i]);
					if (m.find()) {
						output++;
					}
				}
			}
		}
		return output;
	}

	/**
	 * This method returns only the lines that contain a match to a given pattern
	 */
	@Override
	public String getOnlyMatchingLines(String pattern, String input) {
		// TODO Auto-generated method stub

		StringBuilder matchingLines = new StringBuilder();
		String output = "";
		String ls = "\n";
		if (pattern == null || input == null)
			setStatusCode(-1);
		else {
			if (!input.isEmpty()) {
				String[] inputLines = input.split("\n");
				for (int i = 0; i < inputLines.length; i++) {
					Pattern p = Pattern.compile(pattern);
					Matcher m = p.matcher(inputLines[i]);
					if (m.find()) {
						matchingLines.append(inputLines[i]);
						matchingLines.append(ls);
					}
				}
				output += matchingLines.toString();
			}
		}
		return output;
	}

	/**
	 * This method returns lines that contain a match to a given pattern and a specified number of lines after that
	 */
	@Override
	public String getMatchingLinesWithTrailingContext(int option_A,
			String pattern, String input) {
		// TODO Auto-generated method stub
		boolean flag = false;
		StringBuilder matchingLines = new StringBuilder();
		int tracker = -1;
		String output = "";
		String ls = "\n";
		if (pattern == null || input == null || option_A < 0)
			setStatusCode(-1);
		else {
			if (!input.isEmpty()) {
				String[] inputLines = input.split("\n");
				for (int i = 0; i < inputLines.length; i++) {
					Pattern p = Pattern.compile(pattern);
					Matcher m = p.matcher(inputLines[i]);

					if (m.find()) {
						for (int j = i; j <= i + option_A; j++) {
							if (j > tracker && j <= inputLines.length - 1) {
								matchingLines.append(inputLines[j]);
								matchingLines.append(ls);
							}
						}
						tracker = i + option_A;
						flag = true;
					} else {
						if (flag) {
							matchingLines.append("--");
							matchingLines.append(ls);
						}
						flag = false;
					}
				}
				if (matchingLines.length() > 0) {
					String lines = matchingLines.toString();
					if (lines.length() >= 3) {
						String doubleDash = lines.substring(lines.length() - 3,
								lines.length() - 1);
						if (doubleDash.equalsIgnoreCase("--"))
							output += lines.substring(0, lines.length() - 3);
						else
							output += lines;
					} else
						output += lines;
				}
			}
		}

		return output;
	}

	/**
	 * This method returns lines that contain a match to a given pattern and a specified number of lines before that
	 */
	@Override
	public String getMatchingLinesWithLeadingContext(int option_B,
			String pattern, String input) {
		// TODO Auto-generated method stub
		boolean flag = false;
		int tracker = -1;
		StringBuilder matchingLines = new StringBuilder();
		String output = "";
		String ls = "\n";
		if (pattern == null || input == null || option_B < 0)
			setStatusCode(-1);
		else {
			if (!input.isEmpty()) {
				String[] inputLines = input.split("\n");
				for (int i = 0; i < inputLines.length; i++) {
					Pattern p = Pattern.compile(pattern);
					Matcher m = p.matcher(inputLines[i]);
					if (m.find()) {
						for (int j = i - option_B; j <= i; j++) {
							if (j > tracker && j >= 0) {
								matchingLines.append(inputLines[j]);
								matchingLines.append(ls);
							}
						}
						tracker = i;
						flag = true;
					} else {
						if (flag) {
							matchingLines.append("--");
							matchingLines.append(ls);
						}
						flag = false;
					}
				}
				if (matchingLines.length() > 0) {
					String lines = matchingLines.toString();
					if (lines.length() >= 3) {
						String doubleDash = lines.substring(lines.length() - 3,
								lines.length() - 1);
						if (doubleDash.equalsIgnoreCase("--"))
							output += lines.substring(0, lines.length() - 3);
						else
							output += lines;
					} else
						output += lines;
				}
			}
		}
		return output;
	}

	/**
	 * This method returns lines that contain a match to a given pattern and a specified number of lines
	 * before and after that
	 */
	@Override
	public String getMatchingLinesWithOutputContext(int option_C,
			String pattern, String input) {
		// TODO Auto-generated method stub

		boolean flag = false;
		int tracker = -1;
		StringBuilder matchingLines = new StringBuilder();
		String output = "";
		String ls = "\n";
		if (pattern == null || input == null || option_C < 0)
			setStatusCode(-1);
		else {
			if (!input.isEmpty()) {
				String[] inputLines = input.split("\n");
				for (int i = 0; i < inputLines.length; i++) {
					Pattern p = Pattern.compile(pattern);
					Matcher m = p.matcher(inputLines[i]);
					if (m.find()) {
						// leading lines
						for (int j = i - option_C; j <= i; j++) {
							if (j > tracker && j >= 0) {
								matchingLines.append(inputLines[j]);
								matchingLines.append(ls);
							}
						}

						// trailing lines
						for (int j = i + 1; j <= i + option_C; j++) {
							if (j > tracker && j <= inputLines.length - 1) {
								matchingLines.append(inputLines[j]);
								matchingLines.append(ls);
							}
						}
						tracker = i + option_C;
						flag = true;
					} else {
						if (flag) {
							matchingLines.append("--");
							matchingLines.append(ls);
						}
						flag = false;
					}
				}
				if (matchingLines.length() > 0) {
					String lines = matchingLines.toString();
					if (lines.length() >= 3) {
						String doubleDash = lines.substring(lines.length() - 3,
								lines.length() - 1);
						if (doubleDash.equalsIgnoreCase("--"))
							output += lines.substring(0, lines.length() - 3);
						else
							output += lines;
					} else
						output += lines;
				}
			}
		}
		return output;
	}

	/**
	 * This method returns only the matching part of the lines that contain a match to a given pattern 
	 */
	@Override
	public String getMatchingLinesOnlyMatchingPart(String pattern, String input) {
		// TODO Auto-generated method stub
		StringBuilder matchingLines = new StringBuilder();
		String output = "";
		String ls = "\n";
		if (pattern == null || input == null)
			setStatusCode(-1);
		else {
			String[] inputLines = input.split("\n");
			// System.out.println(inputLines.);
			for (int i = 0; i < inputLines.length; i++) {
				Pattern p = Pattern.compile(pattern);
				Matcher m = p.matcher(inputLines[i]);
				int count = 1;
				while (m.find()) {
					matchingLines.append(m.group());
					matchingLines.append(ls);
				}
			}
			output += matchingLines.toString();
		}
		return output;
	}

	/**
	 * This method returns the lines which do not match a given pattern
	 */
	@Override
	public String getNonMatchingLines(String pattern, String input) {
		// TODO Auto-generated method stub
		StringBuilder matchingLines = new StringBuilder();
		String output = "";
		String ls = "\n";
		if (pattern == null || input == null)
			setStatusCode(-1);
		else {
			if (!input.isEmpty()) {
				String[] inputLines = input.split("\n");
				for (int i = 0; i < inputLines.length; i++) {
					Pattern p = Pattern.compile(pattern);
					Matcher m = p.matcher(inputLines[i]);
					if (!m.find()) {
						matchingLines.append(inputLines[i]);
						matchingLines.append(ls);
					}
				}
				output += matchingLines.toString();
			}

		}
		return output;
	}

	/**
	 * This method returns the help output
	 */
	@Override
	public String getHelp() {
		// TODO Auto-generated method stub
		return helpOutput;
	}

	/**
	 * This method executes the grep tool
	 * @Param working directory and standard input
	 * @return output of the tool
	 */
	@Override
	public String execute(File workingDir, String stdin) {
		// TODO Auto-generated method stub

		CommandVerifier cv = new CommandVerifier();
		int validCode = cv.verifyCommand("grep", super.args);

		if(validCode == -1){
			setStatusCode(-1);
			return "";
		}

		output = "";
		String prefixString;
		ArgumentObjectParser argumentObjectParser = new ArgumentObjectParser();
		ArgumentObject argumentObject = argumentObjectParser.parse(args,
				command);
		ArrayList<String> fileList = argumentObject.getFileList();
		ArrayList<String> options = argumentObject.getOptions();
		ArrayList<String> optionArguments = argumentObject.getOptionArguments();
		String pattern = argumentObject.getPattern();
		if(workingDir != null)
		{

			if(!workingDir.exists()){
				setStatusCode(-1);
				return "";
			}
			if (stdin != null && !stdin.isEmpty()) {
				input = stdin + "\n";
				//prefixString = "Standard Input:\n";
				prefixString = "";
				if (!options.isEmpty()) {
					String outputString = executeOptions(options, optionArguments, pattern,
							input);
					if(!outputString.trim().isEmpty())
						output+=prefixString + outputString;

				} else {
					String outputString = getOnlyMatchingLines(pattern, input);
					if(!outputString.trim().isEmpty())
						output+=prefixString + outputString;
				}
			}
			if (fileList != null) {

				for (String fileName : fileList) {

					fileInFlag = false;
					input = "";
					prefixString = fileName + ":";
					if (fileName != null) {
						if (!fileName.startsWith(File.separator)) {
							fileName = workingDir.toString() + File.separator
									+ fileName;
						}
						File file = new File(fileName);
						try {
							input += readFile(file) + "\n";
							fileInFlag = true;
						} catch (Exception e) {
							GREP_FILE_ERR_MSG = "Filenotfound";
							GREP_ERR_CODE =-1;
							setStatusCode(GREP_ERR_CODE);
						}
					}
					if (!options.isEmpty() && fileInFlag) {
						String outputString = executeOptions(options, optionArguments, pattern,input);
						if(!outputString.trim().isEmpty())
							output+=prefixString+outputString;
					} else {
						String outputString = getOnlyMatchingLines(pattern, input);
						if(!outputString.trim().isEmpty())
							output+=prefixString+outputString;
					}
				}
			}
		}
		return output;
	}

	private String executeOptions(ArrayList<String> options,
			ArrayList<String> optionArguments, String pattern, String input) {
		// TODO Auto-generated method stub

		String outputString ="";
		for (int i = 0; i < options.size();) {
			if (options.get(i).equalsIgnoreCase("-help")) {
				outputString = getHelp();
				i++;
			} else {
				if (options.get(i).equalsIgnoreCase("-v")) {
					outputString += getNonMatchingLines(pattern, input);
					i++;
				} else if (options.get(i).equalsIgnoreCase("-o")) {
					outputString += getMatchingLinesOnlyMatchingPart(pattern, input);
					i++;
				} else if (options.get(i).equals("-c")) {
					outputString += Integer.toString(getCountOfMatchingLines(pattern,input));
					i++;
				} else if (options.get(i).equals("-C")) {
					outputString += getMatchingLinesWithOutputContext(
							Integer.parseInt(optionArguments.get(i)), pattern,
							input);
					i++;
				} else if (options.get(i).equalsIgnoreCase("-A")) {
					outputString += getMatchingLinesWithTrailingContext(
							Integer.parseInt(optionArguments.get(i)), pattern,
							input);
					i++;
				} else if (options.get(i).equalsIgnoreCase("-B")) {
					outputString += getMatchingLinesWithLeadingContext(
							Integer.parseInt(optionArguments.get(i)), pattern,
							input);
					i++;
				}
			}
		}
		return outputString;
	}

	/**
	 * Reads from the file specified and returns the contents of the file
	 * @param file
	 * @throws Exception
	 */
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
