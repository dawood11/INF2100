package parser;
import main.*;
import scanner.*;
import static scanner.TokenKind.*;

public class TypeName extends Type {
	String name;

	TypeName(int lNum){
		super(lNum);
	}

	@Override public String identify() {
		return "<TypeName> on line " + lineNum;
	}

	@Override void check(Block curScope, Library lib) {
		type = curScope.findDecl(name, this).type;
		//System.out.println("name " + type);
	}
	
	@Override void genCode(CodeFile f) {}
	
	@Override public void prettyPrint() {
		Main.log.prettyPrint(name);
	}

	static TypeName parse(Scanner s) {
		enterParser("type name");
		
		TypeName tn = new TypeName(s.curLineNum());
		tn.name = s.curToken.id;
		s.skip(nameToken);

		leaveParser("type name");
		return tn;
	}
}