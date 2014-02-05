package sg.edu.nus.comp.cs4218.impl.fileutils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import sg.edu.nus.comp.cs4218.IShell;
import sg.edu.nus.comp.cs4218.fileutils.IMoveTool;
import sg.edu.nus.comp.cs4218.impl.ATool;

public class MOVETool extends ATool implements IMoveTool{

	public MOVETool(String[] arguments) {
		super(arguments);
		// TODO Auto-generated constructor stub
	}

	
	@Override
	public boolean move(File from, File to) {
		// TODO Auto-generated method stub
			Path sourcePath = Paths.get(args[0]);
			Path targetPath = Paths.get(args[1]);
			Path basePath = Paths.get(System.getProperty("user.dir"));
			try 
			{
				Path p = Files.move(from.getAbsoluteFile().toPath(), to.getAbsoluteFile().toPath(), StandardCopyOption.REPLACE_EXISTING);
				return true;
			} catch (IOException e) 
			{
				return false;
			}
	}
	
	@Override
	public String execute(File workingDir, String stdin, IShell shell) {
		
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
			
				arg1 = new File(args[1] + "\\" + arg0.getName());
				if(move(arg0,arg1))
				{
					outputString = args[0] + "'s move completed.";
				}
				else
				{	
					outputString = "Error - Unable to move.";
				}
			}
			else if((arg0.isDirectory()==true) && (arg1.isDirectory()==true))
			{
				arg0 = arg0;
				arg1 = arg1;
			//	File source = new File("c:\\old_directory");
			//	File destination = new File("c:\\some_directory\\new_directory");
				
				arg0.renameTo(new File(arg1 , arg0.getName()));
			}
			else if((arg0.isDirectory()==true)||(arg0.isFile()))
			{
				if(move(arg0,arg1))
				{
					outputString = "Move completed.";
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
			File argLast = new File(args[numArgs-1]);
			if(argLast.isDirectory()==true)
			{
				for(int i=0; i<(numArgs-1); i++)
				{
					File argI = new File(args[i]);
					if(argI.isFile())
					{	
						File argDest = new File(argLast + "\\" + argI.getName());
						if (move(argI,argDest))
						{
							outputString += args[i] + "'s move completed. \n"; 
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
