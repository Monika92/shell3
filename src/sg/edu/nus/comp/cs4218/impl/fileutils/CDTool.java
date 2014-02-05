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
		if((dirName.indexOf('\\')==0) || (dirName.contentEquals(".")))
		{
			return (new File(dirName));
		}
		else if((dirName.indexOf('~')==0))
		{
			dirName = dirName.replaceFirst("~", workingDir + '\\');
			return (new File(dirName));
		}
		else if(dirName.contentEquals(".."))
		{
			File wd = new File(workingDir);
			dirName = wd.getParentFile().getAbsolutePath();
			return (new File(dirName));
		}
		else
		{
			return (new File(workingDir + '\\' + dirName));
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
			File dir = getDirectoryPath(args[0] , workingDir.getAbsolutePath());
			if(dir.isDirectory()) 
	        {
				WorkingDirectory.changeWorkingDirectory(dir);
	        	return "Changed current working directory to " + dir.getAbsolutePath() ;  	
	        } 
	        else 
	        {
	        	return args[0] + " is not a valid directory. The working directory has not changed.";
            }
			 //TODO : Implement relative pathname and cd .. and moving ahead into subdir
		}
	}
	
	



}
