package sg.edu.nus.comp.cs4218.impl.extended2;

import java.io.File;

import sg.edu.nus.comp.cs4218.extended2.ICutTool;
import sg.edu.nus.comp.cs4218.impl.ATool;
/**
 * Do not modify this file
 */
/*
 * 
 * cut : prints a substring that is specified in a certain range
 *
 *	Command Format - cut [OPTIONS] [FILE]
 *		FILE - Name of the file, when no file is present (denoted by "-") use standard input OPTIONS
 *			-c LIST: Use LIST as the list of characters to cut out. Items within the list may be
 *					separated by commas, and ranges of characters can be separated with dashes.
 *					For example, list ‘1-5,10,12,18-30’ specifies characters 1 through 5, 10,12 and
 *					18 through 30.
 *			-d DELIM: Use DELIM as the field-separator character instead of the TAB character
 *			-help : Brief information about supported options
 */
public class CUTTool extends ATool implements ICutTool{

	File filename;
	String options,list,helpOutput,input,delim;
	int noOfArguments;

	public CUTTool(String[] arguments) {
		super(arguments);

		helpOutput = "usage: cut -b list [-n] [file ...] \n  "
				+ "cut -c list [file ...] \n "
				+ "cut -f list [-s] [-d delim] [file ...]";
		// TODO Auto-generated constructor stub
	}

	@Override
	public String cutSpecfiedCharacters(String list, String input) {
		// TODO Auto-generated method stub
		return	 null;
	}

	@Override
	public String cutSpecifiedCharactersUseDelimiter(String list, String delim,
			String input) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getHelp() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String execute(File workingDir, String stdin) 
	{
		if(args!=null)
		{
			noOfArguments = args.length;
		}
		switch(noOfArguments)
		{
			case 1 : if(args[0].equalsIgnoreCase("help"))
				return helpOutput;
				break;

			case 3 : if(args[0].equalsIgnoreCase("-c"))
					{


					}
					else if(args[0].equalsIgnoreCase("-d"))
					{

					}
					else if(args[0].equalsIgnoreCase("-f"))
					{


					}
					break;
			case 5 : if(args[0].equalsIgnoreCase("-d"))
					{
						delim = args[1];
						if(args[2].equalsIgnoreCase("-f"))
						{
							list = args[3];
							if(args[4].equalsIgnoreCase("-"))
								input = stdin;
						}
					}
			break;
		}
		
		return helpOutput;
	}
}
