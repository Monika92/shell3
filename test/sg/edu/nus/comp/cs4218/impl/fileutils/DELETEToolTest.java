package sg.edu.nus.comp.cs4218.impl.fileutils;

import static org.junit.Assert.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import sg.edu.nus.comp.cs4218.fileutils.IDeleteTool;
import sg.edu.nus.comp.cs4218.fileutils.IEchoTool;

public class DELETEToolTest {

	private IDeleteTool deletetool; 
	String actualOutput,expectedOutput;
	File workingDirectory;
	String stdin;
	File inputFile1, inputFile2, inputFile3;
	File absFile1, absFile2, relativeFile;
	
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
		absFile2 = new File(System.getProperty("home.dir") + "Test_Output_5.txt");
		relativeFile = new File("./../Test_Output_6.txt");
		writeToFile(absFile1, input);
		writeToFile(absFile2, input);
		writeToFile(relativeFile, input);
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
		deletetool = null;
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
	}
    
    /*
     * Basic test case for delete with single file
     * Command: delete filename
     */
    @Test
    public void deleteSingleFileTest(){
    	String[] arguments = new String[]{"Test_Output.txt"} ;
		deletetool = new DELETETool(arguments);
		actualOutput = deletetool.execute(workingDirectory, stdin);
		assertFalse(inputFile1.exists());
		assertEquals(deletetool.getStatusCode(), 0);
    }
    
    /*
     * Test case for delete with multiple filenames
     * Command: delete filename1 filename2 filename3
     */
    @Test
    public void deleteMultipleFileTest(){
    	String[] arguments = new String[]{"Test_Output.txt", "Test_Output_2.txt", "Test_Output_3.txt"} ;
		deletetool = new DELETETool(arguments);
		actualOutput = deletetool.execute(workingDirectory, stdin);
		assertFalse(inputFile1.exists());
		assertFalse(inputFile2.exists());
		assertFalse(inputFile3.exists());
		assertEquals(deletetool.getStatusCode(), 0);
    }
    
    /*
     * Negative test case for delete
     * Command: delete filename
     * Error: Non-existent file
     */
    @Test
    public void deleteInvalidFileTest(){
    	String[] arguments = new String[]{"Invalid_File.txt"} ;
		deletetool = new DELETETool(arguments);
		actualOutput = deletetool.execute(workingDirectory, stdin);
		expectedOutput = "No such file";
		assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
		assertEquals(deletetool.getStatusCode(), -1);
    }
    
    /*
     * Negative test case for delete
     * Command: delete filename1 filename2
     * Error: filename1 is invalid
     */
    @Test
    public void deleteInvalidValidFileTest(){
    	String[] arguments = new String[]{"Test_Output.txt", "Invalid_File.txt", "InvalidFile.txt"} ;
		deletetool = new DELETETool(arguments);
		actualOutput = deletetool.execute(workingDirectory, stdin);
		expectedOutput = "No such file";
		assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
		assertFalse(inputFile1.exists());
		assertEquals(deletetool.getStatusCode(), -1);
    }
    
    /*
     * Positive + Negative test case for delete
     * Command: delete filename1 filename2
     * Error: filename2 is invalid
     */
    @Test
    public void deleteValidInvalidFileTest(){
    	String[] arguments = new String[]{"Invalid_File.txt", "Test_Output.txt"} ;
		deletetool = new DELETETool(arguments);
		actualOutput = deletetool.execute(workingDirectory, stdin);
		expectedOutput = "No such file";
		assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
		assertTrue(inputFile1.exists());
		assertEquals(deletetool.getStatusCode(), -1);
    }
    
    /*
     * Test case 1 for delete with absolute path of filename
     * Command: delete filename_abs_path
     */
    @Test
    public void deleteAbsoluteFile1Test(){
    	String[] arguments = new String[]{workingDirectory + "\\" + "Test_Output_4.txt"} ;
		deletetool = new DELETETool(arguments);
		actualOutput = deletetool.execute(workingDirectory, stdin);
		assertFalse(absFile1.exists());
		assertEquals(deletetool.getStatusCode(), 0);
    }
    
    /*
     * Test case 2 for delete with absolute path of filename
     * Command: delete filename_abs_path
     */
    @Test
    public void deleteAbsoluteFile2Test(){
    	String[] arguments = new String[]{System.getProperty("home.dir") + "Test_Output_5.txt"} ;
		deletetool = new DELETETool(arguments);
		actualOutput = deletetool.execute(workingDirectory, stdin);
		assertFalse(absFile2.exists());
		assertEquals(deletetool.getStatusCode(), 0);
    }
    
    /*
     * Test case for delete with relative path of filename
     * Command: delete filename_relative_path
     */
    @Test
    public void deleteRelativeFileTest(){
    	String[] arguments = new String[]{"./../Test_Output_6.txt",} ;
		deletetool = new DELETETool(arguments);
		actualOutput = deletetool.execute(workingDirectory, stdin);
		assertFalse(relativeFile.exists());
		assertEquals(deletetool.getStatusCode(), 0);
    }

    /*
     * Test case for delete interface method with invalid file
     * Command: delete invalid_filename
     */
    @Test
    public void deleteInterfaceInvalidFileTest(){
    	String[] arguments = new String[]{"InvalidFile.txt",} ;
		deletetool = new DELETETool(arguments);
		File toDelete = new File("InvalidFile.txt");
		deletetool.delete(toDelete);
		assertTrue(deletetool.getStatusCode() != 0);
    }
    
    /*
     * Null test case for delete interface method
     * Constructor for deletetool is initialised with null arguments 
     */
    @Test
    public void deleteNullTest(){
    	String[] arguments = null ;
		deletetool = new DELETETool(arguments);
		deletetool.delete(null);
		assertTrue(deletetool.getStatusCode() != 0);
    }
    
    /*
     * Null test case for execute interface method
     * Constructor for deletetool initialised with null arguments
     */
    @Test
    public void deleteExecuteNullTest(){
    	String[] arguments = null ;
		deletetool = new DELETETool(arguments);
		actualOutput = deletetool.execute(null, null);
		assertTrue(deletetool.getStatusCode() != 0);
    }
    
}
