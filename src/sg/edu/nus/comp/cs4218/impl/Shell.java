package sg.edu.nus.comp.cs4218.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
public class Shell extends Thread implements IShell {

	String command;
	String[] argsList, rawArgs;
	int commandVerifyFlag;
	static CommandVerifier verifier;

	@Override
	public ITool parse(String commandline) {

		command = null;
		int argsLength;
		ArrayList<String> list = new ArrayList<String>();
		Matcher m = Pattern.compile("([^\"]\\S*|\".+?\")\\s*").matcher(
				commandline);
		while (m.find())
			list.add(m.group(1));

		if (list.size() >= 1) {
			String[] cmdWords = new String[list.size()];
			cmdWords = list.toArray(cmdWords);

			if (cmdWords.length > 1)
				rawArgs = new String[cmdWords.length - 1];
			else
				rawArgs = null;

			if (cmdWords.length > 1)
				argsList = new String[cmdWords.length - 1];
			else
				argsList = null;

			if (cmdWords != null) {

				command = cmdWords[0];
				for (int i = 1; i < cmdWords.length; i++) {
					argsList[i - 1] = cmdWords[i];
					rawArgs[i - 1] = cmdWords[i];
				}

				// -1 incorrect
				// 0 show help
				// 1 execute normally
//				commandVerifyFlag = verifier.verifyCommand(command, argsList);
//				if (commandVerifyFlag == 0) {
//					argsList = new String[1];
//					argsList[0] = "-help";
//				}

				// Check for redirection
				if (rawArgs != null) {
					argsLength = rawArgs.length;
					if (argsLength > 2
							&& rawArgs[argsLength - 2].equalsIgnoreCase(">"))
						argsLength -= 2;
					argsList = Arrays.copyOfRange(rawArgs, 0, argsLength);
				} else
					argsList = null;

				//if (commandVerifyFlag != -1) {

					if (command.equalsIgnoreCase("pwd"))
						return new PWDTool();
					else if (command.equalsIgnoreCase("cd"))
						return new CDTool(argsList);
					else if (command.equalsIgnoreCase("ls"))
						return new LSTool(argsList);
					else if (command.equalsIgnoreCase("copy"))
						return new COPYTool(argsList);
					else if (command.equalsIgnoreCase("move"))
						return new MOVETool(argsList);
					else if (command.equalsIgnoreCase("delete"))
						return new DELETETool(argsList);
					else if (command.equalsIgnoreCase("cat"))
						return new CATTool(argsList);
					else if (command.equalsIgnoreCase("echo"))
						return new ECHOTool(argsList);

					// text utilities
					else if (command.equalsIgnoreCase("cut"))
						return new CUTTool(argsList);
					else if (command.equalsIgnoreCase("comm"))
						return new COMMTool(argsList);
					else if (command.equalsIgnoreCase("paste"))
						return new PASTETool(argsList);
					else if (command.equalsIgnoreCase("sort"))
						return new SORTTool(argsList);
					else if (command.equalsIgnoreCase("uniq"))
						return new UNIQTool(argsList);
					else if (command.equalsIgnoreCase("wc"))
						return new WCTool(argsList);

					else if (command.equalsIgnoreCase("Ctrl-Z")) {
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
				}
			}
//		}
		System.err.println("Command Syntax InCorrect!");
		return null;
	}

	/**
	 * Executes the tool, starts a new thread, and returns the thread handle.
	 * 
	 * @param tool
	 * @return
	 */
	@Override
	public Runnable execute(ITool itool) {
		// TODO Implement

		String stdin = null;

		if (argsList != null) {
			if (argsList[argsList.length - 1].equalsIgnoreCase("-")
					&& !command.equalsIgnoreCase("cd")
					&& !command.equalsIgnoreCase("echo")
					&& !command.equalsIgnoreCase("ls")
					&& !command.equalsIgnoreCase("pwd")
					&& !command.equalsIgnoreCase("move")
					&& !command.equalsIgnoreCase("copy")
					&& !command.equalsIgnoreCase("delete")) {

				System.out.println("Enter stdin: ");
				Scanner scanner = new Scanner(System.in);
				stdin = scanner.nextLine();

				while (stdin.equalsIgnoreCase("Ctrl-Z") != true) {
					ExecutionThread sThread = new ExecutionThread(itool, stdin,
							argsList);
					ExecutorService executorService = Executors
							.newFixedThreadPool(2);
					Future<?> threadT2 = executorService.submit(sThread);

					scanner = new Scanner(System.in);
					stdin = scanner.nextLine();
					
				}
			} else {
				ExecutionThread sThread = new ExecutionThread(itool, stdin, argsList);
				ExecutorService executorService = Executors
						.newFixedThreadPool(2);
				Future<?> threadT2 = executorService.submit(sThread);

				if (command.equalsIgnoreCase("Ctrl-Z")) {
					threadT2.cancel(true);
					System.out.println("All commands stopped");
				}
			}
		} else {
			ExecutionThread sThread = new ExecutionThread(itool, stdin, argsList);
			ExecutorService executorService = Executors.newFixedThreadPool(2);
			Future<?> threadT2 = executorService.submit(sThread);

			if (command.equalsIgnoreCase("Ctrl-Z")) {
				threadT2.cancel(true);
				System.out.println("All commands stopped");
			}
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

		Shell shell = new Shell();
		ITool itool = null;
		verifier = new CommandVerifier();
		String input = null;
		String userDirectory = System.getProperty("user.dir");
		WorkingDirectory.workingDirectory = new File(userDirectory);
		
		System.out.println(">");
		Scanner scanner = new Scanner(System.in);
		input = scanner.nextLine();
		if (!input.trim().isEmpty())
			itool = shell.parse(input);

		while (true) {
			if (itool != null) {
				shell.execute(itool);
			}

			System.out.println(">");
			scanner = new Scanner(System.in);
			input = scanner.nextLine();
			itool = shell.parse(input);

		}
	}
	
	
	public String[] getArgumentList()
	{
		return argsList;
	}
}
