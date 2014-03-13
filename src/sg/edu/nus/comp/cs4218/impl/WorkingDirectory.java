package sg.edu.nus.comp.cs4218.impl;

import java.io.File;

public class WorkingDirectory {

	public static File workingDirectory;
	
	/**
	 * Called by cdtool to change shell's current working directory
	 * @param newDirectory
	 */
	public File getWorkingDirectory()
	{
		return workingDirectory;
	}
	public static void changeWorkingDirectory(File newDirectory)
	{
		workingDirectory = newDirectory ;
	}
}
