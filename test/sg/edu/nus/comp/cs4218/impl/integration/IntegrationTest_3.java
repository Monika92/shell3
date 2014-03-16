package sg.edu.nus.comp.cs4218.impl.integration;


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

import sg.edu.nus.comp.cs4218.extended2.IUniqTool;
import sg.edu.nus.comp.cs4218.extended2.IWcTool;
import sg.edu.nus.comp.cs4218.fileutils.ICdTool;
import sg.edu.nus.comp.cs4218.impl.extended2.UNIQTool;
import sg.edu.nus.comp.cs4218.impl.extended2.WCTool;
import sg.edu.nus.comp.cs4218.impl.fileutils.CATTool;
import sg.edu.nus.comp.cs4218.impl.fileutils.CDTool;
import sg.edu.nus.comp.cs4218.impl.fileutils.COPYTool;
import sg.edu.nus.comp.cs4218.impl.fileutils.DELETETool;
import sg.edu.nus.comp.cs4218.impl.fileutils.LSTool;
import sg.edu.nus.comp.cs4218.impl.fileutils.MOVETool;
import sg.edu.nus.comp.cs4218.fileutils.ICatTool;
import sg.edu.nus.comp.cs4218.fileutils.ICopyTool;
import sg.edu.nus.comp.cs4218.fileutils.IDeleteTool;
import sg.edu.nus.comp.cs4218.fileutils.ILsTool;
import sg.edu.nus.comp.cs4218.fileutils.IMoveTool;
import sg.edu.nus.comp.cs4218.impl.WorkingDirectory;

/**
 * The test class covers the third requirement of integration testing
 * State of the Shell is changed with cd, copy, move, delete (at least 4
 * complex scenarios)
 */
public class IntegrationTest_3 {
	
	private ICdTool cdtool;
	private CDTool cd;
	String actualOutput,expectedOutput;
	File workingDirectory;
	//WorkingDirectory wd;
	String stdin;
	File argFolderCd = new File("argumentFolder1");
	File inputFileCd = new File("Test_Output.txt") ;
	
	private ICopyTool copytool;
	private COPYTool copy;
	String inputCopy = "This is a test run for copy.";
	List<File> childFilesListCopy = new ArrayList();  
	File argFolderCopy,argFolderEmptyCopy,argFolderInsideCopy;
	File inputFile1Copy,inputFile2Copy;
	
	private IMoveTool movetool;
	private MOVETool move;
	
	private IDeleteTool deletetool;
	private DELETETool delete;
	
	private ILsTool lstool;
	private LSTool ls;

	private ICatTool cattool;
	private CATTool cat;
	
	private IWcTool wctool;
	private WCTool wc;

	private IUniqTool uniqtool;
	private UNIQTool uniq;
	String input = "This is \na test \nrun\nCd.";


@Before
public void before(){
	WorkingDirectory.changeWorkingDirectory(new File(System.getProperty("user.dir")));
	stdin = null;
	if(argFolderCd.mkdirs())
	{
		writeToFile(inputFileCd, input);
		
	}
	
	argFolderCopy = new File("folder1");
	argFolderEmptyCopy = new File("folder2");
	argFolderInsideCopy = new File(WorkingDirectory.workingDirectory+File.separator+"folder1"+ File.separator +"insideFolder1") ;
	inputFile1Copy = new File(WorkingDirectory.workingDirectory+File.separator+"folder1"+File.separator+"file1.txt");
	inputFile2Copy = new File("file2.txt");
	writeToFile(inputFile1Copy, input);
	writeToFile(inputFile2Copy, "abc");
	argFolderCopy.mkdirs();
	inputFile1Copy.getParentFile().mkdirs();
	try {
		inputFile1Copy.createNewFile();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	argFolderEmptyCopy.mkdirs();
	argFolderInsideCopy.mkdirs();

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
//
//public List<File> getFiles(File directory) {
//	
//	// TODO Auto-generated method stu
//    String[] childFiles = directory.list();
//    List<File> childFilesList = new ArrayList();  
//    
//    for(String child: childFiles)
//    {
//    	File childFile = new File(child);
//    	childFilesList.add(childFile);
//    }
//	return childFilesList;		
//}
//
//public String getStringForFiles(List<File> files) {
//	// TODO Auto-generated method stub
//	String outputString = "";
//	for(File child: files)
//	{
//	 outputString += child.toString() + " ";
//	}
//	return outputString;
//}

public static void deleteFolder(File folder) {
    File[] files = folder.listFiles();
    if(files!=null) { //some JVMs return null for empty dirs
        for(File f: files) {
            if(f.isDirectory()) {
                deleteFolder(f);
            } else {
                f.delete();
            }
        }
    }
    folder.delete();
}


public String ifFileExists(File f)
{
	if(f.exists())
		return "yes";
	else
		return "no";
}


@After
public void after(){
	cdtool = null;
	cd = null;
	if(inputFileCd.exists())
		inputFileCd.delete();
	if(argFolderCd.exists())
		argFolderCd.delete();
	
	copytool = null;
	copy = null;
	
	movetool = null;
	move = null;
	
	deletetool = null;
	delete = null;
	
	wctool = null;
	wc = null;
	
	cattool = null;
	cat = null;
	
	lstool = null;
	ls = null;
	
	uniqtool = null;
	uniq = null;
	
	if(inputFile1Copy.exists())
		deleteFolder(inputFile1Copy);//input_file_1.deleteOnExit();
	if(inputFile2Copy.exists())
		deleteFolder(inputFile2Copy);//input_file_2.deleteOnExit();
	if(argFolderEmptyCopy.exists())
		deleteFolder(argFolderEmptyCopy);//argFolderEmpty.deleteOnExit();
	if(argFolderInsideCopy.exists())
		deleteFolder(argFolderInsideCopy);//argFolderInside.deleteOnExit();
	if(argFolderCopy.exists())
		deleteFolder(argFolderCopy);//argFolder.deleteOnExit();	
}


/*
 * Let's begin with the most 'complex' combination. 
 * This test is used to change the state of the shell using cd initially where the working dir is changed to a certain folder1.
 * If the working dir has been successfully change the following copy will work,else it will fail. 
 * Copy is used to copy the contents of folder1/file1.txt into a folder1/file2.txt . file2.txt doesn't already exist, so copy creates it.
 * Move is used next to move contents of folder1/file2.txt to folder1/fileNew.txt. This will succeed only if copy had worked in the first place.
 * folder1/fileNew.txt did not exist earlier, so Move creates it, copies the content of folder1/file2.txt into it and deletes the latter.
 * Finally, we use delete to delete folder1/fileNew.txt which will work only if move had worked in the first place.
 * */
@Test
public void cdCopyMoveDeleteTest(){

	//Change working directory to folder1. This change should reflect in all our future commands if cd works correctly
	if(argFolderCopy.exists())
	{
	String[] arguments = new String[]{"folder1"} ; 
	cdtool = new CDTool(arguments);
	actualOutput = cdtool.execute(WorkingDirectory.workingDirectory, stdin);
	expectedOutput = "Changed current working directory to " + WorkingDirectory.workingDirectory;
	assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
	assertEquals(cdtool.getStatusCode(), 0);
	}
	

	//Now assuming cd worked => WorkingDirectory.workingDirectory = "folder1". Hence we should be able copy the contents of folder1/file1 to folder/file2
	//The file folder/file2 gets created during copy command as it does not already exist.
	String[] arguments = new String[]{WorkingDirectory.workingDirectory + File.separator + "file1.txt", WorkingDirectory.workingDirectory + File.separator + "file2.txt"} ;
	
	copytool = new COPYTool(arguments);
	actualOutput = copytool.execute(WorkingDirectory.workingDirectory, stdin);
	expectedOutput = "Copy completed.";
	String contentFile1 = readFromFile(new File(arguments[0]));
	
	//Test to check if "Copy Completed." is returned. The message is returned only when copy is successful
	assertTrue(expectedOutput.equalsIgnoreCase(actualOutput)); 
	assertEquals(copytool.getStatusCode(), 0);

	//Also test if contents of file1 are copied exactly into file2. To ensure that copy worked correctly.
	String contentFile2 = readFromFile(new File(arguments[1]));
	assertTrue(contentFile1.equalsIgnoreCase(contentFile2));
	
	
	
	//Now lets test move. Assuming copy worked we must a file file2.txt in folder1. Now let's move the contents of file2.txt to fileNew.txt.
	//Since no such file as fileNew.txt exists in folder1, the move() creates it and copies the contents of file2.txt and then deletes file2.txt
	//So now it looks like folder1/file2.txt have been moved into folder1/fileNew.txt 
	arguments = new String[]{WorkingDirectory.workingDirectory + File.separator +"file2.txt", WorkingDirectory.workingDirectory + File.separator +"fileNew.txt"} ;
	String contentFile0 = readFromFile(new File(arguments[0]));

	movetool = new MOVETool(arguments);
	actualOutput = movetool.execute(WorkingDirectory.workingDirectory, stdin);
	expectedOutput = "Move completed.";
	
	//Test case to check if success message is printed by the function.
	assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
	assertEquals(movetool.getStatusCode(), 0);
	
	//Test case to check if content has beem moved correctly
	contentFile1 = readFromFile(new File(arguments[1]));
	assertTrue(contentFile0.equalsIgnoreCase(contentFile1));	

	//Test case to check if file2.txt has been removed
	String result = ifFileExists(new File(arguments[0]));
	assertTrue(result.equalsIgnoreCase("no"));

	
	
	//Okay we've established move works too. Now let's delete the newly create FileNew using delete.
	arguments = new String[]{WorkingDirectory.workingDirectory + File.separator +"fileNew.txt"} ;
	assertTrue((new File(arguments[0])).exists());
	deletetool = new DELETETool(arguments);
	actualOutput = deletetool.execute(WorkingDirectory.workingDirectory, stdin);
	assertFalse((new File(arguments[0])).exists());
	assertEquals(deletetool.getStatusCode(), 0);

}

/*
 * This is the test to change the current directory to folder1 and do ls on the current directory.
 * The test will succeed if ls displays the contents of folder1
 */
@Test
public void cdLsTest()
{
	//Change working directory to folder1.
		if(argFolderCopy.exists())
		{
		String[] arguments = new String[]{"folder1"} ; 
		cdtool = new CDTool(arguments);
		actualOutput = cdtool.execute(WorkingDirectory.workingDirectory, stdin);
		expectedOutput = "Changed current working directory to " + WorkingDirectory.workingDirectory;
		assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
		assertEquals(cdtool.getStatusCode(), 0);
		}
		
		//Now do ls(current working directory) which is what should happen when arguments list is empty
		String[] arguments = new String[]{} ;
		lstool = new LSTool(arguments);
		actualOutput = lstool.execute(WorkingDirectory.workingDirectory, stdin);
		expectedOutput = "";
		String [] childFilesArray = WorkingDirectory.workingDirectory.list();
		for(String child : childFilesArray) {expectedOutput += child + " ";}
		if(expectedOutput == "") expectedOutput = "The folder is empty";
		assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
		assertEquals(lstool.getStatusCode(), 0);
 }


/*
 * This is the test to copy the contents of folder1/file1.txt into folder1/file2.txt
 * And this is followed by cat(folder1/file2.txt)
 */
@Test
public void copyCatTest()
{
	writeToFile(new File("folder1" + File.separator + "file1.txt"), input);
	String[] arguments = new String[]{"folder1" + File.separator + "file1.txt", "folder1" + File.separator + "file3.txt"} ;
	
	copytool = new COPYTool(arguments);
	actualOutput = copytool.execute(WorkingDirectory.workingDirectory, stdin);
	expectedOutput = "Copy completed.";
	String contentFile1 = readFromFile(new File(arguments[0]));
	
	//Test to check if "Copy Completed." is returned. The message is returned only when copy is successful
	assertTrue(expectedOutput.equalsIgnoreCase(actualOutput)); 
	assertEquals(copytool.getStatusCode(), 0);

	//Also test if contents of file1 are copied exactly into file2. To ensure that copy worked correctly.
	String contentFile2 = readFromFile(new File(arguments[1]));
	assertTrue(contentFile1.equalsIgnoreCase(contentFile2));
	
	
	//Check if the contents of file2.txt match the contents of file1.txt using cat
	arguments = new String[]{"folder1" + File.separator + "file3.txt"} ;
	cattool = new CATTool(arguments);
	actualOutput = cattool.execute(WorkingDirectory.workingDirectory, stdin);
	expectedOutput =  "This is \na test \nrun\nCd.";
	assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
	assertEquals(cattool.getStatusCode(), 0);

}

/*
 * This test case is written in the following order : move wc delete uniq
 */
@Test
public void moveWcDeleteUniqTest()
{
	//Start with moving folder1/file.txt to folder1/file2.txt.
	writeToFile(new File("folder1" + File.separator + "file1.txt"), input);
	String[] arguments = new String[]{"folder1" + File.separator + "file1.txt", "folder1" + File.separator + "file4.txt"} ;
	String contentFile0 = readFromFile(new File(arguments[0]));

	movetool = new MOVETool(arguments);
	actualOutput = movetool.execute(WorkingDirectory.workingDirectory, stdin);
	expectedOutput = "Move completed.";
	
	//Test case to check if success message is printed by the function.
	assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
	assertEquals(movetool.getStatusCode(), 0);
	
	
	//Run WC tool with folder1/file2.txt as input
	arguments = new String[]{"folder1" + File.separator + "file4.txt"} ;
	wctool = new WCTool(arguments);
	actualOutput = wctool.execute(WorkingDirectory.workingDirectory, null);
	expectedOutput =  WorkingDirectory.workingDirectory + File.separator + "folder1" + File.separator + "file4.txt : -m  17 , -w  6 , -l 4\n";
	assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
	assertEquals(wctool.getStatusCode(), 0);
	
	//Delete folder1/file.txt to check if the uniq we implement next returns output accordingly
	arguments = new String[]{"folder1" + File.separator + "file4.txt"} ;
	assertTrue((new File(arguments[0])).exists());
	deletetool = new DELETETool(arguments);
	actualOutput = deletetool.execute(WorkingDirectory.workingDirectory, stdin);
	assertFalse((new File(arguments[0])).exists());
	assertEquals(deletetool.getStatusCode(), 0);
	
	//Uniq invalid file input test. If this test success it implies that delete has worked successfully 
	arguments = new String[]{"folder1" + File.separator + "file4.txt"} ;
	uniqtool = new UNIQTool(arguments);
	actualOutput = uniqtool.execute(WorkingDirectory.workingDirectory, stdin);
	expectedOutput = "No such file";
	assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
	assertEquals(uniqtool.getStatusCode(), -1);

	
}



}