package sg.edu.nus.comp.cs4218.impl.extended2;

import static org.junit.Assert.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import sg.edu.nus.comp.cs4218.extended2.IPasteTool;

public class COMMToolTest {
	
	private IPasteTool commTool;
	String actualOutput,expectedOutput,helpOutput;
	File workingDirectory;

	File file_a,file_b,file_c,file_d;
	String fileContent_a,fileContent_b,fileContent_c,fileContent_d;
	
	@Before
	public void before() throws Exception {
		workingDirectory = new File(System.getProperty("user.dir"));
		helpOutput = " /*\n" +
				"\n*" +
				"\n* comm : Compares two sorted files line by line. With no options, produce three-column output." +
				"\n* 		 Column one contains lines unique to FILE1, column two contains lines unique to FILE2," +
				"\n 		 and column three contains lines common to both files."+
				"\n*" +	
				"\n*	Command Format - comm [OPTIONS] FILE1 FILE2" +
				"\n*	FILE1 - Name of the file 1" +
				"\n*	FILE2 - Name of the file 2" +
				"\n*		-c : check that the input is correctly sorted" +
				"\n*      -d : do not check that the input is correctly sorted" +
				"\n*      -help : Brief information about supported options" +
				"\n*/";

		file_a = new File("a.txt");
		file_b = new File("b.txt");
		file_c = new File("c.txt");
		file_d = new File("d.txt");

		fileContent_a = "Apple\nOrange\nPear\nMelon";
		fileContent_b = "Banana\nOrange\nMelon";
		fileContent_c = "Superman\nSpiderman\nBatman";
		fileContent_d = "Banana\nOrange\nMelon";
		
		writeToFile(file_a, fileContent_a);
		writeToFile(file_b, fileContent_b);
		writeToFile(file_c, fileContent_c);
		writeToFile(file_d,fileContent_d);
		
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
	
	@After
	public void after() throws Exception {
		commTool = null;
		file_a.delete();
		file_b.delete();
		file_c.delete();
		file_d.delete();
	}

	@Test
	//Test if overall control flow is correct
	public void overallTest() {
		fail("Not yet implemented");
	}
	
	@Test
	//File doesn't exist
	public void compareFilesInvalidFileArgsTest(){
		fail("Not yet implemented");
	}
	
	@Test
	//comm -c -help file1 file2
	public void compareFilesGiveHelpPriorityTest(){
		fail("Not yet implemented");
	}
	
	@Test
	//Both file arguments are empty files
	public void compareFilesNoOptionsBothFilesEmptyTest(){
		fail("Not yet implemented");
	}
	
	@Test
	//One file argument empty
	public void compareFilesNoOptionsOneFileEmptyTest(){
		fail("Not yet implemented");
	}
	
	@Test
	//Handle filename such as "-a.txt"
	//call execute, not specific function
	public void compareFilesNoOptionsFilenamesWithSpecChar(){
		fail("Not yet implemented");
	}
	
	@Test
	//All lines in both files are unique. Column 3 should be empty
	public void compareFilesNoOptionsAllUniqueTest(){
		fail("Not yet implemented");
	}
	
	@Test
	//Some common lines. All three columns have items
	public void compareFilesNoOptionsSomeUniqueTest(){
		fail("Not yet implemented");
	}
	
	@Test
	//No unique lines at all. Col1 and Col2 are empty
	public void compareFilesNoOptionsNoneUniqueTest(){
		fail("Not yet implemented");
	}

	@Test
	//Positive case: Files are sorted
	public void compareFilesCheckSortStatusTest1(){
		fail("Not yet implemented");
	}
	
	@Test
	//Negative case: Files are not sorted
	public void compareFilesCheckSortStatusTest2(){
		fail("Not yet implemented");
	}
	
	@Test
	//Do not check -which is the default way
	public void compareFilesDoNotCheckSortedStatusTest(){
		
	}
	
	@Test
	//priority to -d option over -c 
	public void compareFilesOptionsPriorityTest(){
		fail("Not yet implemented");
	}
	
	@Test
	//Check command: comm -c -c -d -d -d file1 file2 works
	public void compareFilesMultipleOptionsTest(){
		fail("Not yet implemented");
	}
}
