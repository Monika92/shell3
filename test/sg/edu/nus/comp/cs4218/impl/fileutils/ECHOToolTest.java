package sg.edu.nus.comp.cs4218.impl.fileutils;

import static org.junit.Assert.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import sg.edu.nus.comp.cs4218.extended2.ICutTool;
import sg.edu.nus.comp.cs4218.fileutils.IEchoTool;
import sg.edu.nus.comp.cs4218.impl.extended2.CUTTool;

public class ECHOToolTest {
	
	private IEchoTool echotool; 
	String actualOutput,expectedOutput;
	File workingDirectory;
	String stdin;
	
	@Before
	public void before(){
		workingDirectory = new File(System.getProperty("user.dir"));
		stdin = null;
	}
	
    @After
	public void after(){
		echotool = null;
	}
    
    @Test
    public void echoBasicTest(){
    	String[] arguments = new String[]{"This is a test run."} ;
		echotool = new ECHOTool(arguments);
		actualOutput = echotool.execute(workingDirectory, stdin);
		expectedOutput = "This is a test run.";
		assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
		assertEquals(echotool.getStatusCode(), 0);
    }
    
    @Test
    public void echoEmptyStringTest(){
    	String[] arguments = new String[]{""} ;
		echotool = new ECHOTool(arguments);
		actualOutput = echotool.execute(workingDirectory, stdin);
		expectedOutput = "";
		assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
		assertEquals(echotool.getStatusCode(), 0);
    }
    
    @Test
    public void echoQuotesTest(){
    	String[] arguments = new String[]{"This is \n\"a test' \nr\"un."} ;
		echotool = new ECHOTool(arguments);
		actualOutput = echotool.execute(workingDirectory, stdin);
		expectedOutput = "This is \na test \nrun.";
		assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
		assertEquals(echotool.getStatusCode(), 0);
    }
    
    @Test
    public void echoSpecialChars1Test(){
    	String[] arguments = new String[]{"This is \n$a te:*()/\\;s[]\t \nrun."} ;
		echotool = new ECHOTool(arguments);
		actualOutput = echotool.execute(workingDirectory, stdin);
		expectedOutput = "This is \n$a te:*()/\\;s[]\t \nrun.";
		assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
		assertEquals(echotool.getStatusCode(), 0);
    }
    
    @Test
    public void echoSpecialChars2Test(){
    	String[] arguments = new String[]{"This is \na t%#@~`'^\"&est \nrun."} ;
		echotool = new ECHOTool(arguments);
		actualOutput = echotool.execute(workingDirectory, stdin);
		expectedOutput = "This is \na t%#@~`^&est \nrun.";
		assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
		assertEquals(echotool.getStatusCode(), 0);
    }
    
}
