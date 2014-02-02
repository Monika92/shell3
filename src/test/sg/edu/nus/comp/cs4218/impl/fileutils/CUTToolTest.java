package test.sg.edu.nus.comp.cs4218.impl.fileutils;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import sg.edu.nus.comp.cs4218.extended2.ICutTool;
import sg.edu.nus.comp.cs4218.fileutils.IPwdTool;
import sg.edu.nus.comp.cs4218.impl.extended2.CUTTool;
import sg.edu.nus.comp.cs4218.impl.fileutils.PWDTool;

public class CUTToolTest {
	//TODO Always test against the interface! 
	private ICutTool cuttool; 
	
	@Before
	public void before(){
		cuttool = new CUTTool();
	}

    @After
	public void after(){
		pwdtool = null;
	}
	
	@Test
	public void getStringForDirectoryTest() throws IOException {
		//Test expected behavior
		//Create a tmp-file and get (existing) parent directory
		String existsDirString = File.createTempFile("exists", "tmp").getParent();
		File existsDir = new File(existsDirString);
		String dirString = pwdtool.getStringForDirectory(existsDir);
		assertTrue(dirString.equals(existsDirString));
		assertEquals(pwdtool.getStatusCode(), 0);
    }


	@Test
	public void getStringForNonExistingDirectoryTest() throws IOException { 
		//Test error-handling 1
		//Reference non-existing file
		File notExistsDir = new File("notexists");
        pwdtool.getStringForDirectory(notExistsDir);
		assertTrue(pwdtool.getStatusCode()!= 0);
    }
		

	@Test
	public void getStringForNullDirectoryTest() throws IOException { 
		//Test error-handling 2
		pwdtool.getStringForDirectory(null);
		assertTrue(pwdtool.getStatusCode()!= 0);
		
	}

}
