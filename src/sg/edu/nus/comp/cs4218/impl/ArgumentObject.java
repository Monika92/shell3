package sg.edu.nus.comp.cs4218.impl;

import java.util.ArrayList;

public class ArgumentObject {

	ArrayList<String> fileList;
	ArrayList<String> options ;
	ArrayList<String> optionArguments;
	String pattern;
	
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
	public String getPattern()
	{
		return pattern;
	}
}
