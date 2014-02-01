package sg.edu.nus.comp.cs4218.impl.extended2;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import sg.edu.nus.comp.cs4218.extended2.IPasteTool;
import sg.edu.nus.comp.cs4218.impl.ATool;
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

	private String defaultDelim = "\t";
	private boolean fileNameError = false;
	
	public PASTETool(String[] arguments) {
		super(arguments);
		fileNameError = false;
		
	}

	@Override
	public String pasteSerial(String[] input) {
				
		
		return null;
	}

	@Override
	public String pasteUseDelimiter(String delim, String[] input) {
		
		ArrayList<ArrayList<String>> fileContent = new ArrayList<ArrayList<String>>();
		int longestFileIndex = 0;
		int maxLength = 0;
		
		
		for( int i = 0; i<input.length; i++){
			
			fileContent.add(loadLinesFromFile(input[i]));
			
			if(maxLength < fileContent.get(i).size()){
				maxLength = fileContent.get(i).size();
				longestFileIndex = i;
			}
		}
		
		String finalResult = "", pasteLines = "";
		
		for ( int j = 0; j<maxLength; j++){
			for(int i = 0; i < fileContent.size(); i++){
				pasteLines += fileContent.get(i) + delim;
			}
			
			finalResult += pasteLines + "\n";
		}
		
		return finalResult;
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
				+ " of sequentially corresponding lines of each given file,"
				+ " separated by a TAB character \n"
				+ "\nCommand Format - paste [OPTIONS] [FILE]"
				+ "\nFILE - Name of the file, when no file is present (denoted by \"-\") "
				+ "use standard input OPTIONS"
				+ "\n-s : paste one file at a time instead of in parallel"
				+ "\n-d DELIM: Use characters from the DELIM instead of TAB character"
				+ "\n-help : Brief information about supported options";
		
		return helpText;
	}

	@Override
	public String execute(File workingDir, String stdin) {
		
		ArrayList<String> args = new ArrayList<String>();
		String result = "";
				
		for(int i = 0; i<super.args.length; i++){
			args.add(super.args[i]);
		}
				
		//Priority for "-help"
		if(args.contains("-help")){
			result = getHelp();
			return result;
		}
		
		int minusSIndex = args.indexOf("-s");
		int minusDIndex = args.indexOf("-d");
		
		String[] fileNames = createFileNames(workingDir, super.args);
		if(fileNameError == true){
			return fileNames[0];
		}
		
		if(minusSIndex != -1 && minusDIndex != -1){		
			
			if(minusSIndex < minusDIndex){//-s option first
				result = pasteSerial(fileNames);
			}			
			else{//-d option first
				String delim = args.get(minusDIndex + 1);
				result = pasteUseDelimiter(delim, fileNames); 
			}
		}
		else if(minusSIndex != -1){//only "-s"
			result = pasteSerial(fileNames);
		}
		else if (minusDIndex != -1){//only "-d"
			String delim = args.get(minusDIndex + 1);
			result = pasteUseDelimiter(delim, fileNames); 
		}	
		else{//no options
			result = pasteUseDelimiter(defaultDelim, fileNames); 
		}
				
		return result;
	}
	
	private String[] createFileNames(File workingDir, String[] argsList){
		String[] names = new String[argsList.length];		
		String rootPath = workingDir.getAbsolutePath();
		
		for( int i = 0; i<argsList.length; i++){
			String completePath = "";
			completePath = rootPath + File.separator + argsList[i];
			
			File f = new File(completePath);
			if (!f.exists()){
				fileNameError = true;
				names[0] = argsList[i] + " does not exist!"; 
			}
			else{
				names[i] = completePath;
			}
			
		}
		
		return names;
	
	}
	
}
