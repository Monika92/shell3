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
	File input_file_1 = new File("Test_Output.txt"),  input_file_2 = new File("Test_Output_2.txt"),  input_file_3 = new File("Test_Output_3.txt") ;
	
	@Before
	public void before(){
		workingDirectory = new File(System.getProperty("user.dir"));
		stdin = null;
		
		String input = "This is \na test \nrun.";
		writeToFile(input_file_1, input);
		writeToFile(input_file_2, input);
		writeToFile(input_file_3, input);
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
	
	public String readFromFile(File input_file){
		String output = ""; FileReader fr = null;
		try{
			fr = new FileReader(input_file);
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
	
    @After
	public void after(){
		cattool = null;
		if(input_file_1.exists())
			input_file_1.delete();
		if(input_file_2.exists())
			input_file_2.delete();
		if(input_file_3.exists())
			input_file_3.delete();
	}
    
    @Test
    public void catSingleFileTest(){
    	String[] arguments = new String[]{"Test_Output.txt"} ;
		cattool = new CATTool(arguments);
		actualOutput = cattool.execute(workingDirectory, stdin);
		expectedOutput = "This is \na test \nrun.";
		System.out.println(actualOutput);
		assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
		assertEquals(cattool.getStatusCode(), 0);
    }
    
    @Test
    public void catMultipleFileTest(){
    	String[] arguments = new String[]{"Test_Output.txt", "Test_Output_2.txt", "Test_Output_3.txt"} ;
		cattool = new CATTool(arguments);
		actualOutput = cattool.execute(workingDirectory, stdin);
		expectedOutput = "This is \na test \nrun.\nThis is \na test \nrun.\nThis is \na test \nrun.";
		assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
		assertEquals(cattool.getStatusCode(), 0);
    }
    
    @Test
    public void catInvalidFileTest(){
    	String[] arguments = new String[]{"Invalid_File.txt"} ;
		cattool = new CATTool(arguments);
		actualOutput = cattool.execute(workingDirectory, stdin);
		expectedOutput = "No such file";
		assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
		assertEquals(cattool.getStatusCode(), -1);
    }
    
    @Test
    public void catInvalidValidFileTest(){
    	String[] arguments = new String[]{"Test_Output.txt", "Invalid_File.txt", "InvalidFile.txt"} ;
		cattool = new CATTool(arguments);
		actualOutput = cattool.execute(workingDirectory, stdin);
		expectedOutput = "No such file";
		assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
		assertEquals(cattool.getStatusCode(), -1);
    }
    
    @Test
    public void catValidInvalidFileTest(){
    	String[] arguments = new String[]{"Invalid_File.txt", "Test_Output.txt"} ;
		cattool = new CATTool(arguments);
		actualOutput = cattool.execute(workingDirectory, stdin);
		expectedOutput = "No such file";
		assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
		assertEquals(cattool.getStatusCode(), -1);
    }


}
