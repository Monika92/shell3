package sg.edu.nus.comp.cs4218.impl.extended2;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import sg.edu.nus.comp.cs4218.extended2.ICommTool;
import sg.edu.nus.comp.cs4218.impl.ATool;
import sg.edu.nus.comp.cs4218.impl.ArgumentObject;
import sg.edu.nus.comp.cs4218.impl.ArgumentObjectParser;
import sg.edu.nus.comp.cs4218.impl.CommandVerifier;

/**
 * Do not modify this file
 **/

/*
 * comm : Compares two sorted files line by line. With no options, produce three-column output. 
 * 		 Column one contains lines unique to FILE1, column two contains lines unique to FILE2, 
 * 		 and column three contains lines common to both files.
 *	
 *	Command Format - comm [OPTIONS] FILE1 FILE2
 *	FILE1 - Name of the file 1
 *	FILE2 - Name of the file 2
 *		-c : check that the input is correctly sorted
 *      -d : do not check that the input is correctly sorted
 *      -help : Brief information about supported options
 */

public class COMMTool extends ATool implements ICommTool{

	private int checkOrderFlag = 0;

	/*
	 * Constructor for COMMTool - initializes the super class's arguments 
	 * with the passed arguments.
	 */
	public COMMTool(String[] arguments) {	
		super(arguments);
	}

	/*
	 * Helper method to remove "-" from all args if present
	 */
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
	
	/*Executes the "comm" command*/
	@Override
	public String execute(File workingDir, String stdin) {

		CommandVerifier cv = new CommandVerifier();
		int validCode = cv.verifyCommand("comm", super.args);

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

		//Following case shouldn't occur as Cmd Verifier
		//checks if Args are correct for command.
		String[] userArgs = super.args;
		if(userArgs == null){
			setStatusCode(-1);
			return "";
		}

		String fileName1 = "", fileName2 = "";
		ArgumentObjectParser aop = new ArgumentObjectParser();

		ArgumentObject ao = aop.parse(userArgs, "comm");
		if(ao == null || ao.getFileList().size() != 2){
			setStatusCode(-1);
			return "";
		}

		fileName1 = ao.getFileList().get(0);
		fileName2 = ao.getFileList().get(1);

		//priority to -help option
		if(ao.getOptions().contains("-help")){
			return getHelp();
		}

		String result = "";

		String filePath1 = getCorrectPathFromArg(workingDir,fileName1);
		String filePath2 = getCorrectPathFromArg(workingDir,fileName2);

		if(filePath1 == null && filePath2 == null){
			setStatusCode(-1);
			return "";
		}
		else if(filePath1 == null){
			result = "";
			setStatusCode(-1);
			return result;

		}
		else if (filePath2 == null){
			result = "";
			setStatusCode(-1);
			return result;
		}

		//priority to "-d" option
		if(ao.getOptions().contains("-d")){
			result = compareFilesDoNotCheckSortStatus(filePath1, filePath2);
			return result;
		}

		if(ao.getOptions().contains("-c")){
			result = compareFilesCheckSortStatus(filePath1,filePath2);
			return result;
		}

		result = compareFiles(filePath1, filePath2);

		return result;
	}


	/**
	* Checks whether the input filename is an absolute path
	* and creates the file with the appropriate path.
	*/

	private String getCorrectPathFromArg(File workingDir,String fName){
		String name = null;

		if(!workingDir.exists() || fName == null){
			setStatusCode(-1);
			return null;
		}

		File fTemp = new File(fName);
		if(fTemp.isAbsolute()){
			name = fName;
		}
		else{
			name = workingDir.getAbsolutePath() + File.separator + fName;
		}

		fTemp = new File(name);
		if(fTemp.exists() && fTemp.isFile()){
			return name;
		}
		return null;
	}

	/**
	 * Compares the two given inputs and returns a string with the differences
	 * between the corresponding lines of the two strings.
	 */
	@Override
	public String compareFiles(String input1, String input2) {

		if(input1 == null || input2 == null){
			setStatusCode(-1);
			return "";
		}

		ArrayList<String> fileLines1 = loadFile(input1);
		ArrayList<String> fileLines2 = loadFile(input2);

		if(fileLines1 == null || fileLines2 == null){
			setStatusCode(-1);
			return "";
		}

		String result = "";
		boolean unsorted = false;

		int i=0,j=0;
		for( i=0,j=0; i<fileLines1.size() && j<fileLines2.size() && !unsorted;){

			String val1 = fileLines1.get(i);
			String val2 = fileLines2.get(j);

			//include check for option --check-order
			if(checkOrderFlag != 0){
				if((i+1) <fileLines1.size() && val1.compareTo(fileLines1.get(i+1))>0){
					result += "File 1 not sorted!\n";
					unsorted = true;
					break;
				}
				else if((j+1) <fileLines2.size() && val2.compareTo(fileLines2.get(j+1))>0){
					result += "File 2 not sorted!\n";

					unsorted = true;
					break;
				}
			}

			if(unsorted){
				break;
			}

			if(val1.compareTo(val2) < 0){
				i++;
				result += val1 + "\t" + " " + "\t" + " ";				
			}
			else if(val1.compareTo(val2) > 0){
				j++;
				result += " " + "\t" + val2 + "\t" + " ";
			}else if(val1.compareTo(val2) == 0){
				i++;j++;
				result += " " + "\t" + " " + "\t" + val1;
			}	

			//if not last line of printing
			if(!( i == fileLines1.size() && j == fileLines2.size())){
				result += "\n";
			}

		}

		while(i<fileLines1.size() && unsorted == false){

			result += fileLines1.get(i) + "\t" + " " + "\t" + " ";
			i++;
			if(i < fileLines1.size()){
				result += "\n";
			}
		}
		while(j<fileLines2.size() && unsorted == false){			
			result += " " + "\t" + fileLines2.get(j) + "\t" + " ";
			j++;
			if(j < fileLines2.size()){
				result += "\n";
			}
		}
		return result;
	}

	/**
	 * Private function to load file from the given file path and
	 * return an array of strings containing the line-wise 
	 * information in the file.
	 */
	private ArrayList<String> loadFile(String fname){
		ArrayList<String> lines = new ArrayList<String>();
		String line;

		File f = new File(fname);

		//handles empty string or special characters case
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
	 * Compares the files and checks if the information in them is already sorted.
	 */
	@Override
	public String compareFilesCheckSortStatus(String input1, String input2) {
		checkOrderFlag = 1;

		if(input1 == null || input2 == null){
			setStatusCode(-1);
			return "";
		}

		String result = compareFiles(input1, input2);

		return result;
	}

	/**
	 * Compares the files but does not check whether the information is already sorted.
	 */
	@Override
	public String compareFilesDoNotCheckSortStatus(String input1, String input2) {
		checkOrderFlag = 0;

		if(input1 == null || input2 == null){
			setStatusCode(-1);
			return "";
		}

		String result = compareFiles(input1, input2);
		return result;
	}


	/**
	 * Returns the help message for the comm command.
	 */
	@Override
	public String getHelp() {

		String help =" /*\n" +
				"\n*" +
				"\n* comm : Compares two sorted files line by line. With no options, produce three-column output." +
				"\n* 		 Column one contains lines unique to FILE1, column two contains lines unique to FILE2," +
				"\n 		 and column three contains lines common to both files."+
				"\n*" +	
				"\n*	Command Format - comm [OPTIONS] FILE1 FILE2" +
				"\n*	FILE1 - Name of the file 1" +
				"\n*	FILE2 - Name of the file 2" +
				"\n*		-c : check that the input is correctly sorted" +
				"\n*      -d : do not check that the input is correctly sorted" +
				"\n*      -help : Brief information about supported options" +
				"\n*/";

		return help;
	}
}
