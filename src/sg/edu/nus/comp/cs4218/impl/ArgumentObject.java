package sg.edu.nus.comp.cs4218.impl;

import java.util.ArrayList;
import java.util.HashMap;

public class ArgumentObject {

	ArrayList<String> fileList;
	ArrayList<String> options ;
	ArrayList<String> optionArguments;
	
	public ArgumentObject()
	{
		fileList = new ArrayList<String>();
		options = new ArrayList<String>();
		optionArguments = new ArrayList<String>();
	}
	
	public ArrayList<String> getOptions()
	{
		return options;
	}
	public ArrayList<String> getOptionArguments()
	{
		return optionArguments;
	}
	public ArrayList<String> getFileList()
	{
		return fileList;
	}
}
