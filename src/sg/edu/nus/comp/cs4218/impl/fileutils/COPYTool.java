package sg.edu.nus.comp.cs4218.impl.fileutils;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import sg.edu.nus.comp.cs4218.IShell;
import sg.edu.nus.comp.cs4218.fileutils.ICopyTool;
import sg.edu.nus.comp.cs4218.impl.ATool;

public class COPYTool extends ATool implements ICopyTool{

	public COPYTool(String[] arguments) {
		super(arguments);
		// TODO Auto-generated constructor stub
	}

	/*
	 * Java7 copy funtion used to copy From file to To file
	 */
	@Override
	public boolean copy(File from, File to) {
		// TODO Auto-generated method stub
		
			Path sourcePath = Paths.get(args[0]);
			Path targetPath = Paths.get(args[1]);
			try 
			{
				Files.copy(from.getAbsoluteFile().toPath(), to.getAbsoluteFile().toPath(), StandardCopyOption.REPLACE_EXISTING);
				return true;
			} catch (IOException e) 
			{
				return false;
			}

	}
	
	/*
	 * Copies contents of one directory to another using a recursive function
	 */
	public boolean copyDirectory(File sourceLocation, File targetLocation)
	{
		boolean bool = true; 
		if (sourceLocation.isDirectory()) 
		 {
	            if (!targetLocation.exists()) {
	                targetLocation.mkdir();
	            }

	            String[] children = sourceLocation.list();
	            for (int i=0; i<children.length; i++) {
	                copyDirectory(new File(sourceLocation, children[i]),
	                        new File(targetLocation, children[i]));
	            }
	      } 
		  else 
		  {
			  if(copy(sourceLocation,targetLocation))
	        	bool = true;
			  else
				  bool = false;
	      }
		return bool;
	}
	
	/*
	 * Executes Copy
	 * Possible Executions
	 * 1. Copy file1 file2 - copies contents of file1 to file2. if file2 doesn't already exist, it gets created then copying happens
	 * 2. copy file dir - copies the file into the directory
	 * 3. copy dir1 dir2 - copies contents of one directory into the second
	 * 4. copy file1 file2 file3.. dir - copies all the valid files into dir. The invalid ones are not copied 
	 */
	@Override
	public String execute(File workingDir, String stdin) {
		// TODO Auto-generated method stub
		int numArgs;
		if(args!=null)
			numArgs = args.length;
		else
			numArgs = 0;	
		
		String outputString = "";
		// TODO Auto-generated method stub
		if(numArgs == 2)
		{
			File arg0 = new File(args[0]);
			File arg1 = new File(args[1]);
			if(!arg0.isAbsolute()) 
			{arg0 = new File(workingDir.toString()+File.separator+args[0]);}
			if(!arg1.isAbsolute()) 
			{arg1 = new File(workingDir.toString()+File.separator+args[1]);}
			
			
			if((arg0.isFile()) && (arg1.isDirectory()==true))
			{
				//Copy a file to directory
				arg1 = new File(args[1] + File.separator + arg0.getName());
				if (copy(arg0,arg1))
				{
					outputString = "Copy completed.";
				}
				else
				{
					outputString = "Error - Invalid input.";
					setStatusCode(-1);
				}
			}
			else if((arg0.isDirectory() == true))
			{
				if (copyDirectory(arg0,arg1))
				{
					outputString = "Copy completed.";
				}
				else
				{
					outputString = "Error - Invalid input.";
					setStatusCode(-1);
				}
			}
			else if(arg0.isFile())
			{
				//Copy a file to another file
				if(copy(arg0,arg1))
				{
					outputString = "Copy completed.";
				}
				else
				{
					outputString = "Error - Invalid input.";
					setStatusCode(-1);
				}
			}
			else
			{
				outputString = "Error - Invalid input.";
				setStatusCode(-1);
			}
		}
		else if(numArgs > 2)
		{
			//Copy multiple files to a directory
			File argLast = new File(args[numArgs-1]);
			if(argLast.isDirectory()==true)
			{
				for(int i=0; i<(numArgs-1); i++)
				{
					File argI = new File(args[i]);
					if(!argI.isAbsolute()) 
					{argI = new File(workingDir.toString()+File.separator+args[i]);}
					
					
					if(argI.isFile())
					{				
						//File argDest = copyFileToDir( i , numArgs-1);
						File argDest = new File(argLast + File.separator + argI.getName());
						if (copy(argI,argDest))
						{
							outputString += args[i] + "'s copy completed. \n"; 
						}
						else
						{
							outputString += args[i] + " is an invalid file.\n";
							setStatusCode(-1);
						}
					}
					else
					{
						outputString += args[i] + " is an invalid file.\n";
						setStatusCode(-1);
					}
				 }
			}
			else
			{
				outputString = args[numArgs-1] + " is an invalid directory.";
				setStatusCode(-1);
			}
			
		}
		
		return outputString;

	}

	

}
