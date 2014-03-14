package sg.edu.nus.comp.cs4218.impl.extended2;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import sg.edu.nus.comp.cs4218.extended2.IUniqTool;
import sg.edu.nus.comp.cs4218.impl.ATool;
import sg.edu.nus.comp.cs4218.impl.ArgumentObject;
import sg.edu.nus.comp.cs4218.impl.ArgumentObjectParser;
import sg.edu.nus.comp.cs4218.impl.CommandVerifier;


/**
 * Do not modify this file
 */
/*
 * 
 * uniq : Writes the unique lines in the given input. The input need not be sorted, but repeated input lines are detected only if they are adjacent.
 *
 * Command Format - uniq [OPTIONS] [FILE]
 * FILE - Name of the file, when no file is present (denoted by "-") use standard input
 * OPTIONS
 * 		-f NUM : Skips NUM fields on each line before checking for uniqueness. Use a null
 *             string for comparison if a line has fewer than n fields. Fields are sequences of
 *             non-space non-tab characters that are separated from each other by at least one
 *             space or tab.
 *      -i : Ignore differences in case when comparing lines.
 *      -help : Brief information about supported options
 */
public class UNIQTool extends ATool implements IUniqTool{

	static String cachedline = "";
	
/*
 * Constructor for UNIQTool - intialises the super class's arguments with the passed 
 * arguments.
 */
	public UNIQTool(String[] arguments) {
		super(arguments);
		cachedline = "";
		// TODO Auto-generated constructor stub
	}

/*
 * Returns the unique lines in the given input, ignoring case depending on the checkCase
 * argument
 */
	@Override
	public String getUnique(boolean checkCase, String input) {
		// TODO Auto-generated method stub
		
		//Check for null input
		if (input == null){
			setStatusCode(-1);
			return "";
		}
		
		//1. Separate input by \n
		//2. For each line, except the first and last, compare that line with its predecessor and successor
		//3. If similar to even one of them, remove it from the result (or both checks must be false for line to be in output)
		
		ArrayList<String> output = new ArrayList<String>();
		ArrayList<String> lines = new ArrayList<String>(); 
		String[] tempLines = input.split("\n");
		for(int i = 0; i < tempLines.length; i++){
			lines.add(tempLines[i]);
		}
		
		int ctr = 0; 
		String pred;
		String succ;
		String curr; 
		boolean predCheck = true; 
		boolean succCheck = true;
		
		if(lines.size() == 1)
			output.add(lines.get(0)+"\n"); 
		else if(lines.size()>1){
			while(ctr < lines.size()){
				if(ctr == 0){
					curr = lines.get(ctr);
					succ = lines.get(ctr+1);
					pred = null;
					predCheck = false;
				}
				else if(ctr == lines.size()-1){
					curr = lines.get(ctr);
					pred = lines.get(ctr-1);
					succ = null;
					succCheck = false;
				}
				else{
					curr = lines.get(ctr);
					pred = lines.get(ctr-1);
					succ = lines.get(ctr+1);
				}
				
				if(checkCase){											//Checking the case
					if(pred != null){
						predCheck = curr.equals(pred);
					}
					if(succ != null){
						succCheck = curr.equals(succ);
					}
				}
				else{													//Ignoring case for -i option
					if(pred != null){
						predCheck = curr.equalsIgnoreCase(pred);
					}
					if(succ != null){
						succCheck = curr.equalsIgnoreCase(succ);
					}
				}
				
				//Both check false, then add.
				//If only succ_check is true, then also add curr. 
				//If only pred_check is true, dont add.
				if((succCheck && !predCheck) || (!predCheck && !succCheck)){
					output.add(curr+"\n");
				}
				
				ctr++;
		
			}
		}
		
		String result = "";
		for(String s : output)
			result += s;
		return result;
		
	}

/*
 * Returns the unique lines in the given input after skipping NUM fields in every line
 * of the input. Case check is done depending on the checkCase argument.
 */
	@Override
	public String getUniqueSkipNum(int NUM, boolean checkCase, String input) {
		// TODO Auto-generated method stub
		
		//Check for null input
		if (input == null){
			setStatusCode(-1);
			return "";
		}
				
		//1. Split the input by \n into lines
		//2. For each line, split by delimiter " " or "\t"
		//3. Initialize argument for getUnique() by skipping NUM elements in the list of words in every line
		
		String result = "";
		String[] tempLines = input.split("\n");
		ArrayList<String> lines = new ArrayList<String>();
		for(int i = 0; i < tempLines.length; i++){
			lines.add(tempLines[i]);
		}
		
		for(int j = 0; j < lines.size(); j++){
			String[] tempWords = lines.get(j).split(" ");
			ArrayList<String> words = new ArrayList<String>();
			if(tempWords.length <= NUM)
				words.add("");
			else{
				for(int i = NUM; i < tempWords.length; i++){
					words.add(tempWords[i]);
				}
			}
			String word = "";
			for(String s : words)
				word += s + " ";
			lines.set(j, word.trim());
		}
		
		String str = "";
		for(String s : lines){
			if(s.equalsIgnoreCase(""))
				str += "";
			else
				str += s+"\n";
		}
		result = getUnique(checkCase, str); 
		return result;
	}

/*
 * Returns the help message for Uniq command
 */
	@Override
	public String getHelp() {
		// TODO Auto-generated method stub
		String helpMsg = "uniq : Writes the unique lines in the given input, with repetitions compares only in adjacent input lines.";
		helpMsg += "\nCommand Format - uniq [OPTIONS] [FILE]\nFILE - Name of the file. Alternatively use \"-\" to enter standard input.";
		helpMsg += "\nOPTIONS\n\t-f NUM : Skips NUM fields on each line before checking for uniqueness. Fields are sequences of non-space non-tab characters that are separated from each other by at least one space or tab.";
		helpMsg += "\n\t-i : Ignore differences in case when comparing lines.";
		helpMsg += "\n\t-help : Brief information about supported options";
				 
		return helpMsg;
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
 * Method to write to an output file
 */
	public boolean writeOutputToFile(File outputFile, String input){
    	//Check for output file
		try{
			if(outputFile.exists())
				outputFile.delete();
			outputFile.createNewFile();
			FileWriter fw = new FileWriter(outputFile.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			char[] temp = input.toCharArray(); int i = 0;
			while(i<temp.length){
				while(temp[i]!='\n'){
					bw.write(temp[i]);
					i++;
					if(i>=temp.length)
						break;
				}
				bw.newLine(); i++;
			}
			bw.close();
		} catch (IOException e){
		}
		return true;	    
    }

	
/*
 * Executes the uniq command for the given input - either stdin or input file
 */
	@Override
	public String execute(File workingDir, String stdin) {
		
		//Verify command syntax
		CommandVerifier cv = new CommandVerifier();
		int validCode = cv.verifyCommand("uniq", super.args);
		if(validCode == -1){
			setStatusCode(-1);
			return "";
		}

		//Check for valid workingDir
		if(workingDir == null)
		{
			setStatusCode(-1);
			return "";
		}	
		if(!workingDir.exists()){
			setStatusCode(-1);
			return "";
		}
		
		ArgumentObjectParser argumentObjectParser = new ArgumentObjectParser();
		ArgumentObject argumentObject = argumentObjectParser.parse(args, "uniq");
		ArrayList<String> fileList = argumentObject.getFileList();
		ArrayList<String> options = argumentObject.getOptions();
		ArrayList<String> optionArguments = argumentObject.getOptionArguments();
		
		String result = "" ; 
		String input = "";
		boolean checkCase = true;
	
		//Getting input from stdin or file
		if (stdin != null){
			//Check for \n
			if(!(stdin.contains("\n"))){
				if(cachedline.equalsIgnoreCase(stdin))
						return "";
				else{
						cachedline = stdin;
						return cachedline;
				}
			}
			else{
				File uniq_input_file = new File("uniq_temp_input_file.txt");
				writeOutputToFile(uniq_input_file, stdin);
				String[] temp_args = {"uniq_temp_input_file.txt"};
				super.args = temp_args;
				if (fileList.contains("-"))
					fileList.remove("-");
				
				fileList.add("uniq_temp_input_file.txt");
			}
		}
		
		
		int j = 0; File file, filePath; String fileName;
		while(j < fileList.size()){
			try{
				fileName = fileList.get(j);
				filePath = new File(fileName);
				if(filePath.isAbsolute()){
					file = new File(filePath.getPath());
				}
				else{
					file = new File(workingDir.toString()+File.separator+fileName);
				}
			} catch(Exception e){
				//System.out.println("Invalid file name");
				setStatusCode(-1);
				if (result.equalsIgnoreCase(""))
					return "Invalid file name";
				else if (result.endsWith("\n"))
					return result + "Invalid file name";
				else
					return result+"\nInvalid file name";
			}
			if (!file.exists()){
				setStatusCode(-1);
				//System.out.println("No such file");
				if (result.equalsIgnoreCase(""))
					return "No such file";
				else if(result.endsWith("\n"))
					return result + "No such file";
				else
					return result+"\nNo such file";
			}
			input = readFromFile(file);
			
			//Applying uniq to every file
			if (options!=null){
				
				int i = 0; Integer numArg;
				
				//Check for --help first
				if (options!=null){
					if(options.contains("-help")){
					result = getHelp();
					return result;
					}
				}
				
				if(options.contains("-i"))
					checkCase = false;
				if(options.contains("-f")){
					while(i < options.size()){
							if(options.get(i).equalsIgnoreCase("-f")){
								try{
									numArg = Integer.decode(optionArguments.get(i));
									if(numArg < 0){
										setStatusCode(-1);
										if (result.equalsIgnoreCase(""))
											return "Invalid argument for -f";
										else if(result.endsWith("\n"))
											return result + "Invalid argument for -f";
										else
											return result + "\nInvalid argument for -f";
									}
								}catch(NumberFormatException e){
									setStatusCode(-1);
									if (result.equalsIgnoreCase(""))
										return "Invalid argument for -f";
									else if(result.endsWith("\n"))
										return result + "Invalid argument for -f";
									else
										return result + "\nInvalid argument for -f";
								}
								result += getUniqueSkipNum(numArg, checkCase, input);
							}
							i++;
					}
				}
				else
					result += getUnique(checkCase, input);
			}
			else
				result += getUnique(checkCase, input);
			result = result.trim(); result = result + "\n";
			j++;
		}
		
		//Remvoe uniq_te
		return result.trim();
	}

}
