package sg.edu.nus.comp.cs4218.impl;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import sg.edu.nus.comp.cs4218.IShell;
import sg.edu.nus.comp.cs4218.ITool;

public class SimpleThread extends Thread {

	ITool itool;
	String stdin, stdout;
	String[] args;
	IShell shell;
	SimpleThread(ITool itoolinstance,String input,String[] argsList)
	{
		itool = itoolinstance;
		stdin = input;
		args = argsList;
	}
    public void run(){
    	while (!Thread.currentThread().isInterrupted())
    	{
    		//System.out.println("MyRunnable running");
    		stdout = itool.execute(WorkingDirectory.workingDirectory, stdin);
    		
    		File outputFile = null;

    		if(args!=null){
        	int argsLength = args.length;
				if(argsLength>=2 && args[argsLength -2].equalsIgnoreCase(">")){
					outputFile = new File(args[argsLength - 1]);
					writeOutputToFile(outputFile);
				}
    		}
    		
			System.out.println(stdout);
			System.out.println("Status code is " + itool.getStatusCode());
			Thread.currentThread().interrupt();
    	}
    }

    public boolean writeOutputToFile(File output_file){
    	//Check for output file
    	String outputMsg="";
		
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
			System.out.println(outputMsg+"Unable to create output file");
		}
		return true;	    
    }

  }

