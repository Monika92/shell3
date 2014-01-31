package sg.edu.nus.comp.cs4218.impl;

import java.util.ArrayList;
import java.util.HashMap;

public class CommandVerifier {

	private static HashMap<String, ArrayList<Integer>> commandArgCountMap;
	private final int INVALID_COMMAND = 0;
	private final int INVALID_NUM_ARGS = -1;
	private final int INVALID_OPTIONS = -2;
	private final int COMMAND_VALID = 1;

	public CommandVerifier(){
		commandArgCountMap = new HashMap<String, ArrayList<Integer>>();
		Initialize();
	}

	private void Initialize(){

		ArrayList<Integer> temp;

		//commandArgCountMap
		temp = new ArrayList<Integer>();
		temp.add(0);
		commandArgCountMap.put("pwd", temp);

		temp = new ArrayList<Integer>();
		temp.add(0);
		temp.add(1);
		commandArgCountMap.put("cd", temp);

		temp = new ArrayList<Integer>();
		temp.add(0);
		commandArgCountMap.put("ls", temp);

		temp = new ArrayList<Integer>();
		temp.add(2);
		commandArgCountMap.put("copy", temp);

		temp = new ArrayList<Integer>();
		temp.add(2);
		commandArgCountMap.put("move", temp);

		temp = new ArrayList<Integer>();
		temp.add(1);
		commandArgCountMap.put("delete", temp);		
	}

	public HashMap<String, ArrayList<Integer>> getCommandArgCountMap(){
		return commandArgCountMap;
	}

	public int verifyCommand(String cmd, String[] args, String[] ops){

		int status = checkCommandString(cmd);
		if(status != COMMAND_VALID){
			return status;
		}

		//Check if options are valid
		status = verifyOptions();
		if(status != COMMAND_VALID){
			return status;
		}

		//Check if number of arguments are correct
		status = checkArguments(cmd, args);
		if(status != COMMAND_VALID){
			return status;
		}

		return COMMAND_VALID;
	}

	//Functions for Verifier

	//Check if command exists
	private int checkCommandString(String cmd){
		if(commandArgCountMap.containsKey(cmd)){
			displayErrorMesg(INVALID_COMMAND);
			return INVALID_COMMAND;
		}	
		return COMMAND_VALID;
	}

	private int verifyOptions(){
		return COMMAND_VALID;
	}

	//Check if number of arguments are valid
	private int checkArguments(String cmd, String[] args){
		ArrayList<Integer> argCount = commandArgCountMap.get(cmd);
		int numArgs = args.length;
		
		if(!argCount.contains(numArgs)){
			displayErrorMesg(INVALID_NUM_ARGS);
			return INVALID_NUM_ARGS;
			
		}
		return COMMAND_VALID;
	}

	private void displayErrorMesg(int statusCode){
		switch(statusCode){
		case INVALID_NUM_ARGS: System.err.println("Incorrect number of arguments!");break;
		case INVALID_COMMAND: System.err.println("Command doesn't exist!");break;
		case INVALID_OPTIONS: System.err.println("Invalid Options!"); break;
		}
	}
}
