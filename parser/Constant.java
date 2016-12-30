package parser;
import main.*;
import scanner.*;
import types.*;
import static scanner.TokenKind.*;

/* <constant> ::= <prefix opr> <unsigned constant> ';'*/
				 /* <> */

public class Constant extends PascalSyntax {
	UnsignedConstant uc = null;
	PrefixOperator po = null;

	Constant(int lNum) {
		super(lNum);
	}
	

	public int value() {
		if (po != null) {
			if (po.operatorSymbol.toString().equals("-")) {
				return uc.value() * -1;
			}
		}
		return uc.value();
	}

	@Override public String identify() {
		return "<constant> on line " + lineNum;
	}
	@Override void check(Block curScope, Library lib) {
		uc.check(curScope, lib);
		type = uc.type;
		if (po != null) {
			po.check(curScope, lib);
			String oprName = po.operatorSymbol.toString();
			type.checkType(lib.it.type, "Prefix " + oprName, this, "Prefix + or - may only be applied to Integers.");
		}
	}

	@Override void genCode(CodeFile f) {
		uc.genCode(f);
		if (po != null) {
			if (po.operatorSymbol.toString().equals("-")) {
				f.genInstr("", "negl", "%eax", "");				
			}
		}
	}

	@Override void prettyPrint() {
		if (po != null) {
			po.prettyPrint();
		}
		uc.prettyPrint();
	}

	static Constant parse(Scanner s) {
		enterParser("constant");

		Constant c = new Constant(s.curLineNum());
		if (s.curToken.kind == addToken || s.curToken.kind == subtractToken) {
			c.po = PrefixOperator.parse(s);
		}

		c.uc = UnsignedConstant.parse(s);

		leaveParser("constant");
		return c;
	}
}
