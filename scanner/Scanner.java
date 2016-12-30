package scanner;

import main.Main;
import static scanner.TokenKind.*;

import java.io.*;

public class Scanner {
    public Token curToken = null, nextToken = null; 

    private LineNumberReader sourceFile = null;
    private String sourceFileName, sourceLine = "";
    private int sourcePos = 0, errorOnLine = 0;
    private boolean commentEnds = true;

    public Scanner(String fileName) {
	sourceFileName = fileName;
	try {
	    sourceFile = new LineNumberReader(new FileReader(fileName));
	} catch (FileNotFoundException e) {
	    Main.error("Cannot read " + fileName + "!");
	}

	
		readNextToken();	readNextToken();
    }


    public String identify() {
	return "Scanner reading " + sourceFileName;
    }
 

    public int curLineNum() {
	return curToken.lineNum;
    }

    
    private void error(String message) {
	Main.error("Scanner error on " +
		   (curLineNum()<0 ? "last line" : "line "+curLineNum()) + 
		   ": " + message);
    }


    public void readNextToken() {
    	curToken = nextToken;  nextToken = null;

		// Del 1 her:
		//check if sourceline is empty og has reached the end
		checkWhitespaces();
		//if you are at the end of the file a eof-token is added
		if (getFileLineNum() == 0) {
			nextToken = new Token(eofToken, getFileLineNum());
			//System.out.println(nextToken.identify());
	    	Main.log.noteToken(nextToken);
			return;
		}

    	if (sourceLine.length() == 0) {
    			return;
    	}
    	Token t = null;
    	String word = "";
    	//goes through each line and adds tokens
    	for (sourcePos = sourcePos; sourcePos < sourceLine.length(); sourcePos++) {
    		//skips over every space or tab
    		if(sourceLine.charAt(sourcePos) == ' ' || sourceLine.charAt(sourcePos) == '\t' ){
    			continue;
    		}
    		
    		//adds the character to the string to be testet
    		word += sourceLine.toLowerCase().charAt(sourcePos);
    		//if its a comment, give the correct output og continue when its done..
    		if (checkIfComment(word) == false) {
    			if (commentEnds == false) {
    				Main.error(-1, "No end for comment starting on line " + errorOnLine);
    				return;
    			} else{
    				//checkWhitespaces();
    				nextToken = curToken;
    				readNextToken();
    				return;
    			}
    		}
    		/*if (word.equals(" ")) {
    			System.out.println("Scanner error on line " + getFileLineNum() + ": No end for comment starting on line " + getFileLineNum());
    			Main.error(getFileLineNum(), "No end for comment starting on line ");
    			//nextToken = new Token(eofToken, getFileLineNum());
				//System.out.println(nextToken.identify());
		    	//Main.log.noteToken(nextToken);
				return;
    		}*/

			//checks for char-tokens
			if (sourceLine.charAt(sourcePos) == '\'') {
	    		//if the char is a '
	    		if (sourceLine.charAt(sourcePos+1) == '\'' && sourceLine.charAt(sourcePos+2) == '\'' && sourceLine.charAt(sourcePos+3) == '\'') {
	    			t = new Token(sourceLine.charAt(sourcePos+1), getFileLineNum());
					nextToken = t;
    				//System.out.println(t.identify());
    				Main.log.noteToken(nextToken);
    				sourcePos += 4;
    				//readNextToken();
    				return;
	    		}
	    		//all other chars
	    		if(sourceLine.charAt(sourcePos+2) == '\''){
	    			t = new Token(sourceLine.charAt(sourcePos+1), getFileLineNum());
					nextToken = t;
    				//System.out.println(t.identify());
    				Main.log.noteToken(nextToken);
    				sourcePos += 3;
    				//readNextToken();
    				return;
	    		}
	    		//if the ' is missing
	    		Main.error(getFileLineNum(), "Illegal char literal!");
    			return;
    		}
    		//if it is a token other then number, name or char
    		t = ifTokenKind(sourceLine.charAt(sourcePos), sourcePos);
    		if(t != null){
    			if (t.kind.toString().length() == 2) {
    				sourcePos++;
    			}
    			nextToken = t;
    			//System.out.println(t.identify());
    			Main.log.noteToken(nextToken);
    			sourcePos++;
    			//readNextToken();
    			return;
    		}
    		//checks if the next char is a space or a token, then it makes a name or number token of this
			if(ifTokenKind(sourceLine.charAt(sourcePos+1), sourcePos+1) != null || sourceLine.charAt(sourcePos+1) == ' ' || sourceLine.charAt(sourcePos+1) == '\t'){
				//Check if word is number token
				boolean digit = false;
				for (int i = 0; i < word.length(); i++) {
					digit = isDigit(word.charAt(i));
					if(digit == false){
						break;
					}
				}
				//check if word is valid name token
				boolean word1 = isLetterAZ(word.charAt(0));
				boolean word2 = false;
				boolean digit2 = false;
				if (word1 == true){
					for (int j = 1; j < word.length(); j++) {
						word2 = isLetterAZ(word.charAt(j));
						digit2 = isDigit(word.charAt(j));
						if(word2 == false && digit2 == false){
							Main.error(getFileLineNum(), "Illegal character: " + word.charAt(j));
    						return;
    					}
					}	
				} else if(digit == false && word1 == false) {
					Main.error(getFileLineNum(), "Illegal character: " + word.charAt(0));
    				return;
				}
				//makes number token
				if(digit == true){
					t = new Token(Integer.parseInt(word), getFileLineNum());
				} //makes name token
				else if(word1 == true && (word2 == true || digit2 == true || word.length() == 1)){
					t = new Token(word, getFileLineNum());
				}
				nextToken = t;
				//System.out.println(t.identify());
    			Main.log.noteToken(nextToken);
				sourcePos++;
				//readNextToken();
				return;
			}

    	}
    	if(t != null){
    		//System.out.println(t.identify());
	    	Main.log.noteToken(nextToken);
    	}
    }

    //added trim() to remove leading and trailing whitespaces
    private void readNextLine() {
	if (sourceFile != null) {
	    try {
		sourceLine = sourceFile.readLine();
		if (sourceLine == null) {
		    sourceFile.close();  sourceFile = null;
		    sourceLine = "";  
		} else {
			sourceLine = sourceLine.trim();
		    sourceLine += " ";
		}
		sourcePos = 0;
	    } catch (IOException e) {
		Main.error("Scanner error: unspecified I/O error!");
	    }
	}
	if (sourceFile != null) 
	    Main.log.noteSourceLine(getFileLineNum(), sourceLine);
    }


    private int getFileLineNum() {
	return (sourceFile!=null ? sourceFile.getLineNumber() : 0);
    }


    // Character test utilities:

    private boolean isLetterAZ(char c) {
	return 'A'<=c && c<='Z' || 'a'<=c && c<='z';
    }


    private boolean isDigit(char c) {
	return '0'<=c && c<='9';
    }


    // Parser tests:

    public void test(TokenKind t) {
    	//System.out.println(curToken);
	if (curToken.kind != t)
	    testError(t.toString());
    }

    public void testError(String message) {
	Main.error(curLineNum(), 
		   "Expected a " + message +
		   " but found a " + curToken.kind + "!");
    }

    public void skip(TokenKind t) {
	test(t);  
	readNextToken();
    }

    //cheks if current word is a comment and returns false if it is a comment
    //commentEnds is false if the comment never finished before reaching the end of the file
    public boolean checkIfComment(String word){
    	if (word.equals("{")) {
			errorOnLine = getFileLineNum();
    		while(!(word.charAt(word.length()-1) == '}')){
    			checkWhitespaces();
    			if (getFileLineNum() == 0) {
			    	commentEnds = false;
			    	return false;
				}

    			word+=sourceLine.toLowerCase().charAt(sourcePos);
    			sourcePos++;
    		}
    		return false;
    	}
    	if (word.equals("/") && !nextCharNull(sourcePos)) {
    		if(sourceLine.charAt(sourcePos + 1) == '*'){
				errorOnLine = getFileLineNum();
    			sourcePos++;
    			word+=sourceLine.toLowerCase().charAt(sourcePos);
    			sourcePos++;
    			while(true){
    				checkWhitespaces();
    				if (getFileLineNum() == 0) {
				    	commentEnds = false;
						return false;
					}
    				if (word.charAt(word.length()-1) == '*') {
		    			word+=sourceLine.toLowerCase().charAt(sourcePos);
		    			sourcePos++;
			    		if(word.charAt(word.length()-1) == '/'){
			    			word+=sourceLine.toLowerCase().charAt(sourcePos);

			    			return false;
			    		}
			    	}else{
			    		if (!nextCharNull(sourcePos)) {
			    			word+=sourceLine.toLowerCase().charAt(sourcePos);
			    			sourcePos++;
			    		} else {
			    			readNextLine();
			    		}
			    	}
    			}
    		}
    	}
    	return true;
    }
    //checks if sourcepos/i has reached the end of line
    //this is to avoid index out of bound exceptions
    public boolean nextCharNull(int i) {
    	if (sourceLine.length() == i+1) {
    		return true;
    	}
    	return false;
    }

    //check if the char is one of these tokens (not number, name or char)
    public Token ifTokenKind(char c, int i){
    	Token t = null;
    	switch (c) {
				case '+':	t = new Token(addToken, getFileLineNum()); break;
				case ':':	t = nextdoubleToken(':', i); break;
				//case ":=":	t = new Token(assignToken, getFileLineNum());
				case ',':	t = new Token(commaToken, getFileLineNum()); break;
				case '.':	t = nextdoubleToken('.', i); break;
				//case '..':	t = new Token(rangeToken, getFileLineNum());
				case '=':	t = new Token(equalToken, getFileLineNum()); break;
				case '>':	t = nextdoubleToken('>', i); break;
				//case '>=':	t = new Token(greaterEqualToken, getFileLineNum());
				case '[':	t = new Token(leftBracketToken, getFileLineNum()); break;
				case '(':	t = new Token(leftParToken, getFileLineNum()); break;
				case '<':	t = nextdoubleToken('<', i); break;
				//case '<=':	t = new Token(lessEqualToken, getFileLineNum());
				//case '<>':	t = new Token(notEqualToken, getFileLineNum());
				case '*':	t = new Token(multiplyToken, getFileLineNum()); break;
				case ']':	t = new Token(rightBracketToken, getFileLineNum()); break;
				case ')':	t = new Token(rightParToken, getFileLineNum()); break;
				case ';':	t = new Token(semicolonToken, getFileLineNum()); break;
				case '-':	t = new Token(subtractToken, getFileLineNum()); break;
		}
		return t;
    }

    //checks for whitespaces and newlines, if it finds a newline it continues reading a new line
    public void checkWhitespaces(){
    	while((sourcePos+1) == sourceLine.length() || sourceLine.length() == 0){
			readNextLine();
			if (getFileLineNum() == 0) {
				return;
			}
		}
		while(sourceLine.equals(" ") || sourceLine.equals("\n") || sourceLine.equals("\t")){
			readNextLine();
			if (getFileLineNum() == 0) {
				return;
			}
		}
    }

    //if the token is :=, .., <=, <>, >= we had to make som extra tests
    public Token nextdoubleToken(char c, int i){
    	Token t = null;
    	switch (c) {
    		case ':': 
    			if(!nextCharNull(i)){
					if(sourceLine.charAt(i+1) == '='){
						t = new Token(assignToken, getFileLineNum());
					} else {
						t = new Token(colonToken, getFileLineNum());
					}
				} else {
					t = new Token(colonToken, getFileLineNum());
				} break;
    		case '.': 
    			if(!nextCharNull(i)){
					if(sourceLine.charAt(i+1) == '.'){
						t = new Token(rangeToken, getFileLineNum());
					} else {
						t = new Token(dotToken, getFileLineNum());
					}
				} else {
					t = new Token(dotToken, getFileLineNum());
				} break;
    		case '<':
    			if(!nextCharNull(i)){
					if(sourceLine.charAt(i+1) == '='){
						t = new Token(lessEqualToken, getFileLineNum());
					} else if(sourceLine.charAt(i+1) == '>'){
						t = new Token(notEqualToken, getFileLineNum());
					} else {
						t = new Token(lessToken, getFileLineNum());
					}
				} else {
					t = new Token(lessToken, getFileLineNum());
				} break;
    		case '>':
    		   	if(!nextCharNull(i)){
					if(sourceLine.charAt(i+1) == '='){
						t = new Token(greaterEqualToken, getFileLineNum());
					} else {
						t = new Token(greaterToken, getFileLineNum());
					}
				} else {
					t = new Token(greaterToken, getFileLineNum());
				} break;
    		default:
    			//System.out.println("No valid char");
	    		Main.error(getFileLineNum(), "No valid char");
    	}
    	return t;
    } 
}