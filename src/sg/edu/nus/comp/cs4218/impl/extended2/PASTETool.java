package sg.edu.nus.comp.cs4218.impl.extended2;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Pattern;

import sg.edu.nus.comp.cs4218.IShell;
import sg.edu.nus.comp.cs4218.ITool;
import sg.edu.nus.comp.cs4218.extended2.IPasteTool;
import sg.edu.nus.comp.cs4218.impl.ATool;
import sg.edu.nus.comp.cs4218.impl.ArgumentObject;
import sg.edu.nus.comp.cs4218.impl.ArgumentObjectParser;
import sg.edu.nus.comp.cs4218.impl.CommandVerifier;
/**
 * Do not modify this file
 */
/*
 * 
 * paste : writes to standard output lines consisting of sequentially corresponding 
 * lines of each given file, separated by a TAB character
 * 
 * Command Format - paste [OPTIONS] [FILE]
 * 		FILE - Name of the file, when no file is present (denoted by "-") use standard input OPTIONS
 * 		-s : paste one file at a time instead of in parallel
 * 		-d DELIM: Use characters from the DELIM instead of TAB character
 * 		-help : Brief information about supported options
 */

public class PASTETool extends ATool implements IPasteTool{

	private boolean fileError = false;
	private static boolean executed;
	private static String result;
	private static int toggleBit; 

/*
 * Constructor for PASTETool - initializes the super class's arguments 
 * with the passed arguments and also necessary global variables.
 */
	public PASTETool(String[] arguments) {		
		super(arguments);
		fileError = false;
		toggleBit = 0;
		executed = false;
		result = "";
	}

/*
 * Executes the paste command in serial mode.
 */
	@Override
	public String pasteSerial(String[] input) {

		String result = "";

		for(int i=0; i<input.length; i++){
			ArrayList<String> fileLines = loadLinesFromFile(input[i]);
			for(int j = 0; j<fileLines.size(); j++){
				
				if( j != fileLines.size() -1){
					result += fileLines.get(j) + "\t";
				}
				else{
					result += fileLines.get(j);
				}
			}
			
			if( i != input.length -1){
				result += "\n";
			}
			
		}
		return result;
	}

/*
 * Uses the delimiter provided by the user to separator the output 
 * while executing the paste command.
 */
	@Override
	public String pasteUseDelimiter(String delim, String[] input) {

		char[] delimiters = delim.toCharArray();

		ArrayList<ArrayList<String>> allFileContents = new ArrayList<ArrayList<String>>();
		int maxFileSize = 0;
		
		for( int i=0; i<input.length; i++){
			allFileContents.add(loadLinesFromFile(input[i]));

			if(allFileContents.get(i).size() > maxFileSize){
				maxFileSize = allFileContents.get(i).size();
			}
		}

		String result = "";
		int delimCount = 0, numDelim = delimiters.length;

		for(int i=0; i<maxFileSize; i++){
			delimCount = 0;
			
			for(int j=0; (j<input.length) && (input.length != 1); j++,delimCount++){
				String term = "";
				if(allFileContents.get(j).size() > i){
					term = allFileContents.get(j).get(i);
				}
				if(j != input.length-1){
					result += term + delimiters[delimCount % numDelim];
					
				}
				else{
					result += term;
				}
			}
			
			if(input.length == 1){
				result += allFileContents.get(0).get(i);
			}
			
			if(i != maxFileSize-1){
				result += "\n";
			}
		}

		return result;
	}

/*
 * Loads the file given by the filename and returns 
 * the file's lines as an array of strings.
 */
	private ArrayList<String> loadLinesFromFile(String fname){
		ArrayList<String> lines = new ArrayList<String>();

		String line;
		File f = new File(fname);
		try {
			BufferedReader br = new BufferedReader(new FileReader(f));
			line = br.readLine();

			while(line!=null){
				lines.add(line);
				line = br.readLine();
			}

			br.close();
		} catch (FileNotFoundException e) {
			
			setStatusCode(-1);
			e.printStackTrace();
		} catch (IOException e) {
			setStatusCode(-1);
			e.printStackTrace();
		}		
		return lines;
	}

/*
 * Returns the help message for paste command.
 */
	@Override
	public String getHelp() {

		String helpText = "paste : writes to standard output lines "
				+ "\n* of sequentially corresponding lines of each given file,"
				+ "\n* separated by a TAB character"
				+ "\n* Command Format - paste [OPTIONS] [FILE]"
				+ "\n* FILE - Name of the file, when no file is present (denoted by \"-\") "
				+ "\n* use standard input OPTIONS"
				+ "\n -s : paste one file at a time instead of in parallel"
				+ "\n -d DELIM: Use characters from the DELIM instead of TAB character"
				+ "\n -help : Brief information about supported options";

		return helpText;
	}

/*
 * Resolving the paths of the filenames - absolute or relative and
 * creating the files appropriately.
 */
	public ArrayList<String> getCorrectFileNames(File workingDir, ArrayList<String> fNames){
		String name = "";
		ArrayList<String> names = new ArrayList<String>();

		for(int i=0; i<fNames.size(); i++){
			
			String arg = fNames.get(i);
			String pattern = Pattern.quote(System.getProperty("file.separator"));
			String[] nameSplit = arg.split(pattern);
			String fileName = "";
			
			if(nameSplit.length == 1){
				fileName = nameSplit[0];
			}
			else{
				fileName = nameSplit[nameSplit.length -1];
			}

			File fTemp = new File(fNames.get(i));
			if(fTemp.isAbsolute()){
				name = fNames.get(i);
			}
			else{
				name = workingDir.getAbsolutePath() + File.separator + fNames.get(i);
			}
			
			if((new File(name)).exists()){
				names.add(name);
			}
			else{
				fileError = true;
				names=new ArrayList<String>();
				names.add(fileName + ": No such file or directory!");
				return names;
			}
		}
		return names;
	}

/*
 * Removes the stdin input from the argument list.
 */
	private String[] removeStdinFromArg(String[] args){
		
		setStatusCode(0);
		String[] newArgs = new String[args.length -1];
		
		for(int i=0 ;i<args.length-1; i++){
			newArgs[i] = args[i];
		}
		
		return newArgs;
	}

/*
 * Executes the paste command.
 */
	@Override
	public String execute(File workingDir, String stdin) {

		
		String[] args = super.args;
		setStatusCode(0);
		result = "";
		
		CommandVerifier cv = new CommandVerifier();
		int validCode = cv.verifyCommand("paste", super.args);
		
		if(validCode == -1){
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
		
		/*
		if(stdin != null && toggleBit == 0){
			args = removeStdinFromArg(super.args);
			toggleBit = 1;
		}
		*/
		if(stdin != null && executed == false){
			args = removeStdinFromArg(super.args);
		}
		
		
		
		ArgumentObjectParser aop = new ArgumentObjectParser();
		ArgumentObject ao = aop.parse(args, "paste");

		ArrayList<String> fileNames = ao.getFileList();
		fileNames = getCorrectFileNames(workingDir,fileNames);

		if(fileError){
			setStatusCode(-1);
			return fileNames.get(0);
		}
		
		if(ao.getOptions().contains("-help")){
			result = getHelp();
			return result;
		}

		if(!fileNames.isEmpty() && executed == false){

			String[] fNames = new String[fileNames.size()];
			for(int i=0; i<fileNames.size();i++){
				fNames[i] = fileNames.get(i);
			}

			//priority to -s
			if(ao.getOptions().contains("-s")){
				result = pasteSerial(fNames);
			}					
			else if(ao.getOptions().contains("-d")){
				int delimIdx = ao.getOptions().lastIndexOf("-d");
				String delim = ao.getOptionArguments().get(delimIdx);
				result = pasteUseDelimiter(delim, fNames);
			}
			else {
				//No options
				result = pasteUseDelimiter("\t", fNames);				
			}
			
			executed = true;
			
			if(stdin != null){
				result += "\n" + stdin;
			}
		}	
		else if(stdin != null){
			result = stdin;
		}
		
		return result;
	}

}
