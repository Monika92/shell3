package sg.edu.nus.comp.cs4218.impl.fileutils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import sg.edu.nus.comp.cs4218.fileutils.ICatTool;
import sg.edu.nus.comp.cs4218.impl.ATool;
import sg.edu.nus.comp.cs4218.impl.CommandVerifier;


public class CATTool extends ATool implements ICatTool {

/*
 * Constructor for CATTool - initializes the super class's arguments with the passed
 * arguments.
 */
	public CATTool(String[] arguments) {
		super(arguments);
		// TODO Auto-generated constructor stub
	}

/*
 * Reads the information in the input file and returns it as a string
 */
	@Override
	public String getStringForFile(File toRead) {
		// TODO Auto-generated method stub
		
		//Check for null input
		if (toRead == null){
			setStatusCode(-1);
			return "";
		}
		
		String output = "";
		FileReader fr;
		try{
			fr = new FileReader(toRead);
		} catch(FileNotFoundException e){
			setStatusCode(-1);
			return "File not found";
		}
		BufferedReader br = new BufferedReader(fr);
		try{
			String line = br.readLine();
			while(line != null){
				if(line.equalsIgnoreCase("\n")||line.equalsIgnoreCase(""))
					output+="\n";
				else
					output += line + "\n";
				line = br.readLine();
			}
		} catch(IOException e){
			setStatusCode(-1);
			return "Unable to read file";
		} finally{
			try {
				br.close();
				fr.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				setStatusCode(-1);
				return "";
			}
		}
		return output;
	}

/*
 * Executes the Cat command
 */
	@Override
	public String execute(File workingDir, String stdin) {
						
		//Verify command syntax
		CommandVerifier cv = new CommandVerifier();
		int validCode = cv.verifyCommand("cat", super.args);
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
		
		File file, filePath;
		int argsLength = args.length;
		String output = "", outputMsg = "", fileName;
		
		//Priority for stdin
		if(stdin!=null){
			setStatusCode(0);
			return stdin;
		}
		
		for(int i = 0; i < argsLength; i++){
			if(args[i].equalsIgnoreCase("-")){
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
				else
					return outputMsg+"\nInvalid file name";
			}
			if (!file.exists()){
				setStatusCode(-1);
				if (outputMsg.equalsIgnoreCase(""))
					return "No such file";
				else
					return outputMsg+"\nNo such file";
			}
			output += getStringForFile(file);
		}
		setStatusCode(0);
		return output.trim();
	}
}
