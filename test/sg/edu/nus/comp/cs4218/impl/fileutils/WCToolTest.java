package sg.edu.nus.comp.cs4218.impl.fileutils;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import sg.edu.nus.comp.cs4218.extended2.IWcTool;
import sg.edu.nus.comp.cs4218.impl.extended2.CUTTool;
import sg.edu.nus.comp.cs4218.impl.extended2.WCTool;

public class WCToolTest {

	private IWcTool wctool; 
	String actualOutput,expectedOutput,helpOutput;
	File workingDirectory;
	
	@Before
	public void before(){
		
		workingDirectory = new File(System.getProperty("user.dir"));
		
		helpOutput = "wc [OPTIONS] [FILE]" + "\n" +
				"FILE : Name of the file" + "\n" +
				"OPTIONS : -m : Print only the number of characters \n" +
				"          -w : Print only the number of words \n" +
				"          -l : Print only the number of lines \n" +
				"          -help : Brief information about supported options." ;
	}
	
	@After
	public void after(){
		wctool = null;
	}
	
	@Test
    public void helpTest()
    {
    	String[] arguments = new String[]{"-help"} ;
		wctool = new WCTool(arguments);
		actualOutput = wctool.execute(workingDirectory, null);
		expectedOutput = helpOutput ;
		assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
		assertEquals(wctool.getStatusCode(), 0);
    }
    
	@Test
    public void invalidInputTest()
    {
    	String[] arguments = new String[]{"-m", "-w", "-l", "-"} ;
    	wctool = new WCTool(arguments);
		actualOutput = wctool.execute(workingDirectory, "abcde");
		expectedOutput = "abcde : error - Invalid Input. \n";
		assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
		assertEquals(wctool.getStatusCode(), 0);
    }

	@Test
    public void overallFunctionalityNonExistingFileTest()
    {
    	String[] arguments = new String[]{"-m", "-w", "-l", "C:\\Users\\Madhu\\Dropbox\\sem8\\CS4218\\Code\\shell4\\shell3\\tets.txt"} ;
		wctool = new WCTool(arguments);
		actualOutput = wctool.execute(workingDirectory, null);
		expectedOutput = "C:\\Users\\Madhu\\Dropbox\\sem8\\CS4218\\Code\\shell4\\shell3\\tets.txt :  -m  3 -w  1 -l  1\n";
		//expectedOutput = "tets.txt : -m  3 -w  1 -l  1\n";
		assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
		assertEquals(wctool.getStatusCode(), -1);
    }
    
}
