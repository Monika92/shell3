/*
 * Assumption for output
 * paste serial: if there is a file error (file read or file doesnt exist) in any of the args
 * output is [result (until that point of execution) + "file error!"]
 * 
 * paste with delim: if there is a file error (file read or file doesnt exist) in any of the args
 * output is ["file error!"]
 * 
 * if both options -s and -d exist in a command
 * priority is given to "-s" and -d is ignored
 * 
 * by default, paste is to use delimiter "\t"
 */

package sg.edu.nus.comp.cs4218.impl.extended2;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Pattern;

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

	/*
	 * Constructor for PASTETool - initializes the super class's arguments 
	 * with the passed arguments and also necessary global variables.
	 */
	public PASTETool(String[] arguments) {		
		super(arguments);
		fileError = false;
		executed = false;
		result = "";
	}

	/*
	 * Executes the paste command in serial mode.
	 */
	@Override
	public String pasteSerial(String[] input) {

		if(input == null || input.length == 0){
			setStatusCode(-1);
			return "";
		}

		String result = "";

		for(int i=0; i<input.length; i++){
			ArrayList<String> fileLines = loadLinesFromFile(input[i]);
			if(fileLines == null){
				setStatusCode(-1);
				return result + "file error!";
			}
			
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

	/**
	 * Uses the delimiter provided by the user to separator the output 
	 * while executing the paste command.
	 */
	@Override
	public String pasteUseDelimiter(String delim, String[] input) {
		
		if(delim == null){
			setStatusCode(-1);
			return "";
		}
		char[] delimiters = delim.toCharArray();

		ArrayList<ArrayList<String>> allFileContents = new ArrayList<ArrayList<String>>();
		int maxFileSize = 0;

		for( int i=0; i<input.length; i++){
			ArrayList<String> temp = loadLinesFromFile(input[i]); 
			if(temp == null){
				setStatusCode(-1);
				return "file error!";
			}
			
			allFileContents.add(temp);

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

	/**
	 * Loads the file given by the filename and returns 
	 * the file's lines as an array of strings.
	 */
	private ArrayList<String> loadLinesFromFile(String fname){
		ArrayList<String> lines = new ArrayList<String>();
		String line;

		if(fname == null || fname == ""){
			return null;
		}

		File f = new File(fname);		
		if(!f.exists()){
			return null;
		}

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
			return null;

		} catch (IOException e) {
			setStatusCode(-1);
			return null;
		}		
		return lines;
	}

	/**
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

	/**
	 * Resolving the paths of the filenames - absolute or relative and
	 * creating the files appropriately.
	 */
	public ArrayList<String> getCorrectFileNames(File workingDir, ArrayList<String> fNames){
		String name = "";
		ArrayList<String> names = new ArrayList<String>();
		
		if(workingDir == null || fNames == null){
			setStatusCode(-1);
			return null;
		}	

		if(!workingDir.exists()){
			setStatusCode(-1);
			return null;
		}
		
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
				/*
				fileError = true;
				names=new ArrayList<String>();
				names.add(fileName + ": No such file or directory!");
				return names;
				*/
				
				setStatusCode(-1);
				return null;
			}
		}
		return names;
	}

	/**
	 * Removes the stdin input from the argument list.
	 */
	private String[] removeStdinFromArg(String[] args){
		setStatusCode(0);
		String[] newArgs = new String[args.length -1];

		for(int i=0 ;i<args.length-1; i++){
			//System.out.println(args[i]);
			newArgs[i] = args[i];
		}
		return newArgs;
	}

	private String[] removeHyphenFromArgs(String[] args){
		ArrayList<String> list = new ArrayList<String>();
		
		for( int i = 0; i < args.length; i++){
			if(args[i].compareToIgnoreCase("-") != 0){
				list.add(args[i]);
			}			
		}
		
		String[] newArgs = new String[list.size()];
		for(int i = 0; i<list.size(); i++){
			newArgs[i] = list.get(i);
		}
		
		return newArgs;
	}
	
	/**
	 * Executes the paste command.
	 */
	@Override
	public String execute(File workingDir, String stdin) {

		String[] args = super.args;
		if(args == null){
			setStatusCode(-1);
			return "";
		}
		
		setStatusCode(0);
		result = "";

		if(executed == false){
			CommandVerifier cv = new CommandVerifier();
			int validCode = cv.verifyCommand("paste", super.args);

			if(validCode == -1){
				setStatusCode(-1);
				return "";
			}
			if(workingDir == null){
				setStatusCode(-1);
				return "";
			}	
			if(!workingDir.exists()){
				setStatusCode(-1);
				return "";
			}
		}

		/*
		// if stdin is not null and no prior execution has occured
		// remove "-" from args list
		if(stdin != null && executed == false){
			args = removeStdinFromArg(super.args);
		}	
		*/
		
		args = removeHyphenFromArgs(super.args);
		
		
		ArgumentObjectParser aop = new ArgumentObjectParser();
		ArgumentObject ao = aop.parse(args, "paste");
		ArrayList<String> fileNames = ao.getFileList();
		
		//handling case of "paste -help"
		if(ao.getOptions().size() == 1 && ao.getOptions().get(0).compareToIgnoreCase("-help") == 0){
			return getHelp();
		}
		
		//handling case of only stdin and no input
		if(executed == false && (fileNames == null || fileNames.size() == 0) ){
			executed = true;
		}
		
		//considering 2 cases
		//case 1: only stdin and no input files
		//case 2: stdin and prior input files have been printed already
		if(stdin != null && executed == true){
			return stdin;
		}
		
		fileNames = getCorrectFileNames(workingDir,fileNames);
		
		//Only when either workingDir/fileNames passed are null
		if(fileNames == null || fileNames.size() == 0){
			setStatusCode(-1);
			return "";
		}
		
		//when a file doesnt exist
		if(fileError){
			setStatusCode(-1);
			executed = true;
			return fileNames.get(0);
		}

		if(ao.getOptions().contains("-help")){
			result = getHelp();
			return result;
		}

		//if there are files to paste and they havent been pasted
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
			
			//after pasting files from file list, if stdin is not null
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
