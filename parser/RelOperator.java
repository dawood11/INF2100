package parser;
import main.*;
import scanner.*;
import static scanner.TokenKind.*;

public class RelOperator extends Operator {
	String rotoken;

	RelOperator(int lNum) {
		super(lNum);
	}

	@Override public String identify() {
		return "<RelOperator> on line " + lineNum;
	}


	@Override void check(Block curScope, Library lib) {
		//curScope.findDecl(rotoken, this);
	}

	@Override void genCode(CodeFile f) {}

	@Override public void prettyPrint() {
		Main.log.prettyPrint(" " + rotoken + " ");
	}

	static RelOperator parse(Scanner s) {
		enterParser("rel opr");
		RelOperator ro = new RelOperator(s.curLineNum());
		
		ro.rotoken = s.curToken.kind.toString();
		if(s.curToken.kind == equalToken){
			s.skip(equalToken);
		} else if(s.curToken.kind == notEqualToken){
			s.skip(notEqualToken);
		} else if(s.curToken.kind == lessToken){
			s.skip(lessToken);
		} else if(s.curToken.kind == lessEqualToken){
			s.skip(lessEqualToken);
		} else if(s.curToken.kind == greaterToken){
			s.skip(greaterToken);
		} else if(s.curToken.kind == greaterEqualToken){
			s.skip(greaterEqualToken);
		} else {
			s.testError("rel opr");
		}

		leaveParser("rel opr");
		return ro;
	}
}