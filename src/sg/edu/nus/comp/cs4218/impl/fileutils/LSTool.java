package sg.edu.nus.comp.cs4218.impl.fileutils;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import sg.edu.nus.comp.cs4218.impl.ATool;
import sg.edu.nus.comp.cs4218.fileutils.ILsTool;


public class LSTool extends ATool implements ILsTool
{
	String[] localArgs;
	public LSTool(String[] arguments) {
		super(arguments);
		localArgs = arguments;
		// TODO Auto-generated constructor stub
	}

	@Override
	public List<File> getFiles(File directory) {
		
		// TODO Auto-generated method stu
        String[] childFiles = directory.list();
        List<File> childFilesList = new ArrayList();  
        
        for(String child: childFiles)
        {
        	File childFile = new File(child);
        	childFilesList.add(childFile);
        }
		return childFilesList;		
	}

	@Override
	public String getStringForFiles(List<File> files) {
		// TODO Auto-generated method stub
		String outputString = "";
		for(File child: files)
		{
		 outputString += child.toString() + " ";
		}
		return outputString;
	}

	@Override
	public String execute(File workingDir, String stdin) {
		// TODO Auto-generated method stub
		int numArgs = localArgs.length;
		List<File> childFilesList = new ArrayList();  
		String outputString = "";
		
		if(numArgs == 0)
		{
			childFilesList = getFiles(workingDir);
			outputString = getStringForFiles(childFilesList);
		}
		else
		{
			File dir = new File(localArgs[0]);
			if(dir.isDirectory()==true)
			{
				childFilesList = getFiles(dir);
				outputString = getStringForFiles(childFilesList);
			}
			else if(dir.isFile())
			{
				outputString = "File Exists";
			}
			else
			{
				outputString = "Invalid! Does not exist";
			}
		}
		return outputString;
	}

}
