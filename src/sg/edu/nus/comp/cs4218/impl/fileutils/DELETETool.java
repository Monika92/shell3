package sg.edu.nus.comp.cs4218.impl.fileutils;

import java.io.File;

import sg.edu.nus.comp.cs4218.fileutils.IDeleteTool;
import sg.edu.nus.comp.cs4218.impl.ATool;
import sg.edu.nus.comp.cs4218.impl.CommandVerifier;

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
		
		//Check for null input for toDelete
		if(toDelete == null){
			setStatusCode(-1);
			return false;
		}
		
		//Check valid file and existence
		if (!toDelete.exists()){
			setStatusCode(-1);
			return false;
		}
		
		return toDelete.delete();	
	}

/*
 * Executes the delete command.
 */
	@Override
	public String execute(File workingDir, String stdin) {
		
		//Verify command syntax
		CommandVerifier cv = new CommandVerifier();
		int validCode = cv.verifyCommand("delete", super.args);
		if(validCode == -1){
			setStatusCode(-1);
			return "";
		}
		
		//Check for valid workingDir
		if(workingDir == null)
		{
			setStatusCode(-1);
			return "";
		}	
		if(!workingDir.exists()){
			setStatusCode(-1);
			return "";
		}
				
		//Check for null input for args
		if (args == null){
			setStatusCode(-1);
			return "";
		}
		
		File file, filePath;
		int argsLength = args.length;
		String output = "", outputMsg = "", fileName;
		
		for(int i = 0; i < argsLength; i++){
			if(args[i].equalsIgnoreCase("-")){
				i++;
				continue;
			}
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
