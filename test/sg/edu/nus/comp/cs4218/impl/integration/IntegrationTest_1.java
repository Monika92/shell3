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

import sg.edu.nus.comp.cs4218.extended1.IPipingTool;
import sg.edu.nus.comp.cs4218.impl.extended1.PIPINGTool;


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
	
	/**
	 * This method takes in the file directory path and returns the contents of the file
	 * @param file
	 * @throws Exception
	 */
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
	
	/**
	 * This method writes the given input to a file
	 * @param file
	 * @param input
	 */
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
	 * Integrate Grep with Cut
	 * Positive Case
	 * grep (App|Mel|Ora) a.txt | cut -c 1-2
	 */
	@Test
	public void testExecuteGrepCut() {
		String[] args1 = {"grep","(App|Mel|Ora)", "a.txt", "|", "cut", "-c", "1-2"};
		String[] args2 = {};
		pipingTool = new PIPINGTool(args1, args2);
		actualOutput = pipingTool.execute(workingDir, "");
		expectedOutput = "a.\nMe\nOr\n";
		assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
	}

	/*
	 * Integrate Grep with Cut
	 * Negative Case
	 * grep (App|Mel|Ora) filenotfound.txt | cut -c 1-2
	 */
	@Test
	public void testExecuteGrepCutFileNotFound() {
		String[] args1 = {"grep","(App|Mel|Ora)", "filenotfound.txt", "|", "cut", "-c", "1-2"};
		String[] args2 = {};
		pipingTool = new PIPINGTool(args1, args2);
		pipingTool.execute(workingDir, "");
		assertEquals(pipingTool.getStatusCode(), -1);
	}

	/*
	 * Integrate Grep with Sort
	 * Positive Case
	 * grep (B|C) d.txt | sort
	 */
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
	/*
	 * Integrate Grep with Sort
	 * Positive Case
	 * grep 1234 d.txt | sort
	 */
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
	/*
	 * Integrate Grep with Sort
	 * Negative Case
	 * grep | sort
	 */
	public void testExecuteGrepSortNullParams() {
		String[] args1 = {"grep","|","sort"};
		String[] args2 = {};
		pipingTool = new PIPINGTool(args1, args2);
		pipingTool.execute(workingDir, "");
		actualOutput = readFromFile(new File("stdin.txt"));
		expectedOutput = "";
		assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
	}
	/*
	 * Integrate Grep with Sort 
	 * Positive Case
	 * command : grep A|M|O a.txt | sort -c
	 */
	@Test
	public void testExecuteGrepSortCheck() {
		String[] args1 = {"grep","A|M|O", "a.txt", "|", "sort","-c"};
		String[] args2 = {};
		pipingTool = new PIPINGTool(args1, args2);
		actualOutput = pipingTool.execute(workingDir, "");
		expectedOutput = "a.txt:Apple is out of order";
		assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
	}
	/*
	 * Integrate Grep with WC
	 * Positive Case
	 * command : grep (App|Mel|Ora) a.txt | wc -m
	 */
	@Test
	public void testExecuteGrepWc() {
		String[] args1 = {"grep","(App|Mel|Ora)", "a.txt", "|", "wc", "-m"};
		String[] args2 = {};
		pipingTool = new PIPINGTool(args1, args2);
		actualOutput = pipingTool.execute(workingDir, "");
		expectedOutput = "Stdin :  -m  22\n";
		assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
	}
	
	/*
	 * Integrate Grep with WC
	 * Negative Case
	 * command : grep (App|Mel|Ora) InvalidFile.txt | wc -m
	 */
	@Test
	public void testExecuteGrepWcFileNotFound() {
		String[] args1 = {"grep","(App|Mel|Ora)", "InvalidFile.txt", "|", "wc", "-m"};
		String[] args2 = {};
		pipingTool = new PIPINGTool(args1, args2);
		pipingTool.execute(workingDir, "");
		assertEquals(pipingTool.getStatusCode(), -1);
	}
	
	/*
	 * Integrate Grep with echo
	 * Positive Case
	 * command : grep (A|M) a.txt | echo
	 */
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
	/*
	 * Integrate Grep with echo
	 * Negative Case
	 * command : grep invalid arguments | echo
	 */
	public void testExecuteGrepEchoInvalidParams() {

		String[] args1 = {"grep","", "", "|", "echo"};
		String[] args2 = {};
		pipingTool = new PIPINGTool(args1, args2);
		pipingTool.execute(workingDir, "");
		assertEquals(pipingTool.getStatusCode(), -1);
	}

	@Test
	/*
	 * Integrate Grep with cat
	 * Positive Case
	 * command : cat a.txt| grep (A|M)
	 */
	public void testExecuteCatGrep() {

		String[] args1 = {"Cat", "a.txt", "|","grep", "(A|M)"};
		String[] args2 = {};
		pipingTool = new PIPINGTool(args1, args2);
		actualOutput = pipingTool.execute(workingDir, "");
		expectedOutput = "Apple\nMelon\n";
		assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
	}
	
	@Test
	/*
	 * Integrate Grep with cat
	 * Negative Case
	 * command : cat a.txt| grep (A|M)
	 */
	public void testExecuteCatGrepInvalidParams() {

		String[] args1 = {"Cat", "Invalid.txt", "|","grep", "(A|M)"};
		String[] args2 = {};
		pipingTool = new PIPINGTool(args1, args2);
		actualOutput = pipingTool.execute(workingDir, "");
		assertTrue(pipingTool.getStatusCode() != 0);
	}
	
	@Test
	/*
	 * Integrate Grep with comm
	 * Positive case
	 * command: comm a.txt b.txt | grep (A|M)
	 */
	public void testExecuteCommGrep(){
		String[] args1 = {"Comm", "a.txt", "b.txt","|","grep", "(A|M)"};
		String[] args2 = {};
		pipingTool = new PIPINGTool(args1, args2);
		actualOutput = pipingTool.execute(workingDir, "");
		expectedOutput = "Apple\t \t \n \t \tMelon\n";
		assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
		assertEquals(pipingTool.getStatusCode(), 0);
	}

	/*
	 * Integrate Grep with comm
	 * Negative case
	 * command: comm a.txt invalidFile | grep patt 
	 */
	@Test
	public void testExecuteCommGrepInvalidInput()
	{
		String[] args1 = {"Comm", "a.txt", "noFile","|","grep", "(A|M)"};
		String[] args2 = {};
		pipingTool = new PIPINGTool(args1, args2);
		actualOutput = pipingTool.execute(workingDir, "");
		assertNotEquals(pipingTool.getStatusCode(),0);
	}
	/*
	 * Positive Case
	 * Integrate grep and uniq
	 * grep (A|M) a.txt | uniq
	 */
	@Test
	public void testExecuteGrepUniq()
	{
		String[] args1 = {"grep","(A|M)", "a.txt", "|", "uniq"};
		String[] args2 = {};
		pipingTool = new PIPINGTool(args1, args2);
		actualOutput = pipingTool.execute(workingDir, "");
		expectedOutput = "a.txt:Apple\nMelon";
		assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
		
	}
	/*
	 * Negative Case
	 * Integrate grep and uniq
	 * grep pattern invalidfilename | uniq
	 */
	@Test
	public void testExecuteGrepUniqInvalidInput()
	{
		String[] args1 = {"grep","(A|M)", "dsa", "|", "uniq"};
		String[] args2 = {};
		pipingTool = new PIPINGTool(args1, args2);
		pipingTool.execute(workingDir, "");
		assertEquals(pipingTool.getStatusCode(), -1);
	}

	@Test
	/*
	 * Integrate Grep with paste
	 * Positive command
	 * command: grep pattern filename | paste
	 */
	public void testExecuteGrepPaste1()
	{
		String[] args1 = {"grep", "(A|M)", "a.txt","|","paste"};
		String[] args2 = {};
		pipingTool = new PIPINGTool(args1, args2);
		actualOutput = pipingTool.execute(workingDir, "");
		expectedOutput = "a.txt:Apple\nMelon\n";
		assertEquals(expectedOutput, actualOutput);
		assertEquals(pipingTool.getStatusCode(),0);
	}

	@Test
	/* 
	 * Integrate Grep with paste
	 * Positive case
	 * command: grep patt filename | paste file1 file2 file3
	 */
	public void testExecuteGrepPaste2(){
		String[] args1 = {"grep", "(A|M)", "a.txt","|","paste","a.txt","b.txt","c.txt"};
		String[] args2 = {};
		pipingTool = new PIPINGTool(args1, args2);
		actualOutput = pipingTool.execute(workingDir, "");
		expectedOutput = "Apple\tBanana\tBatman" + "\n" + 
		"Melon\tMelon\tSpiderman" + "\n" + 
				"Orange\tOrange" +"\tSuperman";
		System.out.println("AO:\n" + actualOutput);
		assertEquals(expectedOutput, actualOutput);
		assertEquals(pipingTool.getStatusCode(),0);
	}

	@Test
	/*
	 * Integrate Grep with paste
	 * Negative case
	 * grep pattern invalidFile | paste
	 * 
	 */
	public void testExecuteGrepPasteInvalidInput()
	{
		String[] args1 = {"grep", "(A|M)", "invFile","|","paste"};
		String[] args2 = {};
		pipingTool = new PIPINGTool(args1, args2);
		actualOutput = pipingTool.execute(workingDir, "");
		assertNotEquals(pipingTool.getStatusCode(),0);
	}

}