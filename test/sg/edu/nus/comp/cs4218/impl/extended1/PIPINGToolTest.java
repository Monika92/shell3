/*
 * Assumption while unit testing pipe.
 * When calling pipe(), in case of just command and no arguments
 * the tool needs to be initialized with {-} in arguments
 * 
 *  for example: for "echo hello | cat"
 *  and if pipe(stdout, cat) is called, then CATTool should be initialized
 *  with args={-}
 *  
 *  Above assumption does not apply when calling pipetool's execute function
 * 
 */

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
		pipingTool.execute(workingDir, "");
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
	
	
	//TODO: Check with Swetha for grep for output
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
		String expected = "Standard input: \nBismillah, no! We will not let you go."
				+ "\nBismillah, no! We will not let you go."
				+ "\ntextFiles/bohemian.txt:"
				+ "\nBismillah, no! We will not let you go."
				+ "\nBismillah, no! We will not let you go.\n";
		System.out.println("\nOP:\n" + pipeResult);
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
		String[] rightToolArgs = {"-"};
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
		String[] leftToolArgs = {"-"};
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
		String[] leftToolArgs = {"-m", "-l", "textFiles/testA.txt"};
		String[] rightToolArgs = {"-s","-"};
		IWcTool wcTool = new WCTool(leftToolArgs);
		IPasteTool pasteTool = new PASTETool(rightToolArgs);
		String actualOutput = pipingTool.pipe(wcTool, pasteTool);
		String expectedOutput = workingDir + "\\textFiles\\testA.txt :  -m  37 -l  5\n";
		assertTrue(actualOutput.equalsIgnoreCase(expectedOutput));
	}
	
	//TODO: Swetha removes trailing \n
	/*
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
		System.out.println("Size:" + testDash.length());
		actualOutput.replaceAll("\n", "newline");
		actualOutput.replaceAll("\t", "tab");
		actualOutput.replaceAll(" ", "space");
		System.out.println("op:\n" + actualOutput);
		assertTrue(actualOutput.equalsIgnoreCase(expectedOutput));
	}
	*/
	
	@Test
	public void testPipeSortFromCommTo() {
		String[] leftToolArgs = {"a.txt"};
		String[] rightToolArgs = {"a.txt", "b.txt"};
		ISortTool sortTool = new SORTTool(leftToolArgs);
		ICommTool commTool = new COMMTool(rightToolArgs);
		actualOutput = pipingTool.pipe(sortTool, commTool);
		assertTrue(pipingTool.getStatusCode() != 0);
	}
	
	//TODO: Madhu check 
	/*
	@Test
	public void testPipeEchoFromWcTo() {
		String[] leftToolArgs = {};
		String[] rightToolArgs = {"a.txt"};
		IEchoTool echoTool = new ECHOTool(leftToolArgs);
		IWcTool wcTool = new WCTool(rightToolArgs);
		actualOutput = pipingTool.pipe(echoTool, wcTool);
		assertTrue(pipingTool.getStatusCode() != 0);
	}
	*/
	
	@Test
	public void testPipeCatStdoutUniqTo() {
		String[] catToolArgs = {"textFiles/testC.txt"};
		String[] rightToolArgs = {"-"};
		ICatTool catTool = new CATTool(catToolArgs);
		IUniqTool uniqTool = new UNIQTool(rightToolArgs);
		String stdout = catTool.execute(workingDir, null);
		actualOutput = pipingTool.pipe(stdout, uniqTool);
		System.out.println("OP:" + actualOutput);
		expectedOutput = "a\nb\na\nc";
		assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
	}
	
	/*
	@Test
	public void testPipeCatStdoutEchoTo() {
		String[] catToolArgs = {"a.txt"};
		String[] rightToolArgs = {};
		ICatTool catTool = new CATTool(catToolArgs);
		IEchoTool echoTool = new ECHOTool(rightToolArgs);
		String stdout = catTool.execute(workingDir, null);
		System.out.println(stdout);
		actualOutput = pipingTool.pipe(stdout, echoTool);
		System.out.println("OP:" + actualOutput);
		expectedOutput = "Apple\nMelon\nOrange\n";
		assertTrue(actualOutput.equalsIgnoreCase(expectedOutput));
	}
	
	/*
	//TODO: Check with Swetha about \n for cut
	@Test
	public void testExecuteCatCutPaste() {
		String[] args1 = {"cat", "a.txt", "|", "cut", "-c", "1-4", "|", "paste"};
		String[] args2 = {};
		pipingTool = new PIPINGTool(args1, args2);
		actualOutput = pipingTool.execute(workingDir, null);
		expectedOutput = "Appl\nMelo\nOran";
		System.out.println("OP: \n" + actualOutput);
		assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
	}
	*/
	
	
	@Test
	public void testExecuteWcUniqCat() {
		String[] args1 = {"uniq", "textFiles/testC.txt", "|", "cat", "|", "paste"};
		String[] args2 = {};
		pipingTool = new PIPINGTool(args1, args2);
		actualOutput = pipingTool.execute(workingDir, null);
		expectedOutput = "a\nb\na\nc";
		assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
	}
	
	@Test
	/*
	 * Valid test case
	 * command : echo filename | wc -m -l
	 * wc with no filename args
	 */
	public void testExecuteEchoWc() {
		String[] args1 = {"echo", "textFiles/testC.txt", "|", "wc", "-m", "-l"};
		String[] args2 = {};
		pipingTool = new PIPINGTool(args1, args2);
		actualOutput = pipingTool.execute(workingDir, null);
		expectedOutput = workingDir + "\\textFiles\\testC.txt :  -m  15 -l  15\n";
		assertTrue(actualOutput.equalsIgnoreCase(expectedOutput));
	}
	
	@Test
	/* Negative
	 * Invalid case for comm:
	 * No args given for comm
	 */
	public void testExecuteWcComm() {
		String[] args1 = {"wc", "textFiles/testC.txt", "|", "comm"};
		String[] args2 = {};
		pipingTool = new PIPINGTool(args1, args2);
		actualOutput = pipingTool.execute(workingDir, null);
		assertNotEquals(pipingTool.getStatusCode(),0);
	}
	
	@Test
	/*
	 * Positive case: Valid command
	 * Command: echo hello | comm a.txt b.txt
	 * To show that comm works when both args are present
	 */
	public void testExecuteEchoCommWithArgs() {
		String[] args1 = {"echo", "hello", "|", "comm", "a.txt", "b.txt"};
		String[] args2 = {};
		pipingTool = new PIPINGTool(args1, args2);
		actualOutput = pipingTool.execute(workingDir, null);
		expectedOutput = "Apple" + testTab + testDash + testTab + testDash + testNewLine +
				testDash + testTab + "Banana" + testTab +testDash + testNewLine +
				testDash + testTab + testDash + testTab + "Melon" + testNewLine +
				testDash + testTab + testDash + testTab + "Orange";
		assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
		assertEquals(pipingTool.getStatusCode(), 0);
	}
	
	@Test
	/*
	 * Negative case: invalid command
	 * Command: echo hello | comm a.txt
	 * To show that comm doesnt work when both args arent present
	 */
	public void testExecuteEchoCommWithInvalidArgs() {
		String[] args1 = {"echo", "hello", "|", "comm", "a.txt"};
		String[] args2 = {};
		pipingTool = new PIPINGTool(args1, args2);
		actualOutput = pipingTool.execute(workingDir, null);
		expectedOutput = "";
		assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
		assertNotEquals(pipingTool.getStatusCode(), 0);
	}
	
	@Test
	/*
	 * Positive case: Valid command
	 * command: "cat a.txt | echo hello"
	 */
	public void testExecuteCatEchoWithArgs() {
		String[] args1 = {"cat", "a.txt", "|", "echo", "hello"};
		String[] args2 = {};
		pipingTool = new PIPINGTool(args1, args2);
		actualOutput = pipingTool.execute(workingDir, null);
		String expectedOutput = "hello";
		assertEquals(expectedOutput, actualOutput);
		assertEquals(pipingTool.getStatusCode(), 0);
	}
	
	@Test
	/*
	 * Negative case: Invalid command:
	 * command: echo hello |
	 */
	public void testExecuteEchoWithArgsEndingWithPipe() {
		String[] args1 = {"echo", "hello", "|"};
		String[] args2 = {};
		pipingTool = new PIPINGTool(args1, args2);
		actualOutput = pipingTool.execute(workingDir, null);
		assertTrue(pipingTool.getStatusCode() != 0);
	}
}
