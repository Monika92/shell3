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

import sg.edu.nus.comp.cs4218.fileutils.ICopyTool;
import sg.edu.nus.comp.cs4218.impl.WorkingDirectory;

public class COPYToolTest {

	private ICopyTool copytool;
	private COPYTool copy;
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
	writeToFile(input_file_2, input);
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

@After
public void after(){
	copytool = null;
	copy = null;
	if(input_file_1.exists())
		input_file_1.delete();
	if(input_file_2.exists())
		input_file_2.delete();
	if(argFolderEmpty.exists())
		argFolderEmpty.delete();
	if(argFolderInside.exists())
		argFolderInside.delete();
	if(argFolder.exists())
		argFolder.delete();
	
}

public String getCopyTestResult(String source, String dest)
{
	File sourceFile = new File(source);
	File destFile = new File(dest);
	
	if(readFromFile(sourceFile.getAbsoluteFile()) == readFromFile(destFile.getAbsoluteFile()))
		return "Copy successful";
	else 
		return "Copy failed";
}

@Test
public void copyFileToDirectoryRelativeArgumentTest(){
	String[] arguments = new String[]{"Test_Ouput_2.txt CopiedFile.txt"} ;
	copytool = new COPYTool(arguments);
	actualOutput = copytool.execute(WorkingDirectory.workingDirectory, stdin);
	expectedOutput = "Copy completed.";
	System.out.println(actualOutput);
	assertTrue(expectedOutput.equalsIgnoreCase(actualOutput));
	assertEquals(copytool.getStatusCode(), 0);
}






}
