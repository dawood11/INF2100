package parser;
import main.*;
import scanner.*;
import types.*;
import static scanner.TokenKind.*;

public class ArrayType extends Type{
	
	Constant c1;
	Constant c2;
	Type t;
	
	ArrayType(int n){
		super(n);
	}

	@Override public String identify() {
		return "<arrayType> on line " + lineNum;
	}

	@Override void check(Block curScope, Library lib) {
		c1.check(curScope, lib);
		c2.check(curScope, lib);
		t.check(curScope, lib);
		c1.uc.type.checkType(c2.uc.type, "array limits", this, "");
		if (c1.uc.type == c2.uc.type) {
			//System.out.println("ArrayType: " + t.type);
			types.ArrayType arrayType = new types.ArrayType(t.type, c1.uc.type, c1.value(), c2.value());
			//arrayType.checkType(arrayType, "ArrayType", this, "ArrayType is wrong");
		}
		type = t.type;
	}

	@Override void genCode(CodeFile f) {}

	@Override public void prettyPrint() {
		Main.log.prettyPrint("array");
		Main.log.prettyPrint(" [");
		c1.prettyPrint();
		Main.log.prettyPrint("..");
		c2.prettyPrint();
		Main.log.prettyPrint("] ");
		Main.log.prettyPrint(" of ");
		t.prettyPrint();
	}

	static ArrayType parse(Scanner s) {
		enterParser("array type");

		ArrayType at = new ArrayType(s.curLineNum());

		s.skip(arrayToken);
		s.skip(leftBracketToken);
		at.c1 = Constant.parse(s);
		s.skip(rangeToken);
		at.c2 = Constant.parse(s);
		s.skip(rightBracketToken);
		s.skip(ofToken);
		at.t = Type.parse(s);

		leaveParser("array type");
		return at;
	}
}