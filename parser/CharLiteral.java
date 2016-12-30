package parser;
import main.*;
import scanner.*;
import types.*;
import static scanner.TokenKind.*;

/* <char literal> ::= ''' <char except '> '''*/
						   /*''' ''' */

public class CharLiteral extends UnsignedConstant {
	Character cID;

	CharLiteral(int lNum) {
		super(lNum);
	}
	public int value() {
		if (cID != null) {
			return (int)cID;			
		}
		return -1;
	}

	@Override public String identify() {
		return "<char literal> on line " + lineNum;
	}

	@Override void check(Block curScope, Library lib) {
		type = lib.ct.type;
		//System.out.println("CHARLITERAL: "+ type);
	}

	@Override void genCode(CodeFile f) {
		f.genInstr("", "movl", "$" + (int)cID + ",%eax", " '" + cID + "'");
	}
	
	@Override public void prettyPrint() {
		Main.log.prettyPrint("\'");
		if(cID == '\''){
			Main.log.prettyPrint("\'");
		}
		Main.log.prettyPrint(cID.toString());
		Main.log.prettyPrint("\'");
		//System.out.println(cID);
	}

	static CharLiteral parse(Scanner s) {
		enterParser("char literal");

		CharLiteral cl = new CharLiteral(s.curLineNum());
		cl.cID = s.curToken.charVal;
		s.skip(charValToken);

		leaveParser("char literal");
		return cl;
	}
}