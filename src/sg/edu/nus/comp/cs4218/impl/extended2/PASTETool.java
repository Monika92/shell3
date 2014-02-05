package sg.edu.nus.comp.cs4218.impl.extended2;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import sg.edu.nus.comp.cs4218.IShell;
import sg.edu.nus.comp.cs4218.extended2.IPasteTool;
import sg.edu.nus.comp.cs4218.impl.ATool;
import sg.edu.nus.comp.cs4218.impl.ArgumentObject;
import sg.edu.nus.comp.cs4218.impl.ArgumentObjectParser;
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

	public PASTETool(String[] arguments) {		
		super(arguments);
		fileError = false;
		toggleBit = 0;
		executed = false;
		result = "";
	}

	@Override
	public String pasteSerial(String[] input) {

		String result = "";

		for(int i=0; i<input.length; i++){
			ArrayList<String> fileLines = loadLinesFromFile(input[i]);
			for(String e : fileLines){
				result += e + "\t";
			}
			result += "\n";
		}
		return result;
	}

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
			for(int j=0; j<input.length; j++){
				String term = "";
				if(allFileContents.get(j).size() > i){
					term = allFileContents.get(j).get(i);
				}
				result += term + delimiters[delimCount % numDelim];
			}
			result += "\n";
		}

		return result;
	}

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

			e.printStackTrace();
		} catch (IOException e) {

			e.printStackTrace();
		}		
		return lines;
	}

	@Override
	public String getHelp() {

		String helpText = "paste : writes to standard output lines "
				+ "\n* of sequentially corresponding lines of each given file,"
				+ "\n* separated by a TAB character \n"
				+ "\n* Command Format - paste [OPTIONS] [FILE]"
				+ "\n* FILE - Name of the file, when no file is present (denoted by \"-\") "
				+ "\n* use standard input OPTIONS"
				+ "\n -s : paste one file at a time instead of in parallel"
				+ "\n -d DELIM: Use characters from the DELIM instead of TAB character"
				+ "\n -help : Brief information about supported options";

		return helpText;
	}

	private ArrayList<String> getCorrectFileNames(File workingDir,ArrayList<String> fNames){
		String name = "";
		ArrayList<String> names = new ArrayList<String>();

		for(int i=0; i<fNames.size(); i++){

			String[] nameSplit = fNames.get(i).split(File.separator);
			String fileName = nameSplit[nameSplit.length -1];

			if(fNames.get(i).startsWith("//")){
				name = fNames.get(i);
			}
			else{
				name = workingDir.getAbsolutePath() + File.separator + fNames.get(i);
			}

			if((new File(name)).exists()){
				names.add(name);
			}
			else{
				names=new ArrayList<String>();
				names.add(fileName + ": No such file or directory!");
			}
		}
		return names;
	}

	private String[] removeStdinFromArg(String[] args){
		
		String[] newArgs = new String[args.length -1];
		
		for(int i=0 ;i<args.length-1; i++){
			newArgs[i] = args[i];
		}
		
		return newArgs;
	}

	@Override
	public String execute(File workingDir, String stdin) {

		String[] args = super.args;
		if(stdin != null && toggleBit == 0){
			args = removeStdinFromArg(super.args);
			toggleBit = 1;
		}
		
		result = "";
		
		ArgumentObjectParser aop = new ArgumentObjectParser();
		ArgumentObject ao = aop.parse(args, "paste");

		ArrayList<String> fileNames = ao.getFileList();
		fileNames = getCorrectFileNames(workingDir,fileNames);

		if(fileError == true){
			return fileNames.get(0);
		}

		if(fileNames.size() != 0 && executed == false){

			//priority to -help option
			if(ao.getOptions().contains("-help")){
				result = getHelp();
			}

			String[] fNames = new String[fileNames.size()];
			for(int i=0; i<fileNames.size();i++){
				fNames[i] = fileNames.get(i);
			}

			//priority to -s
			if(ao.getOptions().contains("-s")){
				result = pasteSerial(fNames);
			}

			int delimIdx = ao.getOptions().indexOf("-d");
			String delim = ao.getOptionArguments().get(delimIdx);

			if(ao.getOptions().contains("-d")){
				result = pasteUseDelimiter(delim, fNames);
			}
			
			executed = true;
		}
		
		if(stdin != null && executed == true){
			if(result != ""){
				result = result + "\n" + stdin;
			}
			else{
				result = stdin;
			}
		}
		
		return result;
	}

}
