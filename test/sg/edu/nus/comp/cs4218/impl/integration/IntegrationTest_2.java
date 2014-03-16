package sg.edu.nus.comp.cs4218.impl.integration;

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

import sg.edu.nus.comp.cs4218.extended1.IPipingTool;
import sg.edu.nus.comp.cs4218.impl.extended1.PIPINGTool;

/**
 * This test class covers the second requirement of integration testing
 * Chains of interactions ( at least 10 complex scenarios with at least two
 * pipes and different utilities).
 */
public class IntegrationTest_2 {

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
	
	/*
	 * Positive Complex Scenario: 1
	 * Command: grep (Appl|Mel|Ora) a.txt | sort | echo
	 */
	@Test
	public void testPipeExecuteGrepSortEcho() {
		String[] args1 = {"grep","(App|Mel|Ora)", "a.txt", "|", "sort", "|", "echo"};
		String[] args2 = {};
		pipingTool = new PIPINGTool(args1, args2);
		actualOutput = pipingTool.execute(workingDir, "");
		expectedOutput = "Melon\nOrange\na.txt:Apple";
		assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
	}
	
	/*
	 * Positive Complex Scenario: 2
	 * Command: wc a.txt | cat a.txt b.txt | echo
	 */
	@Test
	public void testPipeExecuteWcCatEcho() {
		String[] args1 = {"wc", "a.txt", "|", "cat", "a.txt", "b.txt", "|", "echo"};
		String[] args2 = {};
		pipingTool = new PIPINGTool(args1, args2);
		actualOutput = pipingTool.execute(workingDir, "");
		expectedOutput = fileContentA + "\n" + fileContentB;
		assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
	}
	
	/*
	 * Positive Complex Scenario: 3
	 * Command: echo a.txt | cat | cat
	 */
	@Test
	public void testPipeExecuteEchoCatCat() {
		String[] args1 = {"echo", "a.txt", "|", "cat", "|", "cat"};
		String[] args2 = {};
		pipingTool = new PIPINGTool(args1, args2);
		actualOutput = pipingTool.execute(workingDir, "");
		expectedOutput = "a.txt";
		assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
	}
	
	/*
	 * Positive Complex Scenario: 4
	 * Command: comm a.txt b.txt | cut -c 1-3 | paste
	 */
	@Test
	public void testPipeExecuteCommCutPaste() {
		String[] args1 = {"comm", "a.txt", "b.txt", "|", "cut", "-c", "1-3", "|", "paste"};
		String[] args2 = {};
		pipingTool = new PIPINGTool(args1, args2);
		actualOutput = pipingTool.execute(workingDir, "");
		expectedOutput = "App\n \tB\n \t \n \t \n";
		assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
	}

	/*
	 * Positive Complex Scenario: 5
	 * Command: uniq a.txt - | cat | sort
	 */
	@Test
	public void testFnHyphenPipeExecuteUniqCatSort() {
		String[] args1 = {"uniq", "a.txt", "-", "|", "cat", "|", "sort"};
		String[] args2 = {};
		pipingTool = new PIPINGTool(args1, args2);
		actualOutput = pipingTool.execute(workingDir, "");
		expectedOutput = "Apple\nMelon\nOrange";
		assertTrue(actualOutput.equalsIgnoreCase(expectedOutput));
		assertTrue(pipingTool.getStatusCode() == 0);
	}
	
	/*
	 * Positive Complex Scenario: 6
	 * Command: paste - a.txt - | uniq | echo
	 */
	@Test
	public void testHyphenFnHyphenPipeExecutePasteUniqEcho() {
		String[] args1 = {"paste", "-", "a.txt", "-", "|", "uniq", "|", "echo"};
		String[] args2 = {};
		pipingTool = new PIPINGTool(args1, args2);
		actualOutput = pipingTool.execute(workingDir, "");
		expectedOutput = fileContentA;
		assertTrue(actualOutput.equalsIgnoreCase(expectedOutput));
		assertTrue(pipingTool.getStatusCode() == 0);
	}
	
	/*
	 * Positive Complex Scenario: 7
	 * Command: comm c.txt d.txt | cat a.txt - b.txt | cut -c 1-3
	 */
	@Test
	public void testFnHyphenFnPipeExecuteCatCommCut() {
		String[] args1 = {"comm", "c.txt", "d.txt", "|", "cat", "a.txt", "-", "b.txt", "|", "cut", "-c", "1-3"};
		String[] args2 = {};
		pipingTool = new PIPINGTool(args1, args2);
		actualOutput = pipingTool.execute(workingDir, "");
		expectedOutput = "App\nMel\nOra\nBan\nMel\nOra\n";
		assertTrue(actualOutput.equalsIgnoreCase(expectedOutput));
		assertTrue(pipingTool.getStatusCode() == 0);
	}
	
	/*
	 * Positive Complex Scenario: 8
	 * Command: cut -c 1-5 - | wc | cat
	 */
	@Test
	public void testHypenErrPipeExecuteCutWcCat() {
		String[] args1 = {"cut", "-c", "1-5", "-", "|", "wc", "|", "cat"};
		String[] args2 = {};
		pipingTool = new PIPINGTool(args1, args2);
		actualOutput = pipingTool.execute(workingDir, "");
		expectedOutput = "Stdin : -m  0 , -w  0 , -l 0\n";
		assertTrue(actualOutput.equalsIgnoreCase(expectedOutput));
		assertTrue(pipingTool.getStatusCode() == 0);
	}
	
	/*
	 * Negative Complex Scenario: 1
	 * Command: cut -c 1-5 a.txt | comm | echo
	 * Error: No args for comm
	 */
	@Test
	public void testInsuffArgsErrPipeExecuteCutCommEcho() {
		String[] args1 = {"cut", "-c", "1-5", "a.txt", "|", "comm", "|", "echo"};
		String[] args2 = {};
		pipingTool = new PIPINGTool(args1, args2);
		actualOutput = pipingTool.execute(workingDir, "");
		assertTrue(pipingTool.getStatusCode() != 0);
	}
	
	/*
	 * Negative Complex Scenario: 2
	 * Command: comm a.txt } cut -c 1-5 | echo
	 * Error: Insufficient args for comm
	 */
	@Test
	public void testInsuffArgsErrPipeExecuteCommCutEcho() {
		String[] args1 = {"comm", "a.txt", "|", "cut", "-c", "1-5", "|", "echo"};
		String[] args2 = {};
		pipingTool = new PIPINGTool(args1, args2);
		actualOutput = pipingTool.execute(workingDir, "");
		assertTrue(pipingTool.getStatusCode() != 0);
	}

	/*
	 * Negative Complex Scenario: 3
	 * Command: cat | sot a.txt | uniq
	 * Error: No args for cat even though subsequent small programs are correct
	 */
	@Test
	public void testNoArgsErrPipeExecuteCatSortUniq() {
		String[] args1 = {"cat", "|", "sort", "a.txt", "|", "uniq"};
		String[] args2 = {};
		pipingTool = new PIPINGTool(args1, args2);
		actualOutput = pipingTool.execute(workingDir, "");
		assertTrue(pipingTool.getStatusCode() != 0);
	}
	
	/*
	 * Negative Complex Scenario: 4
	 * Command: cut a.txt -c 1-2 | cat | paste
	 * Error: Options are before arguments for cut
	 */
	@Test
	public void testFnBeforeOptionsErrPipeExecuteCutCatPaste() {
		String[] args1 = {"cut", "a.txt", "-c", "1-2", "|", "cat", "|", "paste"};
		String[] args2 = {};
		pipingTool = new PIPINGTool(args1, args2);
		actualOutput = pipingTool.execute(workingDir, "");
		assertTrue(pipingTool.getStatusCode() != 0);
	}
	
	/*
	 * Negative Complex Scenario: 5
	 * Command; uniq a.txt | | cat | sort
	 * Error: No program between pipes
	 */
	@Test
	public void testPipeSyntaxErr1PipeExecuteUniqCatSort() {
		String[] args1 = {"uniq", "a.txt", "|", "|", "cat", "|", "sort"};
		String[] args2 = {};
		pipingTool = new PIPINGTool(args1, args2);
		actualOutput = pipingTool.execute(workingDir, "");
		assertTrue(pipingTool.getStatusCode() != 0);
	}
	
	/*
	 * Negative Complex Scenario: 6
	 * Command: uniq a.txt - | cat | sort |
	 * Error: No program after pipe
	 */
	@Test
	public void testPipeSyntaxErr2PipeExecuteUniqCatEcho() {
		String[] args1 = {"uniq", "a.txt", "-", "|", "cat", "|", "sort" , "|"};
		String[] args2 = {};
		pipingTool = new PIPINGTool(args1, args2);
		actualOutput = pipingTool.execute(workingDir, "");
		assertTrue(pipingTool.getStatusCode() != 0);
	}
	
	/*
	 * Negative Complex Scenario: 7
	 * Command: uniq a.txt | cd ../ | echo
	 * Error: cd command not allowed in pipe
	 */
	@Test
	public void testCdErrPipeExecuteUniqCdEcho() {
		String[] args1 = {"uniq", "a.txt", "|", "cd", "../", "|", "echo"};
		String[] args2 = {};
		pipingTool = new PIPINGTool(args1, args2);
		actualOutput = pipingTool.execute(workingDir, "");
		assertTrue(pipingTool.getStatusCode() != 0);
	}
	
	/*
	 * Negative Complex Scenario: 8
	 * Command: uniq a.txt | copy b.txt ../ | echo
	 * Error: copy command not allowed in pipe
	 */
	@Test
	public void testCopyErrPipeExecuteUniqCdEcho() {
		String[] args1 = {"uniq", "a.txt", "|", "copy", "b.txt", "../", "|", "echo"};
		String[] args2 = {};
		pipingTool = new PIPINGTool(args1, args2);
		actualOutput = pipingTool.execute(workingDir, "");
		assertTrue(pipingTool.getStatusCode() != 0);
	}
	
	/*
	 * Negative Complex Scenario: 7
	 * Command: uniq a.txt | echo | move d.txt ../
	 * Error: move command not allowed in pipe
	 */
	@Test
	public void testMoveErrPipeExecuteUniqCdEcho() {
		String[] args1 = {"uniq", "a.txt", "|", "echo", "|", "move", "d.txt", "../"};
		String[] args2 = {};
		pipingTool = new PIPINGTool(args1, args2);
		actualOutput = pipingTool.execute(workingDir, "");
		assertTrue(pipingTool.getStatusCode() != 0);
	}
	
	/*
	 * Negative Complex Scenario: 8
	 * Command: delete a.txt | echo a.txt | sort
	 * Error: delete command not allowed in pipe
	 */
	@Test
	public void testDeleteErrPipeExecuteUniqCdEcho() {
		String[] args1 = {"delete", "a.txt", "|", "echo", "a.txt", "|", "sort"};
		String[] args2 = {};
		pipingTool = new PIPINGTool(args1, args2);
		actualOutput = pipingTool.execute(workingDir, "");
		assertTrue(pipingTool.getStatusCode() != 0);
	}
	
	/*
	 * Negative Complex Scenario: 9
	 * Command: cut null | comm a.txt b.txt | paste
	 * Error: null arguments for cat
	 */
	@Test
	public void testNullArgErrPipeExecuteCatCommPaste() {
		String[] args1 = {"cat", "null", "|", "comm", "a.txt", "b.txt", "|", "paste"};
		String[] args2 = {};
		pipingTool = new PIPINGTool(args1, args2);
		actualOutput = pipingTool.execute(workingDir, "");
		assertTrue(pipingTool.getStatusCode() != 0);
	}
}
