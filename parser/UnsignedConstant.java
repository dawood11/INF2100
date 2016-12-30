package parser;
import main.*;
import scanner.*;
import types.*;
import static scanner.TokenKind.*;

/* <name> or 
<numeric literal> or 
<char literal> */

public abstract class UnsignedConstant extends Factor {
	static UnsignedConstant co;

	UnsignedConstant(int lNum) {
		super(lNum);
	}

	public int value() {
		return co.value();
	}

	@Override public String identify() {
		return "<Unsigned Constant> on line " + lineNum;
	}

	/*@Override void check(Block curScope, Library lib) {
		if(co instanceof NamedConst){
			NamedConst nc = (NamedConst)co;
			nc.check(curScope, lib);
		} else if(co instanceof NumberLiteral){
			NumberLiteral nl = (NumberLiteral)co;
			nl.check(curScope, lib);
		} else if(co instanceof CharLiteral){
			CharLiteral cl = (CharLiteral)co;
			cl.check(curScope, lib);
		}
	}*/

	@Override void genCode(CodeFile f) {}

	@Override public void prettyPrint() {
		if (co != null) {
			co.prettyPrint();
		}
	}

	static UnsignedConstant parse(Scanner s) {
		enterParser("unsigned constant");

		if(s.curToken.kind == nameToken){
			co = NamedConst.parse(s);
		} else if(s.curToken.kind == intValToken){
			co = NumberLiteral.parse(s);
		} else if(s.curToken.kind == charValToken){
			co = CharLiteral.parse(s);
		} else {
			s.testError("name, number or char const");
		}

		leaveParser("unsigned constant");
		return co;
	}
}