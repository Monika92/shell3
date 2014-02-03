package sg.edu.nus.comp.cs4218.impl.fileutils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

import sg.edu.nus.comp.cs4218.IShell;
import sg.edu.nus.comp.cs4218.fileutils.ICatTool;
import sg.edu.nus.comp.cs4218.impl.ATool;

public class CATTool extends ATool implements ICatTool {

	public CATTool(String[] arguments) {
		super(arguments);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String getStringForFile(File toRead) {
		// TODO Auto-generated method stub
		String output = "";
		FileReader fr;
		try{
			fr = new FileReader(toRead);
		} catch(FileNotFoundException e){
			e.printStackTrace();
			System.out.println("File not found");
			return "File not found";
		}
		BufferedReader br = new BufferedReader(fr);
		try{
			String line = br.readLine();
			while(line != null){
				System.out.println(line);
				if(line.equalsIgnoreCase("\n")||line.equalsIgnoreCase(""))
					output+="\n";
				else
					output += line + "\n";
				line = br.readLine();
			}
		} catch(IOException e){
			e.printStackTrace();
			System.out.println("Unable to read file");
			return "Unable to read file";
		} finally{
			try {
				br.close();
				fr.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return output;
	}

	@Override
	public String execute(File workingDir, String stdin, IShell shell) {
		// TODO Auto-generated method stub
		
		File file;
		int args_length = args.length;
		String output = "", output_msg = "";
		
		//Check for stdin
		if(stdin!=null){
			setStatusCode(0);
			return stdin;
		}
		
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
			output += getStringForFile(file);
		}
		
		
		setStatusCode(0);
		return output;
	}
}
