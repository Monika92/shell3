package sg.edu.nus.comp.cs4218.impl.extended2;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import sg.edu.nus.comp.cs4218.IShell;
import sg.edu.nus.comp.cs4218.extended2.IUniqTool;
import sg.edu.nus.comp.cs4218.impl.ATool;
import sg.edu.nus.comp.cs4218.impl.ArgumentObject;
import sg.edu.nus.comp.cs4218.impl.ArgumentObjectParser;

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
	
	public UNIQTool(String[] arguments) {
		super(arguments);
		cachedline = "";
		// TODO Auto-generated constructor stub
	}

	@Override
	public String getUnique(boolean checkCase, String input) {
		// TODO Auto-generated method stub
		
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
		String pred, succ, curr; 
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
				if(succCheck || (!predCheck && !succCheck)){
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

	@Override
	public String getUniqueSkipNum(int NUM, boolean checkCase, String input) {
		// TODO Auto-generated method stub
		
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
			String[] temp_words = lines.get(j).split(" ");
			ArrayList<String> words = new ArrayList<String>();
			if(temp_words.length <= NUM)
				words.add("");
			else{
				for(int i = NUM; i < temp_words.length; i++){
					words.add(temp_words[i]);
				}
			}
			String word = "";
			for(String s : words)
				word += s;
			lines.set(j, word);
		}
		
		String str = "";
		for(String s : lines)
			str += s+"\n";
		result = getUnique(checkCase, str); 
		return result;
	}

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

	@Override
	public String execute(File workingDir, String stdin) {
		// TODO Auto-generated method stub
		
		//Check for stdin
		
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
			if(cachedline.equalsIgnoreCase(stdin))
					return "";
			else{
					cachedline = stdin;
					return cachedline;
			}
		}
		else{
			int j = 0; File file;
			while(j < fileList.size()){
				try{
					file = new File(fileList.get(j));
				} catch(Exception e){
					//System.out.println("Invalid file name");
					setStatusCode(-1);
					return result+"\nInvalid file name";
				}
				if (!file.exists()){
					setStatusCode(-1);
					//System.out.println("No such file");
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
									numArg = Integer.decode(optionArguments.get(i));
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
				
				j++;
			}
		}
		
		return result;
	}

}
