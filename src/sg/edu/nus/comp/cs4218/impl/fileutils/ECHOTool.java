package sg.edu.nus.comp.cs4218.impl.fileutils;

import java.io.File;
import java.util.ArrayList;

import sg.edu.nus.comp.cs4218.IShell;
import sg.edu.nus.comp.cs4218.fileutils.IEchoTool;
import sg.edu.nus.comp.cs4218.impl.ATool;

public class ECHOTool extends ATool implements IEchoTool {

	public ECHOTool(String[] arguments) {
		super(arguments);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String echo(String toEcho) {
		// TODO Auto-generated method stub
		return toEcho;
	}

	@Override
	public String execute(File workingDir, String stdin) {
		// TODO Auto-generated method stub
		
		StringBuilder sb = new StringBuilder();
		for(String s : args){
			sb.append(s);
			sb.append(" ");
		}
		String str = sb.toString();
		str=str.replace("\"", "");
		str=str.replace("\'", "");
		str = str.trim();
		
		return echo(str);
	}
	

}
