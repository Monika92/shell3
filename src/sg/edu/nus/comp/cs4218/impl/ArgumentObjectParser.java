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
		}
		else if(command.equalsIgnoreCase("comm"))
		{
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
		}
		else if(command.equalsIgnoreCase("sort"))
		{
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
			
		}					

		else if(command.equalsIgnoreCase("uniq"))
		{
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
		}
		else if(command.equalsIgnoreCase("wc"))
		{
			int argumentSize = argument.length;
			for(int i = 0 ; i < argumentSize ; i++)
			{
				
				if(argument[i].equalsIgnoreCase("-help") || argument[i].equalsIgnoreCase("-m") || argument[i].equalsIgnoreCase("-w") || argument[i].equalsIgnoreCase("-l"))
				{
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
		}
		return argumentObject;		
	}
}
