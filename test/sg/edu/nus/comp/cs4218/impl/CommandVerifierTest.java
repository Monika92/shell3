package sg.edu.nus.comp.cs4218.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.ArrayList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class CommandVerifierTest {
	
	private CommandVerifier verifier; 
	int expectedResultCode,actualResultCode;
	
	@Before
	public void before(){
		verifier = new CommandVerifier();
	}

    @After
	public void after(){
		verifier = null;
	}

    @Test
    /*
     * Overall command test
     */
    public void verifyCommandTest(){
    	String[] args = new String[3];
    	args[0]="-c";
    	args[1]="1-2";
    	args[2]="c.txt";
    	String cmd = "cut";
    	
    	actualResultCode = verifier.verifyCommand(cmd, args);
    	expectedResultCode = 1;    	
    	assertEquals(expectedResultCode, actualResultCode);	
    	
    	args = new String[2];  	
    	args[0] = "c.txt";
    	args[1] = "b.txt";
    	cmd = "delete";
    	
    	actualResultCode = verifier.verifyCommand(cmd, args);
    	expectedResultCode = 1;    	
    	assertEquals(expectedResultCode, actualResultCode);	
    }
    
    @Test
    /*
     * Invalid command Test
     * For basic - return incorrect command status code -1
     * For text Util - return display help command status code 0
     * 
     */
    public void verifyWrongCommandTest(){
    	ArrayList<String> args = new ArrayList<String>();
    	args.add("a.txt");args.add("b.txt");args.add("ccc");
    	String cmd = "cpy";
    	
    	actualResultCode = verifier.verifyBasic(cmd, args);
    	expectedResultCode = -1;    	
    	assertEquals(expectedResultCode, actualResultCode);	
    	
    	args = new ArrayList<String>();    	
    	args.add("-l");args.add("b.txt");
    	cmd = "wci";
    	
    	actualResultCode = verifier.verifyTextUtil(cmd, args);
    	expectedResultCode = -1;    	
    	assertEquals(expectedResultCode, actualResultCode);	
    }

    @Test
    /*
     * Verify Basic Command Test
     * Command : move a.txt b.txt ccc
     */
    public void verifyBasicCorrectCommandTest(){
    	ArrayList<String> args = new ArrayList<String>();
    	args.add("a.txt");args.add("b.txt");args.add("ccc");
    	String cmd = "move";
    	
    	actualResultCode = verifier.verifyBasic(cmd, args);
    	expectedResultCode = 1;    	
    	assertEquals(expectedResultCode, actualResultCode);	
    }
    
    @Test
    /*
     * Negative: Check that cmd is wrong when args are lesser than req
     */
    public void verifyBasicLesserArgsTest(){
      	ArrayList<String> args = new ArrayList<String>();
    	args.add("a.txt");
    	String cmd = "move";
    	
    	actualResultCode = verifier.verifyBasic(cmd, args);
    	expectedResultCode = -1;  
    	assertEquals(expectedResultCode, actualResultCode);	
    }
    
    @Test
    /*
     * Negative: If argument has more than allowed num of arguments
     */
    public void verifyBasicMoreArgsTest(){
      	ArrayList<String> args = new ArrayList<String>();
    	args.add("./a.txt");args.add("../b.txt");
    	String cmd = "cd";
    	
    	actualResultCode = verifier.verifyBasic(cmd, args);
    	expectedResultCode = -1;    	
    	assertEquals(expectedResultCode, actualResultCode);	
    }
   
    
    @Test
    /*
     * Boundary case: Positive: Check if cmd such as "delete"/"cat" allow infinite args
     * Checking with 1000 arguments
     */
    public void verifyBasicInifiniteArgsTest(){
      	ArrayList<String> args = new ArrayList<String>();
      	for(int i = 0; i<1000; i++){
      		args.add("a.txt");
      	}
      	
    	String cmd = "delete";
    	
    	actualResultCode = verifier.verifyBasic(cmd, args);
    	expectedResultCode = 1;    	
    	assertEquals(expectedResultCode, actualResultCode);	
    }
    
    @Test
    /*
     * Negative: No arguments - boundary case
     */
    public void verifyBasicNoArgsTest(){
      	ArrayList<String> args = new ArrayList<String>();
    	String cmd = "cat";
    	
    	actualResultCode = verifier.verifyBasic(cmd, args);
    	expectedResultCode = -1;    	
    	assertEquals(expectedResultCode, actualResultCode);	
    }
    
    @Test
    /*
     * Combined: 1 positive and 1 negative case for stdin
     * Which commands allow "stdin"
     */
    public void verifyBasicCommandAllowsStdinTest(){
      	ArrayList<String> args = new ArrayList<String>();
      	args.add("-");
    	String cmd = "cat";
    	
    	actualResultCode = verifier.verifyBasic(cmd, args);
    	expectedResultCode = 1;    	
    	assertEquals(expectedResultCode, actualResultCode);	
    	
    	args = new ArrayList<String>();
      	args.add("a.txt");args.add("-");
    	cmd = "copy";
    	
    	actualResultCode = verifier.verifyBasic(cmd, args);
    	expectedResultCode = -1;    	
    	assertEquals(expectedResultCode, actualResultCode);	
    }
    
    
    @Test
    /*
     * Give invalid options for a command
     * command: cut -p 1-2 file
     */
    public void verifyWrongOptionsTest(){
    	ArrayList<String> args = new ArrayList<String>();
    	args.add("-p");args.add("1-2");args.add("file1");
    	String cmd = "cut";
    	
    	actualResultCode = verifier.verifyTextUtil(cmd, args);
    	expectedResultCode = -1;    	
    	assertEquals(expectedResultCode, actualResultCode);	

    }
       
    @Test
    /*
     * Boundary case
     * Negative: No arguments for command
     * command: wc
     */
    public void verifyTextutilNoArgsTest(){
       	ArrayList<String> args = new ArrayList<String>();
    	String cmd = "wc";
    	
    	actualResultCode = verifier.verifyTextUtil(cmd, args);
    	expectedResultCode = -1;    	
    	assertEquals(expectedResultCode, actualResultCode);	
    }
    
    @Test
    /*
     * Negative case: command has no options but with wrong num default file args
     * command: comm file1
     */
    public void verifyTextutilNoOptionsWrongArgumentsTest(){
       	ArrayList<String> args = new ArrayList<String>();
    	String cmd = "comm";
    	args.add("file1");
    	
    	actualResultCode = verifier.verifyTextUtil(cmd, args);
    	expectedResultCode = -1;    	
    	assertEquals(expectedResultCode, actualResultCode);	
    }
    
    @Test
    /*
     * Positive case: 
     * Correct command: uniq file1 file2 
     */
    public void verifyTextutilNoOptionsRightArgumentsTest(){
       	ArrayList<String> args = new ArrayList<String>();
    	String cmd = "uniq";
    	args.add("file1"); args.add("file2");
    	
    	actualResultCode = verifier.verifyTextUtil(cmd, args);
    	expectedResultCode = 1;    	
    	assertEquals(expectedResultCode, actualResultCode);	
    }
    
    @Test
    /*
     * Positive case:
     * correct command: wc
     */
    public void verifyWcCommandNoOptions(){
       	ArrayList<String> args = new ArrayList<String>();
    	String cmd = "wc";
    	args.add("fname");
    	
    	actualResultCode = verifier.verifyTextUtil(cmd, args);
    	expectedResultCode = 1;    	
    	assertEquals(expectedResultCode, actualResultCode);	
    }
    
    @Test
    /*
     * Positive case: command has no options but with correct num default file args
     * command: uniq -f 1 -i hello
     */
    public void verifyTextutilTest(){
       	ArrayList<String> args = new ArrayList<String>();
    	String cmd = "uniq";
    	args.add("-f"); args.add("1");args.add("-i");args.add("hello");
    	
    	actualResultCode = verifier.verifyTextUtil(cmd, args);
    	expectedResultCode = 1;    	
    	assertEquals(expectedResultCode, actualResultCode);	
    }
    
    @Test
    /*
     * Positive case: CUT Tool with correct args
     * command: cut -c 1-2 file1 file2
     */
    public void verifyTextUtilCutToolTest(){
       	ArrayList<String> args = new ArrayList<String>();
    	String cmd = "cut";
    	args.add("-c"); args.add("1-2"); args.add("file1"); args.add("file2");
    	
    	actualResultCode = verifier.verifyTextUtil(cmd, args);
    	expectedResultCode = 1;    	
    	assertEquals(expectedResultCode, actualResultCode);	
    }
    
    @Test
    /*
     * Negative case: mark incorrect when options w/o args have args
     * command: wc -l 0 -m file
     */
    public void verifyTextUtilOptionsWOArgsWrongcaseTest(){
       	ArrayList<String> args = new ArrayList<String>();
    	String cmd = "wc"; 
    	args.add("-l"); args.add("0"); args.add("-m"); args.add("file");
    	
    	actualResultCode = verifier.verifyTextUtil(cmd, args);
    	expectedResultCode = -1;    	
    	assertEquals(expectedResultCode, actualResultCode);	
    }
    
    @Test
    /*
     * Positive case: mark correct when options w/o args have no args
     * command: wc -l -m file1
     */
    public void verifyTextUtilOptionsWOArgsRightcaseTest(){
     	ArrayList<String> args = new ArrayList<String>();
    	String cmd = "wc"; 
    	args.add("-l"); args.add("-m"); args.add("file1");
    	
    	actualResultCode = verifier.verifyTextUtil(cmd, args);
    	expectedResultCode = 1;    	
    	assertEquals(expectedResultCode, actualResultCode);	
    }
    
    @Test
    /*
     * Negative case: mark incorrect when options with args have no args
     * command: paste -d -s file1
     */
    public void verifyTextUtilOptionsWArgsWrongcaseTest(){
     	ArrayList<String> args = new ArrayList<String>();
    	String cmd = "paste"; 
    	args.add("-d"); args.add("-s");args.add("file");
    	
    	actualResultCode = verifier.verifyTextUtil(cmd, args);
    	expectedResultCode = -1;    	

    	assertEquals(expectedResultCode, actualResultCode);	
    }
    
    @Test
    /*
     * Positive case: mark correct when options with args have args
     * command: paste -d : -s file
     */
    public void verifyTextUtilOptionsWArgsRightcaseTest(){
    	ArrayList<String> args = new ArrayList<String>();
    	String cmd = "paste"; 
    	args.add("-d"); args.add(":"); args.add("-s"); args.add("file");
    	
    	actualResultCode = verifier.verifyTextUtil(cmd, args);
    	expectedResultCode = 1;    	
    	assertEquals(expectedResultCode, actualResultCode);	
    }
    
    @Test
    /*
     * Positive case: verify options come before filename args
     * command: uniq file1 file2 -i
     */
    public void verifyTextUtilOptionsBeforeArgumentsTest(){
    	ArrayList<String >args = new ArrayList<String>();
    	String cmd = "uniq"; 
    	args.add("file"); args.add("file2"); args.add("-i");
    	
    	actualResultCode = verifier.verifyTextUtil(cmd, args);
    	expectedResultCode = -1;    
    	assertEquals(expectedResultCode, actualResultCode);
    	
    	cmd = "paste"; 
    	args.clear();
    	args.add("file1"); args.add("file2"); args.add("-s");
    	
    	actualResultCode = verifier.verifyTextUtil(cmd, args);
    	expectedResultCode = -1;    
    	assertEquals(expectedResultCode, actualResultCode);
    	
    }
    
    @Test
    /*
     * Positive: Combination of options for command - with only allowed options for cmd
     * command: wc -m -l file1
     */
    public void verifyTextutilMultipleOptionsCorrectTest(){
    	ArrayList<String >args = new ArrayList<String>();
    	String cmd = "wc"; 
    	args.add("-m"); args.add("-l"); args.add("file");
    	
    	actualResultCode = verifier.verifyTextUtil(cmd, args);
    	expectedResultCode = 1;    	
    	assertEquals(expectedResultCode, actualResultCode);
    }
    
    @Test
    /*
     * Negative: Combination of options for command - with wrong options for cmd
     * command: wc -l -s file1
     */
    public void verifyTextutilMultipleOptionsWrongTest(){
    	ArrayList<String >args = new ArrayList<String>();
    	String cmd = "wc"; 
    	args.add("-l"); args.add("-p"); args.add("file");
    	
    	actualResultCode = verifier.verifyTextUtil(cmd, args);
    	expectedResultCode = -1;   

    	assertEquals(expectedResultCode, actualResultCode);
    }
    
    @Test
    /*
     * Paste -s -d a.txt
     * No filenames present since a.txt is taken as delim string
     */
    public void verifyTextutilWrongArgumentsTest(){
    	ArrayList<String> args = new ArrayList<String>();
    	String cmd = "paste"; 
    	args.add("-s"); args.add("-d");args.add("file");
    	
    	actualResultCode = verifier.verifyTextUtil(cmd, args);
    	expectedResultCode = -1;    	
    	assertEquals(expectedResultCode, actualResultCode);	
    }
    
    @Test
    /*
     * Positive cases
     * uniq -f 0 i file1 file2
     * uniq -i -f 0 file1 file2
     */
    public void verifyTextutilOrderingofArgsTest(){
    	ArrayList<String> args = new ArrayList<String>();
    	String cmd = "uniq"; 
    	args.add("-f"); args.add("0"); args.add("i"); args.add("file1");args.add("file2");
    	
    	actualResultCode = verifier.verifyTextUtil(cmd, args);
    	expectedResultCode = 1;    	
    	assertEquals(expectedResultCode, actualResultCode);
    	
    	args = new ArrayList<String>();
    	cmd = "uniq"; 
    	args.add("-i"); args.add("-f"); args.add("0"); args.add("file1");args.add("file2");
    	
    	actualResultCode = verifier.verifyTextUtil(cmd, args);
    	expectedResultCode = 1;    	
    	assertEquals(expectedResultCode, actualResultCode);
    }
    
    @Test
    /*
     * wrong: cut -c 1-2 file1 >
     * correct: cut -c 1-2 file1 > a.txt
     */
    public void verifyCommandWithRedirectionTest(){
    	String[] args = new String[4];
    	String cmd = "cut"; 
    	args[0] = ("-c"); args[1] = ("1-2"); args[2] = ("file");args[3] = (">");
    	
    	actualResultCode = verifier.verifyCommand(cmd, args);
    	expectedResultCode = -1;    	
    	assertEquals(expectedResultCode, actualResultCode);
    	
    	args = new String[5];
    	cmd = "cut"; 
    	args[0] = "-c"; args[1] = "1-2"; args[2] = "file";
    	args[3] = ">";args[4] = "op";
    	
    	actualResultCode = verifier.verifyCommand(cmd, args);
    	expectedResultCode = 1;    	
    	assertEquals(expectedResultCode, actualResultCode);
    	
    }
    
    @Test
    /*
     * To verify that -a.txt could be possible file name and
     * not thrown as error as incorrect option
     */
    public void verifyTextutilFileNameWithSpecChars(){
    	ArrayList<String> args = new ArrayList<String>();
    	args.add("-a.txt");args.add("b.txt");
    	String cmd = "paste";
    	
    	actualResultCode = verifier.verifyTextUtil(cmd, args);
    	expectedResultCode = 1;    	
    	assertEquals(expectedResultCode, actualResultCode);	
    }
    
    @Test
    /*
     * If option -help is present, allow priority to command
     * Return 0 to indicate print -help
     */
    public void verifyGetHelpOptionPriority(){
    	ArrayList<String> args = new ArrayList<String>();
    	args.add("a.txt");args.add("-help");
    	String cmd = "sort";
    	
    	actualResultCode = verifier.verifyTextUtil(cmd, args);
    	expectedResultCode = -1;    	
    	assertEquals(expectedResultCode, actualResultCode);	

    }
    
    @Test
    /*
     * Checking cut
     * cut -d " " -d ":" -f 1-3 -
     */
    public void verifyCut(){
    	ArrayList<String> args = new ArrayList<String>();
    	args.add("-d");args.add(" ");
    	args.add("-d"); args.add(":");args.add("-f");args.add("1-2");args.add("-");
    	//args.add("hello");
    	
    	String cmd = "cut";
    	
    	actualResultCode = verifier.verifyTextUtil(cmd, args);
    	expectedResultCode = 1;    	
    	assertEquals(expectedResultCode, actualResultCode);	

    }
    
    
    @Test
    /*
     * Test for pipe command. Bypassed check here
     * since pipe is checked within pipe tool
    */
    public void verifyPipe(){
    	
    	String cmd = "pipe";
    	String[] args = {"pwd","paste"};
    	
    	actualResultCode = verifier.verifyCommand(cmd, args);
    	expectedResultCode = 1;    	
    	assertEquals(expectedResultCode, actualResultCode);	
    }
    
    @Test
    /*
     * Test for grep command
     */
    public void verifyGrep(){
    	
    	String cmd = "grep";
    	String[] args = {"patt","fname"};
    	
    	actualResultCode = verifier.verifyCommand(cmd, args);
    	expectedResultCode = 1;    	
    	assertEquals(expectedResultCode, actualResultCode);	
    	
    	String[] args2 = {"patt","-"};
    	actualResultCode = verifier.verifyCommand(cmd, args2);
    	expectedResultCode = 1;    	
    	assertEquals(expectedResultCode, actualResultCode);	
    }
    
    @Test
    /*
     * Test case for command: Sort -
     * 
     */
    public void verifySort(){
    	
    	String cmd = "sort";
    	String[] args = {"-"};
    	
    	actualResultCode = verifier.verifyCommand(cmd, args);
    	expectedResultCode = 1;    	
    	assertEquals(expectedResultCode, actualResultCode);	
    }
}
