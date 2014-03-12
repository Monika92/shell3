package sg.edu.nus.comp.cs4218.impl;

public class ArgumentObjectParser {

	ArgumentObject argumentObject ;
	
	public ArgumentObjectParser() {
		argumentObject = new ArgumentObject();
	}
	
	public ArgumentObject parseCut(String[] argument){
		
		int argumentSize = argument.length;
		for(int i = 0 ; i < argumentSize ; )
		{
			
			if(argument[i].equalsIgnoreCase("-c") || argument[i].equalsIgnoreCase("-d") || argument[i].equalsIgnoreCase("-f"))
			{
				argumentObject.options.add(argument[i]);
				argumentObject.optionArguments.add(argument[i+1]);
				i+=2;
			}
			else if(argument[i].equalsIgnoreCase("-help"))
			{
				argumentObject.options.add(argument[i]);
				argumentObject.optionArguments.add(null);
				argumentObject.fileList.add(null);
				i+=1;
			}
			else if(argument[i].equalsIgnoreCase("-"))
			{
				//do nothing for stdin
				i+=1;
			}
			else 
			{
				//the rest are considered as filenames
				argumentObject.fileList.add(argument[i]);
				i+=1;
			}
		}
		
		return argumentObject;
	}
	
	public ArgumentObject parsePaste(String[] argument){
		int argumentSize = argument.length;
		for(int i = 0 ; i < argumentSize ; )
		{
			
			if(argument[i].equalsIgnoreCase("-d"))
			{
				argumentObject.options.add(argument[i]);
				argumentObject.optionArguments.add(argument[i+1]);
				i+=2;
			}
			else if(argument[i].equalsIgnoreCase("-help") || argument[i].equalsIgnoreCase("-s"))
			{
				argumentObject.options.add(argument[i]);
				argumentObject.optionArguments.add(null);
				i+=1;
			}
			else 
			{
				argumentObject.fileList.add(argument[i]);
				i+=1;
			}
		}
		return argumentObject;
	}
	
	public ArgumentObject parseComm(String[] argument){
		int argumentSize = argument.length;
		for(int i = 0 ; i < argumentSize ; )
		{
			
			if(argument[i].equalsIgnoreCase("-help") || argument[i].equalsIgnoreCase("-c") || argument[i].equalsIgnoreCase("-d"))
			{
				argumentObject.options.add(argument[i]);
				argumentObject.optionArguments.add(null);
				i+=1;
			}
			else 
			{
				argumentObject.fileList.add(argument[i]);
				i+=1;
			}
		}
		return argumentObject;
	}

	public ArgumentObject parseSort(String[] argument){
		int argumentSize = argument.length;
		for(int i = 0 ; i < argumentSize ; )
		{
			
			if(argument[i].equalsIgnoreCase("-help") || argument[i].equalsIgnoreCase("-c"))
			{
				argumentObject.options.add(argument[i]);
				argumentObject.optionArguments.add(null);
				i+=1;
			}
			else
			{
				argumentObject.fileList.add(argument[i]);
				i+=1;
			}
		}
		
		return argumentObject;
	}

	public ArgumentObject parseUniq(String[] argument){
		int argumentSize = argument.length;
		for(int i = 0 ; i < argumentSize ; )
		{
			
			if(argument[i].equalsIgnoreCase("-f"))
			{
				argumentObject.options.add(argument[i]);
				argumentObject.optionArguments.add(argument[i+1]);
				i+=2;
			}
			else if(argument[i].equalsIgnoreCase("-help") || argument[i].equalsIgnoreCase("-i"))
			{
				argumentObject.options.add(argument[i]);
				argumentObject.optionArguments.add(null);
				i+=1;
			}
			else 
			{
				argumentObject.fileList.add(argument[i]);
				i+=1;
			}
		}
		return argumentObject;
	}
	
	public ArgumentObject parseWc(String[] argument){
		int argumentSize = argument.length;
		for(int i = 0 ; i < argumentSize ; i++)
		{
			
			if(argument[i].equalsIgnoreCase("-help") || argument[i].equalsIgnoreCase("-m") || argument[i].equalsIgnoreCase("-w") || argument[i].equalsIgnoreCase("-l"))
			{
				argument[i] = argument[i].toLowerCase();
				argumentObject.options.add(argument[i]);
				argumentObject.optionArguments.add(null);	
			}
			else if(argument[i].equalsIgnoreCase("-"))
			{
				
			}
			else 
			{
				argumentObject.fileList.add(argument[i]);
			}
	
		}
		return argumentObject;
	}

	public ArgumentObject parseGrep(String[] argument){
		int argumentSize = argument.length;
		for(int i = 0 ; i < argumentSize ; )
		{
			
			if(argument[i].equalsIgnoreCase("-help") || argument[i].equalsIgnoreCase("-c") ||
					argument[i].equalsIgnoreCase("-o") ||argument[i].equalsIgnoreCase("-v"))
			{
				argumentObject.options.add(argument[i]);
				argumentObject.optionArguments.add(null);
				i+=1;
			}
			else if(argument[i].equalsIgnoreCase("-A") || argument[i].equalsIgnoreCase("-B") ||
					argument[i].equalsIgnoreCase("-C"))
			{
				argumentObject.options.add(argument[i]);
				argumentObject.optionArguments.add(argument[i+1]);
				i+=2;
			}
			else if(argument[i].equalsIgnoreCase("-"))
			{
				//do nothing for stdin
				i+=1;
			}
			else 
			{
				argumentObject.pattern = argument[i];
				i+=1;
				//the rest are considered as filenames
				while(i <argumentSize)
				{
					argumentObject.fileList.add(argument[i]);
					i++;
				}
			}
		}
		return argumentObject;
	}
	
	public ArgumentObject parsePipe(String[] argument){
		
		int argumentLength = argument.length;
		for( int i=0; i<argumentLength; i++){
			
			
		}
		
		return argumentObject;
	}
	
	public ArgumentObject parse(String[] argument,String command)
	{
		if(command.equalsIgnoreCase("cut")){
			return parseCut(argument);
		}
		else if(command.equalsIgnoreCase("paste")){
			return parsePaste(argument);
		}
		else if(command.equalsIgnoreCase("comm")){
			return parseComm(argument);
		}
		else if(command.equalsIgnoreCase("sort")){	
			return parseSort(argument);
		}					
		else if(command.equalsIgnoreCase("uniq")){
			return parseUniq(argument);
		}
		else if(command.equalsIgnoreCase("wc")){
			return parseWc(argument);
		}
		else if(command.equalsIgnoreCase("grep")){
			return parseGrep(argument);
		}					
		else if(command.equalsIgnoreCase("pipe")){
				return parsePipe(argument);
		}
		return argumentObject;		
	}
}
