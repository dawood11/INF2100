package parser;
import main.*;
import scanner.*;
import static scanner.TokenKind.*;

/* <negation> ::= 'not' <factor>*/

public class Negation extends Factor {
	Factor f;

	Negation(int lNum) {
		super(lNum);
	}

	@Override public String identify() {
		return "<negation> on line " + lineNum;
	}

	@Override void check(Block curScope, Library lib) {
		f.check(curScope, lib);
		type = lib.bt.type;
		//System.out.println("NEGATION: "+ type);
		f.type.checkType(type, "Negation-test", this, "Type is not " + type.identify());
	}

	@Override void genCode(CodeFile ff) {
		f.genCode(ff);
		ff.genInstr("", "xorl", "$0x1,%eax", "not");
	}

	@Override public void prettyPrint() {
		Main.log.prettyPrint("not ");
		f.prettyPrint();
	}

	static Negation parse(Scanner s) {
		enterParser("negation");

		Negation n = new Negation(s.curLineNum());

		s.skip(notToken);
		n.f = Factor.parse(s);

		leaveParser("negation");
		return n;
	}
}