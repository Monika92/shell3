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
    
    /*
     * Basic test case for echo
     * Command: echo string_input
     */
    @Test
    public void echoBasicTest(){
    	String[] arguments = new String[]{"This is a test run."} ;
		echotool = new ECHOTool(arguments);
		actualOutput = echotool.execute(workingDirectory, stdin);
		expectedOutput = "This is a test run.";
		assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
		assertEquals(echotool.getStatusCode(), 0);
    }
    
    /*
     * Test case for echo with empty input
     * Command: echo 
     */
    @Test
    public void echoEmptyStringTest(){
    	String[] arguments = new String[]{""} ;
		echotool = new ECHOTool(arguments);
		actualOutput = echotool.execute(workingDirectory, stdin);
		expectedOutput = "";
		assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
		assertEquals(echotool.getStatusCode(), 0);
    }
    
    /*
     * Test case for echo to check removal of quotes from input
     * Command: echo "This is \n\"a test' \nr\"un."
     */
    @Test
    public void echoQuotesTest(){
    	String[] arguments = new String[]{"This is \n\"a test' \nr\"un."} ;
		echotool = new ECHOTool(arguments);
		actualOutput = echotool.execute(workingDirectory, stdin);
		expectedOutput = "This is \na test \nrun.";
		assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
		assertEquals(echotool.getStatusCode(), 0);
    }
    
    /*
     * Test case 1 for echo with special characters in input string
     * Command; echo "This is \n$a te:*()/\\;s[]\t \nrun."
     */
    @Test
    public void echoSpecialChars1Test(){
    	String[] arguments = new String[]{"This is \n$a te:*()/\\;s[]\t \nrun."} ;
		echotool = new ECHOTool(arguments);
		actualOutput = echotool.execute(workingDirectory, stdin);
		expectedOutput = "This is \n$a te:*()/\\;s[]\t \nrun.";
		assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
		assertEquals(echotool.getStatusCode(), 0);
    }
    
    /*
     * Test case 2 for echo with special characters in input string
     * Command; echo "This is \na t%#@~`'^\"&est \nrun."
     */
    @Test
    public void echoSpecialChars2Test(){
    	String[] arguments = new String[]{"This is \na t%#@~`'^\"&est \nrun."} ;
		echotool = new ECHOTool(arguments);
		actualOutput = echotool.execute(workingDirectory, stdin);
		expectedOutput = "This is \na t%#@~`^&est \nrun.";
		assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
		assertEquals(echotool.getStatusCode(), 0);
    }
    
    /*
     * Null test case for echo interface method
     * Constructor for echotool initialised with null arguments
     */
    @Test
    public void echoNullTest(){
    	String[] arguments = null ;
		echotool = new ECHOTool(arguments);
		actualOutput = echotool.echo(null);
		assertTrue(echotool.getStatusCode() != 0);
    }
    
    /*
     * Null test case for execute interface method
     * Constructor for echotool initialised with null arguments as well as workingDir and stdin
     */
    @Test
    public void echoExecuteNullTest(){
    	String[] arguments = null ;
		echotool = new ECHOTool(arguments);
		actualOutput = echotool.execute(null, null);
		assertTrue(echotool.getStatusCode() != 0);
    }
    
}
