package sg.edu.nus.comp.cs4218.impl;

import java.io.File;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import sg.edu.nus.comp.cs4218.ITool;
import sg.edu.nus.comp.cs4218.IShell;
import sg.edu.nus.comp.cs4218.impl.extended2.COMMTool;
import sg.edu.nus.comp.cs4218.impl.extended2.CUTTool;
import sg.edu.nus.comp.cs4218.impl.extended2.PASTETool;
import sg.edu.nus.comp.cs4218.impl.extended2.SORTTool;
import sg.edu.nus.comp.cs4218.impl.extended2.UNIQTool;
import sg.edu.nus.comp.cs4218.impl.extended2.WCTool;
import sg.edu.nus.comp.cs4218.impl.fileutils.*;

/**
 * The Shell is used to interpret and execute user's commands. Following
 * sequence explains how a basic shell can be implemented in Java
 */
public class Shell extends Thread implements IShell{

	String command;
	String[] argsList;
	// need to remove options list?
	String[] optionsList;
	int commandVerifyFlag;
	static CommandVerifier verifier;

	@Override
	public ITool parse(String commandline) {

		command = null;
		String[] cmdWords = commandline.split(" ");
		if(cmdWords.length!=1)
			argsList = new String[cmdWords.length-1];
		else
			argsList = null;
		
		if (cmdWords != null) {

			command = cmdWords[0];
			for (int i = 1; i < cmdWords.length; i++)
				argsList[i - 1] = cmdWords[i];

			//commandVerifyFlag = verifier.verifyCommand(command, argsList,
			//		optionsList);

			//if (commandVerifyFlag != 0) {
				if (command.equalsIgnoreCase("pwd"))
					return (ITool) new PWDTool();
				else if (command.equalsIgnoreCase("cd"))
					return (ITool) new CDTool(argsList);
				else if (command.equalsIgnoreCase("ls"))
					return (ITool) new LSTool(argsList);
				else if (command.equalsIgnoreCase("copy"))
					return (ITool) new COPYTool(argsList);
				else if (command.equalsIgnoreCase("move"))
					return (ITool) new MOVETool(argsList);
				// else if(command.equalsIgnoreCase("delete"))
				// return new DELETETool(argsList);
				// else if(command.equalsIgnoreCase("cat"))
				// return new CATTool(argsList);
				// else if(command.equalsIgnoreCase("echo"))
				// return new ECHOTool(argsList);
				
				//text utilities
				else if (command.equalsIgnoreCase("cut"))
					return (ITool) new CUTTool(argsList);
				else if (command.equalsIgnoreCase("comm"))
					return (ITool) new COMMTool(argsList);
				else if (command.equalsIgnoreCase("paste"))
					return (ITool) new PASTETool(argsList);
				else if (command.equalsIgnoreCase("sort"))
					return (ITool) new SORTTool(argsList);
				else if (command.equalsIgnoreCase("uniq"))
					return (ITool) new UNIQTool(argsList);
				else if (command.equalsIgnoreCase("wc"))
					return (ITool) new WCTool(argsList);
				
				else if (command.equalsIgnoreCase("Ctrl-Z"))
					return new ITool() {
						
						@Override
						public int getStatusCode() {
							// TODO Auto-generated method stub
							return 0;
						}
						
						@Override
						public String execute(File workingDir, String stdin) {
							// TODO Auto-generated method stub
							return null;
						}
					};
			} 
		System.err.println("Command Syntax InCorrect!" + commandline);
		return null;
	}
	/**
	 * Executes the tool, starts a new thread, and returns the thread handle.
	 * @param tool
	 * @return
	 */
	@Override
	public Runnable execute(ITool itool) {
		// TODO Implement
		
		String stdin = null;
		String userDirectory = System.getProperty("user.dir");
		File workingDirectory = new File(userDirectory);

		if(argsList!=null)
		{
			if (argsList[argsList.length - 1].equalsIgnoreCase("-") && !command.equalsIgnoreCase("cd")) 
			{
				Scanner scanner = new Scanner(System.in);
				stdin = scanner.nextLine();
			}
		}
		
		SimpleThread sThread = new SimpleThread(itool,workingDirectory,stdin);
		ExecutorService executorService = Executors.newFixedThreadPool(2);
        Future<?> threadT2 = executorService.submit(sThread);
		
		if(command.equalsIgnoreCase("Ctrl-Z"))
		{
			threadT2.cancel(true);
			System.out.println("All commands stopped");
		}
		return null;
	
	}

	@Override
	public void stop(Runnable toolExecution) {
		// TODO Implement
		
	}

	
	/**
	 * Do Forever 1. Wait for a user input 2. Parse the user input. Separate the
	 * command and its arguments 3. Create a new thread to execute the command
	 * 4. Execute the command and its arguments on the newly created thread.
	 * Exit with the status code of the executed command 5. In the shell, wait
	 * for the thread to complete execution 6. Report the exit status of the
	 * command to the user
	 */

	// Lets say this is threadT1
	public static void main(String[] args) {
		// TODO Implement

		Shell shell = new Shell();
		verifier = new CommandVerifier();
		String input = null;
		Scanner scanner = new Scanner(System.in);
		input = scanner.nextLine();
		ITool itool = shell.parse(input);
		
		while (true) 
		{
			if (itool != null ) 
			{
				shell.execute(itool);
			}

			scanner = new Scanner(System.in);
			input = scanner.nextLine();
			itool = shell.parse(input);

		}
	}
}
