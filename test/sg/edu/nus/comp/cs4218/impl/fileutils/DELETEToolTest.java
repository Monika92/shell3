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
			System.out.println("Unable to create output file");
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
    
    @Test
    public void deleteSingleFileTest(){
    	String[] arguments = new String[]{"Test_Output.txt"} ;
		deletetool = new DELETETool(arguments);
		actualOutput = deletetool.execute(workingDirectory, stdin);
		System.out.println(actualOutput);
		assertFalse(inputFile1.exists());
		assertEquals(deletetool.getStatusCode(), 0);
    }
    
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
    
    @Test
    public void deleteInvalidFileTest(){
    	String[] arguments = new String[]{"Invalid_File.txt"} ;
		deletetool = new DELETETool(arguments);
		actualOutput = deletetool.execute(workingDirectory, stdin);
		expectedOutput = "No such file";
		assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
		assertEquals(deletetool.getStatusCode(), -1);
    }
    
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
    
    @Test
    public void deleteAbsoluteFile1Test(){
    	String[] arguments = new String[]{workingDirectory + "\\" + "Test_Output_4.txt"} ;
		deletetool = new DELETETool(arguments);
		actualOutput = deletetool.execute(workingDirectory, stdin);
		assertFalse(absFile1.exists());
		assertEquals(deletetool.getStatusCode(), 0);
    }
    
    @Test
    public void deleteAbsoluteFile2Test(){
    	String[] arguments = new String[]{System.getProperty("home.dir") + "Test_Output_5.txt"} ;
		deletetool = new DELETETool(arguments);
		actualOutput = deletetool.execute(workingDirectory, stdin);
		assertFalse(absFile2.exists());
		assertEquals(deletetool.getStatusCode(), 0);
    }
    
    @Test
    public void deleteRelativeFileTest(){
    	String[] arguments = new String[]{"./../Test_Output_6.txt",} ;
		deletetool = new DELETETool(arguments);
		actualOutput = deletetool.execute(workingDirectory, stdin);
		assertFalse(relativeFile.exists());
		assertEquals(deletetool.getStatusCode(), 0);
    }

}
