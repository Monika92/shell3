package sg.edu.nus.comp.cs4218.impl.integration;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
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

public class IntegrationTest_1 {
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
	public void testExecuteGrepCut() {
		String[] args1 = {"grep","(App|Mel|Ora)", "a.txt", "|", "cut", "-c", "1-2"};
		String[] args2 = {};
		pipingTool = new PIPINGTool(args1, args2);
		actualOutput = pipingTool.execute(workingDir, "");
		expectedOutput = "a.\nMe\nOr\n";
		assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
	}
	
	@Test
	public void testExecuteGrepCutFileNotFound() {
		String[] args1 = {"grep","(App|Mel|Ora)", "filenotfound.txt", "|", "cut", "-c", "1-2"};
		String[] args2 = {};
		pipingTool = new PIPINGTool(args1, args2);
		actualOutput = pipingTool.execute(workingDir, "");
		expectedOutput = "";
		assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
	}
    
	@Test
	public void testExecuteGrepSort() {
		String[] args1 = {"grep","(B|C)", "d.txt", "|", "sort"};
		String[] args2 = {};
		pipingTool = new PIPINGTool(args1, args2);
		pipingTool.execute(workingDir, "");
		actualOutput = readFromFile(new File("stdin.txt"));
		expectedOutput = "Bat\nd.txt:Cat\n";
		assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
	}
	@Test
	public void testExecuteGrepSortEmptyOutput() {
		String[] args1 = {"grep","1234", "d.txt", "|", "sort"};
		String[] args2 = {};
		pipingTool = new PIPINGTool(args1, args2);
		pipingTool.execute(workingDir, "");
		actualOutput = readFromFile(new File("stdin.txt"));
		expectedOutput = "";
		assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
	}
	@Test
	public void testExecuteGrepSortCheck() {
		String[] args1 = {"grep","A|M|O", "a.txt", "|", "sort","-c"};
		String[] args2 = {};
		pipingTool = new PIPINGTool(args1, args2);
		actualOutput = pipingTool.execute(workingDir, "");
		expectedOutput = "a.txt:Apple is out of order";
		assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
	}
	@Test
	public void testExecuteGrepWc() {
		String[] args1 = {"grep","(App|Mel|Ora)", "a.txt", "|", "wc", "-m"};
		String[] args2 = {};
		pipingTool = new PIPINGTool(args1, args2);
		actualOutput = pipingTool.execute(workingDir, "");
		expectedOutput = "a.\nMe\nOr\n";
		assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
	}
	@Test
	public void testExecuteGrepWcFileNotFound() {
		String[] args1 = {"grep","(App|Mel|Ora)", "InvalidFile.txt", "|", "wc", "-m"};
		String[] args2 = {};
		pipingTool = new PIPINGTool(args1, args2);
		actualOutput = pipingTool.execute(workingDir, "");
		expectedOutput = "a.\nMe\nOr\n";
		assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
	}
	@Test
	public void testExecuteGrepEcho() {
		
		String[] args1 = {"grep","(A|M)", "a.txt", "|", "echo"};
		String[] args2 = {};
		pipingTool = new PIPINGTool(args1, args2);
		actualOutput = pipingTool.execute(workingDir, "");
		expectedOutput = "a.txt:Apple\nMelon\n";
		assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
	}
	
	@Test
	public void testExecuteGrepEchoInvalidParams() {
		
		String[] args1 = {"grep","", "", "|", "echo"};
		String[] args2 = {};
		pipingTool = new PIPINGTool(args1, args2);
		actualOutput = pipingTool.execute(workingDir, "");
		expectedOutput = "pipe break at echo";
		assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
	}
	
	@Test
	public void testExecuteCatGrep() {
		
		String[] args1 = {"Cat", "a.txt", "|","grep", "(A|M)"};
		String[] args2 = {};
		pipingTool = new PIPINGTool(args1, args2);
		actualOutput = pipingTool.execute(workingDir, "");
		expectedOutput = "";
		assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
	}
	@Test
	public void testExecuteCommGrep()
	{
		
	}
	@Test
	public void testExecuteCommGrepInvalidInput()
	{
		
	}
	@Test
	public void testExecuteGrepUniq()
	{
		
	}
	@Test
	public void testExecuteGrepUniqInvalidInput()
	{
		
	}
	@Test
	public void testExecuteGrepPaste()
	{
		
	}
	@Test
	public void testExecuteGrepPasteInvalidInput()
	{
		
	}
	
}