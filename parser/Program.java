package parser;
import main.*;
import scanner.*;
import static scanner.TokenKind.*;

public class Program extends PascalDecl {
	String name;
	Block b;

	Program(String id, int lNum){
		super(id, lNum);
	}

	@Override public String identify() {
		return "<Program> on line" + lineNum;
	}

	@Override public void check(Block curScope, Library lib) {
		b.check(curScope, lib);
	}

	@Override public void genCode(CodeFile f) {
		//f.genInstr("", "", "", "");
		//int numberOfBytes = 32 + b.nextOffset;
		int numberOfBytes = 32;
		progProcFuncName = f.getLabel(name);

		f.genInstr("", ".globl main", "", "");
		f.genInstr("main", "call", "prog$" + progProcFuncName, "Start program");
		f.genInstr("", "movl", "$0,%eax", "Set status 0 and");
		f.genInstr("", "ret", "", "terminate the program");
		b.genCode(f);
		if(b.vdp != null){
			for (VarDecl var : b.vdp.vd) {
				numberOfBytes += var.type.size();
			}
		}
		f.genInstr("prog$" + progProcFuncName, "enter", "$" + numberOfBytes + "," + "$1", "Start of " + progProcFuncName);
		b.sl.genCode(f);
		f.genInstr("", "leave", "", "");
		f.genInstr("", "ret", "", "");
	}

	@Override void checkWhetherAssignable(PascalSyntax where) {
		where.error("You cannot assign to a program.");
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

	@Override public void prettyPrint() {
		Main.log.prettyPrint("program ");
		Main.log.prettyPrint(name);
		Main.log.prettyPrintLn(";");
		b.prettyPrint();
		Main.log.prettyPrint(".");
	}

	public static Program parse(Scanner s) {
		enterParser("program");

		//s.readNextToken();
		
		s.skip(programToken);

		Program p = new Program(s.curToken.id, s.curLineNum());

		p.name = s.curToken.id;
		s.skip(nameToken);

		s.skip(semicolonToken);

		p.b = Block.parse(s);

		s.skip(dotToken);

		leaveParser("program");
		return p;
	}
}