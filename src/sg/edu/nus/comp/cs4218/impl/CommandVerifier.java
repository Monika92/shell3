package sg.edu.nus.comp.cs4218.impl;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class CommandVerifier {

	private final int INF = 10000;

	ArrayList<String> basCmds;
	ArrayList<String> tuCmds;

	LinkedHashMap<String, Integer> cutMap;
	LinkedHashMap<String, Integer> pasteMap;
	LinkedHashMap<String, Integer> commMap;
	LinkedHashMap<String, Integer> sortMap;
	LinkedHashMap<String, Integer> wcMap;
	LinkedHashMap<String, Integer> uniqMap;

	ArrayList<Integer> pwd_Map;
	ArrayList<Integer> cd_ls_Map;
	ArrayList<Integer> copy_move_Map;
	ArrayList<Integer> echo_Map;
	ArrayList<Integer> del_cat_Map;

	public CommandVerifier(){				
		initializeCommandList();
		initializeBasic();
		initializeTextUtil();
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
	}

	private void initializeBasic(){
		pwd_Map = new ArrayList<Integer>();
		pwd_Map.add(0);pwd_Map.add(0);

		cd_ls_Map = new ArrayList<Integer>();
		cd_ls_Map.add(0);cd_ls_Map.add(1);

		copy_move_Map = new ArrayList<Integer>();
		copy_move_Map.add(2);copy_move_Map.add(INF);

		echo_Map = new ArrayList<Integer>();
		echo_Map.add(0);echo_Map.add(INF);

		del_cat_Map = new ArrayList<Integer>();
		del_cat_Map.add(1);del_cat_Map.add(INF);
	}

	private void initializeTextUtil(){

		cutMap = new LinkedHashMap<String, Integer>();
		cutMap.put("-c", 1);
		cutMap.put("-d", 1);
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
		uniqMap.put("-f",0);
		uniqMap.put("-i",0);
		uniqMap.put("-help",0);
		uniqMap.put("defMin",1);
		uniqMap.put("defMax",INF);

	}

	private int verifyBasic(String cmd, ArrayList<String> args){

		int numArgs = args.size();

		if(cmd.equals("pwd")){
			return basicCheck(pwd_Map, numArgs);
		}
		else if(cmd.equals("cd") || cmd.equals("ls") && args.contains("-") == false){
			return basicCheck(cd_ls_Map, numArgs);
		}
		else if((cmd.equals("copy") || cmd.equals("move")) && args.contains("-") == false){
			return basicCheck(copy_move_Map, numArgs);
		}
		else if(cmd.equals("echo")){
			return basicCheck(echo_Map, numArgs);
		}
		else if(cmd.equals("delete")&& args.contains("-") == false){
			return basicCheck(del_cat_Map, numArgs);
		}
		else if (cmd.equals("cat")){
			return basicCheck(del_cat_Map, numArgs);
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

	private int verifyTextUtil(String cmd, ArrayList<String> args){

		if(cmd.equals("cut")){
			//should contain "-c"
			if(args.contains("-c")){
				return textUtilCheck(cutMap, args);
			}
		}
		else if(cmd.equals("paste")){
			return textUtilCheck(pasteMap, args);
		}
		else if(cmd.equals("comm") && args.contains("-") == false){

			return textUtilCheck(commMap, args);
		}
		else if(cmd.equals("sort") && args.contains("-") == false){
			return textUtilCheck(sortMap, args);
		}
		else if(cmd.equals("wc")){
			return textUtilCheck(wcMap, args);
		}
		else if(cmd.equals("uniq")){
			return textUtilCheck(uniqMap, args);
		}

		return 0;
	}

	private int textUtilCheck(LinkedHashMap<String, Integer> map, ArrayList<String> args){

		ArrayList<Integer> indexUsed = new ArrayList<Integer>();
		int numOptions = map.size() - 2;

		//check if command has only -help
		if(args.size() == 1){
			if(args.get(0).equals("-help")){
				return 0;
			}
		}

		//Iterate thro possible options for given command
		for(int i = 0; i<numOptions; i++){

			String value = (new ArrayList<String>(map.keySet())).get(i);
			int numArgs = 0, idx = 0;

			//if option exists in args
			if(args.contains(value)){

				numArgs = map.get(value);
				idx = args.indexOf(value);
				indexUsed.add(idx);

				//check if num of args after option is correct
				for(int j=1; j<=numArgs; j++){

					String arg = args.get(idx + j);
					if(tuCmds.contains(arg)){
						return 0;
					}
					indexUsed.add(idx + j);					
				}				
			}
		}

		ArrayList<Integer> defArgIdxList = new ArrayList<Integer>();

		//check for default option args
		int countDefaultArgs = 0;
		for (int i=0; i<args.size(); i++){
			if(!indexUsed.contains(i)){
				defArgIdxList.add(i);
				countDefaultArgs++;
			}
		}

		if(countDefaultArgs < map.get("defMin")){
			return 0;
		}
		if(map.get("defMax") != INF){
			if(countDefaultArgs != map.get("defMax")){
				return 0;
			}
		}	

		if(areOptionsBeforeFilenames(defArgIdxList, indexUsed) == 0){
			return 0;
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

		if(args != null){


			for(int i = 0; i<args.length; i++){
				argList.add(args[i]);
			}

			if(argList.contains(">")){
				if (argList.indexOf(">") == argList.size()-2){
					argList.subList(0,argList.indexOf(">")); //removing > filename from check
				}
				else{
					return 0; // case where ">" not followed by fileName
				}
			}
		}
		if(basCmds.contains(cmd)){
			resultCode = verifyBasic(cmd, argList);
			return resultCode;
		}
		else if (tuCmds.contains(cmd)){
			resultCode = verifyTextUtil(cmd, argList);
			return resultCode;
		}				
		return resultCode;
	}
}
