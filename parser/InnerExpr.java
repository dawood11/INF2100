package parser;
import main.*;
import scanner.*;
import static scanner.TokenKind.*;

/* <inner expr> ::= '(' <expression> ')'*/
public class InnerExpr extends Factor {
	Expression ex;

	InnerExpr(int lNum) {
		super(lNum);
	}

	@Override public String identify() {
		return "<inner expr> on line " + lineNum;
	}

	@Override void check(Block curScope, Library lib) {
		ex.check(curScope, lib);
		type = ex.type;
		//System.out.println("InnerExpr " + type);
	}

	@Override void genCode(CodeFile f) {
		ex.genCode(f);
	}

	@Override public void prettyPrint() {
		Main.log.prettyPrint("(");
		ex.prettyPrint();
		Main.log.prettyPrint(")");
	}

	static InnerExpr parse(Scanner s) {
		enterParser("inner expr");
		
		InnerExpr ie = new InnerExpr(s.curLineNum());

		s.skip(leftParToken);
		ie.ex = Expression.parse(s);
		s.skip(rightParToken);

		leaveParser("inner expr");
		return ie;
	}
}
