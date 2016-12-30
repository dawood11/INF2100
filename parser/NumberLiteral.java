package parser;
import main.*;
import types.*;
import scanner.*;
import static scanner.TokenKind.*;
						/*|------------^*/
/* <numeric literal> ::=	<digit 0-9> */

public class NumberLiteral extends UnsignedConstant {
	int number = -1;

	NumberLiteral(int lNum) {
		super(lNum);
	}

	public int value() {
		if (number != -1) {
			return number;		
		}
		return -1;
	}

	@Override public String identify() {
		return "<number literal> on line " + lineNum;
	}

	@Override void check(Block curScope, Library lib) {
		type = lib.it.type;
		//System.out.println("NUMBER LITERAL: "+ type);
	}

	@Override void genCode(CodeFile f) {
		f.genInstr("", "movl", "$" + number + ",%eax", " " + number);
	}

	@Override public void prettyPrint() {
		Main.log.prettyPrint("" + number);
	}

	public static NumberLiteral parse(Scanner s) {
		enterParser("number literal");

		NumberLiteral nl = new NumberLiteral(s.curLineNum());
		nl.number = s.curToken.intVal;
		s.skip(intValToken);

		leaveParser("number literal");
		return nl;
	}
}
