/**
 * 
 */
package sg.edu.nus.comp.cs4218.impl.extended1;

import java.io.File;

import sg.edu.nus.comp.cs4218.extended1.IGrepTool;
import sg.edu.nus.comp.cs4218.impl.ATool;

/**
 * @author Admin
 *
 */
public class GREPTool extends ATool implements IGrepTool {

	public static final String GREP_FILE_ERR_MSG = null;
	public static final Object GREP_ERR_CODE = null;

	/**
	 * @param arguments
	 */
	public GREPTool(String[] arguments) {
		super(arguments);
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see sg.edu.nus.comp.cs4218.extended1.IGrepTool#getCountOfMatchingLines(java.lang.String, java.lang.String)
	 */
	@Override
	public int getCountOfMatchingLines(String pattern, String input) {
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see sg.edu.nus.comp.cs4218.extended1.IGrepTool#getOnlyMatchingLines(java.lang.String, java.lang.String)
	 */
	@Override
	public String getOnlyMatchingLines(String pattern, String input) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see sg.edu.nus.comp.cs4218.extended1.IGrepTool#getMatchingLinesWithTrailingContext(int, java.lang.String, java.lang.String)
	 */
	@Override
	public String getMatchingLinesWithTrailingContext(int option_A,
			String pattern, String input) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see sg.edu.nus.comp.cs4218.extended1.IGrepTool#getMatchingLinesWithLeadingContext(int, java.lang.String, java.lang.String)
	 */
	@Override
	public String getMatchingLinesWithLeadingContext(int option_B,
			String pattern, String input) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see sg.edu.nus.comp.cs4218.extended1.IGrepTool#getMatchingLinesWithOutputContext(int, java.lang.String, java.lang.String)
	 */
	@Override
	public String getMatchingLinesWithOutputContext(int option_C,
			String pattern, String input) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see sg.edu.nus.comp.cs4218.extended1.IGrepTool#getMatchingLinesOnlyMatchingPart(java.lang.String, java.lang.String)
	 */
	@Override
	public String getMatchingLinesOnlyMatchingPart(String pattern, String input) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see sg.edu.nus.comp.cs4218.extended1.IGrepTool#getNonMatchingLines(java.lang.String, java.lang.String)
	 */
	@Override
	public String getNonMatchingLines(String pattern, String input) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see sg.edu.nus.comp.cs4218.extended1.IGrepTool#getHelp()
	 */
	@Override
	public String getHelp() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see sg.edu.nus.comp.cs4218.impl.ATool#execute(java.io.File, java.lang.String)
	 */
	@Override
	public String execute(File workingDir, String stdin) {
		// TODO Auto-generated method stub
		return null;
	}

}
