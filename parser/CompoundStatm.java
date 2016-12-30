package parser;

import java.util.ArrayList;
import main.*;
import scanner.*;
import static scanner.TokenKind.*;

/* <compund statm> ::= 'begin' <statm list> 'end'*/

public class CompoundStatm extends Statement {
	StatmList sl = null;

	CompoundStatm(int lNum) {
		super(lNum);
	}

	@Override public String identify() {
		return "<compound statm> on line " + lineNum;
	}

	@Override void check(Block curScope, Library lib) {
		sl.check(curScope, lib);
	}

	@Override void genCode(CodeFile f) {
		sl.genCode(f);
	}

	@Override public void prettyPrint() {
		Main.log.prettyPrintLn("begin");
		Main.log.prettyIndent();
		sl.prettyPrint();
		Main.log.prettyOutdent();
		Main.log.prettyPrint("end");
	}

	static CompoundStatm parse(Scanner s) {
		enterParser("compound statm");

		CompoundStatm cs = new CompoundStatm(s.curLineNum());
		s.skip(beginToken);
		cs.sl = StatmList.parse(s);
		s.skip(endToken);

		leaveParser("compound statm");
		return cs;
	}
}
