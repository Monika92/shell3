package sg.edu.nus.comp.cs4218.impl.fileutils;

import java.io.File;
import sg.edu.nus.comp.cs4218.fileutils.IDeleteTool;
import sg.edu.nus.comp.cs4218.impl.ATool;
import sg.edu.nus.comp.cs4218.impl.FilePathIdentifier;

public class DELETETool extends ATool implements IDeleteTool{

	
	
	public DELETETool(String[] arguments) {
		super(arguments);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean delete(File toDelete){
		// TODO Auto-generated method stub
		return toDelete.delete();	
	}

	@Override
	public String execute(File workingDir, String stdin) {
		// TODO Auto-generated method stub
		
		File file, file_path;
		int argsLength = args.length;
		String output = "", outputMsg = "", fileName;
		
		for(int i = 0; i < argsLength; i++){
			try{
				fileName = args[i];
				file_path = new File(fileName);
				if(file_path.isAbsolute()){
					file = new File(file_path.getPath());
				}
				else{
					file = new File(workingDir.toString()+File.separator+fileName);
				}
			} catch(Exception e){
				System.out.println(outputMsg+"Invalid file name");
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
				System.out.println("No such file");
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
