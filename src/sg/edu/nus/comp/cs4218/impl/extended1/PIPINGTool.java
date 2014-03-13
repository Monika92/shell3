package sg.edu.nus.comp.cs4218.impl.extended1;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import sg.edu.nus.comp.cs4218.ITool;
import sg.edu.nus.comp.cs4218.extended1.IPipingTool;
import sg.edu.nus.comp.cs4218.impl.ATool;
import sg.edu.nus.comp.cs4218.impl.CommandVerifier;
import sg.edu.nus.comp.cs4218.impl.extended2.*;
import sg.edu.nus.comp.cs4218.impl.fileutils.*;

public class PIPINGTool extends ATool implements IPipingTool {
	
	private File workingDir = null;
	String pipeArgs[];
	List<String> args;
	
	private boolean isFirstTool,toolWithStdin;
	ArrayList<String> invalidPipeTool;
	ArrayList<String> validFirstTools;
	ArrayList<String> validTools;
		
	public PIPINGTool (String[] args, String[] args2){
		super(args);
		pipeArgs = args;
		toolWithStdin = true;
	}
	
	private int getNumberOfToolsInPipe(){
		int numPipe = 0;
		args = new ArrayList<String>();
		for(int i=0; i<pipeArgs.length; i++){
			args.add(pipeArgs[i]);
			
			if(args.get(i).compareToIgnoreCase("|") == 0){
				numPipe++;
			}
		}	
		return numPipe;
	}
	
	public String[] appendHyphenToArgs(String[] toolArguments){
		String[] temp = new String[toolArguments.length + 1];
		int i = 0;
		for(; i<toolArguments.length; i++){
			temp[i] = toolArguments[i];
		}
		temp[i] = "-";
		return temp;
	}
	
	@Override
	public String execute(File workingDir, String stdin) {		
		this.workingDir = workingDir;
		String result = "";
		
		if(workingDir == null){
			setStatusCode(-1);
			return "";
		}		
		if(!workingDir.exists()){
			setStatusCode(-1);
			return "";
		}
		
		String[] argsCopy = super.args;
		if(argsCopy == null){
			setStatusCode(-1);
			return "";
		}
		if(argsCopy[argsCopy.length -1].compareTo("|") == 0){
			setStatusCode(-1);
			return "";
		}
		
		setStatusCode(0); 
	
		int numPipe = getNumberOfToolsInPipe();
			
		boolean firstTool = true,secondTool = true,firstPipeExecuted = false;
		int begIdx, endIdx = 0;
		String pipeCmd = "", intermedResult = "";
		ITool from = null, to = null, temp = null;
		for( int i = 0; i< numPipe + 1; i++){		
			
			pipeCmd = args.get(0); begIdx = 1;
			
			if(i == numPipe){
				endIdx = args.size()-1;
			}
			else{
				endIdx = (args.indexOf("|")) - 1;			
			}
			
			String[] toolArguments = getToolArguments(args,begIdx,endIdx);						
			if(checkTool(toolArguments, pipeCmd) == false){
				setStatusCode(-1);
				return "";
			}
			else if(!firstTool){
				//added for command verifier to pass syntax check for
				//command when called from within the tool			
				if(!pipeCmd.equalsIgnoreCase("comm") && !pipeCmd.equalsIgnoreCase("echo")){				
					toolArguments = appendHyphenToArgs(toolArguments);
				}
			}
			
			temp = getToolType(pipeCmd,toolArguments);				
			if(temp == null){
				setStatusCode(-1);
				return "";
			}
			
			if(firstTool){				
				from = temp;
				printAll(pipeCmd,toolArguments);			
				firstTool = false;
				args = args.subList(endIdx + 2,args.size());
			}
			else if(secondTool){		
				to = temp;
				printAll(pipeCmd,toolArguments);
				secondTool = false;
				if(i != numPipe){
					args = args.subList(endIdx + 2,args.size());
				}
			}
			else{
				to = temp;		
				printAll(pipeCmd,toolArguments);
				intermedResult = pipe(intermedResult,to);
				if(getStatusCode() != 0){
					return "";
				}
				
				if(i != numPipe){
					args = args.subList(endIdx + 2,args.size());
				}
			}
			
			if(!firstPipeExecuted){
				if(from != null && to != null){
					intermedResult = pipe(from, to);
					firstPipeExecuted = true;
					
					//if error in executing these tools.
					if(getStatusCode() != 0){
						return "pipe break at " + pipeCmd;
					}
				}
			}			
		}			
		return intermedResult;
	}
	
	public void printAll(String cmd, String[] args){
		
		System.out.println("Command: " +  cmd);
		System.out.print("Args: ");
		for(int i = 0; i<args.length; i++){
			System.out.print(args[i] + "\t");
		}
	}
	
	public String[] getToolArguments(List<String> list,int beg, int end){
		String[] args = new String[end - beg + 1];	
		for( int i = beg,j=0; i<=end; i++,j++){
			args[j] = list.get(i);
		}		
		return args;
	}
	
	public ITool getToolType(String command, String[] args){
		//Basic utilities
		if (command.equalsIgnoreCase("pwd"))
			return new PWDTool();
		else if (command.equalsIgnoreCase("cd"))
			return new CDTool(args);
		else if (command.equalsIgnoreCase("ls"))
			return new LSTool(args);
		else if (command.equalsIgnoreCase("copy"))
			return new COPYTool(args);
		else if (command.equalsIgnoreCase("move"))
			return new MOVETool(args);
		else if (command.equalsIgnoreCase("delete"))
			return new DELETETool(args);
		else if (command.equalsIgnoreCase("cat"))
			return new CATTool(args);
		else if (command.equalsIgnoreCase("echo"))
			return new ECHOTool(args);

		// text utilities
		else if (command.equalsIgnoreCase("cut"))
			return new CUTTool(args);
		else if (command.equalsIgnoreCase("comm"))
			return new COMMTool(args);
		else if (command.equalsIgnoreCase("paste"))
			return new PASTETool(args);
		else if (command.equalsIgnoreCase("sort"))
			return new SORTTool(args);
		else if (command.equalsIgnoreCase("uniq"))
			return new UNIQTool(args);
		else if (command.equalsIgnoreCase("wc"))
			return new WCTool(args);
		else if (command.equalsIgnoreCase("grep"))
			return new GREPTool(args);
		else return null;
	}

	@Override
	public String pipe(ITool from, ITool to) {
		
		super.setStatusCode(0);		
		String leftToolResult = "",rightToolResult = "";
		
		if(from == null && to == null){
			super.setStatusCode(-1);
			return leftToolResult;
		}
		
		if(from == null){
			leftToolResult = "";
		}
		else{	
					
			leftToolResult = from.execute(workingDir, null); 
			if(from.getStatusCode() != 0){
				leftToolResult = "";
				setStatusCode(-1);
				return "";
			}		
			
			if(to == null){
				setStatusCode(-1);
				return leftToolResult + " " + "pipe error";
			}
			
			rightToolResult = pipe(leftToolResult,to);	
		}
		
		//System.out.println("left Tool result: " + leftToolResult);	
		//System.out.println("right tool resutl: " + rightToolResult);
		
		return rightToolResult;
	}
	
	@Override
	public String pipe(String stdout, ITool to) {
		
		if(toolWithStdin == false){
			stdout = null;
		}
			
		super.setStatusCode(0);		
		String result = "";
		
		if(to == null){
			super.setStatusCode(-1);
			return result;
		}
		
		result = to.execute(workingDir, stdout);	
		if(to.getStatusCode() != 0){
			setStatusCode(-1);
			return result;
		}
		
		return result;
	}
	
	public void initializeCheckerStructures(){		
		if(invalidPipeTool == null){
			isFirstTool = true;// so that initialized to true only once
			invalidPipeTool = new ArrayList<String>();
			invalidPipeTool.add("copy");invalidPipeTool.add("move");
			invalidPipeTool.add("delete");invalidPipeTool.add("cd");
		}
		
		if(validTools == null){
			validTools = new ArrayList<String>();
			validTools.add("grep");validTools.add("uniq");validTools.add("wc");
			validTools.add("echo");validTools.add("cat");validTools.add("cut");
			validTools.add("paste");validTools.add("sort");
			validTools.add("comm");
		}
		
		if(validFirstTools == null){
			validFirstTools = new ArrayList<String>();
			validFirstTools.add("pwd");
			validFirstTools.add("ls");
			//validFirstTools.add("comm");
		}
	}

	public boolean checkTool(String[] args, String cmd){
		boolean valid = true;		
		toolWithStdin = false;
		
		initializeCheckerStructures();
						
		if(isFirstTool){
			isFirstTool = false;			
			if(!validFirstTools.contains(cmd) && !validTools.contains(cmd)){
				return false;
			}
			
			CommandVerifier cv = new CommandVerifier();			
			int validCode = cv.verifyCommand(cmd, args);
			
			if(validCode == -1){
				valid = false;
			}
			else{
				valid = true;
			}	
		}
		else{
			
			//these tools cannot be anything but the first tool in the pipe
			if(validFirstTools.contains(cmd)){
				valid = false;
			}
			//or if these dont belong to valid pipe tools
			else if(!validTools.contains(cmd)){
				valid = false;
			}
			else if(cmd.equalsIgnoreCase("comm")){
				CommandVerifier cv = new CommandVerifier();			
				int validCode = cv.verifyCommand("comm", args);
				
				if(validCode == -1){
					valid = false;
				}
				else{
					valid = true;
				}	
			}
			else{
				//this command verifier is invoked for pipe tool checking
				CommandVerifier cv = new CommandVerifier(true);			
				int validCode = cv.verifyCommand(cmd, args);
				
				if(validCode == -1){
					valid = false;
				}
				else if (validCode == 2){
					toolWithStdin = true;
					valid = true;
				}else{
					valid = true;
				}
			}
		}
		return valid;
	}
	
}
