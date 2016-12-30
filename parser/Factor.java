package parser;
import main.*;
import scanner.*;
import types.*;
import static scanner.TokenKind.*;
import java.util.ArrayList;

public abstract class Factor extends PascalSyntax {
	static Factor f;
	//types.Type type = null;

	Factor(int lNum){
		super(lNum);
	}

	@Override public String identify() {
		return "<factor> on line " + lineNum;
	}

	@Override void check(Block curScope, Library lib) {
		if(f instanceof UnsignedConstant){
			UnsignedConstant uc = (UnsignedConstant)f;
			uc.check(curScope, lib);
		} else if(f instanceof Variable){
			Variable v = (Variable)f;
			v.check(curScope, lib);
		} else if(f instanceof FuncCall){
			FuncCall fc = (FuncCall)f;
			fc.check(curScope, lib);
		} else if(f instanceof InnerExpr){
			InnerExpr ie = (InnerExpr)f;
			ie.check(curScope, lib);
		} else if(f instanceof Negation){
			Negation n = (Negation)f;
			n.check(curScope, lib);
		}
	}

	@Override void genCode(CodeFile f) {}

	@Override public void prettyPrint() {
		f.prettyPrint();
	}

	static Factor parse(Scanner s) {
		enterParser("factor");
		
		/*unsigned constant: name or numeric literalt or char literal
		variable: name, leftBracket or nothing
		func call: name, leftPar or nothing
		innerexpr: leftPar
		negation: not*/

		if(s.curToken.kind == nameToken){
			if(s.nextToken.kind == leftBracketToken){
				f = Variable.parse(s);
			} else if(s.nextToken.kind == leftParToken){
				f = FuncCall.parse(s);
			} else {
				f = Variable.parse(s);
			}
		} else if(s.curToken.kind == intValToken){
			f = UnsignedConstant.parse(s);
		} else if(s.curToken.kind == charValToken){
			f = UnsignedConstant.parse(s);
		} else if(s.curToken.kind == notToken){
			f = Negation.parse(s);
		} else if(s.curToken.kind == leftParToken){
			f = InnerExpr.parse(s);
		} else {
			s.testError("factor");
		}
		
		leaveParser("factor");
		return f;
	}
}