package sg.edu.nus.comp.cs4218.impl.fileutils;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import sg.edu.nus.comp.cs4218.fileutils.ICdTool;
import sg.edu.nus.comp.cs4218.impl.fileutils.CDTool;
import sg.edu.nus.comp.cs4218.impl.WorkingDirectory;

public class CDToolTest {
	
	/*
	 * The way Cd works :
	 * Possible Executions
	 *1. cd (no arguments) - changes working dir to your home directory
	 *2. cd . - keeps your working directory the same
	 *3. cd .. - working directory goes one folder up.
	 *4. cd Directory - Directory can be relatibe or absolute. If its a valid directory path, working dir is changed to that. If invalid, no change.
	 */
	/*
	 * Few Assumptions :
	 * 1. This is what the output looks like when the working directory has been changed successfully :
	 * "Changed current working directory to "+<absolute path of new working dir folder>
	 * 2. In case the input is incorrected, this is the error message :
	 * <file argument entered> + " is not a valid directory. The working directory has not changed."
	 * 
	 * */
	
	private ICdTool cdtool;
	private CDTool cd;
	String actualOutput,expectedOutput;
	File workingDirectory;
	//WorkingDirectory wd;
	String stdin;
	File argFolder = new File("argumentFolder1");
	File inputFile1 = new File("Test_Output.txt") ;
	
@Before
public void before(){
	WorkingDirectory.changeWorkingDirectory(new File(System.getProperty("user.dir")));
	stdin = null;
	String input = "This is \na test \nrun.";
	if(argFolder.mkdirs())
	{}
	
}

@After
public void after(){
	cdtool = null;
	cd = null;
	if(inputFile1.exists())
		inputFile1.delete();
	if(argFolder.exists())
		argFolder.delete();
		
}

/*
 * Test to check the behaviour of chnageDirectory() function
 * */
@Test
public void changeDirectoryTest()
{
	String[] arguments = new String[]{} ;
	cdtool = new CDTool(arguments);
	assertNull(cdtool.changeDirectory("new directory"));
}

/*
 * Test to chcck behaviour of CD Tool with empty argument list.
 * */
@Test
public void cdNoArgumentTest(){
	String[] arguments = new String[]{} ;
	cdtool = new CDTool(arguments);
	actualOutput = cdtool.execute(WorkingDirectory.workingDirectory, stdin);
	expectedOutput = "The working directory is " + (System.getProperty("user.home"));
	assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
	assertEquals(cdtool.getStatusCode(), 0);
}

/*
 * Test to chcck behaviour of CD Tool with file name as an argument.
 * */
@Test
public void cdFileArgumentTest(){
	String[] arguments = new String[]{"Test_Output.txt"} ;
	cdtool = new CDTool(arguments);
	actualOutput = cdtool.execute(WorkingDirectory.workingDirectory, stdin);
	expectedOutput = "Test_Output.txt is not a valid directory. The working directory has not changed.";
	assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
	assertEquals(cdtool.getStatusCode(), -1);
}

/*
 * Test to chcck behaviour of CD Tool with ~ argument.
 * Cd ~ has to change working directory to home directory.
 * */
@Test
public void cdTildeArgumentTest(){
	String[] arguments = new String[]{"~"} ;
	cdtool = new CDTool(arguments);
	actualOutput = cdtool.execute(WorkingDirectory.workingDirectory, stdin);
	expectedOutput = "Changed current working directory to " + (System.getProperty("user.home"));
	assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
	assertEquals(cdtool.getStatusCode(), 0);
	
	String incorrectOutput = "~ is not a valid directory. The working directory has not changed.";
	assertFalse(incorrectOutput.equalsIgnoreCase(actualOutput));
	assertNotEquals(cdtool.getStatusCode(), -1);
	
}

/*
 * Test to chcck behaviour of CD Tool with "~   " argument.
 * Cd ~ has to change working directory to home directory after trimming the argument.
 * */
@Test
public void cdTildeSpaceArgumentTest(){
	String[] arguments = new String[]{"~   "} ;
	cdtool = new CDTool(arguments);
	actualOutput = cdtool.execute(WorkingDirectory.workingDirectory, stdin);
	expectedOutput = "Changed current working directory to " + (System.getProperty("user.home"));
	assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
	assertEquals(cdtool.getStatusCode(), 0);
}

/*
 * Test to chcck behaviour of CD Tool with . argument.
 * Cd . has to keep  the working directory as it is.
 * */
@Test
public void cdDotArgumentTest(){
	String[] arguments = new String[]{"."} ;
	cdtool = new CDTool(arguments);
	actualOutput = cdtool.execute(WorkingDirectory.workingDirectory, stdin);
	expectedOutput = "Changed current working directory to " + WorkingDirectory.workingDirectory;
	assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
	assertEquals(cdtool.getStatusCode(), 0);
}

/*
 * Test to chcck behaviour of CD Tool with "   ." argument.
 * Cd . has to keep  the working directory as it is after trimming the argument.
 * */
@Test
public void cdDotSpaceArgumentTest(){
	String[] arguments = new String[]{"  ."} ;
	cdtool = new CDTool(arguments);
	actualOutput = cdtool.execute(WorkingDirectory.workingDirectory, stdin);
	expectedOutput = "Changed current working directory to " + WorkingDirectory.workingDirectory;
	assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
	assertEquals(cdtool.getStatusCode(), 0);
}

/*
 * Test to chcck behaviour of CD Tool with ".." argument.
 * Cd . has to make  the working directory go one folder up.
 * */
@Test
public void cdDotDotArgumentTest(){
	String[] arguments = new String[]{".."} ;
	cdtool = new CDTool(arguments);
	actualOutput = cdtool.execute(WorkingDirectory.workingDirectory, stdin);
	expectedOutput = "Changed current working directory to " + WorkingDirectory.workingDirectory;
	assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
	assertEquals(cdtool.getStatusCode(), 0);
}

/*
 * Test to chcck behaviour of CD Tool with " .. " argument.
 * Cd . has to make  the working directory go one folder up after trimming the input.
 * */
@Test
public void cdDotDotSpaceArgumentTest(){
	String[] arguments = new String[]{" .. "} ;
	cdtool = new CDTool(arguments);
	actualOutput = cdtool.execute(WorkingDirectory.workingDirectory, stdin);
	expectedOutput = "Changed current working directory to " + WorkingDirectory.workingDirectory;
	assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
	assertEquals(cdtool.getStatusCode(), 0);
}

/*
 * Test to chcck behaviour of CD Tool with "...." argument.
 * Cd .... is invalid input. The OS creates a new folder .... by default but we need to avoid that.
 * */
@Test
public void cdFourDotsArgumentTest(){
	String[] arguments = new String[]{"...."} ;
	cdtool = new CDTool(arguments);
	actualOutput = cdtool.execute(WorkingDirectory.workingDirectory, stdin);
	expectedOutput = ".... is not a valid directory. The working directory has not changed.";
	assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
	assertEquals(cdtool.getStatusCode(), -1);
	
	String incorrectOutput = "Changed current working directory to " + WorkingDirectory.workingDirectory;
	assertFalse(incorrectOutput.equalsIgnoreCase(actualOutput));
	assertNotEquals(cdtool.getStatusCode(), 0);
}

/*
 * Test to chcck behaviour of CD Tool with invalid folder name as argument.
 * Cd Tool has to return error message.
 * */
@Test
public void cdWrongArgumentTest(){
	String[] arguments = new String[]{"blahblah"} ; //assuming a folder called blahblah doesn't exist in your working directory
	cdtool = new CDTool(arguments);
	actualOutput = cdtool.execute(WorkingDirectory.workingDirectory, stdin);
	expectedOutput = "blahblah is not a valid directory. The working directory has not changed.";
	assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
	assertEquals(cdtool.getStatusCode(), -1);
}

/*
 * Test to chcck behaviour of CD Tool with valid folder name as argument with a relative pathname.
 * Cd Tool has to make that folder the working directory.
 * */
@Test
public void cdValidDirectoryArgumentTest(){
	if(argFolder.exists())
	{
	String[] arguments = new String[]{"argumentFolder1"} ; 
	cdtool = new CDTool(arguments);
	actualOutput = cdtool.execute(WorkingDirectory.workingDirectory, stdin);
	expectedOutput = "Changed current working directory to " + WorkingDirectory.workingDirectory;
	assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
	assertEquals(cdtool.getStatusCode(), 0);
	}
}

/*
 * Test to chcck behaviour of CD Tool with valid folder name as argument with an absolute pathname.
 * Cd Tool has to make that folder the working directory.
 * */
@Test
public void cdValidAbsoluteDirectoryArgumentTest(){
	if(argFolder.exists())
	{
	String[] arguments = new String[]{WorkingDirectory.workingDirectory.getAbsoluteFile().toString() + File.separator + "argumentFolder1"} ; //assuming a folder called blahblah doesn't exist in your working directory
	cdtool = new CDTool(arguments);
	actualOutput = cdtool.execute(WorkingDirectory.workingDirectory, stdin);
	expectedOutput = "Changed current working directory to " + WorkingDirectory.workingDirectory;
	assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
	assertEquals(cdtool.getStatusCode(), 0);
	}
}

/*
 * Test to chcck behaviour of CD Tool with invalid folder name as argument.
 * Cd Tool has to return error message.
 * */
@Test
public void cdInvalidDirectoryArgumentTest(){
	String[] arguments = new String[]{"argumentFolder123z"} ; //assuming a folder called argumentFolder123z doesn't exist in your working directory
	cdtool = new CDTool(arguments);
	actualOutput = cdtool.execute(WorkingDirectory.workingDirectory, stdin);
	expectedOutput = expectedOutput = "argumentFolder123z is not a valid directory. The working directory has not changed.";
	assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
	assertEquals(cdtool.getStatusCode(), -1);
}

/*
 * Test to check the behaviour of getDirectoryPath function. It check whether th folder given in its
 * parameter list is absolute or relative and return the absolute path of the folder parameter.
 * */
@Test
public void cdGetDirectoryPathFunctionTest(){
	String[] arguments = new String[]{};
	cd = new CDTool(arguments);
	actualOutput = (cd.getDirectoryPath("testFolder",WorkingDirectory.workingDirectory.getAbsolutePath())).toString();
	expectedOutput = (WorkingDirectory.workingDirectory.toString() + File.separator +  "testFolder");
	assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
	assertEquals(cd.getStatusCode(), 0);
}

}

