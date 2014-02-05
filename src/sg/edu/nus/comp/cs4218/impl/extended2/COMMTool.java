package sg.edu.nus.comp.cs4218.impl.extended2;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import sg.edu.nus.comp.cs4218.IShell;
import sg.edu.nus.comp.cs4218.extended2.ICommTool;
import sg.edu.nus.comp.cs4218.impl.ATool;
import sg.edu.nus.comp.cs4218.impl.ArgumentObject;
import sg.edu.nus.comp.cs4218.impl.ArgumentObjectParser;

/**
 * Do not modify this file
 */
/*
 * 
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

	public COMMTool(String[] arguments) {
		super(arguments);
	}

	@Override
	public String execute(File workingDir, String stdin) {
		
		String fileName1 = "", fileName2 = "";
		ArgumentObjectParser aop = new ArgumentObjectParser();
		ArgumentObject ao = aop.parse(super.args, "paste");
		
		fileName1 = ao.getFileList().get(0);
		fileName2 = ao.getFileList().get(1);
		
		//priority to -help option
		if(ao.getOptions().contains("-help")){
			return getHelp();
		}
		
		//priority to "-d" option
		if(ao.getOptions().contains("-d")){
			return compareFilesDoNotCheckSortStatus(fileName1, fileName2);
		}
				
		if(ao.getOptions().contains("-c")){
			return compareFilesCheckSortStatus(fileName1,fileName2);
		}
		
		String filePath1 = getCorrectPathFromArg(workingDir,fileName1);
		String filePath2 = getCorrectPathFromArg(workingDir,fileName2);

		if(filePath1 == null || filePath2 == null){
			return null;
		}

		String result = compareFiles(filePath1, filePath2);

		return result;
	}

	private String getCorrectPathFromArg(File workingDir,String fName){
		String name = null;
		if(fName.startsWith("//")){
			name = fName;
		}
		else{
			name = workingDir.getAbsolutePath() + File.separator + fName;
		}
		
		if((new File(name)).exists()){
			return name;
		}
		return null;
	}

	@Override
	public String compareFiles(String input1, String input2) {

		ArrayList<String> fileLines1 = loadFile(input1);
		ArrayList<String> fileLines2 = loadFile(input2);

		String result = null;

		int i=0,j=0;
		for( i=0,j=0; i<fileLines1.size()&& j<fileLines2.size();){
			
			result = "";
			
			String val1 = fileLines1.get(i);
			String val2 = fileLines2.get(j);
			
			//include check for option --check-order
			if(checkOrderFlag != 0){
				if((i+1) <fileLines1.size() && val1.compareTo(fileLines1.get(i+1))>0){
					result += " File 1 not sorted!\n";
				}
				if((j+1) <fileLines2.size() && val2.compareTo(fileLines2.get(j+1))>0){
					result += " File 2 not sorted!\n";
				}
			}
			
			if(val1.compareTo(val2) < 0){
				i++;
				result += val1 + "\t" + "-" + "\t" + "-" +"\n";				
			}
			else if(val1.compareTo(val2) > 0){
				j++;
				result += "-" + "\t" + val2 + "\t" + "-" +"\n";
			}else if(val1.compareTo(val2) == 0){
				i++;j++;
				result += "-" + "\t" + "-" + "\t" + val1 +"\n";
			}		
		}

		while(i<fileLines1.size()){

			i++;
			result += fileLines1.get(i) + "\t" + "-" + "\t" + "-" +"\n";
		}
		while(j<fileLines2.size()){

			j++;
			result += "-" + "\t" + fileLines2.get(j) + "\t" + "-" +"\n";
		}

		return result;
	}

	private ArrayList<String> loadFile(String fname){
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
	public String compareFilesCheckSortStatus(String input1, String input2) {
		checkOrderFlag = 1;
		compareFiles(input1, input2);
		
		return null;
	}

	@Override
	public String compareFilesDoNotCheckSortStatus(String input1, String input2) {
		checkOrderFlag = 0;
		compareFiles(input1, input2);

		return null;
	}

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
