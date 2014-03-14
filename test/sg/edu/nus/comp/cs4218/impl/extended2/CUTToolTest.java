package sg.edu.nus.comp.cs4218.impl.extended2;

import static org.junit.Assert.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import sg.edu.nus.comp.cs4218.extended2.ICutTool;
import sg.edu.nus.comp.cs4218.impl.extended2.CUTTool;


public class CUTToolTest {

	private ICutTool cuttool; 
	String actualOutput,expectedOutput,helpOutput;
	File workingDirectory;
	File inputFile1, inputFile2, inputFile3,inputFile4;
	String absoluteFilePath;

	@Before
	public void before(){

		absoluteFilePath = System.getProperty("home.dir")+"test.txt";
		workingDirectory = new File(System.getProperty("user.dir"));

		helpOutput = "usage: cut [OPTIONS] [FILE]" + "\n"
				+ "FILE : Name of the file, when no file is present" + "\n"
				+ "OPTIONS : -c LIST : Use LIST as the list of characters to cut out. Items within "
				+ "the list may be separated by commas, "
				+ "and ranges of characters can be separated with dashes. "
				+ "For example, list 1-5,10,12,18-30 specifies characters "
				+ "1 through 5, 10,12 and 18 through 30" + "\n"
				+ "-d DELIM: Use DELIM as the field-separator character instead of"
				+ "the TAB character" + "\n" 
				+ "-help : Brief information about supported options";


		String input1 = "apple\nball\ncat\ndog";
		String input2 = "hello\nworld\ncoding\nis\nfun";
		String input3 = "";
		inputFile1 = new File("test1.txt");
		inputFile2 = new File("test2.txt");
		inputFile3 = new File("test3.txt");
		inputFile4 = new File(absoluteFilePath);
		writeToFile(inputFile1, input1);
		writeToFile(inputFile2, input2);
		writeToFile(inputFile3, input3);
		writeToFile(inputFile4, input1);
	}

	@After
	public void after(){
		cuttool = null;

		if(inputFile1.exists())
			inputFile1.delete();
		if(inputFile2.exists())
			inputFile2.delete();
		if(inputFile3.exists())
			inputFile3.delete();
		if(inputFile4.exists())
			inputFile4.delete();
	}
	/**
	 * This method writes input contents to a file
	 * @param file and input contents
	 * @throws Exception
	 */
	public void writeToFile(File file, String input){
		try{
			if(!file.exists())
				file.createNewFile();
			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			char[] temp = input.toCharArray(); int i = 0;
			while(i<temp.length){
				while(temp[i]!='\n'){
					bw.write(temp[i]);
					i++;
					if(i>=temp.length)
						break;
				}
				bw.newLine(); i++;
			}
			bw.close();
		} catch (IOException e){
		}
	}
	/**
	 * Test for cut command help interface method. Checks for correct help output
	 */
	@Test
	public void testGetHelp()
	{
		String[] arguments = null ;
		cuttool = new CUTTool(arguments);	
		String expectedOutput = helpOutput;
		String actualOutput = cuttool.getHelp();
		assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
	}
	/**
	 * Test for cut command with -c option with standard input (denoted by -)
	 * Checks for correct output after execution
	 */
	@Test
	public void testExecuteCOption()
	{
		String[] arguments = new String[]{"-c", "1-2","-"} ;
		cuttool = new CUTTool(arguments);
		actualOutput = cuttool.execute(workingDirectory, "abcde");
		expectedOutput = "ab";
		actualOutput = actualOutput.replace("\n", "");
		expectedOutput = expectedOutput.replace("\n", "");
		assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
		assertEquals(cuttool.getStatusCode(), 0);
	}
	/**
	 * Test for cut command with -c option with standard input(denoted by -) and file input
	 * Both standard input and file input are executed (standard input executed first)
	 * Checks for correct output after execution
	 */
	@Test
	public void testExecuteFileInputAndStdInput()
	{
		String[] arguments = new String[]{"-c", "1-2","test1.txt","-"} ;
		cuttool = new CUTTool(arguments);
		actualOutput = cuttool.execute(workingDirectory, "abcde");
		expectedOutput = "ab\nap\nba\nca\ndo\n";
		actualOutput = actualOutput.replace("\n", "");
		expectedOutput = expectedOutput.replace("\n", "");
		assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
		assertEquals(cuttool.getStatusCode(), 0);
	}
	/**
	 * Test for cut command with -c option with file input(absolute file path)
	 * Checks for correct output after execution
	 */
	@Test
	public void testExecuteAbsoluteFilePath()
	{
		String[] arguments = new String[]{"-c", "1-2", absoluteFilePath } ;
		cuttool = new CUTTool(arguments);
		actualOutput = cuttool.execute(workingDirectory, null);
		expectedOutput = "ap\nba\nca\ndo\n";
		actualOutput = actualOutput.replace("\n", "");
		expectedOutput = expectedOutput.replace("\n", "");
		assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
		assertEquals(cuttool.getStatusCode(), 0);
	}
	/**
	 * Test for cut command with -c option with file input(relative file path)
	 * Checks for correct output after execution
	 */
	@Test
	public void testExecuteCOptionWithFile()
	{
		String[] arguments = new String[]{"-c", "1-2","test1.txt"} ;
		cuttool = new CUTTool(arguments);
		actualOutput = cuttool.execute(workingDirectory, null);
		expectedOutput = "ap\nba\nca\ndo\n";
		actualOutput = actualOutput.replace("\n", "");
		expectedOutput = expectedOutput.replace("\n", "");
		assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
		assertEquals(cuttool.getStatusCode(), 0);
	}
	/**
	 * Test for cut command with -c option given invalid range for list
	 * Checks for empty output after execution
	 */
	@Test
	public void testExecuteCOptionInputInvalidRange()
	{
		String[] arguments = new String[]{"-c", "2-1","-"} ;
		cuttool = new CUTTool(arguments);
		actualOutput = cuttool.execute(workingDirectory, "abcde");
		expectedOutput = "";
		actualOutput = actualOutput.replace("\n", "");
		expectedOutput = expectedOutput.replace("\n", "");
		assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
		assertEquals(cuttool.getStatusCode(), 0);
	}

	/**
	 * Test for cut command with -d option followed by -f option given standard input
	 * Checks for correct output after execution
	 */
	@Test
	public void testExecuteDOptionFollowedByFOption()
	{
		String[] arguments = new String[]{"-d", ":","-f","1-2","-"} ;
		cuttool = new CUTTool(arguments);
		actualOutput = cuttool.execute(workingDirectory, "one:two:three:four:five:six:seven");
		expectedOutput = "one:two";
		actualOutput = actualOutput.replace("\n", "");
		expectedOutput = expectedOutput.replace("\n", "");
		assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
		assertEquals(cuttool.getStatusCode(), 0);
	}
	/**
	 * Test for cut command with -f option followed by -d option given standard input
	 * Checks for correct output after execution
	 */
	@Test
	public void testExecuteFOptionFollowedByDOption()
	{
		String[] arguments = new String[]{"-f", "1-2","-d",":","-"} ;
		cuttool = new CUTTool(arguments);
		actualOutput = cuttool.execute(workingDirectory, "one:two:three:four:five:six:seven");
		expectedOutput = "one:two";
		actualOutput = actualOutput.replace("\n", "");
		expectedOutput = expectedOutput.replace("\n", "");
		assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
		assertEquals(cuttool.getStatusCode(), 0);
	}
	/**
	 * Test for cut command with -d option followed by -f option given file input
	 * Checks for correct output after execution
	 */
	@Test
	public void testExecuteDOptionWithFile()
	{
		String[] arguments = new String[]{"-d", ":","-f","0","test1.txt"} ;
		cuttool = new CUTTool(arguments);
		actualOutput = cuttool.execute(workingDirectory, null);
		expectedOutput = "apple\nball\ncat\ndog\n";
		actualOutput = actualOutput.replace("\n", "");
		expectedOutput = expectedOutput.replace("\n", "");
		assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
		assertEquals(cuttool.getStatusCode(), 0);
	}

	/**
	 * Test for cut command with -c option given empty file
	 * Checks for empty output after execution
	 */
	@Test
	public void testExecuteCOptionWithEmptyFileTest()
	{
		String[] arguments = new String[]{"-c", "1-2","test3.txt"} ;
		cuttool = new CUTTool(arguments);
		actualOutput = cuttool.execute(workingDirectory, null);
		expectedOutput = "";
		actualOutput = actualOutput.replace("\n", "");
		expectedOutput = expectedOutput.replace("\n", "");
		assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
		assertEquals(cuttool.getStatusCode(), 0);
	}

	/**
	 * Test for cut command with -c option given multiple files
	 * Checks for correct output after execution
	 */
	@Test
	public void testExecuteCOptionWithMultipleFiles()
	{
		String[] arguments = new String[]{"-c", "1-2","test1.txt","test2.txt"} ;
		cuttool = new CUTTool(arguments);
		actualOutput = cuttool.execute(workingDirectory, null);
		expectedOutput = "ap\nba\nca\ndo\n\n"+ "he\nwo\nco\nis\nfu\n";
		actualOutput = actualOutput.replace("\n", "");
		expectedOutput = expectedOutput.replace("\n", "");
		assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
		assertEquals(cuttool.getStatusCode(), 0);
	}

	/**
	 * Test for cut command with -c option given non-existing file name
	 * Checks for status code -1(failure) after execution
	 */
	@Test
	public void testExecuteCOptionWithFileMissing()
	{
		String[] arguments = new String[]{"-c", "1-2","file.txt"} ;
		cuttool = new CUTTool(arguments);
		cuttool.execute(workingDirectory, null);
		assertEquals(cuttool.getStatusCode(), -1);
	}

	/**
	 * Test for cut command with multiple -c options given standard input
	 * Both the options are executed
	 * Checks for correct output after execution
	 */
	@Test
	public void testExecuteCOptionRepeated()
	{
		String[] arguments = new String[]{"-c", "1-2", "-c", "3-4", "-"} ;
		cuttool = new CUTTool(arguments);
		actualOutput = cuttool.execute(workingDirectory, "abcde");
		expectedOutput = "abcd";
		actualOutput = actualOutput.replace("\n", "");
		expectedOutput = expectedOutput.replace("\n", "");
		assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
		assertEquals(cuttool.getStatusCode(), 0);
	}

	/**
	 * Test for cut command with multiple -d options given standard input
	 * The latest delimiter is considered for execution
	 * Checks for correct output after execution
	 */
	@Test
	public void testExecuteDOptionRepeated()
	{
		String[] arguments = new String[]{"-d", " ", "-d", ":", "-f","1-3","-"} ;
		cuttool = new CUTTool(arguments);
		actualOutput = cuttool.execute(workingDirectory, "one:two:three:four:five:six:seven");
		expectedOutput = "one:two:three";
		actualOutput = actualOutput.replace("\n", "");
		expectedOutput = expectedOutput.replace("\n", "");
		assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
		assertEquals(cuttool.getStatusCode(), 0);
	}

	/**
	 * Test for cut command with -c and -d option given standard input
	 * -c option is given priority
	 * Checks for correct output after execution
	 */
	@Test
	public void testExecuteCDOption()
	{
		String[] arguments = new String[]{"-c", "1-2", "-d", ":", "-"} ;
		cuttool = new CUTTool(arguments);
		actualOutput = cuttool.execute(workingDirectory, "one:two:three:four:five:six:seven");
		expectedOutput = "on";
		actualOutput = actualOutput.replace("\n", "");
		expectedOutput = expectedOutput.replace("\n", "");
		assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
		assertEquals(cuttool.getStatusCode(), 0);
	}

	/**
	 * Test for cut command with -c and -f option given standard input
	 * -c option is given priority
	 * Checks for correct output after execution
	 */
	@Test
	public void testExecuteCFOption()
	{
		String[] arguments = new String[]{"-c", "1-2", "-f", "2", "-"} ;
		cuttool = new CUTTool(arguments);
		actualOutput = cuttool.execute(workingDirectory, "one:two:three:four:five:six:seven");
		expectedOutput = "on";
		actualOutput= actualOutput.replace("\n", "");
		expectedOutput=expectedOutput.replace("\n", "");
		assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
		assertEquals(cuttool.getStatusCode(), 0);
	}

	/**
	 * Test for cut command with -c ,-d and -f option given standard input
	 * all options are executed
	 * Checks for correct output after execution
	 */
	@Test
	public void testExecuteCDFOptions()
	{
		String[] arguments = new String[]{"-c", "1-2", "-f", "2-4", "-d", ":","-"} ;
		cuttool = new CUTTool(arguments);
		actualOutput = cuttool.execute(workingDirectory, "one:two:three:four:five:six:seven");
		expectedOutput = "ontwo:three:four";
		actualOutput = actualOutput.replace("\n", "");
		expectedOutput = expectedOutput.replace("\n", "");
		assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
		assertEquals(cuttool.getStatusCode(), 0);
	}

	/**
	 * Test for cut command with -help option given standard input
	 * all options are executed
	 * Checks for correct output after execution
	 */
	@Test
	public void testExecuteHelp()
	{
		String[] arguments = new String[]{"-help"} ;
		cuttool = new CUTTool(arguments);
		actualOutput = cuttool.execute(workingDirectory, null);
		expectedOutput = helpOutput;
		actualOutput = actualOutput.replace("\n", "");
		expectedOutput = expectedOutput.replace("\n", "");
		assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
		assertEquals(cuttool.getStatusCode(), 0);
	}

	/**
	 * Test for cut specified characters interface given standard input and contiguous list 
	 * Checks for correct output after execution
	 * @throws IOException
	 */
	@Test
	public void testCutSpecfiedCharactersWithContiguousList() throws IOException {

		String[] arguments = null ;
		cuttool = new CUTTool(arguments);
		actualOutput = cuttool.cutSpecfiedCharacters("1-2", "abc");
		expectedOutput = "ab";
		actualOutput = actualOutput.replace("\n", "");
		expectedOutput = expectedOutput.replace("\n", "");
		assertTrue(expectedOutput.equals(actualOutput));
		assertEquals(cuttool.getStatusCode(), 0);
	}

	/**
	 * Test for cut specified characters interface given standard input and contiguous and discrete list 
	 * Checks for correct output after execution
	 * @throws IOException
	 */
	@Test
	public void testCutSpecfiedCharactersWithListContiguousAndDiscrete() throws IOException {
		String[] arguments = null ;
		cuttool = new CUTTool(arguments);
		actualOutput = cuttool.cutSpecfiedCharacters("1-4,5,6,10", "the quick brown fox jumps over the lazy dog");
		expectedOutput = "the qu ";
		actualOutput = actualOutput.replace("\n", "");
		expectedOutput = expectedOutput.replace("\n", "");
		assertTrue(expectedOutput.equals(actualOutput));
		assertEquals(cuttool.getStatusCode(), 0);
	}

	/**
	 * Test for cut specified characters interface given standard input 
	 * and contiguous and discrete list with negative values with the negative input truncated to 1
	 * Checks for correct output after execution
	 * @throws IOException
	 */
	@Test 
	public void testCutSpecfiedCharactersWithNegativeList() throws IOException {

		String[] arguments = null ;
		cuttool = new CUTTool(arguments);
		actualOutput = cuttool.cutSpecfiedCharacters("-2-3", "abc");
		expectedOutput = "ab";
		actualOutput = actualOutput.replace("\n", "");
		expectedOutput = expectedOutput.replace("\n", "");
		assertTrue(expectedOutput.equals(actualOutput));
		assertEquals(cuttool.getStatusCode(), 0);
	}

	/**
	 * Test for cut specified characters interface given empty input
	 * Checks for empty output after execution
	 * @throws IOException
	 */
	@Test
	public void testCutSpecfiedCharactersWithNoInput() throws IOException {

		String[] arguments = null ;
		cuttool = new CUTTool(arguments);
		actualOutput = cuttool.cutSpecfiedCharacters("", "");
		expectedOutput = "";
		actualOutput = actualOutput.replace("\n", "");
		expectedOutput = expectedOutput.replace("\n", "");
		assertTrue(expectedOutput.equals(actualOutput));
		assertEquals(cuttool.getStatusCode(), 0);
	}

	/**
	 * Test for cut specified characters interface given invalid arguments
	 * Checks for empty output and status code -1 after execution
	 * @throws IOException
	 */
	@Test
	public void testCutSpecfiedCharactersWithInvalidParams() throws IOException {

		String[] arguments = null ;
		cuttool = new CUTTool(arguments);
		actualOutput = cuttool.cutSpecfiedCharacters("dfsdsfds", "fds");
		expectedOutput = "";
		actualOutput = actualOutput.replace("\n", "");
		expectedOutput = expectedOutput.replace("\n", "");
		assertTrue(expectedOutput.equals(actualOutput));
		assertEquals(cuttool.getStatusCode(), -1);
	}

	/**
	 * Test for cut specified characters interface given null arguments
	 * Checks for empty output and status code -1 after execution
	 * @throws IOException
	 */
	@Test
	public void testCutSpecfiedCharactersWithNullParams() throws IOException {

		String[] arguments = null ;
		cuttool = new CUTTool(arguments);
		actualOutput = cuttool.cutSpecfiedCharacters(null, null);
		expectedOutput = "";
		actualOutput = actualOutput.replace("\n", "");
		expectedOutput = expectedOutput.replace("\n", "");
		assertTrue(expectedOutput.equals(actualOutput));
		assertEquals(cuttool.getStatusCode(), -1);
	}

	/**
	 * Test for cut specified characters use delimiter interface given invalid arguments
	 * Checks for empty output and status code -1 after execution
	 * @throws IOException
	 */
	@Test
	public void testCutSpecifiedCharactersUseDelimiterWithInvalidParams() throws IOException {

		String[] arguments = null ;
		cuttool = new CUTTool(arguments);	
		actualOutput = cuttool.cutSpecifiedCharactersUseDelimiter("invalidlist", ":" ,"a:b");
		expectedOutput = "";
		actualOutput = actualOutput.replace("\n", "");
		expectedOutput = expectedOutput.replace("\n", "");
		assertTrue(expectedOutput.equals(actualOutput));
		assertEquals(cuttool.getStatusCode(), -1);
	}
	/**
	 * Test for cut specified characters use delimiter interface given null arguments
	 * Checks for empty output and status code -1 after execution
	 * @throws IOException
	 */
	@Test
	public void testCutSpecifiedCharactersUseDelimiterWithNullParams() throws IOException {

		String[] arguments = null ;
		cuttool = new CUTTool(arguments);	
		actualOutput = cuttool.cutSpecifiedCharactersUseDelimiter(null,null,null);
		expectedOutput = "";
		actualOutput = actualOutput.replace("\n", "");
		expectedOutput = expectedOutput.replace("\n", "");
		assertTrue(expectedOutput.equals(actualOutput));
		assertEquals(cuttool.getStatusCode(), -1);
	}

	/**
	 * Test for cut specified characters use delimiter interface given empty std input
	 * Checks for empty output after execution
	 * @throws IOException
	 */
	@Test
	public void testCutSpecifiedCharactersUseDelimiterWithNoInput() throws IOException {

		String[] arguments = null ;
		cuttool = new CUTTool(arguments);	
		actualOutput = cuttool.cutSpecifiedCharactersUseDelimiter("5-", ":" ,"");
		expectedOutput = "";
		actualOutput = actualOutput.replace("\n", "");
		expectedOutput = expectedOutput.replace("\n", "");
		assertTrue(expectedOutput.equals(actualOutput));
		assertEquals(cuttool.getStatusCode(), 0);
	}
	/**
	 * Test for cut specified characters use delimiter interface given std input and list with no ending value
	 * 5- as LIST is interpreted as 5 till end of string
	 * Checks for correct output after execution
	 * @throws IOException
	 */
	@Test
	public void testCutSpecifiedCharactersUseDelimiterAndListWithNoEndingValue() throws IOException {

		String[] arguments = null ;
		cuttool = new CUTTool(arguments);	
		actualOutput = cuttool.cutSpecifiedCharactersUseDelimiter("5-", ":" ,"one:two:three:four:five:six:seven");
		expectedOutput = "five:six:seven";
		actualOutput = actualOutput.replace("\n", "");
		expectedOutput = expectedOutput.replace("\n", "");
		assertTrue(expectedOutput.equals(actualOutput));
		assertEquals(cuttool.getStatusCode(), 0);
	}
	/**
	 * Test for cut specified characters use delimiter interface given std input and list with no starting value
	 * -3 as LIST is interpreted as 1-3
	 * Checks for correct output after execution
	 * @throws IOException
	 */
	@Test
	public void testCutSpecifiedCharactersUseDelimiterTestAndListWithNoStartingValue() throws IOException {

		String[] arguments = null ;
		cuttool = new CUTTool(arguments);
		actualOutput = cuttool.cutSpecifiedCharactersUseDelimiter("-3", ":" ,"one:two:three:four:five:six:seven");
		expectedOutput = "one:two:three";
		actualOutput = actualOutput.replace("\n", "");
		expectedOutput = expectedOutput.replace("\n", "");
		assertTrue(expectedOutput.equals(actualOutput));
		assertEquals(cuttool.getStatusCode(), 0);
	}	

	/**
	 * Test for cut specified characters use delimiter interface given std input and list exceeding length of input
	 * list is truncated to length of input
	 * Checks for correct output after execution
	 * @throws IOException
	 */
	@Test
	public void testCutSpecifiedCharactersUseDelimiterAndListExceedingLengthOfInput() throws IOException {

		String[] arguments = null ;
		cuttool = new CUTTool(arguments);
		actualOutput = cuttool.cutSpecifiedCharactersUseDelimiter("1-10", ":" ,"one:two:three:four:five:six:seven");
		expectedOutput = "one:two:three:four:five:six:seven";
		actualOutput = actualOutput.replace("\n", "");
		expectedOutput = expectedOutput.replace("\n", "");
		assertTrue(expectedOutput.equals(actualOutput));
		assertEquals(cuttool.getStatusCode(), 0);
	}	
	/**
	 * Test for cut specified characters use delimiter interface given std input and non-existing delimiter
	 * Checks for correct output after execution
	 * @throws IOException
	 */
	@Test
	public void testCutSpecifiedCharactersUseNonExistingDelimiterAndListSingleInput() throws IOException {

		String[] arguments = null ;
		cuttool = new CUTTool(arguments);
		actualOutput = cuttool.cutSpecifiedCharactersUseDelimiter("3", " " ,"foo:bar:baz:qux:quux");
		expectedOutput = "foo:bar:baz:qux:quux";
		actualOutput = actualOutput.replace("\n", "");
		expectedOutput = expectedOutput.replace("\n", "");
		assertTrue(expectedOutput.equals(actualOutput));
		assertEquals(cuttool.getStatusCode(), 0);
	}
	/**
	 * Test for cut specified characters use delimiter interface given std input and single number list
	 * Checks for correct output after execution
	 * @throws IOException
	 */
	@Test
	public void testCutSpecifiedCharactersUseDelimiterAndListSingleInput() throws IOException {

		String[] arguments = null ;
		cuttool = new CUTTool(arguments);
		actualOutput = cuttool.cutSpecifiedCharactersUseDelimiter("3", " " ,"the quick brown fox jumps over the lazy dog");
		expectedOutput = "brown";
		actualOutput = actualOutput.replace("\n", "");
		expectedOutput = expectedOutput.replace("\n", "");
		assertTrue(expectedOutput.equals(actualOutput));
		assertEquals(cuttool.getStatusCode(), 0);

	}


}
