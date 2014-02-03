package sg.edu.nus.comp.cs4218.impl;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class CommandVerifier {

	private final int UNDEF = 0;

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
		copy_move_Map.add(2);copy_move_Map.add(UNDEF);

		echo_Map = new ArrayList<Integer>();
		echo_Map.add(0);echo_Map.add(UNDEF);

		del_cat_Map = new ArrayList<Integer>();
		del_cat_Map.add(1);del_cat_Map.add(UNDEF);
	}

	private void initializeTextUtil(){

		cutMap = new LinkedHashMap<String, Integer>();
		cutMap.put("-c", 1);
		cutMap.put("-d", 1);
		cutMap.put("-help", 0);
		cutMap.put("defMin", 1);
		cutMap.put("defMax", UNDEF);

		pasteMap = new LinkedHashMap<String, Integer>();
		pasteMap.put("-s", 0);
		pasteMap.put("-d", 1);
		pasteMap.put("-help", 0);
		pasteMap.put("defMin", 1);
		pasteMap.put("defMax", UNDEF);

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
		sortMap.put("defMax",UNDEF);

		wcMap = new LinkedHashMap<String, Integer>();
		wcMap.put("-m",0);
		wcMap.put("-w",0);
		wcMap.put("-l",0);
		wcMap.put("-help",0);
		wcMap.put("defMin",1);
		wcMap.put("defMax",UNDEF);

		uniqMap = new LinkedHashMap<String, Integer>();
		uniqMap.put("-f",0);
		uniqMap.put("-i",0);
		uniqMap.put("-help",0);
		uniqMap.put("defMin",1);
		uniqMap.put("defMax",UNDEF);

	}

	private int verifyBasic(String cmd, ArrayList<String> args){

		int numArgs = args.size();

		if(cmd.equals("pwd")){
			return basicCheck(pwd_Map, numArgs);
		}
		else if(cmd.equals("cd") || cmd.equals("ls")){
			return basicCheck(cd_ls_Map, numArgs);
		}
		else if(cmd.equals("copy") || cmd.equals("move")){
			return basicCheck(copy_move_Map, numArgs);
		}
		else if(cmd.equals("echo")){
			return basicCheck(echo_Map, numArgs);
		}
		else if(cmd.equals("del") || cmd.equals("cat")){
			return basicCheck(del_cat_Map, numArgs);
		}

		return 0;
	}

	private int basicCheck(ArrayList<Integer> map, int numArgs){

		if(numArgs < map.get(0)){
			return 0;
		}
		if(map.get(1) == UNDEF){
			return 1;
		}
		else if(numArgs > map.get(1)){
			return 0;
		}

		return 0;
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
		else if(cmd.equals("comm")){
			return textUtilCheck(commMap, args);
		}
		else if(cmd.equals("sort")){
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
		if(map.get("defMax") != UNDEF){
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
		for(int i = 0; i<args.length; i++){
			argList.add(args[i]);
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
