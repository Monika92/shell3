package sg.edu.nus.comp.cs4218.impl.fileutils;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import sg.edu.nus.comp.cs4218.IShell;
import sg.edu.nus.comp.cs4218.impl.ATool;
import sg.edu.nus.comp.cs4218.fileutils.ILsTool;


public class LSTool extends ATool implements ILsTool
{

	public LSTool(String[] arguments) {
		super(arguments);
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
			System.out.println(workingDir);
			childFilesList = getFiles(workingDir);
			outputString = getStringForFiles(childFilesList);
		}
		else
		{
			File dir = new File(args[0]);
			if(dir.isDirectory()==true)
			{
				//Displaying contents of directory
				childFilesList = getFiles(dir);
				outputString = getStringForFiles(childFilesList);
			}
			else if(dir.isFile())
			{
				//Displaying if file exits
				outputString = "File Exists";
			}
			else if(args[0].contains("*."))
			{
				//Display files of certain extension
				String fileType = args[0].substring(args[0].indexOf(".") + 1, args[0].length());
				String[] childFiles = workingDir.list();
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
				}
			}
			else
			{
				//If all else fails
				outputString = "Invalid! Doesn't exist.";
			}
		}
		return outputString;
	}

}
