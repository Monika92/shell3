package sg.edu.nus.comp.cs4218.impl.extended2;

import static org.junit.Assert.*;

import java.io.File;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import sg.edu.nus.comp.cs4218.extended2.ISortTool;
import sg.edu.nus.comp.cs4218.impl.extended2.SORTTool;

//Assumption : always sorts in ascending order
public class SORTToolTest {
	//TODO Always test against the interface! 
	private ISortTool sorttool; 
	String actualOutput,expectedOutput,helpOutput;
	File workingDirectory;
	
	@Before
	public void before(){
		workingDirectory = new File(System.getProperty("user.dir"));
		
		helpOutput = "sort [OPTIONS] [FILE]" + "\n" +
				"FILE : Name of the file" + "\n" +
				"OPTIONS : -c : Check whether the given file is already sorted, " +
				"if it is not all sorted, print a diagnostic containing the first " +
				"line that is out of order" + "\n" +
				"-help : Brief information about supported options" ;
	}

    @After
	public void after(){
		sorttool = null;
	}
	
    @Test
    public void multipleOptionsTest1()
    {
    	String[] arguments = new String[]{"-c", "-c" ,"test1.txt"} ;
		sorttool = new SORTTool(arguments);
		actualOutput = sorttool.execute(workingDirectory, null);
		expectedOutput = "Already sorted";
		assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
		assertEquals(sorttool.getStatusCode(), 0);
    }
    
    @Test
    public void sortMultipleFiles()
    {
    	String[] arguments = new String[]{"test1.txt","test2.txt"} ;
		sorttool = new SORTTool(arguments);
		sorttool.execute(workingDirectory, null);
		assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
		assertEquals(sorttool.getStatusCode(), 0);
    }
    
    @Test
    public void MultipleOptionsTest2()
    {
    	String[] arguments = new String[]{"-c", "-help" ,"test1.txt"} ;
		sorttool = new SORTTool(arguments);
		actualOutput = sorttool.execute(workingDirectory, null);
		expectedOutput = helpOutput;
		assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
		assertEquals(sorttool.getStatusCode(), 0);
    }
    
    @Test
    public void MultipleOptionsTest3()
    {
    	String[] arguments = new String[]{"-help", "-c" ,"test1.txt"} ;
		sorttool = new SORTTool(arguments);
		actualOutput = sorttool.execute(workingDirectory, null);
		expectedOutput = helpOutput;
		assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
		assertEquals(sorttool.getStatusCode(), 0);
    }
    
    //Options other than -c and -help are interpreted as file names
    @Test
    public void invalidOptionsTest()
    {
    	String[] arguments = new String[]{"-t"} ;
		sorttool = new SORTTool(arguments);
		actualOutput = sorttool.execute(workingDirectory, null);
		expectedOutput = "File not found";
		assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
		assertEquals(sorttool.getStatusCode(), -1);
    }
    //Stdin is considered as filename
    @Test
    public void overallFunctionalityWithStdinTest()
    {
    	String[] arguments = new String[]{"-"} ;
		sorttool = new SORTTool(arguments);
		actualOutput = sorttool.execute(workingDirectory, "abcd");
		expectedOutput = "File not found";
		assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
		assertEquals(sorttool.getStatusCode(), -1);
    }
    
    
    @Test
    public void overallFunctionalityNonExistingFileTest()
    {
    	String[] arguments = new String[]{"-c", "-c" ,"file.txt"} ;
		sorttool = new SORTTool(arguments);
		actualOutput = sorttool.execute(workingDirectory, null);
		expectedOutput = "File Not Found";
		assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
		assertEquals(sorttool.getStatusCode(), -1);
    }
    
    @Test
    public void helpTest()
    {
    	String[] arguments = new String[]{"-help"} ;
		sorttool = new SORTTool(arguments);
		actualOutput = sorttool.execute(workingDirectory, null);
		expectedOutput = helpOutput;
		assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
		assertEquals(sorttool.getStatusCode(), 0);
    }
    
    
    //help is always prioritized
    @Test
    public void helpWithStdinTest()
    {
    	String[] arguments = new String[]{"-help","-"} ;
		sorttool = new SORTTool(arguments);
		actualOutput = sorttool.execute(workingDirectory, "abcd");
		expectedOutput = helpOutput;
		assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
		assertEquals(sorttool.getStatusCode(), 0);
    }
    
	@Test
	public void sortFileTest() 
	{

		String[] arguments = null ;
		sorttool = new SORTTool(arguments);		
		actualOutput = sorttool.sortFile("apple\ncarrot\nbanana\n");
		expectedOutput = "apple\nbanana\ncarrot\n";
		assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
		assertEquals(sorttool.getStatusCode(), 0);
	}
	
	
	@Test
	public void checkIfSortedUnsortedInputTest()
	{
		String[] arguments = null ;
		sorttool = new SORTTool(arguments);
		
		actualOutput = sorttool.checkIfSorted("apple\ncarrot\nbanana\n");
		expectedOutput = "carrot is out of order";
		assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
		assertEquals(sorttool.getStatusCode(), 0);
	}
	
	@Test
	public void checkIfSortedSortedInputTest()
	{
		String[] arguments = null ;
		sorttool = new SORTTool(arguments);
		
		actualOutput = sorttool.checkIfSorted("apple\nbanana\ncarrot\n");
		expectedOutput = "Already sorted";
		assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
		assertEquals(sorttool.getStatusCode(), 0);
	}

}