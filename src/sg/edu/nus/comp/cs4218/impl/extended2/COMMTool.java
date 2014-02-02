package sg.edu.nus.comp.cs4218.impl.extended2;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import sg.edu.nus.comp.cs4218.extended2.ICommTool;
import sg.edu.nus.comp.cs4218.impl.ATool;

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



	public COMMTool(String[] arguments) {
		super(arguments);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String execute(File workingDir, String stdin) {

		String fileName1 = "", fileName2 = "";

		String rootPath = workingDir.getAbsolutePath();
		String filePath1 = rootPath + File.separator + fileName1;
		String filePath2 = rootPath + File.separator + fileName2;

		String result = compareFiles(filePath1, filePath2);

		return result;
	}

	@Override
	public String compareFiles(String input1, String input2) {

		ArrayList<String> fileLines1 = loadFile(input1);
		ArrayList<String> fileLines2 = loadFile(input2);
		
		//get unique
		fileLines1 = getUniqueSet(fileLines1);
		fileLines2 = getUniqueSet(fileLines2);
		
		
		ArrayList<String> common = new ArrayList<String>();
		
		if(fileLines1 == null || fileLines2 == null){
			return null;
		}

		for(int i = 0; i<fileLines1.size(); i++){
			String line = fileLines1.get(i);
			int idx = Collections.binarySearch(fileLines2,line);
			
			//if present, add to common list and remove from other lists
			if(fileLines2.get(idx).equals(line)){
				common.add(line);
				fileLines1.set(i, null);
				fileLines2.set(idx, null);
			}
			
		}
		
		String result = null;
		result = createOutputString(fileLines1, fileLines2, common);
		
		return result;
	}

	private String createOutputString(ArrayList<String> l1, ArrayList<String> l2,ArrayList<String> l3 ){
		int maxLength = 0;
		maxLength = l1.size();
		if(l2.size() > maxLength){
			maxLength = l2.size();
		}
		if(l3.size() > maxLength){
			maxLength = l3.size();
		}
		
		if(l1.size() != maxLength){
			l1 = addBuffer(l1, maxLength);
		}
		if(l2.size() != maxLength){
			l2 = addBuffer(l2, maxLength);
		}
		if(l3.size() != maxLength){
			l3 = addBuffer(l3, maxLength);
		}
		
		String result = "";
		for( int i = 0; i<maxLength; i++){
			result = l1.get(i) + "\t" + l2.get(i) + "\t" + l3.get(i) + "\n";
		}
		return result;
	}
	
	private ArrayList<String> addBuffer(ArrayList<String> list, int n){
		
		int bufferSize = n - list.size();
		for(int i = 0; i<bufferSize; i++){
			list.add("");
		}
		return list;
	}
	
	private ArrayList<String> getUniqueSet(ArrayList<String> lines){
		Set<String> set = new HashSet<String>();
		
		for( int i = 0; i<lines.size(); i++){
			set.add(lines.get(i));
		}
		
		ArrayList<String> uniq = new ArrayList<String>();
		for(Object object : set) {
		    String element = (String) object;
		    uniq.add(element);
		}
		
		return uniq;
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String compareFilesDoNotCheckSortStatus(String input1, String input2) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getHelp() {
		// TODO Auto-generated method stub
		return null;
	}
}
