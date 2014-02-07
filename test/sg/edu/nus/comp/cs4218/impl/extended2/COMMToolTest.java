package sg.edu.nus.comp.cs4218.impl.extended2;

import static org.junit.Assert.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import sg.edu.nus.comp.cs4218.extended2.ICommTool;
import sg.edu.nus.comp.cs4218.extended2.IPasteTool;

public class COMMToolTest {
	
	private ICommTool commTool;
	String actualOutput,expectedOutput,helpOutput;
	File workingDirectory;

	File file_a,file_b,file_c,file_d,file_em1,file_em2;
	String fileContent_a,fileContent_b,fileContent_c,fileContent_d,fileContent_e;
	String testTab, testNewline, testDash;
	
	@Before
	public void before() throws Exception {
		workingDirectory = new File(System.getProperty("user.dir"));
		helpOutput = " /*\n" +
				"\n*" +
				"\n* comm : Compares two sorted files line by line. With no options, produce three-column output." +
				"\n* 		 Column one contains lines unique to FILE1, column two contains lines unique to FILE2," +
				"\n 		 and column three contains lines common to both files."+
				"\n*" +	
				"\n*	Command Format - comm [OPTIONS] FILE1 FILE2" +
				"\n*	FILE1 - Name of the file 1" +
				"\n*	FILE2 - Name of the file 2" +
				"\n*		-c : check that the input is correctly sorted" +
				"\n*      -d : do not check that the input is correctly sorted" +
				"\n*      -help : Brief information about supported options" +
				"\n*/";

		file_a = new File("a.txt");
		file_b = new File("b.txt");
		file_c = new File("c.txt");
		file_d = new File("d.txt");
		file_em1 = new File("em1.txt");
		file_em2 = new File("em2.txt");
		file_em1.createNewFile();
		file_em2.createNewFile();
		
		fileContent_a = "Apple\nMelon\nOrange";
		fileContent_b = "Banana\nMelon\nOrange";
		fileContent_c = "Batman\nSpiderman\nSuperman";
		fileContent_d = "Cat\nBat";
		fileContent_e = "";
		
		writeToFile(file_a, fileContent_a);
		writeToFile(file_b, fileContent_b);
		writeToFile(file_c, fileContent_c);
		writeToFile(file_d,fileContent_d);

		testTab = "\t";
		testNewline = "\n";
		testDash = " ";
	}

	private void writeToFile(File f, String fContent){
		BufferedWriter bw;
		try {
			String[] lines= fContent.split("\n");
			bw = new BufferedWriter(new FileWriter(f));

			for(int i=0; i<lines.length; i++){
				bw.write(lines[i]); bw.newLine();
			}
			bw.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	@After
	public void after() throws Exception {
		commTool = null;
		file_a.delete();
		file_b.delete();
		file_c.delete();
		file_d.delete();
		file_em1.delete();
		file_em2.delete();
	}

	@Test
	//Test if overall control flow is correct
	public void overallTest() {
		String[] arguments = new String[]{"a.txt","b.txt"};

		commTool = new COMMTool(arguments);
		actualOutput = commTool.execute(workingDirectory, null);
		expectedOutput = "Apple" + testTab + testDash + testTab + testDash + testNewline +
				testDash + testTab + "Banana" + testTab +testDash + testNewline +
				testDash + testTab + testDash + testTab + "Melon" + testNewline +
				testDash + testTab + testDash + testTab + "Orange";

		assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
		assertEquals(commTool.getStatusCode(), 0);	
	}
	
	@Test
	//File doesn't exist
	public void compareFilesInvalidFileArgsTest(){
		String fileName1 = "C:\\Users\\Dale\\a.txt";
		String fileName2 = "./b.txt";
		String[] arguments = new String[]{"C:\\Users\\Dale\\a.txt","./b.txt"};
		commTool = new COMMTool(arguments);		
		actualOutput = commTool.execute(workingDirectory, null);

		expectedOutput = null;
		assertEquals(expectedOutput, actualOutput);	

	}
	
	@Test
	//comm -c -help file1 file2
	public void compareFilesGiveHelpPriorityTest(){
		String[] arguments = new String[]{"-c","-help","file1","file2"};
		commTool = new COMMTool(arguments);
		actualOutput = commTool.execute(workingDirectory, null);
		expectedOutput = helpOutput;
		assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
		assertEquals(commTool.getStatusCode(), 0);	
	}
	
	@Test
	//Both file arguments are empty files
	//comm em1.txt em2.txt
	public void compareFilesNoOptionsBothFilesEmptyTest(){
		String[] arguments = new String[]{"em1.txt","em2.txt"};
		String input1 = "em1.txt";
		String input2 = "em2.txt";
		commTool = new COMMTool(arguments);
		actualOutput = commTool.compareFiles(input1, input2);
		expectedOutput = "";
		
		assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
		assertEquals(commTool.getStatusCode(), 0);	
	}
	
	@Test
	//All lines in both files are unique. Column 3 should be empty
	public void compareFilesNoOptionsAllUniqueTest(){
		String[] arguments = new String[]{"a.txt","c.txt"};
		String input1 = "a.txt";
		String input2 = "c.txt";
		commTool = new COMMTool(arguments);
		actualOutput = commTool.compareFiles(input1, input2);
		expectedOutput = "Apple" + testTab + testDash + testTab + testDash + testNewline +
				testDash + testTab + "Batman" + testTab +testDash + testNewline +
				"Melon" + testTab + testDash + testTab + testDash + testNewline +
				"Orange" + testTab + testDash + testTab + testDash + testNewline +
				testDash + testTab + "Spiderman" + testTab +testDash + testNewline +
				testDash + testTab + "Superman" + testTab +testDash;

		assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
		assertEquals(commTool.getStatusCode(), 0);		
		
	}
	
	@Test
	//Some common lines. All three columns have items
	public void compareFilesNoOptionsSomeUniqueTest(){
		String[] arguments = new String[]{"a.txt","b.txt"};
		String input1 = "a.txt";
		String input2 = "b.txt";
		commTool = new COMMTool(arguments);
		actualOutput = commTool.compareFiles(input1, input2);
		expectedOutput = "Apple" + testTab + testDash + testTab + testDash + testNewline +
				testDash + testTab + "Banana" + testTab +testDash + testNewline +
				testDash + testTab + testDash + testTab + "Melon" + testNewline +
				testDash + testTab + testDash + testTab + "Orange";

		assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
		assertEquals(commTool.getStatusCode(), 0);	
	}
	
	@Test
	//No unique lines at all. Col1 and Col2 are empty
	public void compareFilesNoOptionsNoneUniqueTest(){
		String[] arguments = new String[]{"a.txt","a.txt"};
		String input1 = "a.txt";
		String input2 = "a.txt";
		commTool = new COMMTool(arguments);
		actualOutput = commTool.compareFiles(input1, input2);
		expectedOutput = 
				 testDash + testTab + testDash + testTab + "Apple" + testNewline +
				 testDash + testTab + testDash + testTab + "Melon" + testNewline +
				 testDash + testTab + testDash + testTab + "Orange";

		assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
		assertEquals(commTool.getStatusCode(), 0);	
	}

	@Test
	//Positive case: Files are sorted
	public void compareFilesCheckSortStatusTest1(){
		String[] arguments = new String[]{"-c","a.txt","b.txt"};
		String input1 = "a.txt";
		String input2 = "b.txt";
		commTool = new COMMTool(arguments);
		actualOutput = commTool.compareFilesCheckSortStatus(input1, input2);
		expectedOutput = "Apple" + testTab + testDash + testTab + testDash + testNewline +
				testDash + testTab + "Banana" + testTab +testDash + testNewline +
				testDash + testTab + testDash + testTab + "Melon" + testNewline +
				testDash + testTab + testDash + testTab + "Orange";

		assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
		assertEquals(commTool.getStatusCode(), 0);	
	}
}
