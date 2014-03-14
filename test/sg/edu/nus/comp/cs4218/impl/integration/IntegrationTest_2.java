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
	
	
	@Test
	public void testPipeExecuteGrepSortEcho() {
		String[] args1 = {"grep","(App|Mel|Ora)", "a.txt", "|", "sort", "|", "echo"};
		String[] args2 = {};
		pipingTool = new PIPINGTool(args1, args2);
		actualOutput = pipingTool.execute(workingDir, "");
		expectedOutput = "Melon\r\nOrange\r\na.txt:Apple\r\n";
		assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
	}
	
	@Test
	public void testPipeExecuteWcCatEcho() {
		String[] args1 = {"wc", "a.txt", "|", "cat", "a.txt", "b.txt", "|", "echo"};
		String[] args2 = {};
		pipingTool = new PIPINGTool(args1, args2);
		actualOutput = pipingTool.execute(workingDir, "");
		expectedOutput = fileContentA + "\n" + fileContentB;
		assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
	}
	
	@Test
	public void testPipeExecuteEchoCatCat() {
		String[] args1 = {"echo", "a.txt", "|", "cat", "|", "cat"};
		String[] args2 = {};
		pipingTool = new PIPINGTool(args1, args2);
		actualOutput = pipingTool.execute(workingDir, "");
		expectedOutput = "a.txt";
		assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
	}
	
	@Test
	public void testPipeExecuteCommCutPaste() {
		String[] args1 = {"comm", "a.txt", "b.txt", "|", "cut", "-c", "1-3", "|", "paste"};
		String[] args2 = {};
		pipingTool = new PIPINGTool(args1, args2);
		actualOutput = pipingTool.execute(workingDir, "");
		expectedOutput = "App\n \tB\n \t \n \t \n";
		assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
	}

	
	@Test
	public void testFnHyphenErrPipeExecuteUniqCatSort() {
		String[] args1 = {"uniq", "a.txt", "-", "|", "cat", "|", "sort"};
		String[] args2 = {};
		pipingTool = new PIPINGTool(args1, args2);
		actualOutput = pipingTool.execute(workingDir, "");
		assertTrue(pipingTool.getStatusCode() != 0);
	}
	
	@Test
	public void testHyphenFnHyphenErrPipeExecutePasteUniqEcho() {
		String[] args1 = {"paste", "-", "a.txt", "-", "|", "uniq", "|", "echo"};
		String[] args2 = {};
		pipingTool = new PIPINGTool(args1, args2);
		actualOutput = pipingTool.execute(workingDir, "");
		assertTrue(pipingTool.getStatusCode() != 0);
	}
	
	@Test
	public void testFnHyphenFnErrPipeExecuteCatCommCut() {
		String[] args1 = {"cat", "a.txt", "-", "b.txt", "|", "comm", "c.txt", "d.txt", "|", "cut"};
		String[] args2 = {};
		pipingTool = new PIPINGTool(args1, args2);
		actualOutput = pipingTool.execute(workingDir, "");
		assertTrue(pipingTool.getStatusCode() != 0);
	}
	
	/*
	 * Negative complex scenarios
	 */
	@Test
	public void testInsuffArgsErrPipeExecuteCutCommEcho() {
		String[] args1 = {"cut", "-c", "1-5", "a.txt", "|", "comm", "|", "echo"};
		String[] args2 = {};
		pipingTool = new PIPINGTool(args1, args2);
		actualOutput = pipingTool.execute(workingDir, "");
		assertTrue(pipingTool.getStatusCode() != 0);
	}
	
	@Test
	public void testInsuffArgsErrPipeExecuteCommCutEcho() {
		String[] args1 = {"comm", "a.txt", "|", "cut", "-c", "1-5", "|", "echo"};
		String[] args2 = {};
		pipingTool = new PIPINGTool(args1, args2);
		actualOutput = pipingTool.execute(workingDir, "");
		assertTrue(pipingTool.getStatusCode() != 0);
	}

	@Test
	public void testNoArgsErrPipeExecuteCatSortUniq() {
		String[] args1 = {"cat", "|", "sort", "a.txt", "|", "uniq"};
		String[] args2 = {};
		pipingTool = new PIPINGTool(args1, args2);
		actualOutput = pipingTool.execute(workingDir, "");
		assertTrue(pipingTool.getStatusCode() != 0);
	}
	
	@Test
	public void testHypenPipeExecuteCutWcCat() {
		String[] args1 = {"cut", "-c", "1-5", "-", "|", "wc", "|", "cat"};
		String[] args2 = {};
		pipingTool = new PIPINGTool(args1, args2);
		actualOutput = pipingTool.execute(workingDir, "");
		assertTrue(pipingTool.getStatusCode() != 0);
	}
	
	@Test
	public void testFnBeforeOptionsErrPipeExecuteCutCatPaste() {
		String[] args1 = {"cut", "a.txt", "-c", "1-2", "|", "cat", "|", "paste"};
		String[] args2 = {};
		pipingTool = new PIPINGTool(args1, args2);
		actualOutput = pipingTool.execute(workingDir, "");
		assertTrue(pipingTool.getStatusCode() != 0);
	}
	
	@Test
	public void testPipeSyntaxErr1PipeExecuteUniqCatSort() {
		String[] args1 = {"uniq", "a.txt", "|", "|", "cat", "|", "sort"};
		String[] args2 = {};
		pipingTool = new PIPINGTool(args1, args2);
		actualOutput = pipingTool.execute(workingDir, "");
		assertTrue(pipingTool.getStatusCode() != 0);
	}
	
	@Test
	public void testPipeSyntaxErr2PipeExecuteUniqCatEcho() {
		String[] args1 = {"uniq", "a.txt", "-", "|", "cat", "|", "sort" , "|"};
		String[] args2 = {};
		pipingTool = new PIPINGTool(args1, args2);
		actualOutput = pipingTool.execute(workingDir, "");
		assertTrue(pipingTool.getStatusCode() != 0);
	}
	
	@Test
	public void testCdErrPipeExecuteUniqCdEcho() {
		String[] args1 = {"uniq", "a.txt", "-", "|", "cd", "../", "|", "echo"};
		String[] args2 = {};
		pipingTool = new PIPINGTool(args1, args2);
		actualOutput = pipingTool.execute(workingDir, "");
		assertTrue(pipingTool.getStatusCode() != 0);
	}
	
	@Test
	public void testCopyErrPipeExecuteUniqCdEcho() {
		String[] args1 = {"uniq", "a.txt", "-", "|", "copy", "b.txt", "../", "|", "echo"};
		String[] args2 = {};
		pipingTool = new PIPINGTool(args1, args2);
		actualOutput = pipingTool.execute(workingDir, "");
		assertTrue(pipingTool.getStatusCode() != 0);
	}
	
	@Test
	public void testMoveErrPipeExecuteUniqCdEcho() {
		String[] args1 = {"uniq", "a.txt", "-", "|", "echo", "|", "move", "d.txt", "../"};
		String[] args2 = {};
		pipingTool = new PIPINGTool(args1, args2);
		actualOutput = pipingTool.execute(workingDir, "");
		assertTrue(pipingTool.getStatusCode() != 0);
	}
	
	@Test
	public void testDeleteErrPipeExecuteUniqCdEcho() {
		String[] args1 = {"delete", "a.txt", "|", "echo", "a.txt", "|", "sort"};
		String[] args2 = {};
		pipingTool = new PIPINGTool(args1, args2);
		actualOutput = pipingTool.execute(workingDir, "");
		assertTrue(pipingTool.getStatusCode() != 0);
	}
	
	@Test
	public void testNullArgErrPipeExecuteCatCommPaste() {
		String[] args1 = {"cat", "null", "|", "comm", "a.txt", "b.txt", "|", "paste"};
		String[] args2 = {};
		pipingTool = new PIPINGTool(args1, args2);
		actualOutput = pipingTool.execute(workingDir, "");
		assertTrue(pipingTool.getStatusCode() != 0);
	}
}
