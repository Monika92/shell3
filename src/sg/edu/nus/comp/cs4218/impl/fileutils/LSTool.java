package sg.edu.nus.comp.cs4218.impl.fileutils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import sg.edu.nus.comp.cs4218.IShell;
import sg.edu.nus.comp.cs4218.impl.ATool;
import sg.edu.nus.comp.cs4218.impl.WorkingDirectory;
import sg.edu.nus.comp.cs4218.fileutils.ILsTool;


public class LSTool extends ATool implements ILsTool
{

	public LSTool(String[] arguments) {
		super(arguments);
		// TODO Auto-generated constructor stub
	}

	/*
	 * This function take a File parameter and lists all the childfiles inside it
	 */
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

	/*
	 * Given a File List, this function parses them into a string separated by spaces
	 */
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
	
	/*
	 * Give a String and the working directory file, this function finds if that string is an absolute or relative path
	 */
	public String getFilePath(String fileName, File dir)
	{		
		if((new File(fileName)).isAbsolute())
	 		return fileName;
		else
		{
			return (WorkingDirectory.workingDirectory + File.separator + fileName);
		}
	}

	/* 
	 * Executes Ls
	 * Possible Executions
	 * 1. Ls ie no arguments : just displays all files and folders in present workingdir
	 * 2. Ls Directory : Directory could be absolute or relative. Displays the files and folders inside it
	 * 3. Ls file : File could be absolute or replative. Displays whether the file exits or not
	 * 4. ls *.txt : or any other extension. Displays list of .txt (or any other file extension) files in current working dir.
	 */
	@Override
	public String execute(File workingDir, String stdin) {
		// TODO Auto-generated method stub
		int numArgs;
		if(args!=null)
		numArgs = args.length;
		else
		numArgs = 0;	
		
		List<File> childFilesList = new ArrayList();  
		String outputString = "";
		
		if(numArgs == 0)
		{
			//No arguments
			childFilesList = getFiles(workingDir);
			outputString = getStringForFiles(childFilesList);
			if(outputString == "") 
				outputString += "The folder is empty";
		}
		else
		{
			System.out.println(getFilePath(args[0],workingDir));
			File dir = new File(getFilePath(args[0],workingDir));
			if(dir.isDirectory()==true)
			{
				//Displaying contents of directory
				childFilesList = getFiles(dir);
				outputString = getStringForFiles(childFilesList);
				if(outputString == "") 
					outputString += "The folder is empty";
			}
			else if(dir.isFile())
			{
				//Displaying if file exits
				outputString += "File Exists";
			}
			else if(args[0].contains("*."))
			{
				//Display files of certain extension
				String fileType = args[0].substring(args[0].indexOf(".") + 1, args[0].length());
				dir = new File(getFilePath(args[0].substring(0, args[0].indexOf('*')),workingDir));
				if(dir.isDirectory() ==  true)
				{
					String[] childFiles = dir.list();
					for(String child: childFiles)
					{
					  if(child.endsWith(fileType))
					  {
						  outputString += child + " ";
					  }					
					}
					if(outputString == "")
					{
						outputString = "No files of type ." + fileType;
						setStatusCode(-1);
					}
				}
			}
			else
			{
				//If all else fails
				outputString = "Invalid. Doesn't exist";
				setStatusCode(-1);
			}
		}
		return outputString;
	}

}
