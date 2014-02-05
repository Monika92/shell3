package sg.edu.nus.comp.cs4218.impl.fileutils;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import sg.edu.nus.comp.cs4218.extended2.IPasteTool;
import sg.edu.nus.comp.cs4218.impl.extended2.CUTTool;
import sg.edu.nus.comp.cs4218.impl.extended2.PASTETool;

public class PASTEToolTest {
	
	private IPasteTool pasteTool;
	String actualOutput,expectedOutput,helpOutput;
	File workingDirectory;
	
	@Before
	public void before() throws Exception {
		workingDirectory = new File(System.getProperty("user.dir"));
		helpOutput = "paste : writes to standard output lines "
				+ "\n* of sequentially corresponding lines of each given file,"
				+ "\n* separated by a TAB character \n"
				+ "\n* Command Format - paste [OPTIONS] [FILE]"
				+ "\n* FILE - Name of the file, when no file is present (denoted by \"-\") "
				+ "\n* use standard input OPTIONS"
				+ "\n -s : paste one file at a time instead of in parallel"
				+ "\n -d DELIM: Use characters from the DELIM instead of TAB character"
				+ "\n -help : Brief information about supported options";

	}

	@After
	public void after() throws Exception {
		pasteTool = null;
	}

	@Test
	public void pasteGetHelpAsOnlyArgumentTest() {
		fail("Not yet implemented");
		
		String[] arguments = new String[]{};
		pasteTool = new PASTETool(arguments);
		actualOutput = pasteTool.execute(workingDirectory, null);
		expectedOutput = helpOutput;
		assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
		assertEquals(pasteTool.getStatusCode(), 0);
		
	}
	
	@Test
	public void pasteGetHelpWithOtherArgumentsTest() {
		fail("Not yet implemented");
	}
	
	@Test
	public void pasteNoOptionsNoArgumentsTest(){
		fail("Not yet implemented");
	}
	
	@Test
	public void pasteNoOptionsOneFileTest(){
		
	}
	
	@Test
	public void pasteNoOptionsStdinTest(){
		
	}
	
	@Test
	public void pasteNoOptionsManyFilesTest(){
		
	}
	
	@Test
	public void pasteUseDelimiterNoArgumentsTest(){
		
	}
	
	@Test
	public void pasteUseDelimiterOneFileTest(){
		
	}
	
	@Test
	public void pasteUseDelimiterStdinTest(){
		
	}
	
	@Test
	public void pasteUseDelimiterManyFilesTest(){
		
	}
	
	@Test
	public void pasteSerialNoArgumentsTest(){
		
	}
	
	@Test
	public void pasteSerialOneFileTest(){
		
	}
	
	@Test
	public void pasteSerialStdinTest(){
		
	}
	
	@Test
	public void pasteSerialManyFilesTest(){
		
	}	
	
	
	

}
