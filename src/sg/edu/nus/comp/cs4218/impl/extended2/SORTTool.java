package sg.edu.nus.comp.cs4218.impl.extended2;

import java.io.File;

import sg.edu.nus.comp.cs4218.extended2.ISortTool;
import sg.edu.nus.comp.cs4218.impl.ATool;

/**
 * Do not modify this file
 */
/*
 * 
 * sort : sort lines of text file
 *
 * Command Format - sort [OPTIONS] [FILE]
 *	FILE - Name of the file
 *	OPTIONS
 *		-c : Check whether the given file is already sorted, if it is not all sorted, print a
 *           diagnostic containing the first line that is out of order
 *	    -help : Brief information about supported options
 */

public class SORTTool extends ATool implements ISortTool{

	File filename;
	String options;
	
	public SORTTool(String[] arguments) {
		super(arguments);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String sortFile(String input) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String checkIfSorted(String input) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getHelp() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String execute(File workingDir, String stdin) {
		// TODO Auto-generated method stub
		
		if(args!=null)
		{
			if(args[0].startsWith("-"))
			options = args[0];
		
			//File file = new File();
			
		}
		return null;
	}

}
