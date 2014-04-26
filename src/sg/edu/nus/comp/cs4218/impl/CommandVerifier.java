package sg.edu.nus.comp.cs4218.impl;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class CommandVerifier {

	private boolean pipeCommand;

	//Upper bound for number of arguments
	private final int INF = 10000;

	ArrayList<String> basCmds;
	ArrayList<String> tuCmds;

	LinkedHashMap<String, Integer> cutMap;
	LinkedHashMap<String, Integer> pasteMap;
	LinkedHashMap<String, Integer> commMap;
	LinkedHashMap<String, Integer> sortMap;
	LinkedHashMap<String, Integer> wcMap;
	LinkedHashMap<String, Integer> uniqMap;
	LinkedHashMap<String, Integer> grepMap;

	ArrayList<Integer> pwdMap;
	ArrayList<Integer> cdLsMap;
	ArrayList<Integer> copyMoveMap;
	ArrayList<Integer> echoMap;
	ArrayList<Integer> delCatMap;

	public CommandVerifier(){				
		initializeCommandList();
		initializeBasic();
		initializeTextUtil();
		pipeCommand = false;
	}

	public CommandVerifier(boolean isPipeCmd){				
		initializeCommandList();
		initializeBasic();
		initializeTextUtil();
		pipeCommand = true;
	}

	private void initializeCommandList(){
		basCmds = new ArrayList<String>();
		basCmds.add("pwd");
		basCmds.add("cd");
		basCmds.add("ls");
		basCmds.add("copy");
		basCmds.add("move");
		basCmds.add("delete");
		basCmds.add("echo");
		basCmds.add("cat");

		tuCmds = new ArrayList<String>();
		tuCmds.add("cut");
		tuCmds.add("paste");
		tuCmds.add("comm");
		tuCmds.add("sort");
		tuCmds.add("wc");
		tuCmds.add("uniq");
		tuCmds.add("grep");
	}

	private void initializeBasic(){
		pwdMap = new ArrayList<Integer>();
		pwdMap.add(0);pwdMap.add(0);

		cdLsMap = new ArrayList<Integer>();
		cdLsMap.add(0);cdLsMap.add(1);

		copyMoveMap = new ArrayList<Integer>();
		copyMoveMap.add(2);copyMoveMap.add(INF);

		echoMap = new ArrayList<Integer>();
		echoMap.add(0);echoMap.add(INF);

		delCatMap = new ArrayList<Integer>();
		delCatMap.add(1);delCatMap.add(INF);
	}

	private void initializeTextUtil(){

		cutMap = new LinkedHashMap<String, Integer>();
		cutMap.put("-c", 1);
		cutMap.put("-d", 1);
		cutMap.put("-f", 1);
		cutMap.put("-help", 0);
		cutMap.put("defMin", 1);
		cutMap.put("defMax", INF);

		pasteMap = new LinkedHashMap<String, Integer>();
		pasteMap.put("-s", 0);
		pasteMap.put("-d", 1);
		pasteMap.put("-help", 0);
		pasteMap.put("defMin", 1);
		pasteMap.put("defMax", INF);

		commMap = new LinkedHashMap<String, Integer>();
		commMap.put("-c",0);
		commMap.put("-d",0);
		commMap.put("-help",0);
		commMap.put("defMin",2);
		commMap.put("defMax",2);

		sortMap = new LinkedHashMap<String, Integer>();
		sortMap.put("-c",0);
		sortMap.put("-help",0);
		sortMap.put("defMin",1);
		sortMap.put("defMax",INF);

		wcMap = new LinkedHashMap<String, Integer>();
		wcMap.put("-m",0);
		wcMap.put("-w",0);
		wcMap.put("-l",0);
		wcMap.put("-help",0);
		wcMap.put("defMin",1);
		wcMap.put("defMax",INF);

		uniqMap = new LinkedHashMap<String, Integer>();
		uniqMap.put("-f",1);
		uniqMap.put("-i",0);
		uniqMap.put("-help",0);
		uniqMap.put("defMin",1);
		uniqMap.put("defMax",INF);

		grepMap = new LinkedHashMap<String, Integer>();
		grepMap.put("-A",1);
		grepMap.put("-B",1);
		grepMap.put("-C",1);
		grepMap.put("-c",0);
		grepMap.put("-o",0);
		grepMap.put("-v",0);
		grepMap.put("-help",0);
		grepMap.put("defMin",2); //include 1 pattern and atleast 1 (file or "-")
		grepMap.put("defMax",INF);

	}

	public int verifyBasic(String cmd, ArrayList<String> args){
		cmd = cmd.toLowerCase();
		//if command is from pipe, then either with or w/o args are both valid
		if(pipeCommand){
			if(cmd.equalsIgnoreCase("cat") ||cmd.equalsIgnoreCase("echo")){
				if(args.size() == 0){
					return 2;
				}
				else if(args.size() == 1 && args.get(0).compareTo("-") == 0){
					return 2;
				}
				return 1;
			}
		}

		int numArgs = args.size();		
		if(cmd.equals("pwd")){
			return basicCheck(pwdMap, numArgs);
		}
		else if(cmd.equals("cd") || cmd.equals("ls") && args.contains("-") == false){
			return basicCheck(cdLsMap, numArgs);
		}
		else if((cmd.equals("copy") || cmd.equals("move")) && args.contains("-") == false){
			return basicCheck(copyMoveMap, numArgs);
		}
		else if(cmd.equals("echo")){
			return basicCheck(echoMap, numArgs);
		}
		else if(cmd.equals("delete")&& args.contains("-") == false){
			return basicCheck(delCatMap, numArgs);
		}
		else if (cmd.equals("cat")){
			return basicCheck(delCatMap, numArgs);
		}

		return -1;
	}

	private int basicCheck(ArrayList<Integer> map, int numArgs){

		int lowerLimit = map.get(0);
		int upperLimit = map.get(1);

		if(upperLimit == INF && numArgs >= lowerLimit){
			return 1;
		}

		if(numArgs >= lowerLimit && numArgs <= upperLimit){
			return 1;
		}
		return -1;
	}
	
	public int checkPriorCut(ArrayList<String> args){
		int valid = 1;
		
		int count = 0;
		ArrayList<String> optFromArgs = new ArrayList<String>();
		for( int i = 0; i < args.size(); i++){
			if(args.get(i).length() >1 && args.get(i).charAt(0) == '-'){
				optFromArgs.add(args.get(i));
				count++;
			}
			
		}
		
		//if no args, wrong
		if(count == 0){
			return -1;
		}
		
		boolean atleastOne = false;
		if(optFromArgs.contains("-c")){
			atleastOne = true;
		}
		else if(optFromArgs.contains("-f")){
			if(!optFromArgs.contains("-d")){
				return -1;
			}
			else{
				atleastOne = true;
			}
		}
		else if(optFromArgs.contains("-help")){
			atleastOne = true;
		}
		
		if(atleastOne == false){
			return -1;
		}
		
		return valid;
	}
	
	public int verifyTextUtil(String cmd, ArrayList<String> args){
		cmd = cmd.toLowerCase();
		if(cmd.equals("cut")){
			//should contain "-c"
			if(checkPriorCut(args) == 1){
				return textUtilCheck(cmd,cutMap, args);
			}
		}
		else if(cmd.equals("paste")){
			return textUtilCheck(cmd,pasteMap, args);
		}
		else if(cmd.equals("comm") && args.contains("-") == false){
			return textUtilCheck(cmd,commMap, args);
		}
		else if(cmd.equals("sort")){
			return textUtilCheck(cmd,sortMap, args);
		}
		else if(cmd.equals("wc")){
			return textUtilCheck(cmd,wcMap, args);
		}
		else if(cmd.equals("uniq")){
			return textUtilCheck(cmd,uniqMap, args);
		}
		else if(cmd.equals("grep")){
			return textUtilCheck(cmd,grepMap, args);
		}

		return -1;
	}

	private int textUtilCheck(String cmd,LinkedHashMap<String, Integer> map, 
			ArrayList<String> args){		
		ArrayList<Integer> indexUsed = new ArrayList<Integer>();
		int numOptions = map.size() - 2;
		
		//if options contain -help return only help
		if(args.size() == 1 && args.get(0).equalsIgnoreCase("-help")){
			return 1;
		}

		//Check if arguments are valid options
		for( int i=0; i<args.size(); i++){	
			String argToCheck = args.get(i);

			int numArgs = 0,idx = 0;

			if(argToCheck.length() == 2  && argToCheck.charAt(0) == '-' 
					|| argToCheck.equalsIgnoreCase("-help")){
				if(map.containsKey(argToCheck)){
					
					numArgs = map.get(argToCheck);
					idx = i;
					indexUsed.add(i);
		
					//check if num of args for option after its idx is correct
					for(int j=1; j<=numArgs; j++){
						if(idx + j >= args.size()){
							break;			
						}

						String arg = args.get(idx + j);
						//if immediately followed arguments are other options					
						if(map.keySet().contains(arg)){
							return -1;
						}
						indexUsed.add(idx + j);					
					}
					
				}
				else{ 
					return -1;
				}
			}
		}
	
		ArrayList<Integer> defaultArgIdxList = new ArrayList<Integer>();

		//check for default option args
		int countDefaultArgs = 0;
		for (int i=0; i<args.size(); i++){
			if(!indexUsed.contains(i)){
				defaultArgIdxList.add(i);
				countDefaultArgs++;
			}
		}

		//verifer called from within each tool
		if(!pipeCommand && (countDefaultArgs < map.get("defMin"))){
			return -1;
		}
		if(!pipeCommand && (map.get("defMax") != INF)){
			if(countDefaultArgs != map.get("defMax")){
				return -1;
			}
		}	
		if(!pipeCommand && areOptionsBeforeFilenames(defaultArgIdxList, indexUsed) == 0){
			return -1;
		}

		//in case verifier is called from pipe
		if(pipeCommand){
			if(!cmd.equalsIgnoreCase("grep")){
				if(countDefaultArgs == 0){
					return 2;//code to enable stdin since no def args 
				}
				else{
					return 1;//if not 0, these are valid commands
							 //with min def args present. Stdin disabled
				}
			}		
			else if(cmd.equalsIgnoreCase("grep")){
				if(countDefaultArgs == 0){
					return -1; //invalid since grep needs "pattern"
				}
				if(countDefaultArgs == 1){
					return 2; //case where only pattern and no other args
							  //enable stdin.
				}
				else{
					return 1; //disable stdin since it already has args 
				}				
			}	
			return -1;
		}
		return 1;
	}

	//checking that options come before default filename inputs
	private int areOptionsBeforeFilenames(ArrayList<Integer> l1, ArrayList<Integer> l2){

		for(int i=0; i<l1.size(); i++){
			for(int j=0; j<l2.size(); j++){
				if(l1.get(i) < l2.get(j)){
					return 0;
				}
			}
		}

		return 1;
	}

	public int verifyCommand(String cmd, String[] args){

		ArrayList<String> argList = new ArrayList<String>();
		int resultCode = -1;

		//Perform no checking for pipe
		if(cmd == "pipe"){
			return 1;
		}


		if(args != null){
			
			for(int i = 0; i<args.length; i++){
				if(args[i] == null){
					return -1;
					
				}
				argList.add(args[i]);
			}
			
			
			//get list of args without ">" and after
			if(argList.contains(">")){
			if (argList.indexOf(">") == argList.size()-2){
					argList.subList(0,argList.indexOf(">")); //removing > filename from check
				}
				else{
					return -1; // case where ">" not followed by fileName
				}
			}
		
		}
		else if(cmd == "pwd")
			return 1;
		else{
			return -1;
		}

		if(basCmds.contains(cmd.toLowerCase())){
			resultCode = verifyBasic(cmd, argList);
			return resultCode;
		}
		else if (tuCmds.contains(cmd.toLowerCase())){
			resultCode = verifyTextUtil(cmd, argList);
			return resultCode;
		}				
		
		return resultCode;
	}
}
