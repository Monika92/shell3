package sg.edu.nus.comp.cs4218.impl;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import sg.edu.nus.comp.cs4218.ITool;



public class ShellTest {

	private Shell shell;
	
	@Before
	public void before(){
	
		shell = new Shell();
	}
	
	@After
	public void after()
	{
		shell = null;
	}
	
	@Test
	public void parseCutCommandTest()
	{
		String commandline = "cut -c 1-2 -";
		String[] expectedArgsList = {"-c","1-2","-"};
		assertEquals("CUTTool",shell.parse(commandline).getClass().getSimpleName());
		assertArrayEquals(shell.getArgumentList(), expectedArgsList);		
	}
	
	@Test
	public void parseCutCommandRandomArgumentsTest()
	{
		String commandline = "cut 3432!@#@ fdsf";
		String[] expectedArgsList = {"3432!@#@","fdsf"};
		assertEquals("CUTTool",shell.parse(commandline).getClass().getSimpleName());
		assertArrayEquals(shell.getArgumentList(), expectedArgsList);		
	}
	
	@Test
	public void parseEchoCommandTest()
	{
		String commandline = "echo \" hello world \"";
		String[] expectedArgsList = {"\" hello world \""};
		assertEquals("ECHOTool",shell.parse(commandline).getClass().getSimpleName());
		assertArrayEquals(expectedArgsList,shell.getArgumentList());	
	}
	
	@Test
	public void parseCtrlZCommandTest()
	{
		String commandline = "ctrl-Z";
		assertEquals("",shell.parse(commandline).getClass().getSimpleName());
	}
	
	//@Test
	public void parseInvalidCommandTest()
	{
		String commandline = "nslookup hello";
		ITool itool = shell.parse(commandline);
		assertNull(itool);
	}
	
	@Test
	public void executeTest()
	{
		
	}
	@Test
	public void overallShellTest() {
		//fail("Not yet implemented");
	}

}
