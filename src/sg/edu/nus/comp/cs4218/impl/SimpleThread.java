package sg.edu.nus.comp.cs4218.impl;

import java.io.File;

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
  }
