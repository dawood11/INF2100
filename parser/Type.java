package parser;
import main.*;
import scanner.*;
import static scanner.TokenKind.*;

public abstract class Type extends PascalSyntax{
	static Type t;
	//types.Type type = null;
	
	Type(int lNum){
		super(lNum);
	}

	@Override public String identify() {
		return "<Type> on line " + lineNum;
	}

	@Override void check(Block curScope, Library lib) {
		if (t instanceof TypeName) {
			TypeName tn = (TypeName)t; 
			tn.check(curScope, lib);
		} else if (t instanceof ArrayType) {
			ArrayType at = (ArrayType)t; 
			at.check(curScope, lib);
		}
		// t.check(curScope, lib);
	}
	@Override void genCode(CodeFile f) {}

	@Override public void prettyPrint() {
		if(t != null) {
			t.prettyPrint();
		}
	}

	static Type parse(Scanner s) {
		enterParser("type");

		if(s.curToken.kind == nameToken){
			t = TypeName.parse(s);
		} else if(s.curToken.kind == arrayToken){
			t = ArrayType.parse(s);
		} else {
			s.testError("typeName or ArrayType");
		}

		leaveParser("type");
		return t;
	}
}