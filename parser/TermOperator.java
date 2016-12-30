package parser;
import main.*;
import scanner.*;
import static scanner.TokenKind.*;

/* 	'+' or
	'-' or
	'or' */

public class TermOperator extends Operator {
	String term;
	
	TermOperator(int lNum){
		super(lNum);
	}

	@Override public String identify() {
		return "<TermOperator> on line " + lineNum;
	}

	@Override void check(Block curScope, Library lib) {
		//curScope.findDecl(term, this);
	}

	@Override void genCode(CodeFile f) {}

	@Override public void prettyPrint() {
		Main.log.prettyPrint(term);
	}

	static TermOperator parse(Scanner s) {
		enterParser("term opr");

		TermOperator to = new TermOperator(s.curLineNum());

		to.term = s.curToken.kind.toString();

		if(s.curToken.kind == addToken){
			s.skip(addToken);
		} else if(s.curToken.kind == subtractToken){
			s.skip(subtractToken);
		} else if(s.curToken.kind == orToken){
			s.skip(orToken);
		} else {
			s.testError("+, -, or");
		}

		leaveParser("term opr");
		return to;
	}
}