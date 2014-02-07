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

import sg.edu.nus.comp.cs4218.extended2.IPasteTool;
import sg.edu.nus.comp.cs4218.impl.extended2.PASTETool;

public class PASTEToolTest {

	private IPasteTool pasteTool;
	String actualOutput,expectedOutput,helpOutput;
	File workingDirectory;

	File file_a,file_b,file_c,file_d,file_em1,file_em2;
	String fileContent_a,fileContent_b,fileContent_c,fileContent_d,fileContent_e;

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
		file_em1 = new File("em1.txt");
		file_em2 = new File("em2.txt");
		
		file_em1.createNewFile();
		file_em2.createNewFile();
		
		fileContent_a = "Table\nChair\nMan";
		fileContent_b = "Wall\nFloor";
		fileContent_c = "Superman\nSpiderman\nBatman";
		fileContent_d = "Cat";
		fileContent_e = "";
		
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
		file_em1.delete();
		file_em2.delete();
	}

	@Test
	//If only "-help", print help message
	public void pasteGetHelpAsOnlyArgumentTest() {
		String[] arguments = new String[]{"-help"};
		pasteTool = new PASTETool(arguments);
		actualOutput = pasteTool.execute(workingDirectory, null);
		expectedOutput = helpOutput;
		assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
		assertEquals(pasteTool.getStatusCode(), 0);		
	}

	@Test
	//Test -help is given priority
	public void pasteGetHelpWithOtherArgumentsTest() {
		String[] arguments = new String[]{"-s","-help","-d",":"};
		pasteTool = new PASTETool(arguments);
		actualOutput = pasteTool.execute(workingDirectory, null);
		expectedOutput = helpOutput;
		assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
		assertEquals(pasteTool.getStatusCode(), 0);			

	}
	
	@Test
	//Check for invalid files
	public void pasteNoOptionsInvalidFilesTest(){
		String fileName1 = "C:\\Users\\Dale\\a.txt";
		String fileName2 = "./b.txt";
		ArrayList<String> fNames = new ArrayList<String>();
		fNames.add(fileName1);fNames.add(fileName2);
		
		//pasteTool = new PASTETool(arguments);		
		//actualOutput = "";
		//TODO:
		
		expectedOutput = "a.txt : No such file or directory!";
		assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));	
		fail("not yet implemented");
	}

	@Test
	//Check for empty file
	public void pasteNoOptionsCheckWithOneEmptyFileTest(){
		String[] arguments = new String[]{"em1.txt"};
		pasteTool = new PASTETool(arguments);
		actualOutput = pasteTool.pasteUseDelimiter("\t", arguments);
		
		expectedOutput = "";
		assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
		assertEquals(pasteTool.getStatusCode(), 0);	
		
	}
	
	@Test
	//Check for empty file when between multiple non empty files
	public void pasteUseDelimWithOneEmptyInManyFilesTest(){
		String[] arguments = new String[]{"b.txt","em1.txt","d.txt"};
		pasteTool = new PASTETool(arguments);
		actualOutput = pasteTool.pasteUseDelimiter("*", arguments);
		
		expectedOutput = "Wall**Cat" + "\n" + "Floor**";
		assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
		assertEquals(pasteTool.getStatusCode(), 0);	
	}
	
	@Test
	//Serial output using empty files
	public void pasteUseSerialWithManyEmptyFilesTest(){
		String[] arguments = new String[]{"em1.txt","em2.txt"};
		pasteTool = new PASTETool(arguments);
		actualOutput = pasteTool.pasteSerial(arguments);
		
		expectedOutput = "\n";
		assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
		assertEquals(pasteTool.getStatusCode(), 0);	
	}
	
	@Test
	//Check paste o/p with 1 file, no options
	public void pasteNoOptionsOneFileTest(){
		String[] arguments = new String[]{"b.txt"};
		pasteTool = new PASTETool(arguments);
		actualOutput = pasteTool.pasteUseDelimiter("\t", arguments);
		expectedOutput = fileContent_b;
		assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
		assertEquals(pasteTool.getStatusCode(), 0);	
	}

	@Test
	//Testing paste with Stdin
	public void pasteNoOptionsStdinTest(){
		String[] arguments = new String[]{"-"};
		String stdin = "stdin input";
		pasteTool = new PASTETool(arguments);
		actualOutput = pasteTool.execute(workingDirectory, stdin);
		expectedOutput = "stdin input";
		assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
		assertEquals(pasteTool.getStatusCode(), 0);	
	}

	@Test
	//Paste: no options, many files
	public void pasteNoOptionsManyFilesTest(){
		String[] arguments = new String[]{"a.txt","b.txt","c.txt"};
		pasteTool = new PASTETool(arguments);
		actualOutput = pasteTool.pasteUseDelimiter("\t", arguments);
		String empty = "";
		expectedOutput = "Table\tWall\tSuperman" + "\n" + "Chair\tFloor\tSpiderman" + "\n" + "Man\t"+empty+"\tBatman";
		assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
		assertEquals(pasteTool.getStatusCode(), 0);	
	}


	@Test
	//case where numDelim = 1 for 1 file
	public void pasteUseDelimiterOneFileTest1(){
		String[] arguments = new String[]{"a.txt"};
		pasteTool = new PASTETool(arguments);
		actualOutput = pasteTool.pasteUseDelimiter(":", arguments);
		expectedOutput = "Table" + "\n" + "Chair" + "\n" + "Man";
		assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
		assertEquals(pasteTool.getStatusCode(), 0);	
	}

	@Test
	//case where numDelim > 1 for 1 file
	public void pasteUseDelimiterOneFileTest2(){
		String[] arguments = new String[]{"a.txt"};
		pasteTool = new PASTETool(arguments);
		actualOutput = pasteTool.pasteUseDelimiter(":&", arguments);
		expectedOutput = "Table" + "\n" + "Chair" + "\n" + "Man";
		assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
		assertEquals(pasteTool.getStatusCode(), 0);	
	}

	@Test
	//Delimiter Test with Stdin
	public void pasteUseDelimiterStdinTest(){
		String[] arguments = new String[]{"-d",":","-"};
		String stdin = "Stdin input";
		pasteTool = new PASTETool(arguments);
		actualOutput = pasteTool.execute(workingDirectory, stdin);
		expectedOutput = stdin;
		assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
		assertEquals(pasteTool.getStatusCode(), 0);	
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
	//Paste: Serial with 1 file
	public void pasteSerialOneFileTest(){
		String[] arguments = new String[]{"a.txt"};
		pasteTool = new PASTETool(arguments);
		actualOutput = pasteTool.pasteSerial(arguments);
		expectedOutput = "Table\tChair\tMan";
		assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
		assertEquals(pasteTool.getStatusCode(), 0);	
	}

	@Test
	//Paste serial with Stdin
	public void pasteSerialStdinTest(){
		String[] arguments = new String[]{"-s","-"};
		String stdin = "Stdin input";
		pasteTool = new PASTETool(arguments);
		actualOutput = pasteTool.execute(workingDirectory, stdin);
		expectedOutput = stdin;
		assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
		assertEquals(pasteTool.getStatusCode(), 0);	
	}

	@Test
	//Serial many files
	public void pasteSerialManyFilesTest(){
		String[] arguments = new String[]{"a.txt","b.txt","d.txt"};
		pasteTool = new PASTETool(arguments);
		actualOutput = pasteTool.pasteSerial(arguments);
		expectedOutput = "Table\tChair\tMan" + "\n" + "Wall\tFloor" + "\n" + "Cat";
		assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
		assertEquals(pasteTool.getStatusCode(), 0);	
	}	
	
	@Test
	//if multiple delim, choose last option and args
	public void pasteUseDelimMultipleDelimsTest(){
		String[] arguments = new String[]{"-d",":","-d","*","a.txt","b.txt"};
		pasteTool = new PASTETool(arguments);
		actualOutput = pasteTool.execute(workingDirectory, null);
		expectedOutput = "Table*Wall" + "\n" + "Chair*Floor" + "\n" + "Man*" + "";
		assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
		assertEquals(pasteTool.getStatusCode(), 0);	
	}
	
	@Test
	//should choose -s over -d
	public void pasteOptionsPriorityCheckTest(){
		String[] arguments = new String[]{"-s","-d",":","a.txt"};
		pasteTool = new PASTETool(arguments);
		actualOutput = pasteTool.execute(workingDirectory, null);
		expectedOutput = "Table\tChair\tMan";
		assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
		assertEquals(pasteTool.getStatusCode(), 0);	
	}

}
