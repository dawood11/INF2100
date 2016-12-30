package parser;
import main.*;
import scanner.*;
import static scanner.TokenKind.*;

/* <if-statm> ::= 'if' <expression> 'then' <statement>  'else' <statement>*/
													  /*<				 >*/
public class IfStatm extends Statement {
	Expression ex;
	Statement st1;
	Statement st2;

	IfStatm(int lNum) {
		super(lNum);
	}

	@Override public String identify() {
		return "<if-statm> on line " + lineNum;
	}

	@Override void check(Block curScope, Library lib) {
		ex.check(curScope, lib);
		ex.type.checkType(lib.bt.type, "if-test", this, "if-test is not Boolean.");
		st1.check(curScope, lib);
		if (st2 != null) {
			st2.check(curScope, lib);
		}
	}

	@Override void genCode(CodeFile f) {
		String startLabel = f.getLocalLabel();
		f.genInstr("","","","Start if-statement");
		ex.genCode(f);
		f.genInstr("", "cmpl", "$0,%eax", "");
		f.genInstr("", "je", startLabel, "");
		st1.genCode(f);
		if (st2 == null) {
			f.genInstr(startLabel, "", "", "End if-statement");
		} else{
			String endLabel = f.getLocalLabel();
			f.genInstr("", "jmp", endLabel, "");
			f.genInstr(startLabel, "", "", "");
			st2.genCode(f);
			f.genInstr(endLabel, "", "", "End if-else-statement");
		}
		
	}

	@Override public void prettyPrint() {
		Main.log.prettyPrint("if ");
		ex.prettyPrint();
		Main.log.prettyPrintLn(" then ");
		Main.log.prettyIndent();
		st1.prettyPrint();
		Main.log.prettyOutdent();
		if(st2 != null) {
			Main.log.prettyPrintLn("");
			Main.log.prettyPrintLn("else ");
			Main.log.prettyIndent();
			st2.prettyPrint();
			Main.log.prettyOutdent();
		}
	}

	static IfStatm parse(Scanner s) {
		enterParser("if statm");

		IfStatm is = new IfStatm(s.curLineNum());
		s.skip(ifToken);
		is.ex = Expression.parse(s);
		s.skip(thenToken);
		is.st1 = Statement.parse(s);

		if(s.curToken.kind == elseToken) {
			s.skip(elseToken);
			is.st2 = Statement.parse(s);
		}

		leaveParser("if statm");
		return is;
	}
}
