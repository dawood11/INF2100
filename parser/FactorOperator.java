package parser;
import main.*;
import scanner.*;
import static scanner.TokenKind.*;


/* <factor opr> ::= '*' */
				/* 'div'*/
				/* 'mod'*/
				/* 'and'*/
public class FactorOperator extends Operator {
	TokenKind tk = null;
	FactorOperator(int lNum) {
		super(lNum);
	}

	@Override public String identify() {
		return "<factor opr> on line " + lineNum;
	}

	@Override void check(Block curScope, Library lib) {
		//curScope.findDecl(tk.toString(), this);
	}
	
	@Override void genCode(CodeFile f) {}

	@Override void prettyPrint() {
		if (tk != null) {
			Main.log.prettyPrint(" " + tk.toString() + " ");
		}
	}

	static FactorOperator parse(Scanner s) {
		enterParser("factor opr");

		FactorOperator fo = new FactorOperator(s.curLineNum());
		if (s.curToken.kind == multiplyToken) {
			fo.tk = multiplyToken;
			s.skip(multiplyToken);
		} else if(s.curToken.kind == divToken){
			fo.tk = divToken;
			s.skip(divToken);
		} else if(s.curToken.kind == modToken){
			fo.tk = modToken;
			s.skip(modToken);
		} else if(s.curToken.kind == andToken){
			fo.tk = andToken;
			s.skip(andToken);
		} else{
			s.testError("factor opr");
		}

		leaveParser("factor opr");
		return fo;
	}
}
