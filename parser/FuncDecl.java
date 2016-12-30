
package parser;
import main.*;
import scanner.*;
import static scanner.TokenKind.*;

/* <func decl> ::= 'function' <name> <param decl list> ';' <type name> ';' <block> ';'*/
									/*<				 >*/
public class FuncDecl extends ProcDecl {
	String name;
	ParamDeclList pdl = null;
	TypeName tn = null;
	Block bl = null;
	FuncDecl(String id, int lNum) {
		super(id, lNum);
	}

	@Override public String identify() {
		return "<func decl> on line " + lineNum;
	}

	@Override void check(Block curScope, Library lib) {
		bl.outerScope = curScope;
		this.declLevel = curScope.level + 1;
		bl.level = this.declLevel;
		if (pdl != null) {
			pdl.check(bl, lib);
		}
		tn.check(curScope, lib);
		type = tn.type;
		bl.check(curScope, lib);
	}

	@Override void genCode(CodeFile f) {
		//f.genInstr("", "", "", "");
		//int numberOfBytes = 32 + bl.nextOffset;
		int numberOfBytes = 0;
		progProcFuncName = f.getLabel(name);

		if(bl.vdp != null){
			for (VarDecl var : bl.vdp.vd) {
				numberOfBytes += var.type.size();
				var.declOffset = numberOfBytes;
			}
		}

		int pdOffset = 8;
		for (ParamDecl pd : pdl.pdList) {
			pd.declOffset = pdOffset;
			pdOffset += pd.type.size();
		}
		if (bl.fd != null) {
			bl.fd.genCode(f);
		}
		if (bl.pd != null) {
			bl.pd.genCode(f);
		}

		f.genInstr("func$" + progProcFuncName, "enter", "$" + (numberOfBytes+32) + "," + "$" + this.declLevel, "Start of " + name);
		if(pdl != null){
			pdl.genCode(f);
		}
		bl.sl.genCode(f);
		f.genInstr("", "movl", "-32(%ebp),%eax", "");
		f.genInstr("", "leave", "", "");
		f.genInstr("", "ret", "", "");
	}

	@Override void checkWhetherAssignable(PascalSyntax where) {}

	@Override void checkWhetherFunction(PascalSyntax where){}

    @Override void checkWhetherProcedure(PascalSyntax where) {
        where.error(name + " is a function, not a procedure.");
    }
    @Override void checkWhetherValue(PascalSyntax where) {}

	@Override public void prettyPrint() {
		Main.log.prettyPrint("function " + name);		
		if(pdl != null) 
			pdl.prettyPrint();
		Main.log.prettyPrint(" : ");
		tn.prettyPrint();
		Main.log.prettyPrintLn(";");
		bl.prettyPrint();
		Main.log.prettyPrintLn(";");
	}

	static FuncDecl parse(Scanner s) {
		enterParser("func decl");

		FuncDecl fd = new FuncDecl(s.curToken.id, s.curLineNum());
		s.skip(functionToken);
		fd.name = s.curToken.id;
		s.skip(nameToken);
		if (s.curToken.kind == leftParToken) {
			fd.pdl = ParamDeclList.parse(s);
		}
		s.skip(colonToken);
		fd.tn = TypeName.parse(s);
		s.skip(semicolonToken);
		fd.bl = Block.parse(s);
		s.skip(semicolonToken);

		leaveParser("func decl");
		return fd;
	}
}
