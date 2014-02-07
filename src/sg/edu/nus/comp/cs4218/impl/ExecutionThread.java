package sg.edu.nus.comp.cs4218.impl;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import sg.edu.nus.comp.cs4218.IShell;
import sg.edu.nus.comp.cs4218.ITool;

public class ExecutionThread extends Thread {

	ITool itool;
	String stdin, stdout;
	String[] args;
	IShell shell;
	ExecutionThread(ITool itoolinstance,String input,String[] argsList)
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
    		boolean isOutputRedirection = false;
    		File outputFile = null;

    		if(args!=null && (itool.getStatusCode() != (-1))){
        	int argsLength = args.length;
				if(argsLength>=2 && args[argsLength -2].equalsIgnoreCase(">")){
					outputFile = new File(args[argsLength - 1]);
					writeOutputToFile(outputFile);
					isOutputRedirection = true;
				}
    		}
    		if(!isOutputRedirection)
    			System.out.println(stdout);
			System.out.println("Status code is " + itool.getStatusCode());
			Thread.currentThread().interrupt();
    	}
    }

    public boolean writeOutputToFile(File outputFile){
    	//Check for output file
    	String outputMsg="";
		
		try{
			if(!outputFile.exists())
				outputFile.createNewFile();
			FileWriter fw = new FileWriter(outputFile.getAbsoluteFile());
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
		}
		return true;	    
    }

  }

