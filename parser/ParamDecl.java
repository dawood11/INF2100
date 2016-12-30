package parser;
import main.*;
import scanner.*;
import static scanner.TokenKind.*;

/* <param decl> ::=	<name> ':' <type name>*/

public class ParamDecl extends PascalDecl {
	String name;
	TypeName tn;

	ParamDecl(String id, int lNum) {
		super(id, lNum);
	}

	@Override public String identify() {
		return "<param decl> on line " + lineNum;
	}

	@Override void check(Block curScope, Library lib) {
		this.declLevel = curScope.level;
		curScope.addDecl(name, this);
		tn.check(curScope, lib);
		type = tn.type;
	}

	@Override void genCode(CodeFile f) {
		tn.genCode(f);
	}

	@Override void checkWhetherAssignable(PascalSyntax where) {}

	@Override void checkWhetherFunction(PascalSyntax where) {
        where.error("This is not a function");
    }

    @Override void checkWhetherProcedure(PascalSyntax where) {
        where.error("This is not a procedure");
    }

    @Override void checkWhetherValue(PascalSyntax where) {
        //where.error("This is not a value");
    }

	@Override public void prettyPrint() {
		Main.log.prettyPrint(name + " : ");
		tn.prettyPrint();
	}

	public static ParamDecl parse(Scanner s) {
		enterParser("param decl");

		ParamDecl pd = new ParamDecl(s.curToken.id, s.curLineNum());
		pd.name = s.curToken.id;
		s.skip(nameToken);
		s.skip(colonToken);
		pd.tn = TypeName.parse(s);

		leaveParser("param decl");
		return pd;
	}
}