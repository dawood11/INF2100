package parser;
import main.*;
import scanner.*;
import static scanner.TokenKind.*;

/* <expression> ::= <simple expr>  <real opr> <simple expr>*/
								    /*<                   >*/
class Expression extends PascalSyntax {
	SimpleExpr se1 = null;
	RelOperator ro = null;
	SimpleExpr se2 = null;
	Expression(int lNum) {
		super(lNum);
	}

	@Override public String identify() {
		return "<expression> on line " + lineNum;
	}

	@Override void check(Block curScope, Library lib) {
		se1.check(curScope, lib);
		type = se1.type;
		//System.out.println(type);
		//System.out.println("EXPR" + lineNum);
		if (ro != null) {
			ro.check(curScope, lib);
			se2.check(curScope, lib);
            String oprName = ro.rotoken;
            //System.out.println("EXPR checkType");
            type.checkType(se2.type, oprName + " operands", this, "Operands to " + oprName + " are of different type!");
			type = lib.bt.type;
			//System.out.println("Expression" + type);
		}
	}

	@Override void genCode(CodeFile f) {
		se1.genCode(f);
		if (ro != null) {
			f.genInstr("", "pushl", "%eax", "");
			se2.genCode(f);
			f.genInstr("", "popl", "%ecx", "");
			f.genInstr("", "cmpl", "%eax,%ecx", "");
			f.genInstr("", "movl", "$0,%eax", "");
            String oprName = ro.rotoken;
			if (oprName.equals("=")) {
				f.genInstr("", "sete", "%al", "=");
			} else if (oprName.equals("<>")) {
				f.genInstr("", "setne", "%al", "<>");
			} else if (oprName.equals("<")) {
				f.genInstr("", "setl", "%al", "<");
			} else if (oprName.equals("<=")) {
				f.genInstr("", "setle", "%al", "<=");
			} else if (oprName.equals(">")) {
				f.genInstr("", "setg", "%al", ">");
			} else if (oprName.equals(">=")) {
				f.genInstr("", "setge", "%al", ">=");
			}
		}
	}

	@Override void prettyPrint() {
		se1.prettyPrint();
		if (ro != null) {
			ro.prettyPrint();
			se2.prettyPrint();
		}
	}

	static Expression parse(Scanner s) {
		enterParser("expression");

		Expression ex = new Expression(s.curLineNum());
		ex.se1 = SimpleExpr.parse(s);
		if (s.curToken.kind.isRelOpr()) {
			ex.ro = RelOperator.parse(s);
			ex.se2 = SimpleExpr.parse(s);
		}

		leaveParser("expression");
		return ex;
	}
}
