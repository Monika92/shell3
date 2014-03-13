package sg.edu.nus.comp.cs4218.impl;

import static org.junit.Assert.assertEquals;

import java.io.File;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class WorkingDirectoryTest {

	WorkingDirectory workingDirectory;
	
	@Before
	public void before(){
		workingDirectory = new WorkingDirectory();
	}
	@After
	public void after(){
		 workingDirectory = null;
	}
	@Test
	public void testChangeWorkingDirectory()
	{
		workingDirectory.changeWorkingDirectory(new File("/Users"));
		File expectedOutput = new File("/Users");
		File actualOutput = workingDirectory.getWorkingDirectory();
		assertEquals(expectedOutput,actualOutput);
	}
	 
}
