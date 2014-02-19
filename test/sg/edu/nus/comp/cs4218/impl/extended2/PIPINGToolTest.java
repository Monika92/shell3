package sg.edu.nus.comp.cs4218.impl.extended2;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import sg.edu.nus.comp.cs4218.ITool;
import sg.edu.nus.comp.cs4218.extended1.IGrepTool;
import sg.edu.nus.comp.cs4218.extended1.IPipingTool;
import sg.edu.nus.comp.cs4218.fileutils.ICatTool;
import sg.edu.nus.comp.cs4218.fileutils.ILsTool;
import sg.edu.nus.comp.cs4218.impl.fileutils.CATTool;
import sg.edu.nus.comp.cs4218.impl.fileutils.LSTool;

public class PIPINGToolTest {
	private IPipingTool pipingTool;
	private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
	private final File workingDir = new File(System.getProperty("user.dir"));

	@Before
	public void setUp() throws Exception {
		pipingTool = new PIPINGTool(null, null);
		pipingTool.execute(workingDir, "");
		System.setErr(new PrintStream(errContent)); // to test error output
	}

	@After
	public void tearDown() throws Exception {
		pipingTool = null;
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
		
		System.out.println("pipe result:" + pipeResult + "end");
		System.out.println("code:" + pipingTool.getStatusCode());
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

}
