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

import sg.edu.nus.comp.cs4218.fileutils.ICopyTool;
import sg.edu.nus.comp.cs4218.impl.WorkingDirectory;

public class COPYToolTest {
	/*
	 * Implementation of Copy
	 * Possible Executions
	 * 1. Copy file1 file2 - copies contents of file1 to file2. if file2 doesn't already exist, it gets created then copying happens
	 * 2. copy file dir - copies the file into the directory
	 * 3. copy dir1 dir2 - copies contents of one directory into the second
	 * 4. copy file1 file2 file3.. dir - copies all the valid files into dir. The invalid ones are not copied 
	 */
	
	/*
	 * Few Assumptions 
	 * 1. The following combinations of arguments give a success message which is "Copy Completed."
	 *    valid filename , valid filename
	 *    valid filename , invalid filename (new file is created with the latter's name)
	 *    valid filename , valid directory
	 *    valid filename , invalid directory (new file is created with the latter's name)
	 *    valid directory , valid directory (contents of former copied to latter)
	 *    valid directory , invalid directory (new dir with latter's name is created and contents of former copied to latter)
	 *    multiple valid filenames  valid directory (copy all files into the dir)
	 *    Other combinations of inputs lead to errors
	 * 2. Output formats when there are 2 arguments
	 *    Successful Copying : "Copy Completed."
	 *    Unsuccessful : "Error - Invalid input."
	 * 3. Output formats when there are > 2 arguments ie copy file1,file2,..,fileN,dir  
	 * 	  Successful Copying : <filename> + "'s copy completed. \n" for each file copied
	 *    Unsuccessful if filename is invalid : <filename> + " is an invalid file.\n"
	 *    Unsucessful if directory name is invalid : <dir name> + " is an invalid directory."
	 *    
	 * */
	private ICopyTool copytool;
	private COPYTool copy;
	String actualOutput,expectedOutput="";
	File workingDirectory;
	String stdin;
	String input = "This is a test run.";
	List<File> childFilesList = new ArrayList();  
	File argFolder,argFolderEmpty,argFolderInside;
	File inputFile1,inputFile2;
	
@Before
public void before() {
	
	WorkingDirectory.changeWorkingDirectory(new File(System.getProperty("user.dir")));
	stdin = null;
	argFolder = new File("folder1");
	argFolderEmpty = new File("folder2");
	argFolderInside = new File(WorkingDirectory.workingDirectory+File.separator+"folder1"+ File.separator +"insideFolder1") ;
	inputFile2 = new File("file2.txt");
	inputFile1 = new File("file1.txt");
	writeToFile(inputFile1, input);
	writeToFile(inputFile2, "abc");
	argFolder.mkdirs();
	argFolderEmpty.mkdirs();
	argFolderInside.mkdirs();
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

public List<File> getFiles(File directory) {
	
	// TODO Auto-generated method stu
    String[] childFiles = directory.list();
    List<File> childFilesList = new ArrayList();  
    
    for(String child: childFiles)
    {
    	File childFile = new File(child);
    	childFilesList.add(childFile);
    }
	return childFilesList;		
}

public String getStringForFiles(List<File> files) {
	// TODO Auto-generated method stub
	String outputString = "";
	for(File child: files)
	{
	 outputString += child.toString() + " ";
	}
	return outputString;
}

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

@After
public void after(){
	copytool = null;
	copy = null;
	if(inputFile1.exists())
		deleteFolder(inputFile1);//input_file_1.deleteOnExit();
	if(inputFile2.exists())
		deleteFolder(inputFile2);//input_file_2.deleteOnExit();
	if(argFolderEmpty.exists())
		deleteFolder(argFolderEmpty);//argFolderEmpty.deleteOnExit();
	if(argFolderInside.exists())
		deleteFolder(argFolderInside);//argFolderInside.deleteOnExit();
	if(argFolder.exists())
		deleteFolder(argFolder);//argFolder.deleteOnExit();	
}


/*
 * Test to check the behaviour of Copy Tool when 2 valid file names are given as arguments in relative path names.
*/
@Test
public void existingFileToExistingFileArgumentTest(){
	String[] arguments = new String[]{"file1.txt", "file2.txt"} ;
	copytool = new COPYTool(arguments);
	actualOutput = copytool.execute(WorkingDirectory.workingDirectory, stdin);
	expectedOutput = "Copy completed.";
	
	//Test to check for success message
	assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
	
	//Status code test
	assertEquals(copytool.getStatusCode(), 0);
	
	//Test to check whether the contents to the 2 files match once copying is done.
	String contentFile2 = readFromFile(new File(arguments[1]));
	String contentFile1 = readFromFile(new File(arguments[0]));
	assertTrue(contentFile1.equalsIgnoreCase(contentFile2));
}

/*
 * Test to check the behaviour of Copy Tool when 2 valid file names are given as arguments in absolute path names.
*/
@Test
public void existingFileToExistingFileAbsoluteArgumentTest(){
	String[] arguments = new String[]{WorkingDirectory.workingDirectory + File.separator + "file1.txt", WorkingDirectory.workingDirectory + File.separator + "file2.txt"} ;
	copytool = new COPYTool(arguments);
	actualOutput = copytool.execute(WorkingDirectory.workingDirectory, stdin);
	expectedOutput = "Copy completed.";
	assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
	assertEquals(copytool.getStatusCode(), 0);
	
	String contentFile2 = readFromFile(new File(arguments[1]));
	String contentFile1 = readFromFile(new File(arguments[0]));
	assertTrue(contentFile1.equalsIgnoreCase(contentFile2));
}

/*
 * Test to check the behaviour of Copy Tool when we have 1 valid and one invalid filename argument in that order. 
 * The Copy tool needs to create a file with the invalid file's name and copy the contents of the first file into it. 
*/
@Test
public void existingFileToNonExistingFileArgumentTest(){
	String[] arguments = new String[]{"file1.txt", "fileNew.txt"} ;
	copytool = new COPYTool(arguments);
	actualOutput = copytool.execute(WorkingDirectory.workingDirectory, stdin);
	expectedOutput = "Copy completed.";
	assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
	assertFalse(("Error - Invalid input.").equalsIgnoreCase(actualOutput));
	assertEquals(copytool.getStatusCode(), 0);
	
	String contentFile2 = readFromFile(new File(arguments[1]));
	String contentFile1 = readFromFile(new File(arguments[0]));
	assertTrue(contentFile1.equalsIgnoreCase(contentFile2));
	
	File deletable = new File(arguments[1]);
	deletable.delete();
}

/*
 * Test to check the behaviour of Copy Tool when we have 1 invalid and one valid filename argument in that order. 
 * The Copy tool needs to display an error message.
*/
@Test
public void nonExistingFileToExistingFileArgumentTest(){
	String[] arguments = new String[]{"fileNew.txt", "file1.txt"} ;
	copytool = new COPYTool(arguments);
	actualOutput = copytool.execute(WorkingDirectory.workingDirectory, stdin);
	expectedOutput = "Error - Invalid input.";
	assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
	assertEquals(copytool.getStatusCode(), -1);
}

/*
 * Test to check the behaviour of Copy Tool when we have a valid filename and a valid directory name in the arguments. 
 * The Copy tool needs to create a copy of that file with the same name in that directory.
 * If there is a file with the same name already in the directory, the older version gets replaced.
*/
@Test
public void fileToDirectoryFileArgumentTest(){
	String[] arguments = new String[]{"file1.txt", "folder1"} ;
	copytool = new COPYTool(arguments);
	actualOutput = copytool.execute(WorkingDirectory.workingDirectory, stdin);
	expectedOutput = "Copy completed.";
	assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
	assertEquals(copytool.getStatusCode(), 0);
	
	String contentFile2 = readFromFile(new File(arguments[1] + File.separator + arguments[0]));
	String contentFile1 = readFromFile(new File(arguments[0]));
	assertTrue(contentFile1.equalsIgnoreCase(contentFile2));	

	File deletable = new File(contentFile2);
	deletable.delete();

}

/*
 * Test to check the behaviour of Copy Tool when we have a valid filename and a valid directory name in the arguments. 
 * The Copy tool needs to create a copy of that file with the same name in that directory.
 * If there is a file with the same name already in the directory, the older version gets replaced.
*/
@Test
public void fileToDirectoryFileRelativeArgumentTest(){
	String[] arguments = new String[]{"file1.txt", "folder1" + File.separator + "insidefolder1"} ;
	copytool = new COPYTool(arguments);
	actualOutput = copytool.execute(WorkingDirectory.workingDirectory, stdin);
	expectedOutput = "Copy completed.";
	assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
	assertEquals(copytool.getStatusCode(), 0);
	
	String contentFile2 = readFromFile(new File(arguments[1] + File.separator + arguments[0]));
	String contentFile1 = readFromFile(new File(arguments[0]));
	assertTrue(contentFile1.equalsIgnoreCase(contentFile2));	


	File deletable = new File(contentFile2);
	deletable.delete();
}

/*
 * Test to check the behaviour of Copy Tool when we have a valid asolute path filename and a valid directory name in the arguments. 
 * The Copy tool needs to create a copy of that file with the same name in that directory.
 * If there is a file with the same name already in the directory, the older version gets replaced.
*/
@Test
public void fileToDirectoryFileAbsoluteArgumentTest(){
	String[] arguments = new String[]{WorkingDirectory.workingDirectory + File.separator + "file1.txt", WorkingDirectory.workingDirectory + File.separator + "folder1" + File.separator + "insidefolder1"} ;
	copytool = new COPYTool(arguments);
	actualOutput = copytool.execute(WorkingDirectory.workingDirectory, stdin);
	expectedOutput = "Copy completed.";
	assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
	assertEquals(copytool.getStatusCode(), 0);
	
	String contentFile2 = readFromFile(new File(arguments[1] + File.separator + "file1.txt"));
	String contentFile1 = readFromFile(new File(arguments[0]));
	assertTrue(contentFile1.equalsIgnoreCase(contentFile2));	

	File deletable = new File(contentFile2);
	deletable.delete();
}

/*
 * Test to check the behaviour of Copy Tool when we have a valid filename and a invalid directory name in the arguments. 
 * The Copy tool must create a file with the same name in the working directory(This is how the terminal behves).
 * The Copy tool needs to copy contents of the first file to the second.
*/
@Test
public void fileToNonExistentDirectoryFileArgumentTest(){
	String[] arguments = new String[]{"file1.txt", "folderIdontexisthaha"} ;
	copytool = new COPYTool(arguments);
	actualOutput = copytool.execute(WorkingDirectory.workingDirectory, stdin);
	expectedOutput = "Copy completed.";
	assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
	assertFalse(("folderIdontexisthaha is an invalid directory.").equalsIgnoreCase(actualOutput));
	assertEquals(copytool.getStatusCode(), 0);	

	File deletable = new File(arguments[1]);
	deletable.delete();

}

/*
 * Test to check the behaviour of Copy Tool when we have 2 valid directory names in the arguments. 
 * The Copy tool must copy the contents of first directory to the second.
*/
@Test
public void directoryToDirectoryFileArgumentTest(){
	String[] arguments = new String[]{"folder1", "folder2"} ;
	copytool = new COPYTool(arguments);
	actualOutput = copytool.execute(WorkingDirectory.workingDirectory, stdin);
	expectedOutput = "Copy completed.";
	assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
	assertEquals(copytool.getStatusCode(), 0);	

	String contentFile2 = getStringForFiles(getFiles(new File(arguments[1])));
	String contentFile1 = getStringForFiles(getFiles(new File(arguments[0])));
	assertTrue(contentFile1.equalsIgnoreCase(contentFile2));	
}


/*
 * Test to check the behaviour of Copy Tool when we have 2 directory names one invalid and one valid in the arguments. 
 * The Copy tool must return an error message.
*/
@Test
public void invalidDirectoryToDirectoryFileArgumentTest(){
	String[] arguments = new String[]{"folderIdontexisthahaha", "folder2"} ;
	copytool = new COPYTool(arguments);
	actualOutput = copytool.execute(WorkingDirectory.workingDirectory, stdin);
	expectedOutput = "Error - Invalid input.";
	assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
	assertEquals(copytool.getStatusCode(), -1);	
}

/*
 * Test to check the behaviour of Copy Tool when we have several filenames followed by a directory name in 
 * the arguments. 
 * The Copy tool must copy the files to the directory.
*/
@Test
public void multipleFilesToDirectoryArgumentTest(){
	String[] arguments = new String[]{"file1.txt", "file2.txt","folder1"} ;
	copytool = new COPYTool(arguments);
	actualOutput = copytool.execute(WorkingDirectory.workingDirectory, stdin);
	expectedOutput = "file1.txt's copy completed. \nfile2.txt's copy completed. \n";
	
	assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
	assertEquals(copytool.getStatusCode(), 0);	

	String contentFile3 = getStringForFiles(getFiles(new File(arguments[2])));
	String expectedOutput2 ="";
	
	if((contentFile3.contains("file1.txt"))&&contentFile3.contains("file2.txt"))
	expectedOutput2 = "success"; 
	else expectedOutput2 = "failure";
	assertTrue(expectedOutput2.equalsIgnoreCase("success"));	
}

/*
 * Test to check the behaviour of Copy Tool when we have several filenames(one of which is invalid) 
 * followed by a directory name in the arguments. 
 * The Copy tool must copy the valid files to the directory and return an error message for the invalid file.
*/
@Test
public void multipleFilesOneInvalidToDirectoryArgumentTest(){
	String[] arguments = new String[]{"file1.txt", "fileIdontexisthaha.txt","folder1"} ;
	copytool = new COPYTool(arguments);
	actualOutput = copytool.execute(WorkingDirectory.workingDirectory, stdin);
	expectedOutput = "file1.txt's copy completed. \nfileIdontexisthaha.txt is an invalid file.\n";
	assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
	assertEquals(copytool.getStatusCode(), -1);	

	String contentFile3 = getStringForFiles(getFiles(new File(arguments[2])));
	String expectedOutput2 ="";
	if((contentFile3.contains("file1.txt"))&&!contentFile3.contains("fileIdontexisthaha.txt"))
	expectedOutput2 = "success"; 
	else expectedOutput2 = "failure";
	assertTrue(expectedOutput2.equalsIgnoreCase("success"));	
}

/*
 * Test to check the behaviour of Copy Tool when we have several filenames 
 * followed by a invalid directory name in the arguments. 
 * The Copy tool must return an error message.
*/
@Test
public void multipleFilesToInvalidDirectoryArgumentTest(){
	String[] arguments = new String[]{"file1.txt", "file2.txt","folderIdontexisthaha"} ;
	copytool = new COPYTool(arguments);
	actualOutput = copytool.execute(WorkingDirectory.workingDirectory, stdin);
	expectedOutput = "folderIdontexisthaha is an invalid directory.";
	assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
	assertEquals(copytool.getStatusCode(), -1);	
}


}
