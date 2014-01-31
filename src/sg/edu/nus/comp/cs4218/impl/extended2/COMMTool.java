package sg.edu.nus.comp.cs4218.impl.extended2;

import java.io.File;

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

	String file1name,file2name,options;
	
	public COMMTool(String[] arguments) {
		super(arguments);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String execute(File workingDir, String stdin) {
		// TODO Auto-generated method stub
		if(args.length == 3)
		{
			options = args[0];
			file1name = args[1];
			file2name = args[2];
		}
		else if(args.length == 2)
		{
			file1name = args[1];
			file2name = args[2];
		}
		
		
		File file1 = new File(file1name);
		File file2 = new File(file2name);
		
		
		
		return null;
	}

	@Override
	public String compareFiles(String input1, String input2) {
		// TODO Auto-generated method stub
		return null;
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
