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

		@Override
	public boolean copy(File from, File to) {
		// TODO Auto-generated method stub
		
			Path sourcePath = Paths.get(args[0]);
			Path targetPath = Paths.get(args[1]);
			Path basePath = Paths.get(System.getProperty("user.dir"));
			try 
			{
				Files.copy(from.getAbsoluteFile().toPath(), to.getAbsoluteFile().toPath(), StandardCopyOption.REPLACE_EXISTING);
				return true;
			} catch (IOException e) 
			{
				return false;
			}

	}
	
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
			
			if((arg0.isFile()) && (arg1.isDirectory()==true))
			{
				//Copy a file to directory
				//arg1 = copyFileToDir( 0 , 1);
				arg1 = new File(args[1] + File.separator + arg0.getName());
				if (copy(arg0,arg1))
				{
					outputString = "Copy completed.";
				}
				else
				{
					outputString = "Error - Invalid input.";
				}
			}
			if((arg0.isDirectory() == true))
			{
				if (copyDirectory(arg0,arg1))
				{
					outputString = "Copy completed.";
				}
				else
				{
					outputString = "Error - Invalid input.";
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
				}
			}
			else
			{
				outputString = "Error - Invalid input.";
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
						}
					}
					else
					{
						outputString += args[i] + " is an invalid file.\n";
					}
				 }
			}
			else
			{
				outputString = args[numArgs-1] + " is an invalid directory.";
			}
			
		}
		
		return outputString;

	}

	

}
