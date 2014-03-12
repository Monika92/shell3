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

	File fileA,fileB,fileC,fileD,fileEM1,fileEM2;
	String fileContentA,fileContentB,fileContentC,fileContentD,fileContentE;

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

		fileA = new File("a.txt");
		fileB = new File("b.txt");
		fileC = new File("c.txt");
		fileD = new File("d.txt");

		fileEM1 = new File("em1.txt");
		fileEM2 = new File("em2.txt");

		fileEM1.createNewFile();
		fileEM2.createNewFile();

		fileContentA = "Table\nChair\nMan";
		fileContentB = "Wall\nFloor";
		fileContentC = "Superman\nSpiderman\nBatman";
		fileContentD = "Cat";
		fileContentE = "";

		writeToFile(fileA, fileContentA);
		writeToFile(fileB, fileContentB);
		writeToFile(fileC, fileContentC);
		writeToFile(fileD,fileContentD);

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

		fileA.delete();
		fileB.delete();
		fileC.delete();
		fileD.delete();
		fileEM1.delete();
		fileEM2.delete();
	}

	@Test
	//paste with stdin - valid case
	public void pasteTestValidWithFileAndStdin(){
		String[] arguments = new String[]{"a.txt","-"};
		pasteTool = new PASTETool(arguments);
		actualOutput = pasteTool.execute(workingDirectory, "This is stdin");
		expectedOutput = "Table\nChair\nMan" + "\nThis is stdin";
		assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
		assertEquals(pasteTool.getStatusCode(), 0);	
	}

	@Test
	//Check for invalid command : "paste" and no stdin
	public void pasteTestInvalidCommands1(){
		String[] arguments = new String[]{"-help"};
		pasteTool = new PASTETool(arguments);
		actualOutput = pasteTool.execute(workingDirectory, null);
		expectedOutput = helpOutput;

		assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
		assertEquals(pasteTool.getStatusCode(), 0);	
	}

	@Test
	//Check for invalid command: "paste -s" 
	public void pasteTestInvalidCommands2(){
		String[] arguments = new String[]{"-s"};
		pasteTool = new PASTETool(arguments);
		actualOutput = pasteTool.execute(workingDirectory, null);
		expectedOutput = "";
		assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
		assertNotEquals(pasteTool.getStatusCode(), 0);	
	}

	@Test
	//Check for invalid command: paste filename -s
	public void pasteTestInvalidCommands3(){
		String[] arguments = new String[]{"fname","-s"};
		pasteTool = new PASTETool(arguments);
		actualOutput = pasteTool.execute(workingDirectory, null);
		expectedOutput = "";
		assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
		assertNotEquals(pasteTool.getStatusCode(), 0);	
	}

	@Test
	//Check for invalid option: paste -s -k fname
	public void pasteTestInvalidCommands4(){
		String[] arguments = new String[]{"-s","-k","fname"};		
		pasteTool = new PASTETool(arguments);
		actualOutput = pasteTool.execute(workingDirectory, null);
		expectedOutput = "";
		assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
		assertNotEquals(pasteTool.getStatusCode(), 0);	
	}

	@Test
	//Positive case - valid command: paste -s -d fname
	public void pasteTestValidCommand5(){
		String[] arguments = new String[]{"-s","-d",":","em1.txt"};		
		pasteTool = new PASTETool(arguments);
		actualOutput = pasteTool.execute(workingDirectory, null);
		expectedOutput = "";
		assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
		assertEquals(pasteTool.getStatusCode(), 0);	
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
		String[] arguments = new String[]{"-s","-help","-d",":","a.txt"};
		pasteTool = new PASTETool(arguments);
		actualOutput = pasteTool.execute(workingDirectory, null);
		expectedOutput = helpOutput;
		assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
		assertEquals(pasteTool.getStatusCode(), 0);			

	}

	@Test
	//Check for invalid files
	public void pasteNoOptionsInvalidFilesTest(){
		String[] arguments = new String[]{"C:\\Users\\Dale\\a.txt","./b.txt"};
		String fileName1 = "C:\\Users\\Dale\\a.txt";
		String fileName2 = "./b.txt";
		ArrayList<String> fNames = new ArrayList<String>();
		fNames.add(fileName1);fNames.add(fileName2);

		pasteTool = new PASTETool(arguments);		
		actualOutput = pasteTool.execute(workingDirectory, null);

		expectedOutput = "a.txt: No such file or directory!";
		assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));	

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
		expectedOutput = fileContentB;
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

	@Test
	//Test interface methods with null filename inputs
	public void pasteFileNameNull(){
		String[] toolArguments = new String[]{"-s","-d",":","a.txt"};
		pasteTool = new PASTETool(toolArguments);

		String[] arguments = new String[]{"a.txt",null};
		actualOutput = pasteTool.pasteSerial(arguments);
		expectedOutput = "Table\tChair\tMan\nfile error!";
		assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
		assertNotEquals(pasteTool.getStatusCode(), 0);	
	}

	@Test
	//Test interface methods with null filename inputs
	public void pasteFileNameNull2(){
		String[] toolArguments = new String[]{"-s","-d",":","a.txt"};
		pasteTool = new PASTETool(toolArguments);

		String[] arguments = new String[]{"a.txt",null};
		actualOutput = pasteTool.pasteUseDelimiter(":",arguments);
		expectedOutput = "file error!";
		assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
		assertNotEquals(pasteTool.getStatusCode(), 0);	
	}

	@Test
	//Test interface method "pasteUseDelimiter" with null delim
	public void pasteDelimNull(){
		String[] toolArguments = new String[]{"-s","-d",":","a.txt"};
		pasteTool = new PASTETool(toolArguments);

		String[] arguments = new String[]{"a.txt","b.txt"};
		actualOutput = pasteTool.pasteUseDelimiter(null,arguments);
		expectedOutput = "";
		assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
		assertNotEquals(pasteTool.getStatusCode(), 0);	
	}

	@Test
	//Test Paste execute method with null in args
	public void pasteArgumentsContailNull(){
		String[] arguments = new String[]{"-s","-d",":",null};
		pasteTool = new PASTETool(arguments);
		actualOutput = pasteTool.execute(workingDirectory, null);
		expectedOutput = "";
		assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
		assertNotEquals(pasteTool.getStatusCode(), 0);	
	}
	
	@Test
	//Test Paste execute method with null in args
	public void pasteArgumentsAreNull(){
		String[] arguments = null;
		pasteTool = new PASTETool(arguments);
		actualOutput = pasteTool.execute(workingDirectory, null);
		expectedOutput = "";
		assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
		assertNotEquals(pasteTool.getStatusCode(), 0);	
	}
}
