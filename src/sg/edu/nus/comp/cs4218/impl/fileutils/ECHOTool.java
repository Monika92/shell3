package sg.edu.nus.comp.cs4218.impl.fileutils;

import java.io.File;
import java.util.ArrayList;

import sg.edu.nus.comp.cs4218.IShell;
import sg.edu.nus.comp.cs4218.fileutils.IEchoTool;
import sg.edu.nus.comp.cs4218.impl.ATool;
import sg.edu.nus.comp.cs4218.impl.CommandVerifier;

public class ECHOTool extends ATool implements IEchoTool {

/*
 * Constructor for ECHOTool - initializes the super class's arguments
 * with the passed arguments.
 */
	public ECHOTool(String[] arguments) {
		super(arguments);
		// TODO Auto-generated constructor stub
	}

/*
 * Returns the string passed as argument.
 */
	@Override
	public String echo(String toEcho) {
		// TODO Auto-generated method stub
		
		//Check for null input
		if(toEcho == null){
			setStatusCode(-1);
			return "";
		}
		return toEcho;
	}

/*
 * Executes the echo command.
 */
	@Override
	public String execute(File workingDir, String stdin) {
			
		//Prioritizing stdin over commandline args
		if(stdin != null){
			setStatusCode(0);
			return stdin;
		}
		
		//Verify command syntax
		CommandVerifier cv = new CommandVerifier();
		int validCode = cv.verifyCommand("echo", super.args);
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
				
		StringBuilder sb = new StringBuilder();
		for(String s : args){
			sb.append(s);
			sb.append(" ");
		}
		String str = sb.toString();
		str=str.replace("\"", "");
		str=str.replace("\'", "");
		str = str.trim();
		
		return echo(str);
	}
	

}
