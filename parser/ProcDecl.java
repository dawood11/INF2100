package parser;
import main.*;
import scanner.*;
import static scanner.TokenKind.*;

public class ProcDecl extends PascalDecl {
	ParamDeclList pdl;
	Block b;
	String name;
	String id;

	ProcDecl(String id, int lNum){
		super(id, lNum);
		this.id = id;
	}

	@Override public String identify() {
		return "<ProcDecl> on line " + lineNum;
	}

	@Override void check(Block curScope, Library lib) {
		b.outerScope = curScope;
		this.declLevel = curScope.level + 1;
		b.level = this.declLevel;
		if (pdl != null) {
			pdl.check(b, lib);
		}
		b.check(curScope, lib);
	}
	@Override void genCode(CodeFile f) {
		//int numberOfBytes = 32 + b.nextOffset;
		int numberOfBytes = 0;
		progProcFuncName = f.getLabel(name);

		if(b.vdp != null){
			for (VarDecl var : b.vdp.vd) {
				numberOfBytes += var.type.size();
				var.declOffset = numberOfBytes;
			}
		}

		int pdOffset = 8;
		if (pdl != null) {
			for (ParamDecl pd : pdl.pdList) {
				pd.declOffset = pdOffset;
				pdOffset += pd.type.size();
			}
		}

		if (b.fd != null) {
			b.fd.genCode(f);
		}
		if (b.pd != null) {
			b.pd.genCode(f);
		}
		f.genInstr("proc$" + progProcFuncName, "enter", "$" + (numberOfBytes+32) + "," + "$" + this.declLevel, "Start of " + name);
		if (pdl != null) {
			pdl.genCode(f);
		}
		b.sl.genCode(f);
		f.genInstr("", "leave", "", "");
		f.genInstr("", "ret", "", "");
	}

	@Override void checkWhetherAssignable(PascalSyntax where) {
		where.error("You cannot assign to a procedure.");
	}

	@Override void checkWhetherFunction(PascalSyntax where) {
		where.error("This is not a function");
	}

	@Override void checkWhetherProcedure(PascalSyntax where) {}

	@Override void checkWhetherValue(PascalSyntax where) {
		where.error("This is not a value");
	}

	@Override public void prettyPrint() {
		Main.log.prettyPrint("procedure ");
		Main.log.prettyPrint(name);
		if(pdl != null){
			pdl.prettyPrint();
		}
		Main.log.prettyPrintLn(";");
		Main.log.prettyIndent();
		b.prettyPrint();
		Main.log.prettyPrintLn(";");
		Main.log.prettyOutdent();
	}

	static ProcDecl parse(Scanner s) {
		enterParser("proc decl");

		s.skip(procedureToken);

		ProcDecl pd = new ProcDecl(s.curToken.id, s.curLineNum());

		pd.name = s.curToken.id;
		s.skip(nameToken);

		if(s.curToken.kind == leftParToken){
			pd.pdl = ParamDeclList.parse(s);
		}

		s.skip(semicolonToken);

		pd.b = Block.parse(s);

		s.skip(semicolonToken);

		leaveParser("proc decl");
		return pd;
	}
}