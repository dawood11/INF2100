package parser;
import main.*;
import scanner.*;
import static scanner.TokenKind.*;
import java.util.ArrayList;

public class SimpleExpr extends PascalSyntax {
	PrefixOperator po;
	ArrayList<Term> t = new ArrayList<Term>();
	ArrayList<TermOperator> to = new ArrayList<TermOperator>();

	SimpleExpr(int lNum){
		super(lNum);
	}

	@Override public String identify() {
		return "<SimpleExpr> on line " + lineNum;
	}

	@Override void check(Block curScope, Library lib) {
		if (po != null) {
			po.check(curScope, lib);
		}
		t.get(0).check(curScope, lib);
		type = t.get(0).type;
        //System.out.println("SimpleExpr: " + type);

		if(!to.isEmpty()){
			for (int i = 0; i < to.size(); i++) {
				to.get(i).check(curScope, lib);
				t.get(i+1).check(curScope, lib);

				t.get(i+1).type.checkType(type, "SimpleExpr-integer-test", this, "SimpleExpr is not same type.");

				if (to.get(i).term.equals("or")){
					t.get(i+1).type.checkType(lib.bt.type, "SimpleExpr-or-test", this, "Term is not boolean type.");
				} else {
					t.get(i+1).type.checkType(lib.it.type, "SimpleExpr-integer-test", this, "Term is not integer type.");
				}

			}
		}
		if (po != null) {
			type.checkType(lib.it.type, "SimpleExpr-preopr-test", this, "Prefix Operator cannot be set before boolean type.");
		}
	}

	@Override void genCode(CodeFile f) {
		t.get(0).genCode(f);
		if(!to.isEmpty()){
			for (int i = 0; i < to.size(); i++) {
				f.genInstr("", "pushl", "%eax", "");
				t.get(i+1).genCode(f);
				f.genInstr("", "movl", "%eax,%ecx", "");
				f.genInstr("", "popl", "%eax", "");
				if (to.get(i).term.equals("-")) {
					f.genInstr("", "subl", "%ecx,%eax", "-");
				} else if (to.get(i).term.equals("+")) {
					f.genInstr("", "addl", "%ecx,%eax", "+");
				} else if (to.get(i).term.equals("or")) {
					f.genInstr("", "orl", "%ecx,%eax", "orl");
				}
			}
		}
		if (po != null) {
			if (po.operatorSymbol.toString().equals("-")) {
				f.genInstr("", "negl", "%eax", "");				
			}
		}
	}

	@Override public void prettyPrint() {
		if(po != null){
			po.prettyPrint();
		}
		t.get(0).prettyPrint();
		if(!to.isEmpty()){
			for (int i = 0; i < to.size(); i++) {
				to.get(i).prettyPrint();
				t.get(i+1).prettyPrint();
			}
		}
	}

	static SimpleExpr parse(Scanner s) {
		enterParser("simple expr");

		SimpleExpr se = new SimpleExpr(s.curLineNum());

		if(s.curToken.kind.isPrefixOpr()){
			se.po = PrefixOperator.parse(s);
		}

		se.t.add(Term.parse(s));
		while (s.curToken.kind.isTermOpr()){
			se.to.add(TermOperator.parse(s));
			se.t.add(Term.parse(s));
		}

		leaveParser("simple expr");
		return se;
	}
}