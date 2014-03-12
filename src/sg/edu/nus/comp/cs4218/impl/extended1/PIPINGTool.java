/**
 * 
 */
package sg.edu.nus.comp.cs4218.impl.extended1;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import sg.edu.nus.comp.cs4218.ITool;
import sg.edu.nus.comp.cs4218.extended1.IPipingTool;
import sg.edu.nus.comp.cs4218.impl.ATool;
import sg.edu.nus.comp.cs4218.impl.ArgumentObject;
import sg.edu.nus.comp.cs4218.impl.ArgumentObjectParser;
import sg.edu.nus.comp.cs4218.impl.CommandVerifier;
import sg.edu.nus.comp.cs4218.impl.extended2.COMMTool;
import sg.edu.nus.comp.cs4218.impl.extended2.CUTTool;
import sg.edu.nus.comp.cs4218.impl.extended2.PASTETool;
import sg.edu.nus.comp.cs4218.impl.extended2.SORTTool;
import sg.edu.nus.comp.cs4218.impl.extended2.UNIQTool;
import sg.edu.nus.comp.cs4218.impl.extended2.WCTool;
import sg.edu.nus.comp.cs4218.impl.fileutils.CATTool;
import sg.edu.nus.comp.cs4218.impl.fileutils.CDTool;
import sg.edu.nus.comp.cs4218.impl.fileutils.COPYTool;
import sg.edu.nus.comp.cs4218.impl.fileutils.DELETETool;
import sg.edu.nus.comp.cs4218.impl.fileutils.ECHOTool;
import sg.edu.nus.comp.cs4218.impl.fileutils.LSTool;
import sg.edu.nus.comp.cs4218.impl.fileutils.MOVETool;
import sg.edu.nus.comp.cs4218.impl.fileutils.PWDTool;

/**
 * @author Admin
 *
 */
public class PIPINGTool extends ATool implements IPipingTool {
	
	private File workingDir = null;
	String pipeArgs[];
	
	
	private boolean isFirstTool,toolWithStdin;
	ArrayList<String> invalidPipeTool;
	ArrayList<String> validFirstTools;
	ArrayList<String> validTools;
	
	
	public PIPINGTool (String[] args, String[] args2){
		super(args);
		pipeArgs = args;
	}
	
	/* (non-Javadoc)
	 * @see sg.edu.nus.comp.cs4218.ITool#execute(java.io.File, java.lang.String)
	 */
	@Override
	public String execute(File workingDir, String stdin) {		
		this.workingDir = workingDir;
		String result = "";
		
		if(!workingDir.exists()){
			setStatusCode(-1);
			return "";
		}
				
		setStatusCode(0); 
	
		int numPipe = 0;
		List<String> args = new ArrayList<String>();
		for(int i=0; i<pipeArgs.length; i++){
			args.add(pipeArgs[i]);
			
			if(args.get(i).compareToIgnoreCase("|") == 0){
				numPipe++;
			}
		}
			
		boolean firstCmd = true,secondCmd = true,firstPipeExecuted = false;
		int begIdx, endIdx = 0;
		String pipeCmd = "";
		String intermedResult = "";
		for( int i = 0; i<numPipe; i++){
			
			ITool from = null, to = null;
			
			if(firstCmd){
				pipeCmd = args.get(0);
				begIdx = 1;
				endIdx = (args.indexOf("|")) - 1;
				
				String[] toolArguments = getToolArguments(args,begIdx,endIdx);
				
				if(checkTool(toolArguments, pipeCmd) == false){
					setStatusCode(-1);
					return "";
				}
				
				from = getToolType(pipeCmd,toolArguments);
				
				if(from == null){
					setStatusCode(-1);
					return "";
				}
				printAll(pipeCmd,toolArguments);
				
				firstCmd = false;
				args = args.subList(endIdx + 2,args.size());
			}
			else if(secondCmd){
				pipeCmd = args.get(0);
				begIdx = 1;
				endIdx = (args.indexOf("|")) - 1;
				
				String[] toolArguments = getToolArguments(args,begIdx,endIdx);	
				
				if(checkTool(toolArguments, pipeCmd) == false){
					setStatusCode(-1);
					return "";
				}
							
				to = getToolType(pipeCmd,toolArguments);
				
				if(to == null){
					setStatusCode(-1);
					return "";
				}
				printAll(pipeCmd,toolArguments);
				secondCmd = false;
				args = args.subList(endIdx + 2,args.size());

			}
			else{
				pipeCmd = args.get(0);
				begIdx = 1;
				endIdx = (args.indexOf("|")) - 1;
				
				String[] toolArguments = getToolArguments(args,begIdx,endIdx);	
				
				if(checkTool(toolArguments, pipeCmd) == false){
					setStatusCode(-1);
					return "";
				}
				
				to = getToolType(pipeCmd,toolArguments);
				
				if(to == null){
					setStatusCode(-1);
					return "";
				}
				
				printAll(pipeCmd,toolArguments);
				intermedResult = pipe(intermedResult,to);
				args = args.subList(endIdx + 2,args.size());
			}
			
			if(!firstPipeExecuted){
				if(from != null && to != null);
				intermedResult = pipe(from, to);
				firstPipeExecuted = true;
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
	
	
	/* (non-Javadoc)
	 * @see sg.edu.nus.comp.cs4218.extended1.IPipingTool#pipe(sg.edu.nus.comp.cs4218.ITool, sg.edu.nus.comp.cs4218.ITool)
	 */
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
			}		
				
			rightToolResult = pipe(leftToolResult,to);	
		}
		
		System.out.println("left Tool result: " + leftToolResult);	
		System.out.println("right tool resutl: " + rightToolResult);
		
		return rightToolResult;
	}
	
	
	/* (non-Javadoc)
	 * @see sg.edu.nus.comp.cs4218.extended1.IPipingTool#pipe(java.lang.String, sg.edu.nus.comp.cs4218.ITool)
	 */
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
		return result;
	}
	
	public void initializeCheckerStructures(){
		isFirstTool = true;
		
		invalidPipeTool = new ArrayList<String>();
		invalidPipeTool.add("copy");invalidPipeTool.add("move");
		invalidPipeTool.add("delete");invalidPipeTool.add("cd");
		
		validTools = new ArrayList<String>();
		validTools.add("grep");validTools.add("uniq");validTools.add("wc");
		validTools.add("echo");validTools.add("cat");validTools.add("cut");
		validTools.add("paste");validTools.add("sort");
		
		validFirstTools = new ArrayList<String>();
		validFirstTools.add("pwd");
		validFirstTools.add("ls");
		validFirstTools.add("comm");
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
			else if(!validTools.contains(cmd)){
				valid = false;
			}
			else{
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
