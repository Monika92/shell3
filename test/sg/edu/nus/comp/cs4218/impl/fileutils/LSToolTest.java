package sg.edu.nus.comp.cs4218.impl.fileutils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import sg.edu.nus.comp.cs4218.fileutils.ILsTool;
import sg.edu.nus.comp.cs4218.impl.fileutils.LSTool;
import sg.edu.nus.comp.cs4218.impl.WorkingDirectory;

public class LSToolTest {
	
	/* 
	 * Implementation of Ls
	 * Possible Executions
	 * 1. Ls ie no arguments : just displays all files and folders in present workingdir
	 * 2. Ls Directory : Directory could be absolute or relative. Displays the files and folders inside it
	 * 3. Ls file : File could be absolute or replative. Displays whether the file exits or not
	 * 4. ls *.txt : or any other extension. Displays list of .txt (or any other file extension) files in current working dir.
	 */
	
	/*
	 * Few Assumptions :
	 * The output format when
	 * 1. The folder in the argument list is empty :
	 *    "The folder is empty"
	 * 2. The filename in the argument list exists :
	 *    "File Exists"
	 * 3. When files of a particular filetype in the argument list do not exist :
	 *    "No files of type ." + <fileType>
	 * 4. When we have files/folders in the folder listed in the argument list :
	 *    <filename1> <filename2> <folder1> <filename3>...(and so on)   
	 * */
	
	
	private ILsTool lstool;
	private LSTool ls;
	String actualOutput,expectedOutput="";
	File workingDirectory;
	String stdin;
	List<File> childFilesList = new ArrayList();  
	File argFolder = new File("argumentFolder1");
	File argFolderEmpty = new File("argumentFolderEmpty");
	File inputFile1;
	
@Before
public void before(){
	WorkingDirectory.changeWorkingDirectory(new File(System.getProperty("user.dir")));
	stdin = null;
	inputFile1 = new File(WorkingDirectory.workingDirectory+File.separator+"argumentFolder1"+ File.separator +"Test_Output.txt") ;
	String input = "This is \na test \nrun.";
	argFolder.mkdirs();
	argFolderEmpty.mkdirs();
	writeToFile(inputFile1, input);
	inputFile1.mkdirs();	
}

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

//public String readFromFile(File inputFile){
//	String output = ""; FileReader fr = null;
//	try{
//		fr = new FileReader(inputFile);
//	} catch(FileNotFoundException e){
//		e.printStackTrace();
//		return "File not found";
//	}
//	BufferedReader br = new BufferedReader(fr);
//	try{
//		String line = br.readLine();
//		while(line != null){
//			if(line.equalsIgnoreCase("\n")||line.equalsIgnoreCase(""))
//				output+="\n";
//			else
//				output += line + "\n";
//			line = br.readLine();
//		}
//	} catch(IOException e){
//		e.printStackTrace();
//		return "Unable to read file";
//	} finally{
//		try {
//			br.close();
//			fr.close();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
//	return output;
//}

@After
public void after(){
	lstool = null;
	ls = null;
	if(inputFile1.exists())
		inputFile1.delete();
	if(argFolder.exists())
		argFolder.delete();
	if(argFolderEmpty.exists())
		argFolderEmpty.delete();
}

/*
 * Test to check the behaviour of Ls Tool with no arguments.
 * It needs to return the list of files and folders within the working dir.
 * */
@Test
public void lsNoArgumentTest(){
	String[] arguments = new String[]{} ;
	lstool = new LSTool(arguments);
	actualOutput = lstool.execute(WorkingDirectory.workingDirectory, stdin);
	String [] childFilesArray = WorkingDirectory.workingDirectory.list();
	for(String child : childFilesArray) {expectedOutput += child + " ";}
	if(expectedOutput == "") expectedOutput = "The folder is empty";
	assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
	assertEquals(lstool.getStatusCode(), 0);
}

/*
 * Test to check the behaviour of Ls Tool with valid relative pathname folder as argument.
 * It needs to return the list of files and folders within the folder.
 * */
@Test
public void lsRelativeDirectoryArgumentTest(){
	String[] arguments = new String[]{"argumentFolder1"} ;
	lstool = new LSTool(arguments);
	actualOutput = lstool.execute(WorkingDirectory.workingDirectory, stdin);
	expectedOutput = "Test_Output.txt ";
	assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
	assertEquals(lstool.getStatusCode(), 0);
}

/*
 * Test to check the behaviour of Ls Tool with valid absolute pathname folder as argument.
 * It needs to return the list of files and folders within the folder.
 * */
@Test
public void lsAbsoluteDirectoryArgumentTest(){
	String[] arguments = new String[]{WorkingDirectory.workingDirectory + File.separator +"argumentFolder1"} ;
	lstool = new LSTool(arguments);
	actualOutput = lstool.execute(WorkingDirectory.workingDirectory, stdin);
	expectedOutput = "Test_Output.txt ";
	assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
	assertEquals(lstool.getStatusCode(), 0);
}

/*
 * Test to check the behaviour of Ls Tool with valid folder as argument.
 * It needs to return the list of files and folders within the folder.
 * */
@Test
public void lsEmptyDirectoryArgumentTest(){
	String[] arguments = new String[]{"argumentFolderEmpty"} ;
	lstool = new LSTool(arguments);
	actualOutput = lstool.execute(WorkingDirectory.workingDirectory, stdin);
	expectedOutput = "The folder is empty";
	assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
	assertFalse(("Invalid input").equalsIgnoreCase(actualOutput));
	assertEquals(lstool.getStatusCode(), 0);
}

/*
 * Test to check the behaviour of Ls Tool with invalid folder as argument.
 * It needs to return the list of files and folders within the folder.
 * */
@Test
public void lsInvalidDirectoryArgumentTest(){
	String[] arguments = new String[]{"argumentFolderIDontExist"} ;
	lstool = new LSTool(arguments);
	actualOutput = lstool.execute(WorkingDirectory.workingDirectory, stdin);
	expectedOutput = "Invalid. Doesn't exist";
	assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
	assertEquals(lstool.getStatusCode(), -1);
}

/*
 * Test to check the behaviour of Ls Tool with valid file path as argument.
 * It needs to return a message stating that the file exists.
 * */
@Test
public void lsValidFileArgumentTest(){
	String[] arguments = new String[]{"argumentFolder1"+File.separator+"Test_Output.txt"} ;
	lstool = new LSTool(arguments);
	actualOutput = lstool.execute(WorkingDirectory.workingDirectory, stdin);
	expectedOutput = "File Exists";
	assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
	assertFalse(("Invalid input").equalsIgnoreCase(actualOutput));
	assertEquals(lstool.getStatusCode(), 0);
}

/*
 * Test to check the behaviour of Ls Tool with valid file path as argument.
 * It needs to return a message stating that the file doesn't exist.
 * */
@Test
public void lsInvalidFileArgumentTest(){
	String[] arguments = new String[]{"argumentFolder1"+File.separator+"Test_Output_IDontExist.txt"} ;
	lstool = new LSTool(arguments);
	actualOutput = lstool.execute(WorkingDirectory.workingDirectory, stdin);
	expectedOutput = "Invalid. Doesn't exist";
	assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
	assertFalse(("File Exists").equalsIgnoreCase(actualOutput));
	assertEquals(lstool.getStatusCode(), -1);
}

/*
 * Test to check the behaviour of Ls Tool with valid file type as argument.
 * It needs to return all files of that file type in the working dir.
 * */
@Test
public void lsValidFiletypeArgumentTest(){
	String[] arguments = new String[]{"argumentFolder1"+File.separator+"*.txt"} ;
	lstool = new LSTool(arguments);
	actualOutput = lstool.execute(WorkingDirectory.workingDirectory, stdin);
	expectedOutput = "Test_Output.txt ";
	assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
	assertEquals(lstool.getStatusCode(), 0);
}

/*
 * Test to check the behaviour of Ls Tool with invalid argument.
 * */
@Test
public void lsStarArgumentTest(){
	String[] arguments = new String[]{"*"} ;
	lstool = new LSTool(arguments);
	actualOutput = lstool.execute(WorkingDirectory.workingDirectory, stdin);
	expectedOutput = "Invalid. Doesn't exist";
	assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
	assertEquals(lstool.getStatusCode(), -1);
}

/*
 * Test to check the behaviour of Ls Tool with invalid file type as argument.
 * It needs to return an appropriate error message.
 * */
@Test
public void lsNonExistentFiletypeArgumentTest(){
	String[] arguments = new String[]{"argumentFolder1"+File.separator+"*.txz"} ;
	lstool = new LSTool(arguments);
	actualOutput = lstool.execute(WorkingDirectory.workingDirectory, stdin);
	expectedOutput = "No files of type .txz";
	assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
	assertEquals(lstool.getStatusCode(), -1);
}
}
