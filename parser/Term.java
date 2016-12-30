package parser;
import main.*;
import scanner.*;
import static scanner.TokenKind.*;
import java.util.ArrayList;

/* <factor> 				*/
/*			<factor opr> 	*/

public class Term extends PascalSyntax {
	ArrayList<Factor> f = new ArrayList<Factor>();
	ArrayList<FactorOperator> fo = new ArrayList<FactorOperator>();
	Term(int lNum){
		super(lNum);
	}

	@Override public String identify() {
		return "<Term> on line " + lineNum;
	}

	@Override void check(Block curScope, Library lib) {
        f.get(0).check(curScope, lib);
        type = f.get(0).type;
        //System.out.println("TERM: " + type + ", " + f.get(0));

		if(!fo.isEmpty()){
			for (int i = 0; i < fo.size(); i++) {
				fo.get(i).check(curScope, lib);
				f.get(i+1).check(curScope, lib);
				//System.out.println("f.get.type: "+f.get(i+1).type);
				//System.out.println("f.get: "+f.get(i+1));
				//System.out.println("f.get0.type: "+type);
				f.get(i+1).type.checkType(type, "Term-test", this, "Term is not same type.");

				if (fo.get(i).tk.toString().equals("and")){
					f.get(i+1).type.checkType(lib.bt.type, "Term-and-test", this, "Term is not boolean type.");
				} else {
					f.get(i+1).type.checkType(lib.it.type, "Term-integer-test", this, "Term is not integer type.");
				}
			}
		}
	}

	@Override void genCode(CodeFile ff) {
		f.get(0).genCode(ff);
		if(!fo.isEmpty()){
			for (int i = 0; i < fo.size(); i++) {
				ff.genInstr("", "pushl", "%eax", "");
				f.get(i+1).genCode(ff);
				ff.genInstr("", "movl", "%eax,%ecx", "");
				ff.genInstr("", "popl", "%eax", "");
				if (fo.get(i).tk.toString().equals("*")) {
					ff.genInstr("", "imull", "%ecx,%eax", "imull");
				} else if (fo.get(i).tk.toString().equals("div")) {
					ff.genInstr("", "cdq", "", "");
					ff.genInstr("", "idivl", "%ecx", "div");
				} else if (fo.get(i).tk.toString().equals("mod")) {
					ff.genInstr("", "cdq", "", "");
					ff.genInstr("", "idivl", "%ecx", "");
					ff.genInstr("", "movl", "%edx,%eax", "mod");
				} else if (fo.get(i).tk.toString().equals("and")) {
					ff.genInstr("", "andl", "%ecx,%eax", "andl");
				}
			}
		}
	}

	@Override public void prettyPrint() {
		f.get(0).prettyPrint();
		if(!fo.isEmpty()){
			for(int i = 0; i < fo.size(); i++){
				fo.get(i).prettyPrint();
				f.get(i+1).prettyPrint();
			}
		}
	} 

	static Term parse(Scanner s) {
		enterParser("term");
		
		Term t = new Term(s.curLineNum());

		t.f.add(Factor.parse(s));

		while (s.curToken.kind.isFactorOpr()) {
			t.fo.add(FactorOperator.parse(s));
			t.f.add(Factor.parse(s));
		}

		leaveParser("term");
		return t;
	}

}