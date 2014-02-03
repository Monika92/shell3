package sg.edu.nus.comp.cs4218.impl;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import sg.edu.nus.comp.cs4218.ITool;

public class SimpleThread extends Thread {

	ITool itool;
	File workingDirectory;
	String stdin, stdout;
	String[] args;
	
	SimpleThread(ITool itoolinstance,File directory,String input,String[] argsList)
	{
		itool = itoolinstance;
		workingDirectory = directory;
		stdin = input;
		args = argsList;
	}
    public void run(){
    	while (!Thread.currentThread().isInterrupted())
    	{
    		//System.out.println("MyRunnable running");
    		stdout = itool.execute(workingDirectory, stdin);
    		
    		File output_file = null;

    		if(args!=null){
        	int args_length = args.length;
				if(args_length>=2 && args[args_length -2].equalsIgnoreCase(">")){
					output_file = new File(args[args_length - 1]);
					writeOutputToFile(output_file);
				}
    		}
    		
			System.out.println(stdout);
			System.out.println("Status code is " + itool.getStatusCode());
			Thread.currentThread().interrupt();
    	}
    }

    public boolean writeOutputToFile(File output_file){
    	//Check for output file
    	String output_msg="";
		
		try{
			if(!output_file.exists())
				output_file.createNewFile();
			FileWriter fw = new FileWriter(output_file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			char[] temp = stdout.toCharArray(); int i = 0;
			while(i<temp.length){
				while(temp[i]!='\n'){
					bw.write(temp[i]);
					i++;
					if(i>=temp.length)
						break;
				}
				bw.newLine(); i++;
			}
			bw.close();
		} catch (IOException e){
			System.out.println(output_msg+"Unable to create output file");
		}
		return true;	    
    }

  }

