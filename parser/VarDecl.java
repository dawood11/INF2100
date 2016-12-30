package parser;
import main.*;
import scanner.*;
import static scanner.TokenKind.*;

/* <VarDecl> ::= <name> ':' <type> ';' */

public class VarDecl extends PascalDecl {
	Type t;
	String name;

	VarDecl(String id, int lNum) {
		super(id, lNum);
	}

	@Override public String identify() {
		return "<VarDecl> on line " + lineNum;
	}

	@Override void check(Block curScope, Library lib) {
		this.declLevel = curScope.level;
		curScope.nextOffset += 4;
		this.declOffset = curScope.nextOffset;
		//curScope.findDecl(name, this);
		t.check(curScope, lib);
		type = t.type;
		//System.out.println("inni VarDecl: "+type + ", " + t);
	}

	@Override void genCode(CodeFile f) {
		f.genInstr("", "movl", "" + (-4*this.declLevel) + "(%ebp),%edx", "");
		f.genInstr("", "movl", "" + (-this.declOffset - 32) + "(%edx),%eax", "" + name);
	}

	@Override void checkWhetherAssignable(PascalSyntax where) {}

	@Override void checkWhetherFunction(PascalSyntax where) {
        where.error("This is not a function");
    }

    @Override void checkWhetherProcedure(PascalSyntax where) {
        where.error("This is not a procedure");
    }

    @Override void checkWhetherValue(PascalSyntax where) {}


	@Override void prettyPrint() {
		Main.log.prettyPrint(name);
		Main.log.prettyPrint(" : ");
		if (t != null) {
			t.prettyPrint();
		}
		Main.log.prettyPrintLn(";");
	}

	static VarDecl parse(Scanner s) {
		enterParser("var decl");
		
		VarDecl vd = new VarDecl(s.curToken.id, s.curLineNum());
		vd.name = s.curToken.id;
		s.skip(nameToken);

		s.skip(colonToken);

		vd.t = Type.parse(s);

		s.skip(semicolonToken);

		leaveParser("var decl");
		return vd;
	}
}