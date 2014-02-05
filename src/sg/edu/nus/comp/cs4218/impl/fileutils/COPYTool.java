package sg.edu.nus.comp.cs4218.impl.fileutils;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
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
	@Override
	public boolean copy(File from, File to) {
		// TODO Auto-generated method stub
		FileChannel inputChannel = null;
		FileChannel outputChannel = null;

		try 
		{
			inputChannel = new FileInputStream(from).getChannel();
			outputChannel = new FileOutputStream(to).getChannel();
			outputChannel.transferFrom(inputChannel, 0, inputChannel.size());
	        inputChannel.close();
	        outputChannel.close();
	        return true;
		}
		catch (IOException e) 
		{
			return false;
		}
	}
	*/
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
	
	/*
	public File copyFileToDir( int fromIndex , int toIndex ) {
		
		String arg0 = "" , arg1 = "";
		
		//Remove \ from arg[0] if its the last character
		if(args[fromIndex].lastIndexOf('\\') == args[fromIndex].length()-1)
			arg0 = args[fromIndex].substring(0, args[fromIndex].length()-1);
		else
			arg0 = args[fromIndex];
	
		//append file name on to directory
		if(args[toIndex].lastIndexOf('\\') == args[toIndex].length()-1)
			arg1 = args[toIndex] + arg0.substring(arg0.lastIndexOf('\\')+1 , arg0.length());
		else
			arg1 = args[toIndex] + "\\" + arg0.substring(arg0.lastIndexOf('\\')+1 , arg0.length());
		
		File argFile = new File(arg1);
		
		return argFile;
	}
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
							outputString += args[i] + " is invalid input.\n";
						}
					}
					else
					{
						outputString += args[i] + " is invalid input.\n";
					}
				 }
			}
			else
			{
				outputString = args[numArgs-1] + " is invalid input.";
			}
			
		}
		
		return outputString;

	}

	

}
