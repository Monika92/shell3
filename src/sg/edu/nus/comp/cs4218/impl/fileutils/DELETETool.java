package sg.edu.nus.comp.cs4218.impl.fileutils;

import java.io.File;
import java.io.IOException;

import sg.edu.nus.comp.cs4218.IShell;
import sg.edu.nus.comp.cs4218.fileutils.IDeleteTool;
import sg.edu.nus.comp.cs4218.impl.ATool;

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
	public String execute(File workingDir, String stdin, IShell shell) {
		// TODO Auto-generated method stub
		
		File file;
		int args_length = args.length;
		String output = "", output_msg = "";
		
		for(int i = 0; i < args_length; i++){
			try{
				file = new File(args[i]);
			} catch(Exception e){
				System.out.println(output_msg+"Invalid file name");
				setStatusCode(-1);
				return output_msg+"\nInvalid file name";
			}
			if (!file.exists()){
				setStatusCode(-1);
				System.out.println("No such file");
				return output_msg+"\nNo such file";
			}
			
			if (delete(file))
				output = "";
			else {
				setStatusCode(-1);
				System.out.println("Unable to delete file");
				return output_msg + "\nUnable to delete file";
			}
			
		}
		
		setStatusCode(0);
		return output;
	}
	

}
