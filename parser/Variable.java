package parser;
import main.*;
import scanner.*;
import types.*;
import static scanner.TokenKind.*;

/* <variable> ::= <name(String)> '[' <expression> ']' */
/* <variable> ::= <name(String)>  */

public class Variable extends Factor {
	Expression expr;
	String name;

	Variable(int lNum) {
		super(lNum);
	}

	PascalDecl var = null;

	@Override public String identify() {
		return "<variable> on line " + lineNum;
	}

	@Override void check(Block curScope, Library lib) {
		var = curScope.findDecl(name, this);
		var.checkWhetherValue(this);
		type = var.type;
		//System.out.println("inni Variable: "+type + ", " + var + " lineNum " + lineNum + " name " + name);
		if (expr != null) {
			expr.check(curScope, lib);
			VarDecl vararray = (VarDecl)var;
			ArrayType array = (ArrayType)vararray.t;
			//System.out.println("inni Variable: "+ array.c1.uc.type + " expr.type " + expr.type + " , " + var + " lineNum " + lineNum + " name " + name);
			expr.type.checkType(array.c1.uc.type, "array index", this, "Array index not right");
			//type = expr.type;		
		}
	}

	@Override void genCode(CodeFile f) {
		if(var instanceof FuncDecl){
			f.genInstr("", "call", "func$" + var.progProcFuncName, "call " + var.name);
		} else if (var instanceof ParamDecl) {
			f.genInstr("", "movl", "" + (-4*var.declLevel) + "(%ebp),%edx", "");
			f.genInstr("", "movl", "" + (var.declOffset) + "(%edx),%eax", "" + name);
		} else if (this.type instanceof types.BoolType) {
			if(name.equals("true")){
				f.genInstr("", "movl", "$1,%eax", "Variable true");
			} else if (name.equals("false")){
				f.genInstr("", "movl", "$0,%eax", "Variable false");
			} else {
				var.genCode(f);
			}
		}else {
			if (!name.equals("eol")) {
				var.genCode(f);
			}
		}
	}

	@Override public void prettyPrint() {
		Main.log.prettyPrint(name);
		if(expr != null){
			Main.log.prettyPrint(" [ ");
			expr.prettyPrint(); 
			Main.log.prettyPrint(" ]");
		}
	}

	static Variable parse(Scanner s) {
		enterParser("variable");

		Variable v = new Variable(s.curLineNum());
		
		v.name = s.curToken.id;
		
		s.skip(nameToken);
		
		if(s.curToken.kind == leftBracketToken){
			s.skip(leftBracketToken);
			v.expr = Expression.parse(s);
			s.skip(rightBracketToken);
		}

		leaveParser("variable");
		return v;
	}
}