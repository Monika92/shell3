package sg.edu.nus.comp.cs4218.impl.fileutils;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import sg.edu.nus.comp.cs4218.extended2.ICutTool;
import sg.edu.nus.comp.cs4218.impl.extended2.CUTTool;

public class CUTToolTest {
	//TODO Always test against the interface! 
	private ICutTool cuttool; 
	String actualOutput,expectedOutput,helpOutput;
	File workingDirectory;
	
	@Before
	public void before(){
		
		workingDirectory = new File(System.getProperty("user.dir"));
		
		helpOutput = "usage: cut [OPTIONS] [FILE]" + "\n"
				+ "FILE : Name of the file, when no file is present" + "\n"
				+ "OPTIONS : -c LIST : Use LIST as the list of characters to cut out. Items within "
				+ "the list may be separated by commas, "
				+ "and ranges of characters can be separated with dashes. "
				+ "For example, list 1-5,10,12,18-30 specifies characters "
				+ "1 through 5, 10,12 and 18 through 30" + "\n"
				+ "-d DELIM: Use DELIM as the field-separator character instead of"
				+ "the TAB character" + "\n" 
				+ "-help : Brief information about supported options";
	}

    @After
	public void after(){
		cuttool = null;
	}
	
    @Test
    public void overallFunctionalityTest()
    {
    	String[] arguments = new String[]{"-c", "1-2", "-c", "3-4", "-"} ;
		cuttool = new CUTTool(arguments);
		actualOutput = cuttool.execute(workingDirectory, "abcde");
		expectedOutput = "abcd";
		assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
		assertEquals(cuttool.getStatusCode(), 0);
    }
    
    @Test
    public void helpTest()
    {
    	String[] arguments = new String[]{"-help"} ;
		cuttool = new CUTTool(arguments);
		actualOutput = cuttool.execute(workingDirectory, null);
		expectedOutput = helpOutput;
		assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
		assertEquals(cuttool.getStatusCode(), 0);
    }
    
    @Test
    public void overallFunctionalityNonExistingFileTest()
    {
    	String[] arguments = new String[]{"-c", "1-2" ,"file.txt"} ;
		cuttool = new CUTTool(arguments);
		actualOutput = cuttool.execute(workingDirectory, null);
		expectedOutput = "File Not Found";
		assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
		assertEquals(cuttool.getStatusCode(), -1);
    }
    
	@Test
	public void cutSpecfiedCharactersTest1() throws IOException {
	
		String[] arguments = null ;
		cuttool = new CUTTool(arguments);
		actualOutput = cuttool.cutSpecfiedCharacters("1-2", "abc");
		expectedOutput = "ab";
		assertTrue(expectedOutput.equals(actualOutput));
		assertEquals(cuttool.getStatusCode(), 0);
	}
	
	@Test
	public void cutSpecfiedCharactersTest2() throws IOException {
		String[] arguments = null ;
		cuttool = new CUTTool(arguments);
		actualOutput = cuttool.cutSpecfiedCharacters("1-4,5,6,10", "the quick brown fox jumps over the lazy dog");
		expectedOutput = "the qu ";
		assertTrue(expectedOutput.equals(actualOutput));
		assertEquals(cuttool.getStatusCode(), 0);
    }
	
	@Test
	public void cutSpecfiedCharactersForEmptyStringTest() throws IOException {
	
		String[] arguments = null ;
		cuttool = new CUTTool(arguments);
		actualOutput = cuttool.cutSpecfiedCharacters("1-2", "");
		expectedOutput = "";
		assertTrue(expectedOutput.equals(actualOutput));
		assertEquals(cuttool.getStatusCode(), 0);
    }

	@Test
	public void cutSpecifiedCharactersUseDelimiterTest1() throws IOException {
		
		String[] arguments = null ;
		cuttool = new CUTTool(arguments);
		
		actualOutput = cuttool.cutSpecifiedCharactersUseDelimiter("5-", ":" ,"one:two:three:four:five:six:seven");
		expectedOutput = "five:six:seven";
		assertTrue(expectedOutput.equals(actualOutput));
		assertEquals(cuttool.getStatusCode(), 0);
	}
	
	@Test
	public void cutSpecifiedCharactersUseDelimiterTest2() throws IOException {
		
		String[] arguments = null ;
		cuttool = new CUTTool(arguments);
		actualOutput = cuttool.cutSpecifiedCharactersUseDelimiter("-3", ":" ,"one:two:three:four:five:six:seven");
		expectedOutput = "one:two:three";
		assertTrue(expectedOutput.equals(actualOutput));
		assertEquals(cuttool.getStatusCode(), 0);
	}	
	//test 3
	@Test
	public void cutSpecifiedCharactersUseDelimiterTest3() throws IOException {
		
		String[] arguments = null ;
		cuttool = new CUTTool(arguments);
		actualOutput = cuttool.cutSpecifiedCharactersUseDelimiter("1-10", ":" ,"one:two:three:four:five:six:seven");
		expectedOutput = "one:two:three:four:five:six:seven";
		assertTrue(expectedOutput.equals(actualOutput));
		assertEquals(cuttool.getStatusCode(), 0);
	}	
	//test 4
	@Test
	public void cutSpecifiedCharactersUseDelimiterTest4() throws IOException {
		
		String[] arguments = null ;
		cuttool = new CUTTool(arguments);
		actualOutput = cuttool.cutSpecifiedCharactersUseDelimiter("3", " " ,"foo:bar:baz:qux:quux");
		expectedOutput = "foo:bar:baz:qux:quux";
		assertTrue(expectedOutput.equals(actualOutput));
		assertEquals(cuttool.getStatusCode(), 0);
	}
	
	@Test
	public void cutSpecifiedCharactersUseDelimiterTest5() throws IOException {
		
		String[] arguments = null ;
		cuttool = new CUTTool(arguments);
		actualOutput = cuttool.cutSpecifiedCharactersUseDelimiter("3", " " ,"the quick brown fox jumps over the lazy dog");
		expectedOutput = "brown";
		assertTrue(expectedOutput.equals(actualOutput));
		assertEquals(cuttool.getStatusCode(), 0);
		
    }
		

}
