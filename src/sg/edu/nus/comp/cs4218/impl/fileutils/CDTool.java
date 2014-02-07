package sg.edu.nus.comp.cs4218.impl.fileutils;

import java.io.File;

import sg.edu.nus.comp.cs4218.impl.ATool;
import sg.edu.nus.comp.cs4218.impl.WorkingDirectory;
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
	
	public File getDirectoryPath(String dirName , String workingDir)
	{
		
		if(dirName.replaceAll("\\s","").contentEquals(".."))
		{
			File wd = new File(workingDir);
			if(wd.getParentFile()!=null)
			dirName = wd.getParentFile().getAbsolutePath();	
			return (new File(dirName));
		}
		else if(dirName.replaceAll("\\s","").contentEquals("."))
		{
			//remain as it is
			File wd = new File(workingDir);
			dirName = wd.getAbsolutePath();
			return (new File(dirName));
		}
		else
		{
			//absolute and relative paths
			File dir = new File(dirName);
			if(dir.isAbsolute())
		 		return dir;
			else
				dirName = workingDir + File.separator +  dirName;
				return (new File(dirName));
		 }
		
	}

	@Override
	public String execute(File workingDir, String stdin) {
		// TODO Auto-generated method stub
		int numArgs;
		if(args!=null)
			numArgs = args.length;
		else
			numArgs = 0;	
		
		if(numArgs == 0)
		{
			//To go directly to your home directory: cd
			WorkingDirectory.changeWorkingDirectory(new File(System.getProperty("user.home")));
			return "The working directory is " + (System.getProperty("user.home"));
		}
		else
		{
			//To move to a directory using a full pathname: ex.   cd /home/physics/ercy04/ProjectX
			//File dir = new File(args[0]);
			args[0] = args[0].replaceAll("\\s","");
			if(args[0].contains("~"))
			{			
				String home = System.getProperty("user.home") + File.separator;
				args[0] = args[0].replace("~", home);
			}
			
			File dir = getDirectoryPath(args[0] , workingDir.getAbsolutePath());
			if(dir.exists() && dir.isDirectory() && !dir.getAbsoluteFile().toString().contains(".")) 
	        {
				WorkingDirectory.changeWorkingDirectory(dir);
	        	return "Changed current working directory to " + dir.getAbsolutePath() ;  	
	        } 
	        else 
	        {
	        	setStatusCode(-1);
	        	return args[0] + " is not a valid directory. The working directory has not changed.";
	        	
            }
			 //TODO : Implement relative pathname and cd .. and moving ahead into subdir
		}
	}
	
	



}
