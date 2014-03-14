package sg.edu.nus.comp.cs4218.impl.extended2;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

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
	File inputFile1, inputFile2, inputFile3;
	
	@Before
	public void before(){
		workingDirectory = new File(System.getProperty("user.dir"));
		
		helpOutput = "sort [OPTIONS] [FILE]" + "\n" +
				"FILE : Name of the file" + "\n" +
				"OPTIONS : -c : Check whether the given file is already sorted, " +
				"if it is not all sorted, print a diagnostic containing the first " +
				"line that is out of order" + "\n" +
				"-help : Brief information about supported options" ;
		
		String input1 = "apple\nball\ncat\ndog";
		String input2 = "hello\nworld\ncoding\nis\nfun";
		String input3 = "";
		inputFile1 = new File("test1.txt");
		inputFile2 = new File("test2.txt");
		inputFile3 = new File("test3.txt");
		writeToFile(inputFile1, input1);
		writeToFile(inputFile2, input2);
		writeToFile(inputFile3, input3);
	}

    @After
	public void after(){
		sorttool = null;
		
		if(inputFile1.exists())
			inputFile1.delete();
		if(inputFile2.exists())
			inputFile2.delete();
		if(inputFile3.exists())
			inputFile3.delete();
	}
	
    public void writeToFile(File file, String input){
		try{
			if(!file.exists())
				file.createNewFile();
			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			char[] temp = input.toCharArray(); int i = 0;
			while(i<temp.length){
				while(temp[i]!='\n'){
					bw.write(temp[i]);
					i++;
					if(i>=temp.length)
						break;
				}
				bw.newLine(); i++;
			}
			bw.close();
		} catch (IOException e){
		}
	}
    
    public String readFromFile(File inputFile){
		String output = ""; FileReader fr = null;
		try{
			fr = new FileReader(inputFile);
		} catch(FileNotFoundException e){
			e.printStackTrace();
			return "File not found";
		}
		BufferedReader br = new BufferedReader(fr);
		try{
			String line = br.readLine();
			while(line != null){
				if(line.equalsIgnoreCase("\n")||line.equalsIgnoreCase(""))
					output+="\n";
				else
					output += line + "\n";
				line = br.readLine();
			}
		} catch(IOException e){
			e.printStackTrace();
			return "Unable to read file";
		} finally{
			try {
				br.close();
				fr.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return output;
	}
    
    /**
     * Test for sort command with multiple -c options given file input
     * Only one -c option is considered
     * Checks for correct output after execution
     */
    @Test
    public void testExecuteMultipleOptionsC()
    {
    	String[] arguments = new String[]{"-c", "-c" ,"test1.txt"} ;
		sorttool = new SORTTool(arguments);
		actualOutput = sorttool.execute(workingDirectory, null);
		expectedOutput = "Already sorted";
		assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
		assertEquals(sorttool.getStatusCode(), 0);
    }
    
    /**
     * Test for sort command with multiple files
     * All the files are sorted
     * Checks for correct output after execution
     */
    @Test
    public void testExecuteSortMultipleFiles()
    {
    	String[] arguments = new String[]{"test1.txt","test2.txt"} ;
		sorttool = new SORTTool(arguments);
		sorttool.execute(workingDirectory, null);
		String actualOutput1 = readFromFile(inputFile1);
		String actualOutput2 = readFromFile(inputFile2);
		String expectedOutput1 = "apple\nball\ncat\ndog\n";
		String expectedOutput2 = "coding\nfun\nhello\nis\nworld\n";
		assertTrue(expectedOutput1.equalsIgnoreCase(actualOutput1));
		assertTrue(expectedOutput2.equalsIgnoreCase(actualOutput2));
		assertEquals(sorttool.getStatusCode(), 0);
    }
    
    /**
     * Test for sort command with -c and -help option
     * help is given priority
     * Checks for help output after execution
     */
    @Test
    public void testExecuteMultipleOptionsCHelp()
    {
    	String[] arguments = new String[]{"-c", "-help" ,"test1.txt"} ;
		sorttool = new SORTTool(arguments);
		actualOutput = sorttool.execute(workingDirectory, null);
		expectedOutput = helpOutput;
		assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
		assertEquals(sorttool.getStatusCode(), 0);
    }
    
    /**
     * Test for sort command with -c and -help option
     * help is given priority
     * Checks for help output after execution
     */
    @Test
    public void testExecuteMultipleOptionsHelpC()
    {
    	String[] arguments = new String[]{"-help", "-c" ,"test1.txt"} ;
		sorttool = new SORTTool(arguments);
		actualOutput = sorttool.execute(workingDirectory, null);
		expectedOutput = helpOutput;
		assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
		assertEquals(sorttool.getStatusCode(), 0);
    }
    
    /**
     * Test for sort command with invalid option
     * Checks for status code -1 after execution
     */
    @Test
    public void testExecuteInvalidOptions()
    {
    	String[] arguments = new String[]{"-t"} ;
		sorttool = new SORTTool(arguments);
		sorttool.execute(workingDirectory, null);
		assertEquals(sorttool.getStatusCode(), -1);
    }
    
    /**
     * Test for sort command with std input
     * Checks for correct output after execution
     */
    @Test
    public void testExecuteWithStdin()
    {
    	String[] arguments = new String[]{"-"} ;
		sorttool = new SORTTool(arguments);
		sorttool.execute(workingDirectory, "abcd");
		actualOutput = readFromFile(new File("stdin.txt"));
		expectedOutput = "abcd";
		actualOutput = actualOutput.replace("\n", "");
		assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
		assertEquals(sorttool.getStatusCode(), 0);
    }
    
    /**
     * Test for sort command with -c option non-existing file input
     * Checks for status code -1 after execution
     */
    @Test
    public void testExecuteWithNonExistingFile()
    {
    	String[] arguments = new String[]{"-c", "-c" ,"file.txt"} ;
		sorttool = new SORTTool(arguments);
		actualOutput = sorttool.execute(workingDirectory, null);
		expectedOutput = "File Not Found";
		assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
		assertEquals(sorttool.getStatusCode(), -1);
    }
    
    /**
     * Test for sort command with -help option 
     * Checks for help output after execution
     */
    @Test
    public void testExecuteHelp()
    {
    	String[] arguments = new String[]{"-help"} ;
		sorttool = new SORTTool(arguments);
		actualOutput = sorttool.execute(workingDirectory, null);
		expectedOutput = helpOutput;
		assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
		assertEquals(sorttool.getStatusCode(), 0);
    }
    
    /**
     * Test for sort command with -help option and std input
     * help option is prioritized
     * Checks for help output after execution
     */
    @Test
    public void testExecuteHelpWithStdin()
    {
    	String[] arguments = new String[]{"-help","-"} ;
		sorttool = new SORTTool(arguments);
		actualOutput = sorttool.execute(workingDirectory, "abcd");
		expectedOutput = helpOutput;
		assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
		assertEquals(sorttool.getStatusCode(), 0);
    }
    
	@Test
	public void testSortFile() 
	{

		String[] arguments = null ;
		sorttool = new SORTTool(arguments);		
		actualOutput = sorttool.sortFile("apple\ncarrot\nbanana\n");
		expectedOutput = "apple\nbanana\ncarrot\n";
		assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
		assertEquals(sorttool.getStatusCode(), 0);
	}
	
	@Test
	public void testSortFileNoInput() 
	{

		String[] arguments = null ;
		sorttool = new SORTTool(arguments);		
		actualOutput = sorttool.sortFile("");
		expectedOutput = "";
		assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
		assertEquals(sorttool.getStatusCode(), 0);
	}
	
	@Test
	public void testSortFileNullParams() 
	{

		String[] arguments = null ;
		sorttool = new SORTTool(arguments);		
		actualOutput = sorttool.sortFile(null);
		expectedOutput = "";
		assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
		assertEquals(sorttool.getStatusCode(), 0);
	}
	
	@Test
	public void testCheckIfSortedNullParam()
	{
		String[] arguments = null ;
		sorttool = new SORTTool(arguments);
		actualOutput = sorttool.checkIfSorted(null);
		expectedOutput = "Already sorted";
		assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
		assertEquals(sorttool.getStatusCode(), 0);	
	}
	@Test
	public void testCheckIfSortedNoInput()
	{
		String[] arguments = null ;
		sorttool = new SORTTool(arguments);
		actualOutput = sorttool.checkIfSorted("");
		expectedOutput = "Already sorted";
		assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
		assertEquals(sorttool.getStatusCode(), 0);
	}
	
	@Test
	public void testCheckIfSortedGivenUnsortedInput()
	{
		String[] arguments = null ;
		sorttool = new SORTTool(arguments);
		
		actualOutput = sorttool.checkIfSorted("apple\ncarrot\nbanana\n");
		expectedOutput = "carrot is out of order";
		assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
		assertEquals(sorttool.getStatusCode(), 0);
	}
	
	@Test
	public void testCheckIfSortedGivenSortedInput()
	{
		String[] arguments = null ;
		sorttool = new SORTTool(arguments);
		
		actualOutput = sorttool.checkIfSorted("apple\nbanana\ncarrot\n");
		expectedOutput = "Already sorted";
		assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
		assertEquals(sorttool.getStatusCode(), 0);
	}

}