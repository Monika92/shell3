package sg.edu.nus.comp.cs4218.impl;

public class ArgumentObjectParser {

	ArgumentObject argumentObject ;
	
	public ArgumentObjectParser() {
		// TODO Auto-generated constructor stub
		
		argumentObject = new ArgumentObject();
	}
	public ArgumentObject parse(String[] argument,String command)
	{
		if(command.equalsIgnoreCase("cut"))
		{
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
				else 
				{
					argumentObject.fileList.add(argument[i]);
					i+=1;
				}
			}
		}
		else if(command.equalsIgnoreCase("paste"))
		{
			
		}
		else if(command.equalsIgnoreCase("sort"))
		{
			int argumentSize = argument.length;
			for(int i = 0 ; i < argumentSize ; )
			{
				
				if(argument[i].equalsIgnoreCase("-help"))
				{
					argumentObject.options.add(argument[i]);
					argumentObject.optionArguments.add(null);
					argumentObject.fileList.add(null);
					i+=1;
				}
				else if(argument[i].equalsIgnoreCase("-c") )
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
			
		}
		else if(command.equalsIgnoreCase("wc"))
		{
			int argumentSize = argument.length;
			for(int i = 0 ; i < argumentSize ; i++)
			{
				argumentObject.optionArguments.add(null);
				if(argument[i].equalsIgnoreCase("-help") || argument[i].equalsIgnoreCase("-m") || argument[i].equalsIgnoreCase("-w") || argument[i].equalsIgnoreCase("-l"))
				{
					argumentObject.options.add(argument[i]);
				}
				else 
				{
					argumentObject.fileList.add(argument[i]);
				}
			}
		}
		return argumentObject;		
	}
}
