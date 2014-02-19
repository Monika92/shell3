/**
 * 
 */
package sg.edu.nus.comp.cs4218.impl.extended2;

import java.io.File;

import sg.edu.nus.comp.cs4218.ITool;
import sg.edu.nus.comp.cs4218.extended1.IPipingTool;
import sg.edu.nus.comp.cs4218.impl.ATool;
import sg.edu.nus.comp.cs4218.impl.fileutils.CATTool;

/**
 * @author Admin
 *
 */
public class PIPINGTool extends ATool implements IPipingTool {
	
	private File workingDir = null;
	
	public PIPINGTool (String[] args, String[] args2){
		super(args);
		
	}
	
	/* (non-Javadoc)
	 * @see sg.edu.nus.comp.cs4218.ITool#execute(java.io.File, java.lang.String)
	 */
	@Override
	public String execute(File workingDir, String stdin) {
		// TODO Auto-generated method stub
		
		this.workingDir = workingDir;
		return null;
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
		
		//System.out.println("left Tool result: " + leftToolResult);	
		//System.out.println("right tool resutl: " + rightToolResult);
		
		return rightToolResult;
	}
	
	
	/* (non-Javadoc)
	 * @see sg.edu.nus.comp.cs4218.extended1.IPipingTool#pipe(java.lang.String, sg.edu.nus.comp.cs4218.ITool)
	 */
	@Override
	public String pipe(String stdout, ITool to) {
		
		super.setStatusCode(0);		
		String result = "";
		
		if(to == null){
			super.setStatusCode(-1);
			return result;
		}
		
		result = to.execute(workingDir, stdout);		
		return result;
	}

}
