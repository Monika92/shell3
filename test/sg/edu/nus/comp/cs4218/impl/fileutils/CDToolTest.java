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
	
	private ICdTool cdtool;
	private CDTool cd;
	String actualOutput,expectedOutput;
	File workingDirectory;
	//WorkingDirectory wd;
	String stdin;
	File argFolder = new File("argumentFolder1");
	File input_file_1 = new File("Test_Output.txt") ;
	
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
	if(input_file_1.exists())
		input_file_1.delete();
	if(argFolder.exists())
		argFolder.delete();
		
}

@Test
public void cdNoArgumentTest(){
	String[] arguments = new String[]{} ;
	cdtool = new CDTool(arguments);
	actualOutput = cdtool.execute(WorkingDirectory.workingDirectory, stdin);
	expectedOutput = "The working directory is " + (System.getProperty("user.home"));
	System.out.println(actualOutput);
	assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
	assertEquals(cdtool.getStatusCode(), 0);
}

@Test
public void cdFileArgumentTest(){
	String[] arguments = new String[]{"Test_Output.txt"} ;
	cdtool = new CDTool(arguments);
	actualOutput = cdtool.execute(WorkingDirectory.workingDirectory, stdin);
	expectedOutput = "Test_Output.txt is not a valid directory. The working directory has not changed.";
	System.out.println(actualOutput);
	assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
	assertEquals(cdtool.getStatusCode(), 0);
}

@Test
public void cdTildeArgumentTest(){
	String[] arguments = new String[]{"~"} ;
	cdtool = new CDTool(arguments);
	actualOutput = cdtool.execute(WorkingDirectory.workingDirectory, stdin);
	expectedOutput = "Changed current working directory to " + (System.getProperty("user.home"));
	System.out.println(actualOutput);
	assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
	assertEquals(cdtool.getStatusCode(), 0);
}

@Test
public void cdTildeSpaceArgumentTest(){
	String[] arguments = new String[]{"~   "} ;
	cdtool = new CDTool(arguments);
	actualOutput = cdtool.execute(WorkingDirectory.workingDirectory, stdin);
	expectedOutput = "Changed current working directory to " + (System.getProperty("user.home"));
	System.out.println(actualOutput);
	assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
	assertEquals(cdtool.getStatusCode(), 0);
}

@Test
public void cdDotArgumentTest(){
	String[] arguments = new String[]{"."} ;
	cdtool = new CDTool(arguments);
	actualOutput = cdtool.execute(WorkingDirectory.workingDirectory, stdin);
	expectedOutput = "Changed current working directory to " + WorkingDirectory.workingDirectory;
	System.out.println(actualOutput);
	assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
	assertEquals(cdtool.getStatusCode(), 0);
}

@Test
public void cdDotSpaceArgumentTest(){
	String[] arguments = new String[]{"  ."} ;
	cdtool = new CDTool(arguments);
	actualOutput = cdtool.execute(WorkingDirectory.workingDirectory, stdin);
	expectedOutput = "Changed current working directory to " + WorkingDirectory.workingDirectory;
	System.out.println(actualOutput);
	assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
	assertEquals(cdtool.getStatusCode(), 0);
}

@Test
public void cdDotDotArgumentTest(){
	String[] arguments = new String[]{".."} ;
	cdtool = new CDTool(arguments);
	actualOutput = cdtool.execute(WorkingDirectory.workingDirectory, stdin);
	expectedOutput = "Changed current working directory to " + WorkingDirectory.workingDirectory;
	System.out.println(actualOutput);
	assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
	assertEquals(cdtool.getStatusCode(), 0);
}

@Test
public void cdDotDotSpaceArgumentTest(){
	String[] arguments = new String[]{" .. "} ;
	cdtool = new CDTool(arguments);
	actualOutput = cdtool.execute(WorkingDirectory.workingDirectory, stdin);
	expectedOutput = "Changed current working directory to " + WorkingDirectory.workingDirectory;
	System.out.println(actualOutput);
	assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
	assertEquals(cdtool.getStatusCode(), 0);
}

@Test
public void cdFourDotsArgumentTest(){
	String[] arguments = new String[]{"...."} ;
	cdtool = new CDTool(arguments);
	actualOutput = cdtool.execute(WorkingDirectory.workingDirectory, stdin);
	expectedOutput = ".... is not a valid directory. The working directory has not changed.";
	System.out.println(actualOutput);
	assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
	assertEquals(cdtool.getStatusCode(), 0);
}

@Test
public void cdWrongArgumentTest(){
	String[] arguments = new String[]{"blahblah"} ; //assuming a folder called blahblah doesn't exist in your working directory
	cdtool = new CDTool(arguments);
	actualOutput = cdtool.execute(WorkingDirectory.workingDirectory, stdin);
	expectedOutput = "blahblah is not a valid directory. The working directory has not changed.";
	System.out.println(actualOutput);
	assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
	assertEquals(cdtool.getStatusCode(), 0);
}

@Test
public void cdValidDirectoryArgumentTest(){
	if(argFolder.exists())
	{
	String[] arguments = new String[]{"argumentFolder1"} ; 
	cdtool = new CDTool(arguments);
	actualOutput = cdtool.execute(WorkingDirectory.workingDirectory, stdin);
	expectedOutput = "Changed current working directory to " + WorkingDirectory.workingDirectory;
	System.out.println(actualOutput);
	assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
	assertEquals(cdtool.getStatusCode(), 0);
	}
}

@Test
public void cdValidAbsoluteDirectoryArgumentTest(){
	if(argFolder.exists())
	{
	String[] arguments = new String[]{WorkingDirectory.workingDirectory.getAbsoluteFile().toString() + File.separator + "argumentFolder1"} ; //assuming a folder called blahblah doesn't exist in your working directory
	cdtool = new CDTool(arguments);
	actualOutput = cdtool.execute(WorkingDirectory.workingDirectory, stdin);
	expectedOutput = "Changed current working directory to " + WorkingDirectory.workingDirectory;
	System.out.println(actualOutput);
	assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
	assertEquals(cdtool.getStatusCode(), 0);
	}
}

@Test
public void cdInvalidDirectoryArgumentTest(){
	String[] arguments = new String[]{"argumentFolder123z"} ; //assuming a folder called argumentFolder123z doesn't exist in your working directory
	cdtool = new CDTool(arguments);
	actualOutput = cdtool.execute(WorkingDirectory.workingDirectory, stdin);
	expectedOutput = expectedOutput = "argumentFolder123z is not a valid directory. The working directory has not changed.";
	System.out.println(actualOutput);
	assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
	assertEquals(cdtool.getStatusCode(), 0);
}

@Test
public void cdGetDirectoryPathFunctionTest(){
	String[] arguments = new String[]{};
	cd = new CDTool(arguments);
	actualOutput = (cd.getDirectoryPath("testFolder",WorkingDirectory.workingDirectory.getAbsolutePath())).toString();
	expectedOutput = (WorkingDirectory.workingDirectory.toString() + File.separator +  "testFolder");
	System.out.println(actualOutput);
	assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
	assertEquals(cd.getStatusCode(), 0);
}

}

