package sg.edu.nus.comp.cs4218.impl.fileutils;

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

import sg.edu.nus.comp.cs4218.fileutils.ICatTool;
import sg.edu.nus.comp.cs4218.fileutils.IDeleteTool;

public class CATToolTest {

	private ICatTool cattool; 
	String actualOutput,expectedOutput;
	File workingDirectory;
	String stdin;
	File inputFile1, inputFile2, inputFile3, absFile1, absFile2, relativeFile, emptyFile;
	
	@Before
	public void before(){
		workingDirectory = new File(System.getProperty("user.dir"));
		stdin = null;
		String input = "This is \na test \nrun.";
		
		inputFile1 = new File("Test_Output.txt");
		inputFile2 = new File("Test_Output_2.txt");
		inputFile3 = new File("Test_Output_3.txt");
		writeToFile(inputFile1, input);
		writeToFile(inputFile2, input);
		writeToFile(inputFile3, input);
		
		absFile1 = new File(workingDirectory + "\\" + "Test_Output_4.txt");
		absFile2 = new File(System.getProperty("home.dir")+"Test_Output_5.txt");
		relativeFile = new File("./../Test_Output_6.txt");
		writeToFile(absFile1, input);
		writeToFile(absFile2, input);
		writeToFile(relativeFile, input);
		
		emptyFile = new File("Test_Output_7.txt");
		writeToFile(emptyFile, "");
	}

	@After
	public void after(){
		cattool = null;
		if(inputFile1.exists())
			inputFile1.delete();
		if(inputFile2.exists())
			inputFile2.delete();
		if(inputFile3.exists())
			inputFile3.delete();
		if(absFile1.exists())
			absFile1.delete();
		if(absFile2.exists())
			absFile2.delete();
		if(relativeFile.exists())
			relativeFile.delete();
		if(emptyFile.exists())
			emptyFile.delete();
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
	
	/*
	 * Basic test case for cat with single file
	 * Command: cat filename
	 */
    @Test
    public void catSingleFileTest(){
    	String[] arguments = new String[]{"Test_Output.txt"} ;
		cattool = new CATTool(arguments);
		actualOutput = cattool.execute(workingDirectory, stdin);
		expectedOutput = "This is \na test \nrun.";
		assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
		assertEquals(cattool.getStatusCode(), 0);
    }
    
    /*
     * Test case for cat with multiple files
     * Command: cat filename1 filename2
     */
    @Test
    public void catMultipleFileTest(){
    	String[] arguments = new String[]{"Test_Output.txt", "Test_Output_2.txt", "Test_Output_3.txt"} ;
		cattool = new CATTool(arguments);
		actualOutput = cattool.execute(workingDirectory, stdin);
		expectedOutput = "This is \na test \nrun.\nThis is \na test \nrun.\nThis is \na test \nrun.";
		assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
		assertEquals(cattool.getStatusCode(), 0);
    }
    
    /*
     * Negative test case for cat
     * Command: cat filename
     * Error: Non-existent file
     */
    @Test
    public void catInvalidFileTest(){
    	String[] arguments = new String[]{"Invalid_File.txt"} ;
		cattool = new CATTool(arguments);
		actualOutput = cattool.execute(workingDirectory, stdin);
		expectedOutput = "No such file";
		assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
		assertEquals(cattool.getStatusCode(), -1);
    }
    
    /*
     * Negative test case for cat
     * Command: cat filename1 filename2
     * Error: filename1 is invalid
     */
    @Test
    public void catInvalidValidFileTest(){
    	String[] arguments = new String[]{"Test_Output.txt", "Invalid_File.txt", "InvalidFile.txt"} ;
		cattool = new CATTool(arguments);
		actualOutput = cattool.execute(workingDirectory, stdin);
		expectedOutput = "No such file";
		assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
		assertEquals(cattool.getStatusCode(), -1);
    }
    
    /*
     * Positive + Negative test case for cat
     * Command: cat filename1 filename2
     * Error: filename2 is invalid
     */
    @Test
    public void catValidInvalidFileTest(){
    	String[] arguments = new String[]{"Invalid_File.txt", "Test_Output.txt"} ;
		cattool = new CATTool(arguments);
		actualOutput = cattool.execute(workingDirectory, stdin);
		expectedOutput = "No such file";
		assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
		assertEquals(cattool.getStatusCode(), -1);
    }

    /*
     * Test case 1 for cat with absolute path of filename
     * Command: cat filename_abs_path
     */
    @Test
    public void catAbsoluteFile1Test(){
    	String[] arguments = new String[]{workingDirectory + "\\" + "Test_Output_4.txt"} ;
		cattool = new CATTool(arguments);
		actualOutput = cattool.execute(workingDirectory, stdin);
		expectedOutput = "This is \na test \nrun.";
		assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
		assertEquals(cattool.getStatusCode(), 0);
    }
    
    /*
     * Test case 1 for cat with absolute path of filename
     * Command: cat filename_abs_path
     */
    @Test
    public void catAbsoluteFile2Test(){
    	String[] arguments = new String[]{System.getProperty("home.dir")+ "Test_Output_5.txt"} ;
		cattool = new CATTool(arguments);
		actualOutput = cattool.execute(workingDirectory, stdin);
		expectedOutput = "This is \na test \nrun.";
		assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
		assertEquals(cattool.getStatusCode(), 0);
    }
    
    /*
     * Test case for cat with relative path of filename
     * Command: cat filename_relative_path
     */
    @Test
    public void catRelativeFileTest(){
    	String[] arguments = new String[]{"./../Test_Output_6.txt",} ;
		cattool = new CATTool(arguments);
		actualOutput = cattool.execute(workingDirectory, stdin);
		expectedOutput = "This is \na test \nrun.";
		assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
		assertEquals(cattool.getStatusCode(), 0);
    }

    /*
     * Test case for cat with empty file
     * Command: cat empty_filename
     */
    @Test
    public void catEmptyFileTest(){
    	String[] arguments = new String[]{"Test_Output_7.txt",} ;
		cattool = new CATTool(arguments);
		actualOutput = cattool.execute(workingDirectory, stdin);
		expectedOutput = "";
		assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
		assertEquals(cattool.getStatusCode(), 0);
    }
    
    /*
     * Test case 1 for cat with stdin
     * Command: cat -
     * Stdin: "This is a test run."
     */
    @Test
    public void catStdin1Test(){
    	String[] arguments = new String[]{"-"} ;
		cattool = new CATTool(arguments);
		stdin = "This is a test run.";
		actualOutput = cattool.execute(workingDirectory, stdin);
		expectedOutput = "This is a test run.";
		assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
		assertEquals(cattool.getStatusCode(), 0);
    }
    
    /*
     * Test case 2 for cat with stdin
     * Command: cat -
     * Stdin: ""
     */
    @Test
    public void catStdin2Test(){
    	String[] arguments = new String[]{"-"} ;
		cattool = new CATTool(arguments);
		stdin = " ";
		actualOutput = cattool.execute(workingDirectory, stdin);
		expectedOutput = " ";
		assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
		assertEquals(cattool.getStatusCode(), 0);
    }
    
    /*
     * Null test case for getStringForFile()
     * Constructor for cattool initialised with null arguments
     */
    @Test
    public void catGetStringForFileNullTest(){
    	String[] arguments = null ;
		cattool = new CATTool(arguments);
		actualOutput = cattool.getStringForFile(null);
		assertTrue(cattool.getStatusCode() != 0);
    }
    
    /*
     * Null test case for execute()
     * Constructor for cattool initialised with null arguments
     */
    @Test
    public void catExecuteNullTest(){
    	String[] arguments = null ;
		cattool = new CATTool(arguments);
		actualOutput = cattool.execute(null, null);
		assertTrue(cattool.getStatusCode() != 0);
    }
    
    /*
	 * Input : 3 valid file names
	 * Bug : New line not appended at end of file correctly
	 * 
	 * BUG_ID : CAT.5
	 * Fix in Source Code:
	 * CATTool.java Line Number : 99
	 * Class Name : CATTool
	 */
    @Test
    public void catNewLineBugFixTest(){
    	String[] arguments = new String[]{"Bug_Fix_1.txt", "Bug_Fix_2.txt", "Bug_Fix_3.txt"} ;
		cattool = new CATTool(arguments);
		actualOutput = cattool.execute(workingDirectory, stdin);
		expectedOutput = "abc\ndef\ncontains no new line" + "abc\n def ghi\ncontains new line\n" + "hello";
		assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
		assertEquals(cattool.getStatusCode(), 0);
    }
    
}
