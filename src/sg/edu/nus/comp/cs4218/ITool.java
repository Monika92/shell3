package sg.edu.nus.comp.cs4218;

import java.io.File;

/**
 * Do not modify this file
 */
public interface ITool {
	//this function returns stdout
	String execute(File workingDir, String stdin ,IShell shell);
	int getStatusCode();
}
