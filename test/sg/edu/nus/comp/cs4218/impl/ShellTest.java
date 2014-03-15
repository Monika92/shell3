package sg.edu.nus.comp.cs4218.impl;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import sg.edu.nus.comp.cs4218.ITool;
import sg.edu.nus.comp.cs4218.impl.extended2.SORTTool;



public class ShellTest {

	private Shell shell;
	File inputFile1;
	
	@Before
	public void before(){
	
		shell = new Shell();
		
		String input1 = "apple\nball\ncat\ndog";
		inputFile1 = new File("test1.txt");
		writeToFile(inputFile1, input1);

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
	public void after()
	{
		shell = null;
		if(inputFile1.exists())
			inputFile1.delete();
	}
	
	@Test
	public void parseNullCommandTest()
	{
		String commandline = null;
		assertNull(shell.parse(commandline));		
	}
	
	@Test
	public void parseCutCommandTest()
	{
		String commandline = "cut -c 1-2 -";
		String[] expectedArgsList = {"-c","1-2","-"};
		assertEquals("CUTTool",shell.parse(commandline).getClass().getSimpleName());
		assertArrayEquals(expectedArgsList,shell.getArgumentList());		
	}
	@Test
	public void parseCdCommandTest()
	{
		String commandline = "cd /Users/";
		String[] expectedArgsList = {"/Users/"};
		assertEquals("CDTool",shell.parse(commandline).getClass().getSimpleName());
		assertArrayEquals(expectedArgsList,shell.getArgumentList());		
	}
	@Test
	public void parseEchoCommandTest()
	{
		String commandline = "echo \" hello world \"";
		String[] expectedArgsList = {"\" hello world \""};
		assertEquals("ECHOTool",shell.parse(commandline).getClass().getSimpleName());
		assertArrayEquals(expectedArgsList,shell.getArgumentList());	
	}
	
	@Test
	public void parseCatCommandTest()
	{
		String commandline = "cat -";
		String[] expectedArgsList = {"-"};
		assertEquals("CATTool",shell.parse(commandline).getClass().getSimpleName());
		assertArrayEquals(expectedArgsList,shell.getArgumentList());	
	}
	@Test
	public void parsePipeCommandWithSpacesTest()
	{
		String commandline = "echo hello | cut -c 1-2";
		String[] expectedArgsList = {"echo", "hello", "|", "cut", "-c", "1-2"};
		assertEquals("PIPINGTool",shell.parse(commandline).getClass().getSimpleName());
		assertArrayEquals(expectedArgsList,shell.getArgumentList());	
	}
	@Test
	public void parsePipeCommandWithoutSpacesTest()
	{
		String commandline = "echo hello|cut -c 1-2";
		String[] expectedArgsList = {"echo", "hello", "|", "cut", "-c", "1-2"};
		assertEquals("PIPINGTool",shell.parse(commandline).getClass().getSimpleName());
		assertArrayEquals(expectedArgsList,shell.getArgumentList());	
	}
	@Test
	public void parseGrepCommandTest()
	{
		String commandline = "grep (A|B) -";
		String[] expectedArgsList = {"(A|B)","-"};
		assertEquals("GREPTool",shell.parse(commandline).getClass().getSimpleName());
		assertArrayEquals(expectedArgsList,shell.getArgumentList());	
	}
	@Test
	public void parseGrepPipeEchoCommandTest()
	{
		String commandline = "echo hello|grep (h|o) test.txt";
		String[] expectedArgsList = {"echo", "hello","|","grep", "(h|o)", "test.txt"};
		assertEquals("PIPINGTool",shell.parse(commandline).getClass().getSimpleName());
		assertArrayEquals(expectedArgsList,shell.getArgumentList());	
	}
	@Test
	public void parseGrepPipeEchoCommandExtraPipeAtEndTest()
	{
		String commandline = "echo hello|grep (h|o) test.txt|";
		String[] expectedArgsList = {"echo", "hello","|","grep", "(h|o)", "test.txt"};
		assertEquals("PIPINGTool",shell.parse(commandline).getClass().getSimpleName());
		assertArrayEquals(expectedArgsList,shell.getArgumentList());	
	}
	
	
	@Test
	public void parseGrepPipeSortCommand()
	{
		String commandline = "grep | sort";
		String[] expectedArgsList = {"grep","|","sort"};
		assertEquals("PIPINGTool",shell.parse(commandline).getClass().getSimpleName());
		assertArrayEquals(expectedArgsList,shell.getArgumentList());	
	}
	@Test
	public void parseCtrlZCommandTest()
	{
		String commandline = "ctrl-Z";
		assertEquals("",shell.parse(commandline).getClass().getSimpleName());
	}
	
	@Test
	public void parseInvalidCommandTest()
	{
		String commandline = "nslookup hello";
		ITool itool = shell.parse(commandline);
		assertNull(itool);
	}
	@Test
	public void stopTest()
	{
		Thread r = new Thread();
		shell.stop(r);
	}
	@Test
	public void executeTest()
	{
		  String input = "";

		  // set stdin
		  System.setIn(new ByteArrayInputStream(input.getBytes()));
		  shell.command = "sort";
		  
		  // call the method that reads from stdin and writes to stdout
		  shell.execute(new SORTTool(new String[]{"test1.txt"}));
		 
		  // assert stdout's content value
		  String actualOutput1 = readFromFile(inputFile1);
		  String expectedOutput1 = "apple\nball\ncat\ndog\n";
		  assertTrue(expectedOutput1.equalsIgnoreCase(actualOutput1));
	}
	
	public String readFromFile(File inputFile){
		String output = ""; FileReader fileReader = null;
		try{
			fileReader = new FileReader(inputFile);
		} catch(FileNotFoundException e){
			e.printStackTrace();
			return "File not found";
		}
		BufferedReader br = new BufferedReader(fileReader);
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
				fileReader.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return output;
	}
	
	@Test
	public void overallShellTest() {
		//fail("Not yet implemented");
	}

}
