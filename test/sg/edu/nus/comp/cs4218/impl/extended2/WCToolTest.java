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

	private IWcTool wctool; 
	private WCTool wc; 
	String actualOutput,expectedOutput,helpOutput;
	File workingDirectory;
	File input_file_1,input_file_2;

	
	@Before
	public void before(){
		
		WorkingDirectory.changeWorkingDirectory(new File(System.getProperty("user.dir")));
		input_file_1 = new File("input1.txt");
		input_file_2 = new File("input2.txt");
		writeToFile(input_file_1, "abc\nb d\n---\n");
		writeToFile(input_file_2, "  ");
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
			System.out.println("Unable to create output file");
		}
	}

	@After
	public void after(){
		wctool = null;
		wc = null;
		if(input_file_1.exists())
			input_file_1.delete();
		if(input_file_2.exists())
			input_file_2.delete();
		
	}
	
	@Test
    public void helpTest()
    {
    	String[] arguments = new String[]{"-help"} ;
		wctool = new WCTool(arguments);
		actualOutput = wctool.execute(workingDirectory, null);
		expectedOutput = helpOutput ;
		assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
		assertEquals(wctool.getStatusCode(), 0);
    }
	
	@Test
    public void helpPriorityTest()
    {
    	String[] arguments = new String[]{"-m" , "-help" , "input.txt"} ;
		wctool = new WCTool(arguments);
		actualOutput = wctool.execute(workingDirectory, null);
		expectedOutput = helpOutput ;
		assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
		assertEquals(wctool.getStatusCode(), 0);
    }
    
	@Test
    public void invalidStdinInputTest()
    {
    	String[] arguments = new String[]{"-m", "-w" , "-l" , "-"} ;
    	wctool = new WCTool(arguments);
		actualOutput = wctool.execute(workingDirectory, "abcde");
		expectedOutput = "abcde : error - Invalid Input. \n";
		assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
		assertEquals(wctool.getStatusCode(), 0);
    }

	@Test
    public void invalidFilenameInputTest()
    {
    	String[] arguments = new String[]{"-m", "-w", "-l", "idontexisthaha.txt"} ;
		wctool = new WCTool(arguments);
		actualOutput = wctool.execute(workingDirectory, null);
		expectedOutput =  "idontexisthaha.txt : error - Invalid Input. \n";
		assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
		assertEquals(wctool.getStatusCode(), 0);
    }
	
	@Test
    public void noFilenameOrStdinInputTest()
    {
    	String[] arguments = new String[]{"-m", "-w", "-l"} ;
		wctool = new WCTool(arguments);
		actualOutput = wctool.execute(workingDirectory, null);
		expectedOutput =  "No filename given.";
		assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
		assertEquals(wctool.getStatusCode(), 0);
    }
	
	@Test
    public void noFilenameOrStdinOrOptionsInputTest()
    {
    	String[] arguments = new String[]{} ;
		wctool = new WCTool(arguments);
		actualOutput = wctool.execute(workingDirectory, null);
		expectedOutput =  "No filename given.";
		assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
		assertEquals(wctool.getStatusCode(), 0);
    }
	
	@Test
    public void allOptionsInputTest()
    {
    	String[] arguments = new String[]{"-m", "-w", "-l", "input1.txt"} ;
		wctool = new WCTool(arguments);
		actualOutput = wctool.execute(WorkingDirectory.workingDirectory, null);
		expectedOutput =  WorkingDirectory.workingDirectory + File.separator + "input1.txt :  -m  8 -w  4 -l  3\n";
		System.out.println(actualOutput);
		assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
		assertEquals(wctool.getStatusCode(), 0);
    }
	
	@Test
    public void ignoreTheAccidentalMinusInputTest()
    {
    	String[] arguments = new String[]{"-m", "-" , "-w", "-l", "input1.txt"} ;
		wctool = new WCTool(arguments);
		actualOutput = wctool.execute(WorkingDirectory.workingDirectory, null);
		expectedOutput =  WorkingDirectory.workingDirectory + File.separator + "input1.txt :  -m  8 -w  4 -l  3\n";
		System.out.println(actualOutput);
		assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
		assertEquals(wctool.getStatusCode(), 0);
    }
	
	@Test
    public void fewOptionsInputTest()
    {
    	String[] arguments = new String[]{"-w", "-m", "input1.txt"} ;
		wctool = new WCTool(arguments);
		actualOutput = wctool.execute(WorkingDirectory.workingDirectory, null);
		expectedOutput =  WorkingDirectory.workingDirectory + File.separator + "input1.txt :  -m  8 -w  4\n";
		System.out.println(actualOutput);
		assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
		assertEquals(wctool.getStatusCode(), 0);
    }
	
	@Test
    public void capsOptionsInputTest()
    {
    	String[] arguments = new String[]{"-W", "-M", "-L", "input1.txt"} ;
		wctool = new WCTool(arguments);
		actualOutput = wctool.execute(WorkingDirectory.workingDirectory, null);
		expectedOutput =  WorkingDirectory.workingDirectory + File.separator + "input1.txt :  -m  8 -w  4 -l  3\n";
		System.out.println(actualOutput);
		assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
		assertEquals(wctool.getStatusCode(), 0);
    }
	
	@Test
    public void absoluteFilenameInputTest()
    {
    	String[] arguments = new String[]{"-w", "-m", WorkingDirectory.workingDirectory + File.separator + "input1.txt"} ;
		wctool = new WCTool(arguments);
		actualOutput = wctool.execute(WorkingDirectory.workingDirectory, null);
		expectedOutput =  WorkingDirectory.workingDirectory + File.separator + "input1.txt :  -m  8 -w  4\n";
		System.out.println(actualOutput);
		assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
		assertEquals(wctool.getStatusCode(), 0);
    }
	
	@Test
    public void emptyFileInputTest()
    {
    	String[] arguments = new String[]{"-w", "-m", "-l", WorkingDirectory.workingDirectory + File.separator + "input2.txt"} ;
		wctool = new WCTool(arguments);
		actualOutput = wctool.execute(WorkingDirectory.workingDirectory, null);
		expectedOutput =  WorkingDirectory.workingDirectory + File.separator + "input2.txt :  -m  0 -w  0 -l  0\n";
		System.out.println(actualOutput);
		assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
		assertEquals(wctool.getStatusCode(), 0);
    }
	
	@Test
    public void noOptionsValidFilenameInputTest()
    {
    	String[] arguments = new String[]{WorkingDirectory.workingDirectory + File.separator + "input2.txt"} ;
		wctool = new WCTool(arguments);
		actualOutput = wctool.execute(WorkingDirectory.workingDirectory, null);
		expectedOutput =  WorkingDirectory.workingDirectory + File.separator + "input2.txt : -m  0 , -w  0 , -l 0\n";
		System.out.println(actualOutput);
		System.out.println(expectedOutput);
		assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
		assertEquals(wctool.getStatusCode(), 0);
    }
    
	@Test
    public void multipleFilenamesInputTest()
    {
    	String[] arguments = new String[]{"-w", "-m", "input1.txt", "input2.txt"} ;
		wctool = new WCTool(arguments);
		actualOutput = wctool.execute(WorkingDirectory.workingDirectory, null);
		expectedOutput =  WorkingDirectory.workingDirectory + File.separator + "input1.txt :  -m  8 -w  4\n"
				         + WorkingDirectory.workingDirectory + File.separator + "input2.txt :  -m  0 -w  0\n";
		System.out.println(actualOutput);
		assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
		assertEquals(wctool.getStatusCode(), 0);
    }
	
	@Test
    public void oneValidOneInvalidFilenamesInputTest()
    {
    	String[] arguments = new String[]{"-w", "-m", "input1.txt", "input3.txt"} ;
		wctool = new WCTool(arguments);
		actualOutput = wctool.execute(WorkingDirectory.workingDirectory, null);
		expectedOutput =  WorkingDirectory.workingDirectory + File.separator + "input1.txt :  -m  8 -w  4\n"
				         + "input3.txt : error - Invalid Input. \n";
		System.out.println(actualOutput);
		assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
		assertEquals(wctool.getStatusCode(), 0);
    }
	
	@Test
    public void stdinAndFilenameInputTest()
    {
    	String[] arguments = new String[]{"-w", "-m", "input1.txt", "input3.txt"} ;
		wctool = new WCTool(arguments);
		actualOutput = wctool.execute(WorkingDirectory.workingDirectory, "input2.txt");
		expectedOutput =  WorkingDirectory.workingDirectory + File.separator + "input1.txt :  -m  8 -w  4\n"
				         + "input3.txt : error - Invalid Input. \n"
				         +  WorkingDirectory.workingDirectory + File.separator + "input2.txt :  -m  0 -w  0\n" ;
		System.out.println(actualOutput);
		assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
		assertEquals(wctool.getStatusCode(), 0);
    }
	
	@Test
    public void ivalidStdinAndvalidFilenameInputTest()
    {
    	String[] arguments = new String[]{"-w", "-m", "input1.txt", "input2.txt"} ;
		wctool = new WCTool(arguments);
		actualOutput = wctool.execute(WorkingDirectory.workingDirectory, "input3.txt");
		expectedOutput =  WorkingDirectory.workingDirectory + File.separator + "input1.txt :  -m  8 -w  4\n"
				         +  WorkingDirectory.workingDirectory + File.separator + "input2.txt :  -m  0 -w  0\n"
				         + "input3.txt : error - Invalid Input. \n";
		System.out.println(actualOutput);
		assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
		assertEquals(wctool.getStatusCode(), 0);
    }
	
	@Test
    public void onlyStdinInputTest()
    {
    	String[] arguments = new String[]{"-w", "-m"} ;
		wctool = new WCTool(arguments);
		actualOutput = wctool.execute(WorkingDirectory.workingDirectory, "input2.txt");
		expectedOutput = WorkingDirectory.workingDirectory + File.separator + "input2.txt :  -m  0 -w  0\n";
		System.out.println(actualOutput);
		assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
		assertEquals(wctool.getStatusCode(), 0);
    }
	
	@Test
    public void noOptionsStdinInputTest()
    {
    	String[] arguments = new String[]{} ;
		wctool = new WCTool(arguments);
		actualOutput = wctool.execute(WorkingDirectory.workingDirectory, "input2.txt");
		expectedOutput =  WorkingDirectory.workingDirectory + File.separator + "input2.txt : -m  0 , -w  0 , -l 0\n";
		System.out.println(actualOutput);
		System.out.println(expectedOutput);
		assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
		assertEquals(wctool.getStatusCode(), 0);
    }
	
	@Test
    public void onlyAbsoluteStdinInputTest()
    {
    	String[] arguments = new String[]{"-w", "-m"} ;
		wctool = new WCTool(arguments);
		actualOutput = wctool.execute(WorkingDirectory.workingDirectory, WorkingDirectory.workingDirectory + File.separator + "input2.txt");
		expectedOutput = WorkingDirectory.workingDirectory + File.separator + "input2.txt :  -m  0 -w  0\n";
		System.out.println(actualOutput);
		assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
		assertEquals(wctool.getStatusCode(), 0);
    }
}
