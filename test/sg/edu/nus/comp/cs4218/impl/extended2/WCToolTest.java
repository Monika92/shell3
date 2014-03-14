package sg.edu.nus.comp.cs4218.impl.extended2;

import static org.junit.Assert.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import sg.edu.nus.comp.cs4218.extended2.IWcTool;
import sg.edu.nus.comp.cs4218.impl.WorkingDirectory;
import sg.edu.nus.comp.cs4218.impl.extended2.CUTTool;
import sg.edu.nus.comp.cs4218.impl.extended2.WCTool;


public class WCToolTest {


	/*
	 * Few assumptions :
	 * 1. wc file1.txt file2.txt - abcde : In case where there are file name arguments and a stdin input, priority 
	 * is given to the result of the filename input arguments. 
	 * 2. No options in the arguments implies the results of all options are displayed
	 * 3. For a command wc -m -w -l input1.txt, the output format is as follows : 
	 *    "<input1.txt's absolute pathname> :  -m  8 -w  4 -l  3\n"
	 * 4. If the arguments contain multiple options with -help as one of them, all other options are disregarded
	 * 	  and helpOutput string is returned.
	 */
	
	private IWcTool wctool; 
	private WCTool wc; 
	String actualOutput,expectedOutput,helpOutput;
	File workingDirectory;
	File inputFile1,inputFile2;

	
	@Before
	public void before(){
		
		WorkingDirectory.changeWorkingDirectory(new File(System.getProperty("user.dir")));
		workingDirectory = WorkingDirectory.workingDirectory;
		inputFile1 = new File("input1.txt");
		inputFile2 = new File("input2.txt");
		writeToFile(inputFile1, "abc\nb d\n---\n");
		writeToFile(inputFile2, "  ");
		helpOutput = "wc [OPTIONS] [FILE]" + "\n" +
				"FILE : Name of the file" + "\n" +
				"OPTIONS : -m : Print only the number of characters \n" +
				"          -w : Print only the number of words \n" +
				"          -l : Print only the number of lines \n" +
				"          -help : Brief information about supported options." ;
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

	@After
	public void after(){
		wctool = null;
		wc = null;
		if(inputFile1.exists())
			inputFile1.delete();
		if(inputFile2.exists())
			inputFile2.delete();
		
	}
	
	/*
	 * Test case to check the functionality of getHelp function
	 * */
	@Test
    public void getHelpTest()
    {
    	String[] arguments = new String[]{} ;
		wctool = new WCTool(arguments);
		actualOutput = wctool.getHelp();
		expectedOutput = helpOutput ;
		assertTrue(expectedOutput.equals(actualOutput));
		assertEquals(wctool.getStatusCode(), 0);
    }

	/*
	 * Test case to check the behaviour of WC with only -help option 
	 * */
	@Test
    public void helpTest()
    {
    	String[] arguments = new String[]{"-help"} ;
		wctool = new WCTool(arguments);
		actualOutput = wctool.execute(workingDirectory, null);
		expectedOutput = helpOutput ;
		String incorrectOutput = "Invalid input. No options given.";
		assertFalse(incorrectOutput.equalsIgnoreCase(actualOutput));
		assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
		assertEquals(wctool.getStatusCode(), 0);
    }
	
	/*
	 * Test case to check the behaviour of WC with -help option coupled with few oher options.
	 * The -help option must be given priority and the othr options must not be executed. 
	 * */
	@Test
    public void helpPriorityTest()
    {
    	String[] arguments = new String[]{"-m" , "-help" , "input.txt"} ;
		wctool = new WCTool(arguments);
		actualOutput = wctool.execute(workingDirectory, null);
		expectedOutput = helpOutput ;
		String incorrectOutput = WorkingDirectory.workingDirectory + File.separator + "input1.txt :  -m  8\n";
		assertFalse(incorrectOutput.equalsIgnoreCase(actualOutput));
		assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
		assertEquals(wctool.getStatusCode(), 0);
    }
    
	/*
	 * Test case to check the behaviour of WC with invalid file argument.
	 * */
	@Test
    public void invalidFilenameInputTest()
    {
    	String[] arguments = new String[]{"-m", "-w", "-l", "idontexisthaha.txt"} ;
		wctool = new WCTool(arguments);
		actualOutput = wctool.execute(workingDirectory, null);
		expectedOutput =  "idontexisthaha.txt : error - Invalid Input. \n";
		String incorrectOutput = "idontexisthaha.txt :  -m  0 , -w 0 , -l 0\n";
		assertFalse(incorrectOutput.equalsIgnoreCase(actualOutput));
		assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
		assertEquals(wctool.getStatusCode(), -1);
    }
	
	/*
	 * Test case to check the behaviour of WC with no file argument.
	 * */
	@Test
    public void noFilenameOrStdinInputTest()
    {
    	String[] arguments = new String[]{"-m", "-w", "-l"} ;
		wctool = new WCTool(arguments);
		actualOutput = wctool.execute(workingDirectory, null);
		assertEquals(wctool.getStatusCode(), -1);
    }
	
	/*
	 * Test case to check the behaviour of WC with no file argument and no options.
	 * */
	@Test
    public void noFilenameOrStdinOrOptionsInputTest()
    {
    	String[] arguments = new String[]{} ;
		wctool = new WCTool(arguments);
		actualOutput = wctool.execute(workingDirectory, null);
		assertEquals(wctool.getStatusCode(), -1);
    }
	
	/*
	 * Test case to check the behaviour of WC with valid file argument with.
	 * */
	@Test
    public void allOptionsInputTest()
    {
    	String[] arguments = new String[]{"-m", "-w", "-l", "input1.txt"} ;
		wctool = new WCTool(arguments);
		actualOutput = wctool.execute(WorkingDirectory.workingDirectory, null);
		expectedOutput =  WorkingDirectory.workingDirectory + File.separator + "input1.txt :  -m  8 -w  4 -l  3\n";
		assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
		assertEquals(wctool.getStatusCode(), 0);
    }
	
	/*
	 * Test case to check the behaviour of WC with an extra minus. The WC tool must flag error.
	 * */
	@Test
    public void ignoreTheAccidentalMinusInputTest()
    {
    	String[] arguments = new String[]{"-m", "-" , "-w", "-l", "input1.txt"} ;
		wctool = new WCTool(arguments);
		actualOutput = wctool.execute(WorkingDirectory.workingDirectory, null);
		String incorrectOutput =  WorkingDirectory.workingDirectory + File.separator + "input1.txt :  -m  8 -w  4 -l  3\n";
		assertFalse(incorrectOutput.equalsIgnoreCase(actualOutput));
		assertEquals(wctool.getStatusCode(), -1);
    }
	
	/*
	 * Test case to check the behaviour of WC with valid file argument and 2 options.
	 * */
	@Test
    public void fewOptionsInputTest()
    {
    	String[] arguments = new String[]{"-w", "-m", "input1.txt"} ;
		wctool = new WCTool(arguments);
		actualOutput = wctool.execute(WorkingDirectory.workingDirectory, null);
		expectedOutput =  WorkingDirectory.workingDirectory + File.separator + "input1.txt :  -m  8 -w  4\n";
		assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
		assertEquals(wctool.getStatusCode(), 0);
    }
	
	/*
	 * Test case to check the behaviour of WC with valid file name and options in Capital case.
	 * */
	@Test
    public void capsOptionsInputTest()
    {
    	String[] arguments = new String[]{"-W", "-M", "-L", "input1.txt"} ;
		wctool = new WCTool(arguments);
		
		actualOutput = wctool.execute(WorkingDirectory.workingDirectory, null);
		expectedOutput =  WorkingDirectory.workingDirectory + File.separator + "input1.txt :  -m  8 -w  4 -l  3\n";
		assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
		assertEquals(wctool.getStatusCode(), 0);
		
		String incorrectOutput =  "input1.txt : error - Invalid Input. \n";
		assertFalse(incorrectOutput.equalsIgnoreCase(actualOutput));
		assertNotEquals(wctool.getStatusCode(), -1);
    }
	
	/*
	 * Test case to check the behaviour of WC with valid file name which is in absolute path.
	 * */
	@Test
    public void absoluteFilenameInputTest()
    {
    	String[] arguments = new String[]{"-w", "-m", WorkingDirectory.workingDirectory + File.separator + "input1.txt"} ;
		wctool = new WCTool(arguments);
		actualOutput = wctool.execute(WorkingDirectory.workingDirectory, null);
		expectedOutput =  WorkingDirectory.workingDirectory + File.separator + "input1.txt :  -m  8 -w  4\n";
		assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
		assertEquals(wctool.getStatusCode(), 0);
    }
	
	/*
	 * Test case to check the behaviour of WC with valid file name but empty content.
	 * */
	@Test
    public void emptyFileInputTest()
    {
    	String[] arguments = new String[]{"-w", "-m", "-l", WorkingDirectory.workingDirectory + File.separator + "input2.txt"} ;
		wctool = new WCTool(arguments);
		actualOutput = wctool.execute(WorkingDirectory.workingDirectory, null);
		expectedOutput =  WorkingDirectory.workingDirectory + File.separator + "input2.txt :  -m  0 -w  0 -l  0\n";
		assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
		assertEquals(wctool.getStatusCode(), 0);
    }
	
	/*
	 * Test case to check the behaviour of WC with valid file name but no options. 
	 * This must give result for all 3 options.
	 * */
	@Test
    public void noOptionsValidFilenameInputTest()
    {
    	String[] arguments = new String[]{WorkingDirectory.workingDirectory + File.separator + "input2.txt"} ;
		wctool = new WCTool(arguments);
		actualOutput = wctool.execute(WorkingDirectory.workingDirectory, null);
		expectedOutput =  WorkingDirectory.workingDirectory + File.separator + "input2.txt : -m  0 , -w  0 , -l 0\n";
		assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
		assertEquals(wctool.getStatusCode(), 0);
    }
    
	/*
	 * Test case to check the behaviour of WC with valid but multiple file names.
	 * */
	@Test
    public void multipleFilenamesInputTest()
    {
    	String[] arguments = new String[]{"-w", "-m", "input1.txt", "input2.txt"} ;
		wctool = new WCTool(arguments);
		actualOutput = wctool.execute(WorkingDirectory.workingDirectory, null);
		expectedOutput =  WorkingDirectory.workingDirectory + File.separator + "input1.txt :  -m  8 -w  4\n"
				         + WorkingDirectory.workingDirectory + File.separator + "input2.txt :  -m  0 -w  0\n";
		assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
		assertEquals(wctool.getStatusCode(), 0);
    }
	
	/*
	 * Test case to check the behaviour of WC with one valid file name and one invalid file name.
	 * */
	@Test
    public void oneValidOneInvalidFilenamesInputTest()
    {
    	String[] arguments = new String[]{"-w", "-m", "input1.txt", "input3.txt"} ;
		wctool = new WCTool(arguments);
		actualOutput = wctool.execute(WorkingDirectory.workingDirectory, null);
		expectedOutput =  WorkingDirectory.workingDirectory + File.separator + "input1.txt :  -m  8 -w  4\n"
				         + "input3.txt : error - Invalid Input. \n";
		assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
		assertEquals(wctool.getStatusCode(), -1);
		
		String incorrectOutput =  "input1.txt : error - Invalid Input. \n"
		         + "input3.txt : error - Invalid Input. \n";
		assertFalse(incorrectOutput.equalsIgnoreCase(actualOutput));
		assertNotEquals(wctool.getStatusCode(), 0);
    }
	
	/*
	 * Test case to check the behaviour of WC with one valid file name and one invalid file name and stdin input.
	 * */
	@Test
    public void stdinAndFilenameInputTest()
    {
    	String[] arguments = new String[]{"-w", "-m", "input1.txt", "input3.txt"} ;
		wctool = new WCTool(arguments);
		actualOutput = wctool.execute(WorkingDirectory.workingDirectory, "input2-.txt");
		expectedOutput =  WorkingDirectory.workingDirectory + File.separator + "input1.txt :  -m  8 -w  4\n"
				         + "input3.txt : error - Invalid Input. \n"
				         +  "Stdin :  -m  11 -w  1\n" ;
		assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
		assertEquals(wctool.getStatusCode(), -1);
    }
	
	/*
	 * Test case to check the behaviour of WC with two valid file name arguments.
	 * */
	@Test
    public void twoValidFilenameInputsTest()
    {
    	String[] arguments = new String[]{"-w", "-m", "input1.txt", "input2.txt"} ;
		wctool = new WCTool(arguments);
		actualOutput = wctool.execute(WorkingDirectory.workingDirectory,null);
		expectedOutput =  WorkingDirectory.workingDirectory + File.separator + "input1.txt :  -m  8 -w  4\n"
				         +  WorkingDirectory.workingDirectory + File.separator + "input2.txt :  -m  0 -w  0\n"
				         ;
		assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
		assertEquals(wctool.getStatusCode(), 0);
    }
	
	/*
	 * Test case to check the behaviour of WC with only stdin input.
	 * */
	@Test
    public void onlyStdinInputTest()
    {
    	String[] arguments = new String[]{"-w", "-m", "-"} ;
		wctool = new WCTool(arguments);
		actualOutput = wctool.execute(WorkingDirectory.workingDirectory, "input2\ntxt");
		expectedOutput = "Stdin :  -m  9 -w  2\n";
		assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
		assertEquals(wctool.getStatusCode(), 0);
    }
	
	/*
	 * Test case to check the behaviour of WC with only stdin input and no options.
	 * */
	@Test
    public void noOptionsStdinInputTest()
    {
    	String[] arguments = new String[]{"-"} ;
		wctool = new WCTool(arguments);
		actualOutput = wctool.execute(WorkingDirectory.workingDirectory, "input2 txt");
		expectedOutput =  "Stdin : -m  9 , -w  2 , -l 1\n";
		assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
		assertEquals(wctool.getStatusCode(), 0);
    }
	
	/*
	 * Test case to check the getCharacterCount() interface function
	 * */
	@Test
    public void getCharacterCountTest()
    {
    	String[] arguments = new String[]{" "} ;
		wctool = new WCTool(arguments);
		actualOutput = wctool.getCharacterCount("lo3");
		expectedOutput = "3";
		assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
		assertEquals(wctool.getStatusCode(), 0);
    }
	
	/*
	 * Test case to check the getCharacterCount() interface function with empty input.
	 * */
	@Test
    public void getCharacterCountEmptyInputTest()
    {
    	String[] arguments = new String[]{" "} ;
		wctool = new WCTool(arguments);
		actualOutput = wctool.getCharacterCount(" ");
		expectedOutput = "0";
		assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
		assertEquals(wctool.getStatusCode(), 0);
		
		assertFalse("1".equalsIgnoreCase(actualOutput));
		assertNotEquals(wctool.getStatusCode(), -1);
    }
	
	/*
	 * Test case to check the getCharacterCount() interface function with newline input.
	 * */
	@Test
    public void getCharacterCountNewlineInputTest()
    {
    	String[] arguments = new String[]{" "} ;
		wctool = new WCTool(arguments);
		actualOutput = wctool.getCharacterCount("\n");
		expectedOutput = "0";
		assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
		assertEquals(wctool.getStatusCode(), 0);
		
		assertFalse("1".equalsIgnoreCase(actualOutput));
		assertNotEquals(wctool.getStatusCode(), -1);
    
    }
	
	/*
	 * Test case to check the getCharacterCount() interface function with special character input.
	 * */
	@Test
    public void getCharacterCountSplCharInputTest()
    {
    	String[] arguments = new String[]{" "} ;
		wctool = new WCTool(arguments);
		actualOutput = wctool.getCharacterCount("..\n!@");
		expectedOutput = "4";
		assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
		assertEquals(wctool.getStatusCode(), 0);
    }
	
	/*
	 * Test case to check the getWordCount() interface function.
	 * */
	@Test
    public void getWordCountTest()
    {
    	String[] arguments = new String[]{" "} ;
		wctool = new WCTool(arguments);
		actualOutput = wctool.getWordCount("lol lol lollll");
		expectedOutput = "3";
		assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
		assertEquals(wctool.getStatusCode(), 0);
    }
	
	/*
	 * Test case to check the getWordCount() interface function with multiline inputs.
	 * */
	@Test
    public void getWordCountCrazyInputWithNewLinesTest()
    {
    	String[] arguments = new String[]{" "} ;
		wctool = new WCTool(arguments);
		actualOutput = wctool.getWordCount("lol\n \n    lol\n2 . lollll");
		expectedOutput = "5";
		assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
		assertEquals(wctool.getStatusCode(), 0);
		
		assertFalse("6".equalsIgnoreCase(actualOutput)); // Should not count the space between the first and second newline 
		assertNotEquals(wctool.getStatusCode(), -1);
    }
	
	/*
	 * Test case to check the getNewLineCount() interface function with multiline inputs.
	 * */
	@Test
    public void getNewLineCountTest()
    {
    	String[] arguments = new String[]{" "} ;
		wctool = new WCTool(arguments);
		actualOutput = wctool.getNewLineCount("lol\n lol\n lollll");
		expectedOutput = "3";
		assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
		assertEquals(wctool.getStatusCode(), 0);
    }
	
	/*
	 * Test case to check the getWordCount() interface function with empty multiline inputs.
	 * */
	@Test
    public void getNewLineCountEmptylinesTest()
    {
    	String[] arguments = new String[]{" "} ;
		wctool = new WCTool(arguments);
		actualOutput = wctool.getNewLineCount("\n \n");
		expectedOutput = "0";
		assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
		assertFalse(expectedOutput.equals("3"));
		assertEquals(wctool.getStatusCode(), 0);
    }
	
}
