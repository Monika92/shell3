package sg.edu.nus.comp.cs4218.impl.fileutils;

import java.io.File;

import sg.edu.nus.comp.cs4218.IShell;
import sg.edu.nus.comp.cs4218.impl.ATool;
import sg.edu.nus.comp.cs4218.fileutils.ICdTool;


public class CDTool extends ATool implements ICdTool{
	
	public CDTool(String[] arguments) 
	{
		super(arguments);
	}
	
	@Override
	public File changeDirectory(String newDirectory)
	{
		return null;	
	}

	@Override
	public String execute(File workingDir, String stdin,IShell shell) {
		// TODO Auto-generated method stub
		int numArgs;
		if(args!=null)
			numArgs = args.length;
		else
			numArgs = 0;	
		
		if(numArgs == 0)
		{
			//To go directly to your home directory: cd
			//This solution may be innapropriate for windows at times because Windows doesn't have a fixed meaning for 'Home Directory'
			shell.changeWorkingDirectory(new File(System.getProperty("user.home")));
			return "The working directory is " + (System.getProperty("user.home"));
		}
		else
		{
			//To move to a directory using a full pathname: ex.   cd /home/physics/ercy04/ProjectX
			File dir = new File(args[0]);
	        if(dir.isDirectory()==true) 
	        {
	        	shell.changeWorkingDirectory( dir );
	        	return "Changed current working directory" ;
	            	
	        } else 
	        {
	        	return args[0] + "is not a valid directory. The working directory has not changed.";
            }
			 //TODO : Implement relative pathname and cd .. and moving ahead into subdir
		}
	}
	
	



}
