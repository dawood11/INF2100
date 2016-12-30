package parser;
import main.*;
import scanner.*;
import static scanner.TokenKind.*;
/* <prefix opr> ::= '+'*/
				  /*'-'*/

public class PrefixOperator extends Operator {
	public TokenKind operatorSymbol = null;

	PrefixOperator(int lNum) {
		super(lNum);
	}

	@Override void check(Block curScope, Library lib) {}

	@Override void genCode(CodeFile f) {}

	@Override public String identify() {
		return "<prefix opr> on line " + lineNum;
	}

	@Override public void prettyPrint() {

		if(operatorSymbol != null){
			Main.log.prettyPrint(" " + operatorSymbol.toString() + " ");
		}
	}

	static PrefixOperator parse(Scanner s) {
		enterParser("prefix opr");

		PrefixOperator po = new PrefixOperator(s.curLineNum());
		
		if (s.curToken.kind == addToken) {
			po.operatorSymbol = addToken;
			s.skip(addToken);
		} else if (s.curToken.kind == subtractToken) {
			po.operatorSymbol = subtractToken;
			s.skip(subtractToken);
		} else{
			s.testError("prefix opr");
		}

		leaveParser("prefix opr");
		return po;
	}
}