package sg.edu.nus.comp.cs4218.impl.extended1;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;
import java.util.ArrayList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import sg.edu.nus.comp.cs4218.extended1.IGrepTool;

public class GREPToolTest {
	private IGrepTool grepTool;
	private static final String CONTENT_STORY = "A gurl was walkin 2 skewl wit her bf\n"
			+ "n they were crossin da rode.\n"
			+ "she sed \"bbz will u luv me 4evr?\"\n"
			+ "he said \"NO\".\n"
			+ "da gurl cryed N ran across da rode b4 green man came on the sine.\n"
			+ "boy was cryin and went to pic up her body.\n"
			+ "she was ded.\n"
			+ "he whispered 2 her corpse \"I ment 2 sey i will luv u...FIVE-ever\"\n"
			+ "(dat mean he luv her moar den 4evr)\n"
			+ "xxx~*...like dis if u cry evry time...~*xxx\n";
	private static final String CONTENT_ALPHABET = "a\na\na\na\na\na\na\na\n"
			+ "b\nb\nb\nb\nb\n" + "c\nc\n" + "z\n";
	private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
	private final File workingDir = new File(System.getProperty("user.dir"));

	@Before
	public void before() {
		String[] arguments = new String[0];
		grepTool = new GREPTool(arguments);
		System.setErr(new PrintStream(errContent)); // to test error output
	}

	@After
	public void after() {
		grepTool = null;
	}

	/**
	 * Test for grep command on an empty file. Checks for empty result.
	 */
	@Test
	public void testExecuteEmptyFile() {
		String[] cmdArgs = { "../../textFiles/empty.txt" };
		IGrepTool tool = new GREPTool(cmdArgs);
		assertTrue("".equals(tool.execute(workingDir, "")));
		assertEquals(tool.getStatusCode(), 0);
	}

	/**
	 * Test for grep command on a binary file. Checks for empty result.
	 */
	@Test
	public void testExecuteBinaryFile() {
		String[] cmdArgs = { "../../textFiles/picture.gif" };
		IGrepTool tool = new GREPTool(cmdArgs);
		assertTrue("".equals(tool.execute(workingDir, "")));
		assertEquals(tool.getStatusCode(), 0);
	}

	/**
	 * Several tests for invalid arguments in a grep command. Checks for correct
	 * error exit status after execution.
	 */
	@Test
	public void testExecuteInvalidArguments() {
		ArrayList<String[]> invalidCmdArgsList = new ArrayList<String[]>();
		String[] cmdArgs1 = {}; // grep
		String[] cmdArgs2 = { "-" }; // grep -
		String[] cmdArgs3 = { "-o" }; // grep -o
		String[] cmdArgs4 = { "-A" }; // grep -A
		String[] cmdArgs5 = { "-A", "a" }; // grep -A a
		String[] cmdArgs6 = { "-A", "5", "-k", "-o" }; // grep -A 5 -k -o
		String[] cmdArgs7 = { "-A", "5", "-A", "-o" }; // grep -A 5 -A -o
		String[] cmdArgs8 = { "-C", "-5" }; // grep -C -5
		invalidCmdArgsList.add(cmdArgs1);
		invalidCmdArgsList.add(cmdArgs2);
		invalidCmdArgsList.add(cmdArgs3);
		invalidCmdArgsList.add(cmdArgs4);
		invalidCmdArgsList.add(cmdArgs5);
		invalidCmdArgsList.add(cmdArgs6);
		invalidCmdArgsList.add(cmdArgs7);
		invalidCmdArgsList.add(cmdArgs8);
		for (String[] cmdArgs : invalidCmdArgsList) {
			IGrepTool tool = new GREPTool(cmdArgs);
			try {
				tool.execute(workingDir, "");
			} catch (IllegalArgumentException e) {
				assertEquals(GREPTool.GREP_ERR_CODE, tool.getStatusCode());
				continue;
			}
			//erm whats this
			assertTrue(false);
		}
	}

	/**
	 * Test for null stdin in grep command. Should not fail. Instead, convert
	 * stdin to empty string.
	 */
	@Test
	public void testExecuteNullStdin() {
		String[] cmdArgs = { "pattern" };
		IGrepTool tool = new GREPTool(cmdArgs);
		assertEquals("", tool.execute(workingDir, ""));
	}

	/** Changed : exception not thrown,just error message"
	/**
	 * Test for cat. Destructive case where workingDir is not valid. Should
	 * throw exception
	 */
	@Test
	public void testExecuteNullWorkingDir() {
		String[] cmdArgs = { "pattern", "someFile" };
		IGrepTool tool = new GREPTool(cmdArgs);
		assertEquals("", tool.execute(null, ""));
	}

	/**
	 * Several tests for invalid files passed in as arguments in a grep command.
	 * Checks for correct error exit status and error message after execution.
	 */
	@Test
	public void testExecuteOneFileNotFound() {
		ArrayList<String[]> cmdArgsList = new ArrayList<String[]>();
		String[] cmdArgs1 = { "-c", "pattern", "filenotfound" };
		String[] cmdArgs2 = { "-o", "pattern", "filenotfound" };
		String[] cmdArgs3 = { "-v", "pattern", "filenotfound" };
		String[] cmdArgs4 = { "-A", "1", "pattern", "filenotfound" };
		String[] cmdArgs5 = { "-B", "1", "pattern", "filenotfound" };
		String[] cmdArgs6 = { "-B", "1", "pattern", "filenotfound" };
		cmdArgsList.add(cmdArgs1);
		cmdArgsList.add(cmdArgs2);
		cmdArgsList.add(cmdArgs3);
		cmdArgsList.add(cmdArgs4);
		cmdArgsList.add(cmdArgs5);
		cmdArgsList.add(cmdArgs6);
		for (String[] cmdArgs : cmdArgsList) {
			IGrepTool tool = new GREPTool(cmdArgs);
			assertTrue("".equals(tool.execute(workingDir, "")));
			String errorMessage = errContent.toString();
			assertTrue(String
					.format(GREPTool.GREP_FILE_ERR_MSG, "filenotfound").equals(
							errorMessage));
			assertEquals(GREPTool.GREP_ERR_CODE, tool.getStatusCode());
			errContent.reset();
		}
	}

	/**
	 * Test for normal grep command with multiple files passed in as arguments.
	 * Checks for correct output after execution.
	 */
	@Test
	public void testExecuteMultipleFiles() {
		//file problem
		String[] cmdArgs = { " over", "../../textFiles/testA.txt",
				"../textFiles/filenotfound" };
		String expected = "../textFiles/testA.txt:\njumped over\n";
		IGrepTool tool = new GREPTool(cmdArgs);
		assertTrue(expected.equals(tool.execute(workingDir, "")));
		assertTrue(tool.getStatusCode() != 0);
		String errorMessage = errContent.toString();
		assertTrue(String.format(GREPTool.GREP_FILE_ERR_MSG,
				"../textFiles/filenotfound").equals(errorMessage));
	}

	/**
	 * Test for grep command with -c option with multiple files passed in as
	 * arguments. Checks for correct output after execution.
	 */
	@Test
	public void testExecuteCountOptionMultipleFiles() {
		//file issues
		String[] cmdArgs = { "-c", "(t|T)", "../textFiles/testA.txt",
				"../textFiles/testB.txt" };
		String expected = "../textFiles/testA.txt:\n2\n../textFiles/testB.txt:\n2\n";
		IGrepTool tool = new GREPTool(cmdArgs);
		assertTrue(expected.equals(tool.execute(workingDir, "")));
		assertEquals(tool.getStatusCode(), 0);
	}

	/**
	 * Test for grep command with -c option with standard input (denoted by -).
	 * Checks for correct output after execution.
	 */
	@Test
	public void testExecuteCountOptionStdin() {
		String[] cmdArgs = { "-c", "(t|T)", "-" };
		String expected = "2\n";
		IGrepTool tool = new GREPTool(cmdArgs);
		System.out.println(tool.execute(workingDir, "a\nT\nt\na\n"));
		assertTrue(expected.equals(tool.execute(workingDir, "a\nT\nt\na\n")));
		assertEquals(tool.getStatusCode(), 0);
	}

	/**
	 * Test for grep command with -o option with multiple files passed in as
	 * arguments. Checks for correct output after execution.
	 */
	@Test
	public void testExecuteOOptionMultipleFiles() {
		//file prob
		String[] cmdArgs = { "-o", "[a-z]*", "../textFiles/testA.txt",
				"../textFiles/testB.txt" };
		String expected = "../textFiles/testA.txt:\nhe\nquick\nbrown\nfox\njumped\nover\nthe\nlazy\ndog\n"
				+ "../textFiles/testB.txt:\needs\nare\nflowers\ntoo\nonce\nyou\nget\nto\nknow\nthem\n";
		IGrepTool tool = new GREPTool(cmdArgs);
		assertTrue(expected.equals(tool.execute(workingDir, "")));
		assertEquals(tool.getStatusCode(), 0);
	}

	/**
	 * Test for grep command with -c option with standard input (denoted by -).
	 * Checks for correct output after execution.
	 */
	@Test
	public void testExecuteOOptionStdinMultipleFiles() {
		String[] cmdArgs = { "-o", "[a-z]*", "-", "../textFiles/testA.txt",
				"../textFiles/testB.txt" };
		String expected = "Standard Input:\ne\nr\ni\n../textFiles/testA.txt:\nhe\nquick\nbrown\nfox\njumped\nover\nthe\nlazy\ndog\n"
				+ "../textFiles/testB.txt:\needs\nare\nflowers\ntoo\nonce\nyou\nget\nto\nknow\nthem\n";
		IGrepTool tool = new GREPTool(cmdArgs);
		System.out.println(tool.execute(workingDir, "e\nr\ni\nN\n")+"lol");
		assertTrue(expected.equals(tool.execute(workingDir, "e\nr\ni\nN\n")));
		assertEquals(tool.getStatusCode(), 0);
	}

	/**
	 * Test for grep command with -o option with multiple files passed in as
	 * arguments and an empty string passed in as input. Checks for empty result
	 * after execution.
	 */
	@Test
	public void testExecuteOOptionNoResultMultipleFiles() {
		//file problem
		String[] cmdArgs = { "-o", "", "../textFiles/testA.txt",
				"../textFiles/testB.txt" };
		String expected = "";
		IGrepTool tool = new GREPTool(cmdArgs);
		assertTrue(expected.equals(tool.execute(workingDir, "")));
		assertEquals(tool.getStatusCode(), 0);
	}

	/**
	 * Test for grep command with -v option with multiple files passed in as
	 * arguments. Checks for correct output after execution.
	 */
	@Test
	public void testExecuteVOptionMultipleFiles() {
		//file not found problem
		String[] cmdArgs = { "-v", " \\w", "..\textFiles\testA.txt",
				"..\textFiles\testB.txt" };
		String expected = "..\textFiles\testA.txt:\ndog.\n";
		IGrepTool tool = new GREPTool(cmdArgs);
		assertTrue(expected.equals(tool.execute(workingDir, "")));
		assertEquals(tool.getStatusCode(), 0);
	}

	/**
	 * Test for grep command with -v option with standard input. Checks for
	 * correct output after execution.
	 */
	@Test
	public void testExecuteVOptionStdin() {
		String[] cmdArgs = { "-v", " " };
		String expected = "hello\n";
		IGrepTool tool = new GREPTool(cmdArgs);
		assertTrue(expected.equals(tool.execute(workingDir,
				"hello\ni am here for you\n")));
		assertEquals(tool.getStatusCode(), 0);
	}

	/**
	 * Test for grep command with -A option with extra options and multiple
	 * files passed in as arguments. Extra options should be ignored. Checks for
	 * correct output after execution.
	 */
	@Test
	public void testExecuteAOptionExtraOptionsMultipleFiles() {
		String[] cmdArgs = { "-A", "2", "-o", "-v", "k", "../textFiles/testA.txt",
				"../textFiles/testB.txt" };
		String expected = "../textFiles/testA.txt:\nThe quick\nbrown fox\njumped over\n../textFiles/testB.txt:\n  once you get to know them.\n";
		IGrepTool tool = new GREPTool(cmdArgs);
		assertTrue(expected.equals(tool.execute(workingDir, "")));
		assertEquals(tool.getStatusCode(), 0);
	}

	/**
	 * Test for grep command with -B option with multiple files passed in as
	 * arguments. Checks for correct output after execution.
	 */
	@Test
	public void testExecuteBOptionMultipleFiles() {
		String[] cmdArgs = { "-B", "1", "(T|W)", "../textFiles/testA.txt",
				"../textFiles/testB.txt" };
		String expected = "../textFiles/testA.txt:\nThe quick\n../textFiles/testB.txt:\nWeeds are flowers,\n";
		IGrepTool tool = new GREPTool(cmdArgs);
		assertTrue(expected.equals(tool.execute(workingDir, "")));
		assertEquals(tool.getStatusCode(), 0);
	}

	/**
	 * Test for grep command with -C option with multiple files passed in as
	 * arguments. Checks for correct output after execution.
	 */
	@Test
	public void testExecuteCOptionMultipleFiles() {
		String[] cmdArgs = { "-C", "2", "(brown|fox|too)",
				"../textFiles/testA.txt", "../textFiles/testB.txt" };
		String expected = "../textFiles/testA.txt:\nThe quick\nbrown fox\njumped over\nthe lazy \n../textFiles/testB.txt:\nWeeds are flowers,\n too,\n  once you get to know them.\n";
		IGrepTool tool = new GREPTool(cmdArgs);
		assertTrue(expected.equals(tool.execute(workingDir, "")));
		assertEquals(tool.getStatusCode(), 0);
	}

	/**
	 * Test for grep command with -help as first argument. Expected correct help
	 * text as output.
	 */
	/*
	@Test
	public void testExecuteHelpExtraOptions() {
		String[] cmdArgs = { "-help", "-o", "nonsense" };
		String expected = "Usage: grep [OPTIONS] PATTERN [FILE]\n"
				+ "Search for PATTERN in each FILE or standard input.\n"
				+ "PATTERN - This specifies a regular expression pattern that describes a set of strings.\n"
				+ "FILE - Name of the file, when no file is present (denoted by \"-\") uses standard input.\n"
				+ "OPTIONS\n"
				+ "	-A NUM : Print NUM lines of trailing context after matching lines\n"
				+ "	-B NUM : Print NUM lines of leading context before matching lines\n"
				+ "	-C NUM : Print NUM lines of output context\n"
				+ "	-c : Suppress normal output. Instead print a count of matching lines for each input file\n"
				+ "	-v : Select non-matching (instead of matching) lines\n";
		IGrepTool tool = new GREPTool(cmdArgs);
		assertTrue(expected.equals(tool.execute(workingDir, "")));
		assertEquals(tool.getStatusCode(), 0);
	}
	*/

	/**
	 * Test for interface method getHelp(). Expects the correct help text to be
	 * returned.
	 */
	@Test
	public void testGrepGetHelp() {
		String helpText = grepTool.getHelp();
		String expected = "Usage: grep [OPTIONS] PATTERN [FILE]\n"
				+ "Search for PATTERN in each FILE or standard input.\n"
				+ "PATTERN - This specifies a regular expression pattern that describes a set of strings.\n"
				+ "FILE - Name of the file, when no file is present (denoted by \"-\") uses standard input.\n"
				+ "OPTIONS\n"
				+ "	-A NUM : Print NUM lines of trailing context after matching lines\n"
				+ "	-B NUM : Print NUM lines of leading context before matching lines\n"
				+ "	-C NUM : Print NUM lines of output context\n"
				+ "	-c : Suppress normal output. Instead print a count of matching lines for each input file\n"
				+ "	-v : Select non-matching (instead of matching) lines\n";
		assertTrue(expected.equals(helpText));
		assertEquals(grepTool.getStatusCode(), 0);
	}

	/**
	 * Test for interface method getMatchingLinesWithTrailingContext(int
	 * option_A, String pattern, String input) with empty strings as parameters.
	 * Checks for empty result.
	 */
	@Test
	public void testMatchingLinesTrailingContextNoParams() {
		String matchingLines = grepTool.getMatchingLinesWithTrailingContext(0,
				"", "");
		assertTrue("".equals(matchingLines));
	}

	/**
	 * Test for interface method getMatchingLinesWithTrailingContext(int
	 * option_A, String pattern, String input) with invalid parameters. Checks
	 * for empty result and error exit code.
	 */
	@Test
	public void testMatchingLinesTrailingContextInvalidParams() {
		String matchingLines = grepTool.getMatchingLinesWithTrailingContext(-1,
				"", "");
		assertTrue("".equals(matchingLines));
		assertTrue(grepTool.getStatusCode() != 0);
	}

	/**
	 * Test for interface method getMatchingLinesWithTrailingContext(int
	 * option_A, String pattern, String input) with null parameters. Checks for
	 * empty result and error exit code.
	 */
	@Test
	public void testMatchingLinesTrailingContextNullParams() {
		String matchingLines = grepTool.getMatchingLinesWithTrailingContext(1,
				null, "");
		assertTrue("".equals(matchingLines));
		assertTrue(grepTool.getStatusCode() != 0);
	}

	/**
	 * Test for interface method getMatchingLinesWithTrailingContext(int
	 * option_A, String pattern, String input) with empty string input. Checks
	 * for empty result.
	 */
	@Test
	public void testMatchingLinesTrailingContextNoInput() {
		String matchingLines = grepTool.getMatchingLinesWithTrailingContext(0,
				"some match", "");
		assertTrue("".equals(matchingLines));
	}

	/**
	 * Test for interface method getMatchingLinesWithTrailingContext(int
	 * option_A, String pattern, String input) with no trailing lines and
	 * pattern that doesn't match any lines of input. Checks for empty result.
	 */
	@Test
	public void testMatchingLinesTrailingContextNoMatchNoContext() {
		String matchingLines = grepTool.getMatchingLinesWithTrailingContext(0,
				"^$", CONTENT_ALPHABET);
		assertTrue("".equals(matchingLines));
	}

	/**
	 * Test for interface method getMatchingLinesWithTrailingContext(int
	 * option_A, String pattern, String input) with many trailing lines and
	 * pattern that doesn't match any lines of input. Checks for empty result.
	 */
	@Test
	public void testMatchingLinesTrailingContextNoMatchWithContext() {
		String matchingLines = grepTool.getMatchingLinesWithTrailingContext(
				100, "^$", CONTENT_ALPHABET);
		assertTrue("".equals(matchingLines));
	}

	/**
	 * Test for interface method getMatchingLinesWithTrailingContext(int
	 * option_A, String pattern, String input) with no trailing lines and
	 * pattern that matches all lines of input. Checks for correct output.
	 */
	@Test
	public void testMatchingLinesTrailingContextFullMatchNoContext() {
		String matchingLines = grepTool.getMatchingLinesWithTrailingContext(0,
				"[a-z]", CONTENT_ALPHABET);
		assertTrue(CONTENT_ALPHABET.equals(matchingLines));
	}

	/**
	 * Test for interface method getMatchingLinesWithTrailingContext(int
	 * option_A, String pattern, String input) with overlapping trailing lines
	 * and pattern that matches all lines of input. Checks for correct output.
	 */
	@Test
	public void testMatchingLinesTrailingContextFullMatchWithContext() {
		String matchingLines = grepTool.getMatchingLinesWithTrailingContext(
				675, "(.*)", CONTENT_ALPHABET);
		assertTrue(CONTENT_ALPHABET.equals(matchingLines));
	}

	/**
	 * Test for interface method getMatchingLinesWithTrailingContext(int
	 * option_A, String pattern, String input) with no trailing lines. Checks
	 * for correct output.
	 */
	@Test
	public void testMatchingLinesTrailingContextNoContext() {
		String matchingLines = grepTool.getMatchingLinesWithTrailingContext(0,
				"c", CONTENT_ALPHABET);
		assertTrue("c\nc\n".equals(matchingLines));
	}

	/**
	 * Test for interface method getMatchingLinesWithTrailingContext(int
	 * option_A, String pattern, String input) with pattern written in regex.
	 * Different sections of matching lines should be separated with "--".Checks
	 * for correct output.
	 */
	@Test
	public void testMatchingLinesTrailingContextWithRegex() {
		String matchingLines = grepTool.getMatchingLinesWithTrailingContext(1,
				"(a|b|z)", CONTENT_ALPHABET);
		//check with mac
		assertTrue("a\na\na\na\na\na\na\na\nb\nb\nb\nb\nb\nc\n--\nz\n"
				.equals(matchingLines));
	}

	/**
	 * Test for interface method getMatchingLinesWithTrailingContext(int
	 * option_A, String pattern, String input) with pattern matching the first
	 * word. Checks for correct output.
	 */
	@Test
	public void testMatchingLinesTrailingContextFirstWord() {
		String matchingLines = grepTool.getMatchingLinesWithTrailingContext(2,
				"A ", CONTENT_STORY);
		String expected = "A gurl was walkin 2 skewl wit her bf\nn they were crossin da rode.\nshe sed \"bbz will u luv me 4evr?\"\n";
		assertTrue(expected.equals(matchingLines));
	}

	/**
	 * Test for interface method getMatchingLinesWithTrailingContext(int
	 * option_A, String pattern, String input) with pattern matching the last
	 * word. Checks for correct output.
	 */
	@Test
	public void testMatchingLinesTrailingContextLastWord() {
		String matchingLines = grepTool.getMatchingLinesWithTrailingContext(2,
				"(~*xxx)", CONTENT_STORY);
		String expected = "xxx~*...like dis if u cry evry time...~*xxx\n";
		assertTrue(expected.equals(matchingLines));
	}

	/**
	 * Test for interface method getMatchingLinesWithLeadingContext(int
	 * option_B, String pattern, String input) with empty strings as parameters.
	 * Checks for empty result.
	 */
	@Test
	public void testMatchingLinesLeadingContextNoParams() {
		String matchingLines = grepTool.getMatchingLinesWithLeadingContext(0,
				"", "");
		assertTrue("".equals(matchingLines));
	}

	/**
	 * Test for interface method getMatchingLinesWithLeadingContext(int
	 * option_B, String pattern, String input) with invalid parameters. Checks
	 * for empty result.
	 */
	@Test
	public void testMatchingLinesLeadingContextInvalidParams() {
		String matchingLines = grepTool.getMatchingLinesWithLeadingContext(
				-100, "", "");
		assertTrue("".equals(matchingLines));
		assertTrue(grepTool.getStatusCode() != 0);
	}

	/**
	 * Test for interface method getMatchingLinesWithLeadingContext(int
	 * option_B, String pattern, String input) with null parameters. Checks for
	 * empty result.
	 */
	@Test
	public void testMatchingLinesLeadingContextNullParams() {
		String matchingLines = grepTool.getMatchingLinesWithLeadingContext(0,
				null, null);
		assertTrue("".equals(matchingLines));
		assertTrue(grepTool.getStatusCode() != 0);
	}

	/**
	 * Test for interface method getMatchingLinesWithLeadingContext(int
	 * option_B, String pattern, String input) with empty string input. Checks
	 * for empty result.
	 */
	@Test
	public void testMatchingLinesLeadingContextNoInput() {
		String matchingLines = grepTool.getMatchingLinesWithLeadingContext(0,
				"some match", "");
		assertTrue("".equals(matchingLines));
	}

	/**
	 * Test for interface method getMatchingLinesWithLeadingContext(int
	 * option_B, String pattern, String input) with no leading lines and pattern
	 * that doesn't match any lines of input. Checks for empty result.
	 */
	@Test
	public void testMatchingLinesLeadingContextNoMatchNoContext() {
		String matchingLines = grepTool.getMatchingLinesWithLeadingContext(0,
				"d", CONTENT_ALPHABET);
		assertTrue("".equals(matchingLines));
	}

	/**
	 * Test for interface method getMatchingLinesWithLeadingContext(int
	 * option_B, String pattern, String input) with leading lines and pattern
	 * that doesn't match any lines of input. Checks for empty result.
	 */
	@Test
	public void testMatchingLinesLeadingContextNoMatchWithContext() {
		String matchingLines = grepTool.getMatchingLinesWithLeadingContext(1,
				"thisdunmatchbro", CONTENT_ALPHABET);
		assertTrue("".equals(matchingLines));
	}

	/**
	 * Test for interface method getMatchingLinesWithLeadingContext(int
	 * option_B, String pattern, String input) with no leading lines and pattern
	 * that matches all lines of input. Checks for correct output.
	 */
	@Test
	public void testMatchingLinesLeadingContextFullMatchNoContext() {
		String matchingLines = grepTool.getMatchingLinesWithLeadingContext(0,
				"[a-z]", CONTENT_ALPHABET);
		assertTrue(CONTENT_ALPHABET.equals(matchingLines));
	}

	/**
	 * Test for interface method getMatchingLinesWithLeadingContext(int
	 * option_B, String pattern, String input) with overlapping leading lines
	 * and pattern that matches all lines of input. Checks for correct output.
	 */
	@Test
	public void testMatchingLinesLeadingContextFullMatchWithContext() {
		String matchingLines = grepTool.getMatchingLinesWithLeadingContext(10,
				"", CONTENT_ALPHABET);
		//check output on mac
		assertTrue(CONTENT_ALPHABET.equals(matchingLines));
	}

	/**
	 * Test for interface method getMatchingLinesWithLeadingContext(int
	 * option_B, String pattern, String input) with no leading lines. Checks for
	 * correct output.
	 */
	@Test
	public void testMatchingLinesLeadingContextNoContext() {
		String matchingLines = grepTool.getMatchingLinesWithLeadingContext(0,
				"z", CONTENT_ALPHABET);
		assertTrue("z\n".equals(matchingLines));
	}

	/**
	 * Test for interface method getMatchingLinesWithLeadingContext(int
	 * option_B, String pattern, String input) with pattern written in regex.
	 * Different sections of matching lines should be separated with "--".Checks
	 * for correct output.
	 */
	@Test
	public void testMatchingLinesLeadingContextWithRegex() {
		String matchingLines = grepTool.getMatchingLinesWithLeadingContext(1,
				"(a|b|z)", CONTENT_ALPHABET);
		//check on mac
		assertTrue("a\na\na\na\na\na\na\na\nb\nb\nb\nb\nb\n--\nc\nz\n"
				.equals(matchingLines));
	}

	/**
	 * Test for interface method getMatchingLinesWithLeadingContext(int
	 * option_B, String pattern, String input) with pattern matching the first
	 * word. Checks for correct output.
	 */
	@Test
	public void testMatchingLinesLeadingContextFirstWord() {
		String matchingLines = grepTool.getMatchingLinesWithLeadingContext(2,
				"A ", CONTENT_STORY);
		String expected = "A gurl was walkin 2 skewl wit her bf\n";
		assertTrue(expected.equals(matchingLines));
	}

	/**
	 * Test for interface method getMatchingLinesWithLeadingContext(int
	 * option_B, String pattern, String input) with pattern matching the last
	 * word. Checks for correct output.
	 */
	@Test
	public void testMatchingLinesLeadingContextLastWord() {
		String matchingLines = grepTool.getMatchingLinesWithLeadingContext(2,
				"(~*xxx)", CONTENT_STORY);
		String expected = "he whispered 2 her corpse \"I ment 2 sey i will luv u...FIVE-ever\"\n(dat mean he luv her moar den 4evr)\nxxx~*...like dis if u cry evry time...~*xxx\n";
		assertTrue(expected.equals(matchingLines));
	}

	/**
	 * Test for interface method getMatchingLinesWithOutputContext(int option_C,
	 * String pattern, String input) with empty strings as parameters. Checks
	 * for empty result.
	 */
	@Test
	public void testMatchingLinesOutputContextNoParams() {
		String matchingLines = grepTool.getMatchingLinesWithOutputContext(0,
				"", "");
		assertTrue("".equals(matchingLines));
	}

	/**
	 * Test for interface method getMatchingLinesWithOutputContext(int option_C,
	 * String pattern, String input) with invalid parameters. Checks for empty
	 * result.
	 */
	@Test
	public void testMatchingLinesOutputContextInvalidParams() {
		String matchingLines = grepTool.getMatchingLinesWithOutputContext(-1,
				"", "");
		assertTrue("".equals(matchingLines));
		assertTrue(grepTool.getStatusCode() != 0);
	}

	/**
	 * Test for interface method getMatchingLinesWithOutputContext(int option_C,
	 * String pattern, String input) with null parameters. Checks for empty
	 * result.
	 */
	@Test
	public void testMatchingLinesOutputContexNullParams() {
		String matchingLines = grepTool.getMatchingLinesWithOutputContext(1,
				"", null);
		assertTrue("".equals(matchingLines));
		assertTrue(grepTool.getStatusCode() != 0);
	}

	/**
	 * Test for interface method getMatchingLinesWithOutputContext(int option_C,
	 * String pattern, String input) with empty string input. Checks for empty
	 * result.
	 */
	@Test
	public void testMatchingLinesOutputContextNoInput() {
		String matchingLines = grepTool.getMatchingLinesWithOutputContext(0,
				"some match", "");
		assertTrue("".equals(matchingLines));
	}

	/**
	 * Test for interface method getMatchingLinesWithOutputContext(int option_C,
	 * String pattern, String input) with no output context and pattern that
	 * doesn't match any lines of input. Checks for empty result.
	 */
	@Test
	public void testMatchingLinesOutputContextNoMatch() {
		String matchingLines = grepTool.getMatchingLinesWithOutputContext(0,
				"d", CONTENT_ALPHABET);
		assertTrue("".equals(matchingLines));
	}

	/**
	 * Test for interface method getMatchingLinesWithOutputContext(int option_C,
	 * String pattern, String input) with no leading lines and pattern that
	 * matches all lines of input. Checks for correct output.
	 */
	@Test
	public void testMatchingLinesOutputContextFullMatchNoContext() {
		String matchingLines = grepTool.getMatchingLinesWithOutputContext(0,
				"[a-z]", CONTENT_ALPHABET);
		assertTrue(CONTENT_ALPHABET.equals(matchingLines));
	}

	/**
	 * Test for interface method getMatchingLinesWithOutputContext(int option_C,
	 * String pattern, String input) with overlapping context and pattern that
	 * matches all lines of input. Checks for correct output.
	 */
	@Test
	public void testMatchingLinesOutputContextFullMatchWithContext() {
		String matchingLines = grepTool.getMatchingLinesWithOutputContext(10,
				"[a-z]", CONTENT_ALPHABET);
		//check on mac
		assertTrue(CONTENT_ALPHABET.equals(matchingLines));
	}

	/**
	 * Test for interface method getMatchingLinesWithOutputContext(int option_C,
	 * String pattern, String input) with no context. Checks for correct output.
	 */
	@Test
	public void testMatchingLinesOutputContextNoContext() {
		String matchingLines = grepTool.getMatchingLinesWithOutputContext(0,
				"a", CONTENT_ALPHABET);
		assertTrue("a\na\na\na\na\na\na\na\n".equals(matchingLines));
		matchingLines = grepTool.getMatchingLinesWithOutputContext(0, "z",
				CONTENT_ALPHABET);
		assertTrue("z\n".equals(matchingLines));
	}

	/**
	 * Test for interface method getMatchingLinesWithOutputContext(int option_C,
	 * String pattern, String input) with pattern written in regex. Different
	 * sections of matching lines should be separated with "--".Checks for
	 * correct output. ASSUMPTION: grep -C prints around the context and does
	 * not combine different sections that are close to each other.
	 */
	@Test
	public void testMatchingLinesOutputContextWithRegex() {
		String matchingLines = grepTool.getMatchingLinesWithOutputContext(2,
				"(b|z)", CONTENT_ALPHABET);
		assertTrue("a\na\nb\nb\nb\nb\nb\nc\nc\n--\nc\nc\nz\n"
				.equals(matchingLines));
	}

	/**
	 * Test for interface method getMatchingLinesWithOutputContext(int option_C,
	 * String pattern, String input) with pattern that contains one character.
	 * Different sections of matching lines should be separated with "--".Checks
	 * for correct output. ASSUMPTION: grep -C prints around the context and
	 * does not combine different sections that are close to each other.
	 */
	@Test
	public void testMatchingLinesOutputContextWithCharacter() {
		String matchingLines = grepTool.getMatchingLinesWithOutputContext(1,
				"c", CONTENT_ALPHABET);
		//check mac
		assertTrue("b\nc\nc\nz\n".equals(matchingLines));
	}

	/**
	 * Test for interface method getMatchingLinesWithOutputContext(int option_C,
	 * String pattern, String input) with overlapping context. Different
	 * sections of matching lines should be separated with "--".Checks for
	 * correct output.
	 */
	@Test
	public void testMatchingLinesOutputContextOverlappingContext() {
		String matchingLines = grepTool.getMatchingLinesWithOutputContext(3,
				"(b|z)", CONTENT_ALPHABET);
		//check mac
		assertTrue("a\na\na\nb\nb\nb\nb\nb\nc\nc\nz\n".equals(matchingLines));
	}

	/**
	 * Test for interface method getMatchingLinesWithOutputContext(int option_C,
	 * String pattern, String input) with overlapping context. Different
	 * sections of matching lines should be separated with "--".Checks for
	 * correct output.
	 */
	@Test
	public void testMatchingLinesOutputContextHighContext() {
		String matchingLines = grepTool.getMatchingLinesWithOutputContext(300,
				"(b|z)", CONTENT_ALPHABET);
		String expected = "a\na\na\na\na\na\na\na\n" + "b\nb\nb\nb\nb\n"
				+ "c\nc\n" + "z\n";
		assertTrue(expected.equals(matchingLines));
	}

	/**
	 * Test for interface method getMatchingLinesWithTrailingContext(int
	 * option_A, String pattern, String input) with pattern matching the first
	 * word. Checks for correct output.
	 */
	@Test
	public void testMatchingLinesOutputContextFirstWord() {
		String matchingLines = grepTool.getMatchingLinesWithOutputContext(2,
				"A ", CONTENT_STORY);
		String expected = "A gurl was walkin 2 skewl wit her bf\nn they were crossin da rode.\nshe sed \"bbz will u luv me 4evr?\"\n";
		assertTrue(expected.equals(matchingLines));
	}

	/**
	 * Test for interface method getMatchingLinesWithTrailingContext(int
	 * option_A, String pattern, String input) with pattern matching the last
	 * word. Checks for correct output.
	 */
	@Test
	public void testMatchingLinesOutputContextLastWord() {
		String matchingLines = grepTool.getMatchingLinesWithOutputContext(2,
				"~*xxx", CONTENT_STORY);
		String expected = "he whispered 2 her corpse \"I ment 2 sey i will luv u...FIVE-ever\"\n(dat mean he luv her moar den 4evr)\nxxx~*...like dis if u cry evry time...~*xxx\n";
		assertTrue(expected.equals(matchingLines));
	}

	/**
	 * Test for interface method getMatchingLinesOnlyMatchingPart(String
	 * pattern, String input) with pattern that doesn't match any lines of
	 * input. Checks for empty result.
	 */
	@Test
	public void testMatchingLinesOnlyNoMatch() {
		String matchingLines = grepTool.getMatchingLinesOnlyMatchingPart(
				"[6-9]+", CONTENT_STORY);
		assertTrue("".equals(matchingLines));
	}

	/**
	 * Test for interface method getMatchingLinesOnlyMatchingPart(String
	 * pattern, String input) with null parameters. Checks for empty result.
	 */
	@Test
	public void testMatchingLinesNullParams() {
		String matchingLines = grepTool.getMatchingLinesOnlyMatchingPart(
				"[6-9]+", null);
		assertTrue("".equals(matchingLines));
		assertTrue(grepTool.getStatusCode() != 0);
	}

	/**
	 * Test for interface method getMatchingLinesOnlyMatchingPart(String
	 * pattern, String input) with pattern that matches multiple words in a
	 * line. Each matching word should be on a new line. Checks for correct
	 * output.
	 */
	@Test
	public void testMatchingLinesOnlyManyMatchesInLine() {
		String matchingLines = grepTool.getMatchingLinesOnlyMatchingPart("(a)",
				"a a a b c d a");
		assertTrue("a\na\na\na\n".equals(matchingLines));
	}

	/**
	 * Test for interface method getMatchingLinesOnlyMatchingPart(String
	 * pattern, String input) with pattern that matches all words in a line.
	 * Each matching word should be on a new line. Checks for correct output.
	 */
	@Test
	public void testMatchingLinesOnlyAllMatchesInLine() {
		String matchingLines = grepTool.getMatchingLinesOnlyMatchingPart(
				"[a-zA-Z]+", "xxx~*...like dis if u cry evry time...~*xxx\n");
		//System.out.println(matchingLines+"lol");
		assertTrue("xxx\nlike\ndis\nif\nu\ncry\nevry\ntime\nxxx\n".equals(matchingLines));
	}

	/**
	 * Test for interface method getMatchingLinesOnlyMatchingPart(String
	 * pattern, String input) with pattern written in regex. Checks for correct
	 * output.
	 */
	@Test
	public void testMatchingLinesOnlyWithRegex() {
		String matchingLines = grepTool.getMatchingLinesOnlyMatchingPart(
				"(luv u|will luv)", CONTENT_STORY);
		assertTrue("will luv\n".equals(matchingLines));
	}

	/**
	 * Test for interface method getMatchingLinesOnlyMatchingPart(String
	 * pattern, String input) with a single space as pattern. Output should
	 * contain all spaces in the text separated by new line.
	 */
	@Test
	public void testMatchingLinesOnlyWithPatternSpace() {
		String matchingLines = grepTool.getMatchingLinesOnlyMatchingPart(" ",
				"; .,  _)  (");
		//System.out.println(matchingLines+"lol");
		assertTrue(" \n \n \n \n \n".equals(matchingLines));
	}

	/**
	 * Test for interface method getCountOfMatchingLines(String pattern, String
	 * input) with pattern containing new line character. Checks for correct
	 * output.
	 */
	@Test
	public void testMatchingLinesOnlyWithPatternNewLine() {
		String matchingLines = grepTool.getMatchingLinesOnlyMatchingPart("\n",
				CONTENT_STORY);
		assertTrue("".equals(matchingLines));
	}

	/**
	 * Test for interface method getCountOfMatchingLines(String pattern, String
	 * input) with empty strings as parameters. Checks for empty result.
	 */
	@Test
	public void testCountLinesEmptyParams() {
		int matchingLines = grepTool.getCountOfMatchingLines("", "");
		assertEquals(0, matchingLines);
		matchingLines = grepTool.getCountOfMatchingLines("a", "");
		assertEquals(0, matchingLines);
	}

	/**
	 * Test for interface method getCountOfMatchingLines(String pattern, String
	 * input) with empty string input. Checks for empty result.
	 */
	@Test
	public void testCountLinesNoInput() {
		int matchingLines = grepTool.getCountOfMatchingLines("a", "");
		assertEquals(0, matchingLines);
	}

	/**
	 * Test for interface method getCountOfMatchingLines(String pattern, String
	 * input) with pattern that doesn't match any lines of input. Checks for
	 * empty result.
	 */
	@Test
	public void testCountLinesNoMatch() {
		int matchingLines = grepTool.getCountOfMatchingLines("$^",
				CONTENT_STORY);
		assertEquals(0, matchingLines);
	}

	/**
	 * Test for interface method getCountOfMatchingLines(String pattern, String
	 * input) with null parameters. Checks for empty result.
	 */
	@Test
	public void testCountLinesNullParams() {
		int matchingLines = grepTool.getCountOfMatchingLines(null,
				CONTENT_STORY);
		assertEquals(0, matchingLines);
		assertTrue(grepTool.getStatusCode() != 0);
	}

	/**
	 * Test for interface method getCountOfMatchingLines(String pattern, String
	 * input) with pattern that matches every line in input. Checks for correct
	 * output.
	 */
	@Test
	public void testCountLinesFullMatch() {
		int matchingLines = grepTool.getCountOfMatchingLines("[a-zA-Z]+",
				CONTENT_STORY);
		assertEquals(10, matchingLines);
		matchingLines = grepTool.getCountOfMatchingLines("", CONTENT_STORY);
		assertEquals(10, matchingLines);
	}

	/**
	 * Test for interface method getCountOfMatchingLines(String pattern, String
	 * input) with pattern containing one word. Checks for correct output.
	 */
	@Test
	public void testCountLinesWithMatchOneWord() {
		int matchingLines = grepTool.getCountOfMatchingLines("cry",
				CONTENT_STORY);
		assertEquals(3, matchingLines);
	}

	/**
	 * Test for interface method getCountOfMatchingLines(String pattern, String
	 * input) with pattern containing new line character. Checks for correct
	 * output.
	 */
	@Test
	public void testCountLinesWithPatternNewLine() {
		int matchingLines = grepTool.getCountOfMatchingLines("\n",
				CONTENT_STORY);
		assertEquals(0, matchingLines);
	}

	/**
	 * Test for interface method getOnlyMatchingLines(String pattern, String
	 * input) with empty strings as parameters. Checks for empty result.
	 */
	@Test
	public void testOnlyMatchingLinesEmptyParams() {
		String matchingLines = grepTool.getOnlyMatchingLines("", "");
		assertTrue("".equals(matchingLines));
	}

	/**
	 * Test for interface method getOnlyMatchingLines(String pattern, String
	 * input) with null parameters. Checks for empty result.
	 */
	@Test
	public void testOnlyMatchingLinesNullParams() {
		String matchingLines = grepTool.getOnlyMatchingLines(null, null);
		assertTrue("".equals(matchingLines));
		assertTrue(grepTool.getStatusCode() != 0);
	}

	/**
	 * Test for interface method getOnlyMatchingLines(String pattern, String
	 * input) with empty string input. Checks for empty result.
	 */
	@Test
	public void testOnlyMatchingLinesNoInput() {
		String matchingLines = grepTool.getOnlyMatchingLines("a b\b", "");
		assertTrue("".equals(matchingLines));
	}

	/**
	 * Test for interface method getOnlyMatchingLines(String pattern, String
	 * input) with pattern that doesn't match any lines of input. Checks for
	 * empty result.
	 */
	@Test
	public void testOnlyMatchingLinesNoMatch() {
		String matchingLines = grepTool.getOnlyMatchingLines("$^",
				CONTENT_STORY);
		assertTrue("".equals(matchingLines));
	}

	/**
	 * Test for interface method getOnlyMatchingLines(String pattern, String
	 * input) with pattern that contains only one character. Checks for correct
	 * output.
	 */
	@Test
	public void testOnlyMatchingLinesWithMatchOneCharacter() {
		String matchingLines = grepTool
				.getOnlyMatchingLines("2", CONTENT_STORY);
		String expected = "A gurl was walkin 2 skewl wit her bf\nhe whispered 2 her corpse \"I ment 2 sey i will luv u...FIVE-ever\"\n";
		assertTrue(expected.equals(matchingLines));
	}

	/**
	 * Test for interface method getOnlyMatchingLines(String pattern, String
	 * input) with pattern that contains only one word. Checks for correct
	 * output.
	 */
	@Test
	public void testOnlyMatchingLinesWithMatchOneWord() {
		String matchingLines = grepTool.getOnlyMatchingLines("4evr",CONTENT_STORY);
		String expected = "she sed \"bbz will u luv me 4evr?\"\n(dat mean he luv her moar den 4evr)\n";
		assertTrue(expected.equals(matchingLines));
	}

	/**
	 * Multiple tests for interface method getOnlyMatchingLines(String pattern,
	 * String input) with pattern that matches every line in input. Checks for
	 * correct output.
	 */
	@Test
	public void testOnlyMatchingLinesWithFullMatch() {
		String matchingLines = grepTool.getOnlyMatchingLines(" ", CONTENT_STORY);
		assertTrue(CONTENT_STORY.equals(matchingLines));
		matchingLines = grepTool.getOnlyMatchingLines("$", CONTENT_STORY);
		assertTrue(CONTENT_STORY.equals(matchingLines));
		matchingLines = grepTool.getOnlyMatchingLines("^", CONTENT_STORY);
		assertTrue(CONTENT_STORY.equals(matchingLines));
		matchingLines = grepTool.getOnlyMatchingLines("[a-zA-Z]+",
				CONTENT_STORY);
		assertTrue(CONTENT_STORY.equals(matchingLines));
	}

	/**
	 * Tests for interface method getOnlyMatchingLines(String pattern, String
	 * input) with pattern that contains regex. Checks for correct input.
	 */
	@Test
	public void testOnlyMatchingLinesWithRegex() {
		String matchingLines = grepTool.getOnlyMatchingLines(
				"(luv u|will luv)", CONTENT_STORY);
		String expected = "he whispered 2 her corpse \"I ment 2 sey i will luv u...FIVE-ever\"\n";
		assertTrue(expected.equals(matchingLines));
	}

	/**
	 * Test for interface method getNonMatchingLines(String pattern, String
	 * input) with empty strings as parameters. Checks for empty result.
	 */
	@Test
	public void testNonMatchingLinesEmptyParams() {
		String matchingLines = grepTool.getNonMatchingLines("", "");
		assertTrue("".equals(matchingLines));
	}

	/**
	 * Test for interface method getNonMatchingLines(String pattern, String
	 * input) with null parameters. Checks for empty result.
	 */
	@Test
	public void testNonMatchingLineNullParams() {
		String matchingLines = grepTool.getNonMatchingLines(null, "");
		assertTrue("".equals(matchingLines));
		assertTrue(grepTool.getStatusCode() != 0);
	}

	/**
	 * Test for interface method getNonMatchingLines(String pattern, String
	 * input) with pattern that does not match any line of input. Should return
	 * entire input.
	 */
	@Test
	public void testNonMatchingLinesNoMatch() {
		String matchingLines = grepTool.getNonMatchingLines(
				"omgthisshouldnotmatchanything", CONTENT_STORY);
		assertTrue(CONTENT_STORY.equals(matchingLines));
	}

	/**
	 * Test for interface method getNonMatchingLines(String pattern, String
	 * input) with pattern that matches all lines of input. Checks for empty
	 * result.
	 */
	@Test
	public void testNonMatchingLinesFullMatch() {
		String matchingLines = grepTool.getNonMatchingLines(" ", CONTENT_STORY);
		assertTrue("".equals(matchingLines));
		matchingLines = grepTool
				.getNonMatchingLines("[a-zA-Z]+", CONTENT_STORY);
		assertTrue("".equals(matchingLines));
	}

	/**
	 * Test for interface method getNonMatchingLines(String pattern, String
	 * input) with pattern that contains only one word. Checks for correct
	 * output.
	 */
	@Test
	public void testNonMatchingLinesWithMatchOneWord() {
		String matchingLines = grepTool.getNonMatchingLines("4evr",
				CONTENT_STORY);
		String expected = "A gurl was walkin 2 skewl wit her bf\n"
				+ "n they were crossin da rode.\n"
				+ "he said \"NO\".\n"
				+ "da gurl cryed N ran across da rode b4 green man came on the sine.\n"
				+ "boy was cryin and went to pic up her body.\n"
				+ "she was ded.\n"
				+ "he whispered 2 her corpse \"I ment 2 sey i will luv u...FIVE-ever\"\n"
				+ "xxx~*...like dis if u cry evry time...~*xxx\n";
		assertTrue(expected.equals(matchingLines));
	}

}
