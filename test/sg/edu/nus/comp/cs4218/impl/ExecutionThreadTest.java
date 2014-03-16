package sg.edu.nus.comp.cs4218.impl;

import java.io.File;

import org.junit.Test;

import sg.edu.nus.comp.cs4218.impl.fileutils.ECHOTool;
import org.junit.After;
import org.junit.Before;
public class ExecutionThreadTest {

	ExecutionThread executionThread;
	File file;
	
	@Before
	public void before()
	{
		executionThread = new ExecutionThread(new ECHOTool(new String[]{"hello"}), null, new String[]{"hello",">","output.txt"}) ;
	}
	
	@After
	public void after()
	{
		
	}
	@Test
	public void testRun()
	{
		executionThread.run();
	}
}
