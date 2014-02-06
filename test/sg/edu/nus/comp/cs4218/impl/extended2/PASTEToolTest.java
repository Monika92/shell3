package sg.edu.nus.comp.cs4218.impl.extended2;

import static org.junit.Assert.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import sg.edu.nus.comp.cs4218.extended2.IPasteTool;
import sg.edu.nus.comp.cs4218.impl.extended2.PASTETool;

public class PASTEToolTest {

	private IPasteTool pasteTool;
	String actualOutput,expectedOutput,helpOutput;
	File workingDirectory;

	File file_a,file_b,file_c,file_d;
	String fileContent_a,fileContent_b,fileContent_c,fileContent_d;

	@Before
	public void before() throws Exception {
		workingDirectory = new File(System.getProperty("user.dir"));
		helpOutput = "paste : writes to standard output lines "
				+ "\n* of sequentially corresponding lines of each given file,"
				+ "\n* separated by a TAB character"
				+ "\n* Command Format - paste [OPTIONS] [FILE]"
				+ "\n* FILE - Name of the file, when no file is present (denoted by \"-\") "
				+ "\n* use standard input OPTIONS"
				+ "\n -s : paste one file at a time instead of in parallel"
				+ "\n -d DELIM: Use characters from the DELIM instead of TAB character"
				+ "\n -help : Brief information about supported options";

		file_a = new File("a.txt");
		file_b = new File("b.txt");
		file_c = new File("c.txt");
		file_d = new File("d.txt");

		fileContent_a = "Table\nChair\nMan";
		fileContent_b = "Wall\nFloor";
		fileContent_c = "Superman\nSpiderman\nBatman";
		fileContent_d = "Cat";
		
		writeToFile(file_a, fileContent_a);
		writeToFile(file_b, fileContent_b);
		writeToFile(file_c, fileContent_c);
		writeToFile(file_d,fileContent_d);
		
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
		pasteTool = null;
		file_a.delete();
		file_b.delete();
		file_c.delete();
		file_d.delete();
	}

	@Test
	public void pasteGetHelpAsOnlyArgumentTest() {
		String[] arguments = new String[]{"-help"};
		pasteTool = new PASTETool(arguments);
		actualOutput = pasteTool.execute(workingDirectory, null);
		expectedOutput = helpOutput;
		assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
		assertEquals(pasteTool.getStatusCode(), 0);		
	}

	@Test
	public void pasteGetHelpWithOtherArgumentsTest() {
		String[] arguments = new String[]{"-s","-help","-d",":"};
		pasteTool = new PASTETool(arguments);
		actualOutput = pasteTool.execute(workingDirectory, null);
		expectedOutput = helpOutput;
		assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
		assertEquals(pasteTool.getStatusCode(), 0);			

	}
	
	@Test
	public void pasteNoOptionsInvalidFilesTest(){
		fail("not yet implemented");
	}
	
	@Test
	public void pasteNoOptionsCheckFilenameWithSpecialChar(){
		fail("not yet implemented");
	}
	
	@Test
	public void pasteNoOptionsCheckWithOneEmptyFileTest(){
		fail("not yet implemented");
	}
	
	@Test
	public void pasteUseDelimWithOneEmptyInManyFilesTest(){
		fail("not yet implemented");
	}
	
	@Test
	public void pasteUseDelimWithManyEmptyFilesTest(){
		fail("not yet implemented");
	}
	
	@Test
	public void pasteUseSerialWithManyEmptyFilesTest(){
		fail("not yet implemented");
	}
	
	@Test
	public void pasteNoOptionsOneFileTest(){
		String[] arguments = new String[]{"b.txt"};
		pasteTool = new PASTETool(arguments);
		actualOutput = pasteTool.pasteUseDelimiter("\t", arguments);
		expectedOutput = fileContent_b;
		assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
		assertEquals(pasteTool.getStatusCode(), 0);	
	}

	@Test
	public void pasteNoOptionsStdinTest(){
		fail("not yet implemented");
	}

	@Test
	public void pasteNoOptionsManyFilesTest(){
		String[] arguments = new String[]{"a.txt","b.txt","c.txt"};
		pasteTool = new PASTETool(arguments);
		actualOutput = pasteTool.pasteUseDelimiter("\t", arguments);
		String empty = "";
		expectedOutput = "Table\tWall\tSuperman" + "\n" + "Chair\tFloor\tSpiderman" + "\n" + "Man\t"+empty+"\tBatman";
		assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
		assertEquals(pasteTool.getStatusCode(), 0);	
	}


	//case where numDelim = 1 for 1 file
	@Test
	public void pasteUseDelimiterOneFileTest1(){
		String[] arguments = new String[]{"a.txt"};
		pasteTool = new PASTETool(arguments);
		actualOutput = pasteTool.pasteUseDelimiter(":", arguments);
		expectedOutput = "Table" + "\n" + "Chair" + "\n" + "Man";
		assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
		assertEquals(pasteTool.getStatusCode(), 0);	
	}

	//case where numDelim > 1 for 1 file
	@Test
	public void pasteUseDelimiterOneFileTest2(){
		String[] arguments = new String[]{"a.txt"};
		pasteTool = new PASTETool(arguments);
		actualOutput = pasteTool.pasteUseDelimiter(":&", arguments);
		expectedOutput = "Table" + "\n" + "Chair" + "\n" + "Man";
		assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
		assertEquals(pasteTool.getStatusCode(), 0);	
	}

	@Test
	public void pasteUseDelimiterStdinTest(){
		fail("not yet implemented");
	}

	@Test
	//case where numDelim = n - 1 for n files
	public void pasteUseDelimiterManyFilesTest1(){
		String[] arguments = new String[]{"a.txt","b.txt","c.txt"};
		pasteTool = new PASTETool(arguments);
		actualOutput = pasteTool.pasteUseDelimiter(":&", arguments);
		String empty = "";
		expectedOutput = "Table:Wall&Superman" + "\n" + "Chair:Floor&Spiderman" + "\n" + "Man:"+empty+"&Batman";
		assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
		assertEquals(pasteTool.getStatusCode(), 0);	
	}

	@Test
	//case where 1 delim - many files
	public void pasteUseDelimiterManyFilesTest2(){
		String[] arguments = new String[]{"a.txt","b.txt","c.txt"};
		pasteTool = new PASTETool(arguments);
		actualOutput = pasteTool.pasteUseDelimiter(":", arguments);
		String empty = "";
		expectedOutput = "Table:Wall:Superman" + "\n" + "Chair:Floor:Spiderman" + "\n" + "Man:"+empty+":Batman";
		assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
		assertEquals(pasteTool.getStatusCode(), 0);	
	}

	@Test
	//case where numDelim < n - 1 and many files
	public void pasteUseDelimiterManyFilesTest2_1(){
		String[] arguments = new String[]{"a.txt","b.txt","c.txt","d.txt"};
		pasteTool = new PASTETool(arguments);
		actualOutput = pasteTool.pasteUseDelimiter(":&", arguments);
		String empty = "";
		expectedOutput = "Table:Wall&Superman:Cat" + "\n" + ("Chair:Floor&Spiderman:" + empty) 
				+ "\n" + "Man:"+empty+"&Batman:"+empty;
		assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
		assertEquals(pasteTool.getStatusCode(), 0);	
	}

	@Test
	//case where numDelim > n - 1
	public void pasteUseDelimiterManyFilesTest3(){
		String[] arguments = new String[]{"a.txt","b.txt"};
		pasteTool = new PASTETool(arguments);
		actualOutput = pasteTool.pasteUseDelimiter(":&", arguments);
		String empty = "";
		expectedOutput = "Table:Wall" + "\n" + "Chair:Floor" + "\n" + "Man:"+empty;

		assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
		assertEquals(pasteTool.getStatusCode(), 0);	
	}

	@Test
	public void pasteSerialOneFileTest(){
		String[] arguments = new String[]{"a.txt"};
		pasteTool = new PASTETool(arguments);
		actualOutput = pasteTool.pasteSerial(arguments);
		expectedOutput = "Table\tChair\tMan";
		assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
		assertEquals(pasteTool.getStatusCode(), 0);	
	}

	@Test
	public void pasteSerialStdinTest(){
		fail("not yet implemented");
	}

	@Test
	public void pasteSerialManyFilesTest(){
		String[] arguments = new String[]{"a.txt","b.txt","d.txt"};
		pasteTool = new PASTETool(arguments);
		actualOutput = pasteTool.pasteSerial(arguments);
		expectedOutput = "Table\tChair\tMan" + "\n" + "Wall\tFloor" + "\n" + "Cat";
		assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
		assertEquals(pasteTool.getStatusCode(), 0);	
	}	
	
	
	//if multiple delim, choose last option and args
	@Test
	public void pasteUseDelimMultipleDelimsTest(){
		String[] arguments = new String[]{"-d",":","-d","*","a.txt","b.txt"};
		pasteTool = new PASTETool(arguments);
		actualOutput = pasteTool.execute(workingDirectory, null);
		expectedOutput = "Table*Wall" + "\n" + "Chair*Floor" + "\n" + "Man*" + "";
		assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
		assertEquals(pasteTool.getStatusCode(), 0);	
	}
	
	//should choose -s over -d
	@Test
	public void pasteOptionsPriorityCheckTest(){
		String[] arguments = new String[]{"-s","-d",":","a.txt"};
		pasteTool = new PASTETool(arguments);
		actualOutput = pasteTool.execute(workingDirectory, null);
		expectedOutput = "Table\tChair\tMan";
		assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
		assertEquals(pasteTool.getStatusCode(), 0);	
	}

}
