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
    public void cOptionTest()
    {
    	String[] arguments = new String[]{"-c", "1-2","-"} ;
		cuttool = new CUTTool(arguments);
		actualOutput = cuttool.execute(workingDirectory, "abcde");
		expectedOutput = "ab";
		assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
		assertEquals(cuttool.getStatusCode(), 0);
    }
    
    @Test
    public void cOptionInputInvalidRangeTest()
    {
    	String[] arguments = new String[]{"-c", "2-1","-"} ;
		cuttool = new CUTTool(arguments);
		actualOutput = cuttool.execute(workingDirectory, "abcde");
		expectedOutput = "";
		assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
		assertEquals(cuttool.getStatusCode(), 0);
    }
    
    @Test
    public void dOptionFollowedByFOptionTest()
    {
    	String[] arguments = new String[]{"-d", ":","-f","1-2","-"} ;
		cuttool = new CUTTool(arguments);
		actualOutput = cuttool.execute(workingDirectory, "one:two:three:four:five:six:seven");
		expectedOutput = "one:two";
		assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
		assertEquals(cuttool.getStatusCode(), 0);
    }
    
    @Test
    public void fOptionFollowedByDOptionTest()
    {
    	String[] arguments = new String[]{"-f", "1-2","-d",":","-"} ;
		cuttool = new CUTTool(arguments);
		actualOutput = cuttool.execute(workingDirectory, "one:two:three:four:five:six:seven");
		expectedOutput = "one:two";
		assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
		assertEquals(cuttool.getStatusCode(), 0);
    }
    
    @Test
    public void dOptionWithFileTest()
    {
    	String[] arguments = new String[]{"-d", ":","-f","0","test1.txt"} ;
		cuttool = new CUTTool(arguments);
		actualOutput = cuttool.execute(workingDirectory, null);
		expectedOutput = "apple\nball\ncat\ndog\n";
		assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
		assertEquals(cuttool.getStatusCode(), 0);
    }
    
    @Test
    public void cOptionWithFileTest()
    {
    	String[] arguments = new String[]{"-c", "1-2","test1.txt"} ;
		cuttool = new CUTTool(arguments);
		actualOutput = cuttool.execute(workingDirectory, null);
		expectedOutput = "ap\nba\nca\ndo\n";
		assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
		assertEquals(cuttool.getStatusCode(), 0);
    }
    
    @Test
    public void cOptionWithEmptyFileTest()
    {
    	String[] arguments = new String[]{"-c", "1-2","test3.txt"} ;
		cuttool = new CUTTool(arguments);
		actualOutput = cuttool.execute(workingDirectory, null);
		expectedOutput = "";
		assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
		assertEquals(cuttool.getStatusCode(), 0);
    }
    
    @Test
    public void cOptionWithMultipleFilesTest()
    {
    	String[] arguments = new String[]{"-c", "1-2","test1.txt","test2.txt"} ;
		cuttool = new CUTTool(arguments);
		actualOutput = cuttool.execute(workingDirectory, null);
		expectedOutput = "ap\nba\nca\ndo\n\n"+ "he\nwo\nco\nis\nfu\n";
		assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
		assertEquals(cuttool.getStatusCode(), 0);
    }
    
    @Test
    public void cOptionWithFileMissingTest()
    {
    	String[] arguments = new String[]{"-c", "1-2","file.txt"} ;
		cuttool = new CUTTool(arguments);
		actualOutput = cuttool.execute(workingDirectory, null);
		expectedOutput = "File not found";
		assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
		assertEquals(cuttool.getStatusCode(), -1);
    }
    
    @Test
    public void cOptionRepeatedTest()
    {
    	String[] arguments = new String[]{"-c", "1-2", "-c", "3-4", "-"} ;
		cuttool = new CUTTool(arguments);
		actualOutput = cuttool.execute(workingDirectory, "abcde");
		expectedOutput = "abcd";
		assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
		assertEquals(cuttool.getStatusCode(), 0);
    }
    
    // in case of multiple d options, the latest delimiter is considered
    @Test
    public void dOptionRepeatedTest()
    {
    	String[] arguments = new String[]{"-d", " ", "-d", ":", "-f","1-3","-"} ;
		cuttool = new CUTTool(arguments);
		actualOutput = cuttool.execute(workingDirectory, "one:two:three:four:five:six:seven");
		expectedOutput = "one:two:three";
		assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
		assertEquals(cuttool.getStatusCode(), 0);
    }
    
    //when c and d options are present, c option is taken as priority
    @Test
    public void cDOptionTest()
    {
    	String[] arguments = new String[]{"-c", "1-2", "-d", ":", "-"} ;
		cuttool = new CUTTool(arguments);
		actualOutput = cuttool.execute(workingDirectory, "one:two:three:four:five:six:seven");
		expectedOutput = "on";
		assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
		assertEquals(cuttool.getStatusCode(), 0);
    }
    
   //when c and f options are present, c option is taken as priority
    @Test
    public void cFOptionTest()
    {
    	String[] arguments = new String[]{"-c", "1-2", "-f", "2", "-"} ;
		cuttool = new CUTTool(arguments);
		actualOutput = cuttool.execute(workingDirectory, "one:two:three:four:five:six:seven");
		expectedOutput = "on";
		assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
		assertEquals(cuttool.getStatusCode(), 0);
    }
    
   //when c , d and f options are present, all options are executed
    @Test
    public void cDFOptionTest()
    {
    	String[] arguments = new String[]{"-c", "1-2", "-f", "2-4", "-d", ":","-"} ;
		cuttool = new CUTTool(arguments);
		actualOutput = cuttool.execute(workingDirectory, "one:two:three:four:five:six:seven");
		expectedOutput = "ontwo:three:four";
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
		System.out.println(actualOutput);
		assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
		assertEquals(cuttool.getStatusCode(), 0);
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
	//LIST with negative values as -2-3 as  1 till 2
	public void cutSpecfiedCharactersTest3() throws IOException {
	
		String[] arguments = null ;
		cuttool = new CUTTool(arguments);
		actualOutput = cuttool.cutSpecfiedCharacters("-2-3", "abc");
		expectedOutput = "ab";
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

	//5- as LIST is interpreted as 5 till end of string
	@Test
	public void cutSpecifiedCharactersUseDelimiterTest1() throws IOException {
		
		String[] arguments = null ;
		cuttool = new CUTTool(arguments);
		
		actualOutput = cuttool.cutSpecifiedCharactersUseDelimiter("5-", ":" ,"one:two:three:four:five:six:seven");
		expectedOutput = "five:six:seven";
		assertTrue(expectedOutput.equals(actualOutput));
		assertEquals(cuttool.getStatusCode(), 0);
	}
	
	//-3 as LIST is interpreted as 1-3
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
