package sg.edu.nus.comp.cs4218.impl.fileutils;

import static org.junit.Assert.assertEquals;
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

import sg.edu.nus.comp.cs4218.fileutils.IMoveTool;
import sg.edu.nus.comp.cs4218.impl.WorkingDirectory;

public class MOVEToolTest {

	private IMoveTool movetool;
	private MOVETool move;
	String actualOutput,expectedOutput="";
	File workingDirectory;
	String stdin;
	String input = "This is a test run.";
	List<File> childFilesList = new ArrayList();  
	File argFolder,argFolderEmpty,argFolderInside;
	File input_file_1,input_file_2;
	

@Before
public void before() {
	
	WorkingDirectory.changeWorkingDirectory(new File(System.getProperty("user.dir")));
	stdin = null;
	argFolder = new File("folder1");
	argFolderEmpty = new File("folder2");
	argFolderInside = new File(WorkingDirectory.workingDirectory+File.separator+"folder1"+ File.separator +"insideFolder1") ;
	input_file_2 = new File("file2.txt");
	input_file_1 = new File("file1.txt");
	writeToFile(input_file_1, input);
	writeToFile(input_file_2, "abc");
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
		System.out.println("Unable to create output file");
	}
}

public String readFromFile(File input_file){
	String output = ""; FileReader fr = null;
	try{
		fr = new FileReader(input_file);
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
	movetool = null;
	move = null;
	if(input_file_1.exists())
		deleteFolder(input_file_1);//input_file_1.deleteOnExit();
	if(input_file_2.exists())
		deleteFolder(input_file_2);//input_file_2.deleteOnExit();
	if(argFolderEmpty.exists())
		deleteFolder(argFolderEmpty);//argFolderEmpty.deleteOnExit();
	if(argFolderInside.exists())
		deleteFolder(argFolderInside);//argFolderInside.deleteOnExit();
	if(argFolder.exists())
		deleteFolder(argFolder);//argFolder.deleteOnExit();	
}

public String ifFileExists(File f)
{
	if(f.exists())
		return "yes";
	else
		return "no";
}

@Test
public void existingFileToExistingFileArgumentTest(){
	String[] arguments = new String[]{"file1.txt", "file2.txt"} ;
	
	String contentFile0 = readFromFile(new File(arguments[0]));
	
	movetool = new MOVETool(arguments);
	actualOutput = movetool.execute(WorkingDirectory.workingDirectory, stdin);
	expectedOutput = "Move completed.";
	System.out.println(actualOutput);
	assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
	assertEquals(movetool.getStatusCode(), 0);
	
	//check if content has been move
	String contentFile1 = readFromFile(new File(arguments[1]));
	System.out.println(contentFile0 + "   :::   " + contentFile1);
	assertTrue(contentFile0.equalsIgnoreCase(contentFile1));
	
	//check if file1.txt has been removed
	String result = ifFileExists(new File(arguments[0]));
	assertTrue(result.equalsIgnoreCase("no"));
}

@Test
public void nonExistingFileToExistingFileArgumentTest(){
	String[] arguments = new String[]{"fileNew.txt", "file1.txt"} ;
	movetool = new MOVETool(arguments);
	actualOutput = movetool.execute(WorkingDirectory.workingDirectory, stdin);
	expectedOutput = "Error - Invalid input.";
	System.out.println(actualOutput);
	assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
	assertEquals(movetool.getStatusCode(), 0);
}


@Test
public void FileToDirectoryFileArgumentTest(){
	String[] arguments = new String[]{"file1.txt", "folder1"} ;
	String contentFile0 = readFromFile(new File(arguments[0]));

	movetool = new MOVETool(arguments);
	actualOutput = movetool.execute(WorkingDirectory.workingDirectory, stdin);
	expectedOutput = "file1.txt's move completed.";
	System.out.println(actualOutput);
	assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
	assertEquals(movetool.getStatusCode(), 0);
	
	String contentFile1 = readFromFile(new File(arguments[1] + File.separator + "file1.txt"));
	assertTrue(contentFile0.equalsIgnoreCase(contentFile1));	

	//check if file1.txt has been removed
	String result = ifFileExists(new File(arguments[0]));
	assertTrue(result.equalsIgnoreCase("no"));

}

@Test
public void existingFileToExistingFileAbsoluteArgumentTest(){
	String[] arguments = new String[]{WorkingDirectory.workingDirectory + File.separator + "file1.txt", WorkingDirectory.workingDirectory + File.separator + "file2.txt"} ;
	String contentFile0 = readFromFile(new File(arguments[0]));

	movetool = new MOVETool(arguments);
	actualOutput = movetool.execute(WorkingDirectory.workingDirectory, stdin);
	expectedOutput = "Move completed.";
	System.out.println(actualOutput);
	assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
	assertEquals(movetool.getStatusCode(), 0);
	
	String contentFile1 = readFromFile(new File(arguments[1]));
	assertTrue(contentFile0.equalsIgnoreCase(contentFile1));	

	//check if file1.txt has been removed
	String result = ifFileExists(new File(arguments[0]));
	assertTrue(result.equalsIgnoreCase("no"));
}

@Test
public void existingFileToNonExistingFileArgumentTest(){
	String[] arguments = new String[]{"file1.txt", "fileNew.txt"} ;
	String contentFile0 = readFromFile(new File(arguments[0]));

	movetool = new MOVETool(arguments);
	actualOutput = movetool.execute(WorkingDirectory.workingDirectory, stdin);
	expectedOutput = "Move completed.";
	System.out.println(actualOutput);
	assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
	assertEquals(movetool.getStatusCode(), 0);
	
	String contentFile1 = readFromFile(new File(arguments[1]));
	assertTrue(contentFile0.equalsIgnoreCase(contentFile1));	

	//check if file1.txt has been removed
	String result = ifFileExists(new File(arguments[0]));
	assertTrue(result.equalsIgnoreCase("no"));

	File deletable = new File(arguments[1]);
	deletable.delete();
}

@Test
public void FileToDirectoryFileRelativeArgumentTest(){
	String[] arguments = new String[]{"file1.txt", "folder1" + File.separator + "insidefolder1"} ;
	String contentFile0 = readFromFile(new File(arguments[0]));

	movetool = new MOVETool(arguments);
	actualOutput = movetool.execute(WorkingDirectory.workingDirectory, stdin);
	expectedOutput = "file1.txt's move completed.";
	System.out.println(actualOutput);
	assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
	assertEquals(movetool.getStatusCode(), 0);
	
	String contentFile1 = readFromFile(new File(arguments[1] + File.separator + "file1.txt"));
	assertTrue(contentFile0.equalsIgnoreCase(contentFile1));	

	//check if file1.txt has been removed
	String result = ifFileExists(new File(arguments[0]));
	assertTrue(result.equalsIgnoreCase("no"));
}

@Test
public void FileToDirectoryFileAbsoluteArgumentTest(){
	String[] arguments = new String[]{WorkingDirectory.workingDirectory + File.separator + "file1.txt", WorkingDirectory.workingDirectory + File.separator + "folder1" + File.separator + "insidefolder1"} ;
	String contentFile0 = readFromFile(new File(arguments[0]));

	movetool = new MOVETool(arguments);
	actualOutput = movetool.execute(WorkingDirectory.workingDirectory, stdin);
	expectedOutput = arguments[0] + "'s move completed.";
	System.out.println(actualOutput);
	assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
	assertEquals(movetool.getStatusCode(), 0);
	
	String contentFile1 = readFromFile(new File(arguments[1] + File.separator + "file1.txt"));
	assertTrue(contentFile0.equalsIgnoreCase(contentFile1));	

	//check if file1.txt has been removed
	String result = ifFileExists(new File(arguments[0]));
	assertTrue(result.equalsIgnoreCase("no"));
}


@Test
public void FileToNonExistentDirectoryFileArgumentTest(){
	String[] arguments = new String[]{"file1.txt", "folderIdontexisthaha"} ;
	String contentFile0 = readFromFile(new File(arguments[0]));

	movetool = new MOVETool(arguments);
	actualOutput = movetool.execute(WorkingDirectory.workingDirectory, stdin);
	expectedOutput = "Move completed.";
	System.out.println(actualOutput);
	assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
	assertEquals(movetool.getStatusCode(), 0);	

	//check if file1.txt has been removed
	String result = ifFileExists(new File(arguments[0]));
	assertTrue(result.equalsIgnoreCase("no"));
	
	File deletable = new File(arguments[1]);
	deletable.delete();
}

@Test
public void directoryToDirectoryFileArgumentTest(){
	String[] arguments = new String[]{"folder1", "folder2"} ;
	String contentFile0 = getStringForFiles(getFiles(new File(arguments[0])));
	
	movetool = new MOVETool(arguments);
	actualOutput = movetool.execute(WorkingDirectory.workingDirectory, stdin);
	expectedOutput = "Move completed.";
	System.out.println(actualOutput);
	assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
	assertEquals(movetool.getStatusCode(), 0);	

	String contentFile1 = getStringForFiles(getFiles(new File(arguments[1])));
	System.out.println(actualOutput);
	assertTrue(contentFile0.contains(contentFile1));	
	
	//check if file1.txt has been removed
	String result = ifFileExists(new File(arguments[0]));
	assertTrue(result.equalsIgnoreCase("no"));
}

@Test
public void invalidDirectoryToDirectoryFileArgumentTest(){
	String[] arguments = new String[]{"folderIdontexisthahaha", "folder2"} ;
	movetool = new MOVETool(arguments);
	actualOutput = movetool.execute(WorkingDirectory.workingDirectory, stdin);
	expectedOutput = "Error - Invalid input.";
	System.out.println(actualOutput);
	assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
	assertEquals(movetool.getStatusCode(), 0);	
}

@Test
public void multipleFilesToDirectoryArgumentTest(){
	String[] arguments = new String[]{"file1.txt", "file2.txt","folder1"} ;
	movetool = new MOVETool(arguments);
	actualOutput = movetool.execute(WorkingDirectory.workingDirectory, stdin);
	expectedOutput = "file1.txt's move completed. \nfile2.txt's move completed. \n";
	System.out.println(actualOutput);
	assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
	assertEquals(movetool.getStatusCode(), 0);	

	String contentFile3 = getStringForFiles(getFiles(new File(arguments[2])));
	String expectedOutput2 ="";
	if((contentFile3.contains("file1.txt"))&&contentFile3.contains("file2.txt"))
	expectedOutput2 = "success"; 
	else expectedOutput2 = "failure";
	assertTrue(expectedOutput2.equalsIgnoreCase("success"));	

	//check if file1.txt has been removed
	String result = ifFileExists(new File(arguments[0]));
	assertTrue(result.equalsIgnoreCase("no"));
		
	//check if file1.txt has been removed
	result = ifFileExists(new File(arguments[1]));
	assertTrue(result.equalsIgnoreCase("no"));
}

@Test
public void multipleFilesOneInvalidToDirectoryArgumentTest(){
	String[] arguments = new String[]{"file1.txt", "fileIdontexisthaha.txt","folder1"} ;
	movetool = new MOVETool(arguments);
	actualOutput = movetool.execute(WorkingDirectory.workingDirectory, stdin);
	expectedOutput = "file1.txt's move completed. \nfileIdontexisthaha.txt is invalid input.\n";
	System.out.println(actualOutput);
	assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
	assertEquals(movetool.getStatusCode(), 0);	

	String contentFile3 = getStringForFiles(getFiles(new File(arguments[2])));
	String expectedOutput2 ="";
	if((contentFile3.contains("file1.txt"))&&!contentFile3.contains("fileIdontexisthaha.txt"))
	expectedOutput2 = "success"; 
	else expectedOutput2 = "failure";
	assertTrue(expectedOutput2.equalsIgnoreCase("success"));	
	
	//check if file1.txt has been removed
	String result = ifFileExists(new File(arguments[0]));
	assertTrue(result.equalsIgnoreCase("no"));
		
}


@Test
public void multipleFilesToInvalidDirectoryArgumentTest(){
	String[] arguments = new String[]{"file1.txt", "file2.txt","folderIdontexisthaha"} ;
	movetool = new MOVETool(arguments);
	actualOutput = movetool.execute(WorkingDirectory.workingDirectory, stdin);
	expectedOutput = "folderIdontexisthaha is invalid input.";
	System.out.println(actualOutput);
	assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
	assertEquals(movetool.getStatusCode(), 0);	

	//check if file1.txt has been removed
	String result = ifFileExists(new File(arguments[0]));
	assertTrue(result.equalsIgnoreCase("yes"));
	
	//check if file1.txt has been removed
	result = ifFileExists(new File(arguments[1]));
	assertTrue(result.equalsIgnoreCase("yes"));
		
}


}