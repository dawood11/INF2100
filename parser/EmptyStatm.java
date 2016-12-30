package parser;
import main.*;
import scanner.*;
import static scanner.TokenKind.*;

/* <empty statm> ::= <>*/
public class EmptyStatm extends Statement {

	EmptyStatm(int lNum) {
		super(lNum);
	}

	@Override public String identify() {
		return "<empty statm> on line " + lineNum;
	}

	@Override void check(Block curScope, Library lib) {}

	@Override void genCode(CodeFile f) {}

	@Override public void prettyPrint() {}

	static EmptyStatm parse(Scanner s) {
		enterParser("empty statm");

		EmptyStatm es = new EmptyStatm(s.curLineNum());

		leaveParser("empty statm");
		return es;
	}
}
