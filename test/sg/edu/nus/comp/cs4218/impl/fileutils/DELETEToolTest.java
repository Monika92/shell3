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
	File input_file_1 = new File("Test_Output.txt"),  input_file_2 = new File("Test_Output_2.txt"),  input_file_3 = new File("Test_Output_3.txt") ;
	File abs_file_1, abs_file_2, relative_file;
	
	@Before
	public void before(){
		workingDirectory = new File(System.getProperty("user.dir"));
		stdin = null;
		
		String input = "This is \na test \nrun.";
		writeToFile(input_file_1, input);
		writeToFile(input_file_2, input);
		writeToFile(input_file_3, input);
		
		abs_file_1 = new File(workingDirectory + "\\" + "Test_Output_4.txt");
		abs_file_2 = new File("C:\\Users\\monika92\\Desktop\\" + "Test_Output_5.txt");
		relative_file = new File("./../Test_Output_6.txt");
		writeToFile(abs_file_1, input);
		writeToFile(abs_file_2, input);
		writeToFile(relative_file, input);
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
		if(input_file_1.exists())
			input_file_1.delete();
		if(input_file_2.exists())
			input_file_2.delete();
		if(input_file_3.exists())
			input_file_3.delete();
		if(abs_file_1.exists())
			abs_file_1.delete();
		if(abs_file_2.exists())
			abs_file_2.delete();
		if(relative_file.exists())
			relative_file.delete();
	}
    
    @Test
    public void deleteSingleFileTest(){
    	String[] arguments = new String[]{"Test_Output.txt"} ;
		deletetool = new DELETETool(arguments);
		actualOutput = deletetool.execute(workingDirectory, stdin);
		System.out.println(actualOutput);
		assertFalse(input_file_1.exists());
		assertEquals(deletetool.getStatusCode(), 0);
    }
    
    @Test
    public void deleteMultipleFileTest(){
    	String[] arguments = new String[]{"Test_Output.txt", "Test_Output_2.txt", "Test_Output_3.txt"} ;
		deletetool = new DELETETool(arguments);
		actualOutput = deletetool.execute(workingDirectory, stdin);
		assertFalse(input_file_1.exists());
		assertFalse(input_file_2.exists());
		assertFalse(input_file_3.exists());
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
		assertFalse(input_file_1.exists());
		assertEquals(deletetool.getStatusCode(), -1);
    }
    
    @Test
    public void deleteValidInvalidFileTest(){
    	String[] arguments = new String[]{"Invalid_File.txt", "Test_Output.txt"} ;
		deletetool = new DELETETool(arguments);
		actualOutput = deletetool.execute(workingDirectory, stdin);
		expectedOutput = "No such file";
		assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
		assertTrue(input_file_1.exists());
		assertEquals(deletetool.getStatusCode(), -1);
    }
    
    @Test
    public void deleteAbsoluteFile1Test(){
    	String[] arguments = new String[]{workingDirectory + "\\" + "Test_Output_4.txt"} ;
		deletetool = new DELETETool(arguments);
		actualOutput = deletetool.execute(workingDirectory, stdin);
		assertFalse(abs_file_1.exists());
		assertEquals(deletetool.getStatusCode(), 0);
    }
    
    @Test
    public void deleteAbsoluteFile2Test(){
    	String[] arguments = new String[]{"C:\\Users\\monika92\\Desktop\\" + "Test_Output_5.txt"} ;
		deletetool = new DELETETool(arguments);
		actualOutput = deletetool.execute(workingDirectory, stdin);
		assertFalse(abs_file_2.exists());
		assertEquals(deletetool.getStatusCode(), 0);
    }
    
    @Test
    public void deleteRelativeFileTest(){
    	String[] arguments = new String[]{"./../Test_Output_6.txt",} ;
		deletetool = new DELETETool(arguments);
		actualOutput = deletetool.execute(workingDirectory, stdin);
		assertFalse(relative_file.exists());
		assertEquals(deletetool.getStatusCode(), 0);
    }

}
