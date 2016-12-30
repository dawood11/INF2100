package parser;
import main.*;
import types.*;
import scanner.*;
import static scanner.TokenKind.*;

/* <named const> ::= */

public class NamedConst extends UnsignedConstant {
	String name;

	NamedConst(int lNum) {
		super(lNum);
	}

	public ConstDecl constRef = null;

	public int value() {
		if (constRef != null) {
			return constRef.value();
		}
		return -1;
	}

	@Override public String identify() {
		return "<named const> on line " + lineNum;
	}

	@Override void check(Block curScope, Library lib) {
		constRef = (ConstDecl)curScope.findDecl(name, this);
		//type = curScope.findDecl(name, this).type;
		type = constRef.type;
		//System.out.println("NAMECONST: "+ type);
	}

	@Override void genCode(CodeFile f) {
		f.genInstr("","movl", "$" + name + ",%eax", "  name: "+ name);
	}

	@Override public void prettyPrint() {
		Main.log.prettyPrint(name);
	}

	static NamedConst parse(Scanner s) {
		enterParser("named const");

		NamedConst nc = new NamedConst(s.curLineNum());
		nc.name = s.curToken.id;
		s.skip(nameToken);

		leaveParser("named const");
		return nc;
	}
}
