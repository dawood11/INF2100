package parser;
import main.*;
import scanner.*;
import static scanner.TokenKind.*;

/* <assign-statm> ::= <variable> ’:=’ <expression> */

public class AssignStatm extends Statement {
	Expression expr;
	Variable v;

	AssignStatm(int lNum) {
		super(lNum);
	}

	@Override public String identify() {
		return "<assign-statm> on line " + lineNum;
	}

	@Override void check(Block curScope, Library lib) {
		v.check(curScope, lib);
		v.var.checkWhetherAssignable(this);
		expr.check(curScope, lib);
		v.type.checkType(expr.type, ":=", this, "Different types in AssignStatm");
	}

	@Override void genCode(CodeFile f) {
		expr.genCode(f);
		if (v.var instanceof FuncDecl) {
			f.genInstr("", "movl", (-4 * (v.var.declLevel)) + "(%ebp),%edx", "");	
			f.genInstr("", "movl", "%eax,-32(%edx)", "");
		} else if (v.var instanceof VarDecl) {
			f.genInstr("", "movl", (-4 * v.var.declLevel) + "(%ebp),%edx", "");	
			f.genInstr("", "movl", "%eax," + (-v.var.declOffset - 32)  + "(%edx)", "" + v.name + " :=");
		}
	}

	@Override public void prettyPrint() {
		v.prettyPrint();
		Main.log.prettyPrint(" := ");		
		expr.prettyPrint();
	}

	static AssignStatm parse(Scanner s) {
		enterParser("assign statm");

		AssignStatm as = new AssignStatm(s.curLineNum());
		as.v = Variable.parse(s);
		s.skip(assignToken);
		as.expr = Expression.parse(s);

		leaveParser("assign statm");
		return as;
	}
}
