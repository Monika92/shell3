package sg.edu.nus.comp.cs4218.impl.fileutils;

import java.io.File;

import sg.edu.nus.comp.cs4218.impl.ATool;
import sg.edu.nus.comp.cs4218.fileutils.ICdTool;


public class CDTool extends ATool implements ICdTool{
	
	public CDTool(String[] arguments) 
	{
		super(arguments);
	}
	
	public File changeDirectory(String newDirectory)
	{
		return null;	
	}

	@Override
	public String execute(File workingDir, String stdin) 
	{
		// TODO Auto-generated method stub
		return null;
	}
	
	



}
