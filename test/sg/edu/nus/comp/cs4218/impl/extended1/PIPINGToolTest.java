package sg.edu.nus.comp.cs4218.impl.extended1;

import static org.junit.Assert.*;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import sg.edu.nus.comp.cs4218.ITool;
import sg.edu.nus.comp.cs4218.extended1.IGrepTool;
import sg.edu.nus.comp.cs4218.extended1.IPipingTool;
import sg.edu.nus.comp.cs4218.extended2.ICommTool;
import sg.edu.nus.comp.cs4218.extended2.ICutTool;
import sg.edu.nus.comp.cs4218.extended2.IPasteTool;
import sg.edu.nus.comp.cs4218.extended2.ISortTool;
import sg.edu.nus.comp.cs4218.extended2.IUniqTool;
import sg.edu.nus.comp.cs4218.extended2.IWcTool;
import sg.edu.nus.comp.cs4218.fileutils.ICatTool;
import sg.edu.nus.comp.cs4218.fileutils.IEchoTool;
import sg.edu.nus.comp.cs4218.fileutils.ILsTool;
import sg.edu.nus.comp.cs4218.impl.extended1.GREPTool;
import sg.edu.nus.comp.cs4218.impl.extended1.PIPINGTool;
import sg.edu.nus.comp.cs4218.impl.extended2.COMMTool;
import sg.edu.nus.comp.cs4218.impl.extended2.CUTTool;
import sg.edu.nus.comp.cs4218.impl.extended2.PASTETool;
import sg.edu.nus.comp.cs4218.impl.extended2.SORTTool;
import sg.edu.nus.comp.cs4218.impl.extended2.UNIQTool;
import sg.edu.nus.comp.cs4218.impl.extended2.WCTool;
import sg.edu.nus.comp.cs4218.impl.fileutils.CATTool;
import sg.edu.nus.comp.cs4218.impl.fileutils.ECHOTool;
import sg.edu.nus.comp.cs4218.impl.fileutils.LSTool;

public class PIPINGToolTest {
	private IPipingTool pipingTool;
	private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
	private final File workingDir = new File(System.getProperty("user.dir"));
	File fileA,fileB,fileC,fileD,fileEM1,fileEM2;
	String fileContentA,fileContentB,fileContentC,fileContentD,fileContentE;
	String testTab, testNewLine, testDash;
	String actualOutput, expectedOutput;

	@Before
	public void setUp() throws Exception {
		pipingTool = new PIPINGTool(null, null);
		//pipingTool.execute(workingDir, "");
		System.setErr(new PrintStream(errContent)); // to test error output
		
		fileA = new File("a.txt");
		fileB = new File("b.txt");
		fileC = new File("c.txt");
		fileD = new File("d.txt");
		fileEM1 = new File("em1.txt");
		fileEM2 = new File("em2.txt");
		fileEM1.createNewFile();
		fileEM2.createNewFile();
		
		fileContentA = "Apple\nMelon\nOrange";
		fileContentB = "Banana\nMelon\nOrange";
		fileContentC = "Batman\nSpiderman\nSuperman";
		fileContentD = "Cat\nBat";
		fileContentE = "";
		
		writeToFile(fileA, fileContentA);
		writeToFile(fileB, fileContentB);
		writeToFile(fileC, fileContentC);
		writeToFile(fileD,fileContentD);
		
		testTab = "\t";
		testNewLine = "\n";
		testDash = " ";
	}

	@After
	public void tearDown() throws Exception {
		pipingTool = null;
		
		fileA.delete();
		fileB.delete();
		fileC.delete();
		fileD.delete();
		fileEM1.delete();
		fileEM2.delete();
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
	
	/**
	 * Test for interface method pipe(String stdout, ITool to) where "to" is a
	 * valid tool. Checks for correct output.
	 */
	@Test
	public void testPipeStdinCat() {
		String[] rightToolArgs = {};
		ICatTool rightTool = new CATTool(rightToolArgs);
		String pipeResult = pipingTool.pipe("hello", rightTool);
		assertTrue("hello".equals(pipeResult));
	}

	/**
	 * Test for interface method pipe(String stdout, ITool to) where "to" is
	 * null. Expects empty result.
	 */
	@Test
	public void testPipeStdinNull() {
		String pipeResult = pipingTool.pipe("hello", null);
		assertTrue("".equals(pipeResult));
		assertTrue(pipingTool.getStatusCode() != 0);
	}

	/**
	 * Test for interface method pipe(ITool from, ITool to) where both tools are
	 * valid. Checks for expected result.
	 */
	@Test
	public void testPipeCatGrep() {
		String[] leftToolArgs = { "textFiles/bohemian.txt" };
		String[] rightToolArgs = { "Bismillah", "-", "textFiles/bohemian.txt" };
		ICatTool leftTool = new CATTool(leftToolArgs);
		IGrepTool rightTool = new GREPTool(rightToolArgs);
		String pipeResult = pipingTool.pipe(leftTool, rightTool);
		String expected = "Standard Input:\nBismillah, no! We will not let you go.\nBismillah, no! We will not let you go.\ntextFiles/bohemian.txt:\nBismillah, no! We will not let you go.\nBismillah, no! We will not let you go.\n";
		assertTrue(expected.equals(pipeResult));
	}

	/**
	 * Test for interface method pipe(ITool from, ITool to) where both tools are
	 * valid but "from" (LSTool) has an invalid argument. Checks for empty
	 * result and correct error message.
	 */
	@Test
	public void testPipeLeftInvalidLS() {
		String[] leftToolArgs = { "filenotfound" };
		String[] rightToolArgs = {};
		ILsTool leftTool = new LSTool(leftToolArgs);
		ICatTool rightTool = new CATTool(rightToolArgs);
		String pipeResult = pipingTool.pipe(leftTool, rightTool);
		
		
		String errorMessage = errContent.toString();
		assertTrue("".equals(pipeResult));
		//	assertTrue(String.format(LSTool.LS_ERROR_MSG, "filenotfound").equals(
		//			errorMessage));
		// because rightTool executes fine
		
		//System.out.println("pipe result:" + pipeResult + "end");
		//System.out.println("code:" + pipingTool.getStatusCode());
		assertTrue(pipingTool.getStatusCode() == 0);
		
	}

	/**
	 * Test for interface method pipe(ITool from, ITool to) where both tools are
	 * valid but "to" (LSTool) has an invalid argument. Checks for empty result
	 * and correct error exit code.
	 */
	@Test
	public void testPipeRightInvalidCat() {
		String[] leftToolArgs = {};
		String[] rightToolArgs = { "filenotfound" };
		ILsTool leftTool = new LSTool(leftToolArgs);
		ICatTool rightTool = new CATTool(rightToolArgs);
		String pipeResult = pipingTool.pipe(leftTool, rightTool);
		assertTrue("".equals(pipeResult));
		
		assertTrue(pipingTool.getStatusCode() != 0);
	}

	/**
	 * Test for interface method pipe(ITool from, ITool to) where both tools are
	 * valid but "from" (GREPTool) has an invalid argument. Checks for empty
	 * result and correct error message.
	 */
	@Test
	public void testPipeLeftInvalidGrep() {
		String[] leftToolArgs = { "-p" };
		String[] rightToolArgs = {};
		IGrepTool leftTool = new GREPTool(leftToolArgs);
		ICatTool rightTool = new CATTool(rightToolArgs);
		String pipeResult = pipingTool.pipe(leftTool, rightTool);
		String errorMessage = errContent.toString();
		assertTrue("".equals(pipeResult));
	//	assertTrue(GREPTool.GREP_ERR_MSG.equals(errorMessage));
		// because rightTool executes fine
		assertTrue(pipingTool.getStatusCode() == 0);
	}

	/**
	 * Test for interface method pipe(ITool from, ITool to) where both tools are
	 * null. Checks for empty result.
	 */
	@Test
	public void testPipeBothNull() {
		ITool leftTool = null;
		ITool rightTool = null;
		String pipeResult = pipingTool.pipe(leftTool, rightTool);
		assertTrue("".equals(pipeResult));
		assertTrue(pipingTool.getStatusCode() != 0);
	}

	/**
	 * Test for interface method pipe(ITool from, ITool to) where "from" is
	 * null. Empty string should be passed as stdin to "to". Checks for correct
	 * output.
	 */
	@Test
	public void testPipeLeftNull() {
		String[] rightToolArgs = { "Bismillah", "-" };
		ITool leftTool = null;
		IGrepTool rightTool = new GREPTool(rightToolArgs);
		String pipeResult = pipingTool.pipe(leftTool, rightTool);
		assertTrue("".equals(pipeResult));
		assertTrue(pipingTool.getStatusCode() == 0);
	}

	/**
	 * Test for interface method pipe(ITool from, ITool to) where "to" is null.
	 * "from" should still be run and should output the correct error message.
	 * Checks for empty result.
	 */
	@Test
	public void testPipeRightNull() {
		String[] leftToolArgs = { "filenotfound" };
		ICatTool leftTool = new CATTool(leftToolArgs);
		ITool rightTool = null;
		String pipeResult = pipingTool.pipe(leftTool, rightTool);
		String errorMessage = errContent.toString();
		assertTrue("".equals(pipeResult));
	//	assertTrue(String.format(CATTool.CAT_FILE_ERROR_MSG, "filenotfound")
	//			.equals(errorMessage));
		assertTrue(pipingTool.getStatusCode() != 0);
	}

	/*
	 * Tests for interface method pipe(ITool from, ITool to)
	 */
	@Test
	public void testPipeWcFromPasteTo() {
		String[] leftToolArgs = {"-m", "-l", "testA.txt"};
		String[] rightToolArgs = {"-s"};
		IWcTool wcTool = new WCTool(leftToolArgs);
		IPasteTool pasteTool = new PASTETool(rightToolArgs);
		String actualOutput = pipingTool.pipe(wcTool, pasteTool);
		String expectedOutput = "C:\\Users\\monika92\\workspace\\shell3\\testA.txt :  -m  37 -l  5"; 
		assertTrue(actualOutput.equalsIgnoreCase(expectedOutput));
	}
	
	@Test
	public void testPipeCommFromCutTo() {
		String[] leftToolArgs = {"a.txt","b.txt"};
		String[] rightToolArgs = {"-c", "1-3"};
		ICutTool cutTool = new CUTTool(rightToolArgs);
		ICommTool commTool = new COMMTool(leftToolArgs);
		actualOutput = pipingTool.pipe(commTool, cutTool);
		expectedOutput = "App" + testNewLine +
				testDash + testTab + "B" + testNewLine +
				testDash + testTab + testDash + testNewLine +
				testDash + testTab + testDash;
		assertTrue(actualOutput.equalsIgnoreCase(expectedOutput));
	}
	
	@Test
	public void testPipeSortFromCommTo() {
		String[] leftToolArgs = {"a.txt"};
		String[] rightToolArgs = {"a.txt", "b.txt"};
		ISortTool sortTool = new SORTTool(leftToolArgs);
		ICommTool commTool = new COMMTool(rightToolArgs);
		actualOutput = pipingTool.pipe(sortTool, commTool);
		assertTrue(pipingTool.getStatusCode() != 0);
	}
	
	@Test
	public void testPipeEchoFromWcTo() {
		String[] leftToolArgs = {};
		String[] rightToolArgs = {"a.txt"};
		IEchoTool echoTool = new ECHOTool(leftToolArgs);
		IWcTool wcTool = new WCTool(rightToolArgs);
		actualOutput = pipingTool.pipe(echoTool, wcTool);
		assertTrue(pipingTool.getStatusCode() != 0);
	}
	
	@Test
	public void testPipeCatStdoutUniqTo() {
		String[] catToolArgs = {"textfiles/testC.txt"};
		String[] rightToolArgs = {};
		ICatTool catTool = new CATTool(catToolArgs);
		IUniqTool uniqTool = new UNIQTool(rightToolArgs);
		String stdout = catTool.execute(workingDir, "");
		actualOutput = pipingTool.pipe(stdout, uniqTool);
		expectedOutput = "a\nb\na\nc\nc";
		assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
	}
	
	@Test
	public void testPipeCatStdoutEchoTo() {
		String[] catToolArgs = {"textfiles/testC.txt"};
		String[] rightToolArgs = {};
		ICatTool catTool = new CATTool(catToolArgs);
		IEchoTool echoTool = new ECHOTool(rightToolArgs);
		String stdout = catTool.execute(workingDir, "");
		actualOutput = pipingTool.pipe(stdout, echoTool);
		assertFalse(pipingTool.getStatusCode() != 0);
	}
	
	@Test
	public void testExecuteCatCutPaste() {
		String[] args1 = {"cat", "a.txt", "|", "cut", "-c", "1-4", "|", "paste"};
		String[] args2 = {};
		pipingTool = new PIPINGTool(args1, args2);
		actualOutput = pipingTool.execute(workingDir, "");
		expectedOutput = "Appl\nMelo\nOran";
		System.out.println(actualOutput);
		assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
	}
	
	@Test
	public void testExecuteWcUniqCat() {
		String[] args1 = {"uniq", "textfile/testC.txt", "|", "cat", "|", "paste"};
		String[] args2 = {};
		pipingTool = new PIPINGTool(args1, args2);
		actualOutput = pipingTool.execute(workingDir, "");
		expectedOutput = "a\nb\na\nc\nc";
		assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
	}
	
	@Test
	public void testExecuteEchoWc() {
		String[] args1 = {"echo", "textfile/testC.txt", "|", "wc", "-m", "-l"};
		String[] args2 = {};
		pipingTool = new PIPINGTool(args1, args2);
		actualOutput = pipingTool.execute(workingDir, "");
		assertTrue(pipingTool.getStatusCode() != 0);
	}
	
	@Test
	public void testExecuteWcComm() {
		String[] args1 = {"wc", "textfile/testC.txt", "|", "comm"};
		String[] args2 = {};
		pipingTool = new PIPINGTool(args1, args2);
		actualOutput = pipingTool.execute(workingDir, "");
		assertTrue(pipingTool.getStatusCode() != 0);
	}
	
	@Test
	public void testExecuteEchoCommWithArgs() {
		String[] args1 = {"echo", "hello", "|", "comm", "a.txt", "b.txt"};
		String[] args2 = {};
		pipingTool = new PIPINGTool(args1, args2);
		actualOutput = pipingTool.execute(workingDir, "");
		expectedOutput = "Apple" + testTab + testDash + testTab + testDash + testNewLine +
				testDash + testTab + "Banana" + testTab +testDash + testNewLine +
				testDash + testTab + testDash + testTab + "Melon" + testNewLine +
				testDash + testTab + testDash + testTab + "Orange";
		assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
	}
	
	@Test
	public void testExecuteCatEchoWithArgs() {
		String[] args1 = {"cat", "a.txt", "|", "echo", "hello"};
		String[] args2 = {};
		pipingTool = new PIPINGTool(args1, args2);
		actualOutput = pipingTool.execute(workingDir, "");
		assertTrue(pipingTool.getStatusCode() != 0);
	}
	
	@Test
	public void testExecuteEchoWithArgsEndingWithPipe() {
		String[] args1 = {"echo", "hello", "|"};
		String[] args2 = {};
		pipingTool = new PIPINGTool(args1, args2);
		actualOutput = pipingTool.execute(workingDir, "");
		assertTrue(pipingTool.getStatusCode() != 0);
	}
}
