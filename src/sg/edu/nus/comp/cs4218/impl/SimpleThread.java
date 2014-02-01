package sg.edu.nus.comp.cs4218.impl;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import sg.edu.nus.comp.cs4218.ITool;

public class SimpleThread extends Thread {

	ITool itool;
	File workingDirectory;
	String stdin;
	
	SimpleThread(ITool itoolinstance,File directory,String input)
	{
		itool = itoolinstance;
		workingDirectory = directory;
		stdin = input;
	}
    public void run(){
    	while (!Thread.currentThread().isInterrupted())
    	{
    		//System.out.println("MyRunnable running");
			String stdout = itool.execute(workingDirectory, stdin);
			System.out.println(stdout);
			System.out.println("Status code is " + itool.getStatusCode());
			Thread.currentThread().interrupt();
    	}
    }
    
    public void writeOutputToFile(String output){
    	
    	try{
			if(!output_file.exists())
				output_file.createNewFile();
			FileWriter fw = new FileWriter(output_file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(output);
			bw.close();
		} catch (IOException e){
			System.out.println(output_msg+"Unable to create output file");
			setStatusCode(-1);
			return output_msg+"Unable to create output file";
		}
		
    }
  }
