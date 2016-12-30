package parser;
import main.*;
import scanner.*;
import static scanner.TokenKind.*;

public class TypeDecl extends PascalDecl {

	TypeDecl(String id, int lNum) {
		super(id, lNum);
	}

	@Override public String identify() {
		return "<type decl> on line " + lineNum;
		//return "";
	}

	@Override void check(Block curScope, Library lib) {}
	
	@Override void genCode(CodeFile f) {}
	
	@Override void checkWhetherAssignable(PascalSyntax where) {
		where.error("You can not assign to a TypeDecl");
	}

	@Override void checkWhetherFunction(PascalSyntax where) {
        where.error("This is not a function");
    }

    @Override void checkWhetherProcedure(PascalSyntax where) {
        where.error("This is not a procedure");
    }
    
    @Override void checkWhetherValue(PascalSyntax where) {
        where.error("This is not a value");
    }
	
	@Override public void prettyPrint() {}

	static TypeDecl parse(Scanner s) {
		return null;
	}
}