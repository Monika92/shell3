package sg.edu.nus.comp.cs4218.impl.extended2;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

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

import sg.edu.nus.comp.cs4218.extended2.IUniqTool;
import sg.edu.nus.comp.cs4218.impl.fileutils.CATTool;

public class UNIQToolTest {

	private IUniqTool uniqtool; 
	String actualOutput,expectedOutput, helpOutput;
	File workingDirectory;
	String stdin;
	File inputFile1, inputFile2, inputFile3, inputFile4, absFile1, absFile2, relativeFile, emptyFile;

	@Before
	public void before(){
		workingDirectory = new File(System.getProperty("user.dir"));
		stdin = null;

		helpOutput = "uniq : Writes the unique lines in the given input, with repetitions compares only in adjacent input lines.";
		helpOutput += "\nCommand Format - uniq [OPTIONS] [FILE]\nFILE - Name of the file. Alternatively use \"-\" to enter standard input.";
		helpOutput += "\nOPTIONS\n\t-f NUM : Skips NUM fields on each line before checking for uniqueness. Fields are sequences of non-space non-tab characters that are separated from each other by at least one space or tab.";
		helpOutput += "\n\t-i : Ignore differences in case when comparing lines.";
		helpOutput += "\n\t-help : Brief information about supported options";

		String input1 = "hi\nhi\nhello\nhello\nhi\nabc";
		String input2 = "n hi\nf hi\nhi\nabc fgh hihi";
		String input3 = "* abc hi\n(( hi hi\n[] l hi";
		String input4 = "hi\nHi\nn Hi\nn hi\nf hi";
		inputFile1 = new File("Test_Output.txt");
		inputFile2 = new File("Test_Output_2.txt");
		inputFile3 = new File("Test_Output_3.txt");
		inputFile4 = new File("Test_Output_4.txt");
		writeToFile(inputFile1, input1);
		writeToFile(inputFile2, input2);
		writeToFile(inputFile3, input3);
		writeToFile(inputFile4, input4);

		absFile1 = new File(workingDirectory + "\\" + "Test_Output_4.txt");
		absFile2 = new File(System.getProperty("home.dir") + "Test_Output_5.txt");
		relativeFile = new File("./../Test_Output_6.txt");
		//writeToFile(abs_file_1, input);
		//writeToFile(abs_file_2, input);
		//writeToFile(relative_file, input);

		emptyFile = new File("Test_Output_7.txt");
		writeToFile(emptyFile, "");
	}

	@After
	public void after(){
		uniqtool = null;
		if(inputFile1.exists())
			inputFile1.delete();
		if(inputFile2.exists())
			inputFile2.delete();
		if(inputFile3.exists())
			inputFile3.delete();
		if(inputFile4.exists())
			inputFile4.delete();
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

	@Test
	public void uniqSingleFileGetUniqTest(){
		String[] arguments = new String[]{} ;
		uniqtool = new UNIQTool(arguments);
		actualOutput = uniqtool.getUnique(true, (readFromFile(inputFile1)));
		expectedOutput = "hi\nhello\nhi\nabc\n";
		assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
		assertEquals(uniqtool.getStatusCode(), 0);
	}

	@Test 
	public void uniqSingleFileExecuteTest(){
		String[] arguments = new String[]{"Test_Output.txt"} ;
		uniqtool = new UNIQTool(arguments);
		actualOutput = uniqtool.execute(workingDirectory, stdin);
		expectedOutput = "hi\nhello\nhi\nabc";
		assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
		assertEquals(uniqtool.getStatusCode(), 0);
	}

	@Test 
	public void uniqMultipleFileExecuteTest(){
		String[] arguments = new String[]{"Test_Output.txt", "Test_Output_2.txt", "Test_Output_3.txt"} ;
		uniqtool = new UNIQTool(arguments);
		actualOutput = uniqtool.execute(workingDirectory, stdin);
		expectedOutput = "hi\nhello\nhi\nabc\nn hi\nf hi\nhi\nabc fgh hihi\n* abc hi\n(( hi hi\n[] l hi";
		assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
		assertEquals(uniqtool.getStatusCode(), 0);
	}

	@Test 
	public void uniqInvalidFileExecuteTest(){
		String[] arguments = new String[]{"Invalid.txt"} ;
		uniqtool = new UNIQTool(arguments);
		actualOutput = uniqtool.execute(workingDirectory, stdin);
		expectedOutput = "No such file";
		assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
		assertEquals(uniqtool.getStatusCode(), -1);
	}

	@Test 
	public void uniqInvalidValidFileExecuteTest(){
		String[] arguments = new String[]{"Invalid.txt", "Test_Output.txt"} ;
		uniqtool = new UNIQTool(arguments);
		actualOutput = uniqtool.execute(workingDirectory, stdin);
		expectedOutput = "No such file";
		assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
		assertEquals(uniqtool.getStatusCode(), -1);
	}

	@Test 
	public void uniqValidInvalidFileExecuteTest(){
		String[] arguments = new String[]{"Test_Output.txt", "Invalid.txt"} ;
		uniqtool = new UNIQTool(arguments);
		actualOutput = uniqtool.execute(workingDirectory, stdin);
		expectedOutput = "hi\nhello\nhi\nabc\nNo such file";
		assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
		assertEquals(uniqtool.getStatusCode(), -1);
	}

	@Test
	public void uniqMinusIGetUniqTest(){
		String[] arguments = new String[]{"-i", "Test_Output_4.txt"} ;
		uniqtool = new UNIQTool(arguments);
		actualOutput = uniqtool.getUnique(false, (readFromFile(inputFile4)));
		expectedOutput = "hi\nn Hi\nf hi\n";
		assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
		assertEquals(uniqtool.getStatusCode(), 0);
	}

	@Test
	public void uniqMinusIExecuteTest(){
		String[] arguments = new String[]{"-i", "Test_Output_4.txt"} ;
		uniqtool = new UNIQTool(arguments);
		actualOutput = uniqtool.execute(workingDirectory, stdin);
		expectedOutput = "hi\nn Hi\nf hi";
		assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
		assertEquals(uniqtool.getStatusCode(), 0);
	}

	@Test
	public void uniqMinusFGetUniqTest(){
		String[] arguments = new String[]{"-f", "1", "Test_Output_4.txt"} ;
		uniqtool = new UNIQTool(arguments);
		actualOutput = uniqtool.getUniqueSkipNum(1, true, (readFromFile(inputFile4)));
		expectedOutput = "Hi\nhi\n";
		assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
		assertEquals(uniqtool.getStatusCode(), 0);
	}

	@Test
	public void uniqMinusFExecuteTest(){
		String[] arguments = new String[]{"-f", "1", "Test_Output_4.txt"} ;
		uniqtool = new UNIQTool(arguments);
		actualOutput = uniqtool.execute(workingDirectory, stdin);
		expectedOutput = "Hi\nhi";
		assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
		assertEquals(uniqtool.getStatusCode(), 0);
	}

	@Test
	public void uniqMinusIMinusFExecuteTest(){
		String[] arguments = new String[]{"-i", "-f", "1", "Test_Output_4.txt"} ;
		uniqtool = new UNIQTool(arguments);
		actualOutput = uniqtool.execute(workingDirectory, stdin);
		expectedOutput = "Hi";
		assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
		assertEquals(uniqtool.getStatusCode(), 0);
	}

	@Test
	public void uniqMinusFMinusIExecuteTest(){
		String[] arguments = new String[]{"-f", "1", "-i", "Test_Output_4.txt"} ;
		uniqtool = new UNIQTool(arguments);
		actualOutput = uniqtool.execute(workingDirectory, stdin);
		expectedOutput = "Hi";
		assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
		assertEquals(uniqtool.getStatusCode(), 0);
	}

	@Test
	public void uniqMultipleMinusFMultipleFileExecuteTest(){
		String[] arguments = new String[]{"-f", "0", "-f", "1", "-f", "2", "Test_Output.txt", "Test_Output_2.txt"} ;// 
		uniqtool = new UNIQTool(arguments);
		actualOutput = uniqtool.execute(workingDirectory, stdin);
		expectedOutput = "hi\nhello\nhi\nabc\nn hi\nf hi\nhi\nabc fgh hihi\nhi\nfgh hihi\nhihi";
		assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
		assertEquals(uniqtool.getStatusCode(), 0);
	}

	@Test
	public void uniqMultipleMinusIMultipleFileExecuteTest(){
		String[] arguments = new String[]{"-i", "-i", "Test_Output.txt", "Test_Output_2.txt"} ;// 
		uniqtool = new UNIQTool(arguments);
		actualOutput = uniqtool.execute(workingDirectory, stdin);
		expectedOutput = "hi\nhello\nhi\nabc\nn hi\nf hi\nhi\nabc fgh hihi";
		assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
		assertEquals(uniqtool.getStatusCode(), 0);
	}

	@Test
	public void uniqHelpExecuteTest(){
		String[] arguments = new String[]{"-i", "-f", "1", "-help", "Test_Output.txt", "Test_Output_2.txt"} ;// 
		uniqtool = new UNIQTool(arguments);
		actualOutput = uniqtool.execute(workingDirectory, stdin);
		assertTrue(actualOutput.equalsIgnoreCase(helpOutput));
		assertEquals(uniqtool.getStatusCode(), 0);
	}

	@Test
	public void uniqMinusFStringArgExecuteTest(){
		String[] arguments = new String[]{"-f", "abc", "Test_Output_4.txt"} ;
		uniqtool = new UNIQTool(arguments);
		actualOutput = uniqtool.execute(workingDirectory, stdin);
		expectedOutput = "Invalid argument for -f";
		assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
		assertTrue(uniqtool.getStatusCode() != 0);
	}

	@Test
	public void uniqMinusFDecimalArgExecuteTest(){
		String[] arguments = new String[]{"-f", "0", "-f", "1.55", "Test_Output.txt"} ;
		uniqtool = new UNIQTool(arguments);
		actualOutput = uniqtool.execute(workingDirectory, stdin);
		expectedOutput = "hi\nhello\nhi\nabc\nInvalid argument for -f";
		assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
		assertTrue(uniqtool.getStatusCode() != 0);
	}

	@Test
	public void uniqMinusFNegArgExecuteTest(){
		String[] arguments = new String[]{"-f", "-1", "Test_Output.txt"} ;
		uniqtool = new UNIQTool(arguments);
		actualOutput = uniqtool.execute(workingDirectory, stdin);
		//expectedOutput = "Invalid argument for -f";
		//assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
		assertTrue(uniqtool.getStatusCode() != 0);
	}

	@Test
	public void uniqMinusFMaxIntArgExecuteTest(){
		String[] arguments = new String[]{"-f", ""+(Integer.MAX_VALUE+1), "Test_Output.txt"} ;
		uniqtool = new UNIQTool(arguments);
		actualOutput = uniqtool.execute(workingDirectory, stdin);
		expectedOutput = "Invalid argument for -f";
		assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
		assertTrue(uniqtool.getStatusCode() != 0);
	}

	@Test
	public void uniqStdinWithNewlineExecuteTest(){
		String[] arguments = new String[]{"-"} ;
		uniqtool = new UNIQTool(arguments);
		stdin = "a\na\na\nb\nb\nc";
		actualOutput = uniqtool.execute(workingDirectory, stdin);
		expectedOutput = "a\nb\nc";
		assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
		assertEquals(uniqtool.getStatusCode(), 0);
	}

	@Test
	public void uniqGetUniqNullTest(){
		String[] arguments = null;
		uniqtool = new UNIQTool(arguments);
		actualOutput = uniqtool.getUnique(false, null);
		assertTrue(uniqtool.getStatusCode() != 0);
	}

	@Test
	public void uniqGetUniqSkipNumNullTest(){
		String[] arguments = null;
		uniqtool = new UNIQTool(arguments);
		actualOutput = uniqtool.getUniqueSkipNum(-1, false, null);
		assertTrue(uniqtool.getStatusCode() != 0);
	}

	@Test
	public void uniqExecuteNullTest(){
		String[] arguments = null;
		uniqtool = new UNIQTool(arguments);
		stdin = null;
		actualOutput = uniqtool.execute(null, stdin);
		assertTrue(uniqtool.getStatusCode() != 0);
	}


}
