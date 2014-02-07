package sg.edu.nus.comp.cs4218.impl.fileutils;

import java.io.File;
import sg.edu.nus.comp.cs4218.fileutils.IDeleteTool;
import sg.edu.nus.comp.cs4218.impl.ATool;

public class DELETETool extends ATool implements IDeleteTool{

/*
 * Constructor for DELETETool - initializes the super class's arguments 
 * with the passed arguments.
 */
	public DELETETool(String[] arguments) {
		super(arguments);
		// TODO Auto-generated constructor stub
	}

/*
 * Deletes the File passed as argument.
 */
	@Override
	public boolean delete(File toDelete){
		// TODO Auto-generated method stub
		return toDelete.delete();	
	}

/*
 * Executes the delete command.
 */
	@Override
	public String execute(File workingDir, String stdin) {
		// TODO Auto-generated method stub
		
		File file, filePath;
		int argsLength = args.length;
		String output = "", outputMsg = "", fileName;
		
		for(int i = 0; i < argsLength; i++){
			try{
				fileName = args[i];
				filePath = new File(fileName);
				if(filePath.isAbsolute()){
					file = new File(filePath.getPath());
				}
				else{
					file = new File(workingDir.toString()+File.separator+fileName);
				}
			} catch(Exception e){
				setStatusCode(-1);
				if (outputMsg.equalsIgnoreCase(""))
					return "Invalid file name";
				else if(outputMsg.endsWith("\n"))
					return outputMsg + "Invalid file name";
				else
					return outputMsg+"\nInvalid file name";
			}
			if (!file.exists()){
				setStatusCode(-1);
				if (outputMsg.equalsIgnoreCase(""))
					return "No such file";
				else if(outputMsg.endsWith("\n"))
					return outputMsg + "No such file";
				else
					return outputMsg+"\nNo such file";
			}
			
			if (delete(file))
				output = "";
			else {
				setStatusCode(-1);
				if (outputMsg.equalsIgnoreCase(""))
					return "Unable to delete file";
				else if(outputMsg.endsWith("\n"))
					return outputMsg + "Unable to delete file";
				else
					return outputMsg+"\nUnable to delete file";
			}
			
		}
		
		setStatusCode(0);
		return output;
	}
	

}
