package parser;
import main.*;
import scanner.*;
import static scanner.TokenKind.*;

/* <const decl> ::= <name> '=' <constant> ';'*/

public class ConstDecl extends PascalDecl {
	public Constant c = null;
	String name;

	ConstDecl(String id, int lNum) {
		super(id, lNum);
	}

	public int value() {
		return c.value();
	}

	@Override public String identify() {
		return "<const decl> on line " + lineNum;
	}
	@Override void check(Block curScope, Library lib) {
		//curScope.findDecl(name, this);
		c.check(curScope, lib);
		type = c.type;
		//System.out.println("inni ConstDecl: "+type + ", " + c);
	}

	@Override void genCode(CodeFile f) {
		if(c != null){
			c.genCode(f);
		}
	}

	@Override void checkWhetherAssignable(PascalSyntax where) {
		where.error("You cannot assign to a constant.");
	}

	@Override void checkWhetherFunction(PascalSyntax where) {
        where.error("This is not a function");
    }

    @Override void checkWhetherProcedure(PascalSyntax where) {
        where.error("This is not a procedure");
    }

    @Override void checkWhetherValue(PascalSyntax where) {}

	@Override void prettyPrint() {
		Main.log.prettyPrint(name + " ");
		Main.log.prettyPrint(" = ");
		c.prettyPrint();
		Main.log.prettyPrint(";");
	}

	static ConstDecl parse(Scanner s) {
		enterParser("const decl");

		ConstDecl cd = new ConstDecl(s.curToken.id, s.curLineNum());
		cd.name = s.curToken.id;
		s.skip(nameToken);
		s.skip(equalToken);
		cd.c = Constant.parse(s);
		s.skip(semicolonToken);

		leaveParser("const decl");
		return cd;
	}
}
