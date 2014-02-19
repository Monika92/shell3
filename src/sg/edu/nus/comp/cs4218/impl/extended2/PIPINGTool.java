/**
 * 
 */
package sg.edu.nus.comp.cs4218.impl.extended2;

import java.io.File;

import sg.edu.nus.comp.cs4218.ITool;
import sg.edu.nus.comp.cs4218.extended1.IPipingTool;
import sg.edu.nus.comp.cs4218.impl.ATool;

/**
 * @author Admin
 *
 */
public class PIPINGTool extends ATool implements IPipingTool {
	
	public PIPINGTool (String[] args, String[] args2){
		super(args);
	}
	
	/* (non-Javadoc)
	 * @see sg.edu.nus.comp.cs4218.ITool#execute(java.io.File, java.lang.String)
	 */
	@Override
	public String execute(File workingDir, String stdin) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see sg.edu.nus.comp.cs4218.ITool#getStatusCode()
	 */
	@Override
	public int getStatusCode() {
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see sg.edu.nus.comp.cs4218.extended1.IPipingTool#pipe(sg.edu.nus.comp.cs4218.ITool, sg.edu.nus.comp.cs4218.ITool)
	 */
	@Override
	public String pipe(ITool from, ITool to) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see sg.edu.nus.comp.cs4218.extended1.IPipingTool#pipe(java.lang.String, sg.edu.nus.comp.cs4218.ITool)
	 */
	@Override
	public String pipe(String stdout, ITool to) {
		// TODO Auto-generated method stub
		return null;
	}

}
