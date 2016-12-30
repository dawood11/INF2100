package parser;
import main.*;
import scanner.*;
import static scanner.TokenKind.*;
import java.util.ArrayList;

							/*|----','------^*/
/* <func call> ::= <name> '('  <expression>  ')'*/
						/*<					   >*/
public class FuncCall extends Factor {
	Expression e = null;
	String name;
	ArrayList<Expression> eList;
	FuncCall(int lNum) {
		super(lNum);
		eList = new ArrayList<Expression>();
	}

	FuncDecl func = null;

	@Override public String identify() {
		return "<func call> on line " + lineNum;
	}

	@Override void check(Block curScope, Library lib) {
		func = (FuncDecl)curScope.findDecl(name, this);
		type = func.type;
		//System.out.println("FuncCall: "+ type);
		func.checkWhetherFunction(this);
		int paramCounter = 1;
		if (!eList.isEmpty()) {
			for (Expression eTmpCheck : eList) {
				eTmpCheck.check(curScope, lib);
				types.Type tmpType = func.pdl.pdList.get(paramCounter - 1).type;
				eTmpCheck.type.checkType(tmpType, "Param #" + paramCounter, this, "Param #" + paramCounter + " is not " + tmpType.identify());
				paramCounter++;
			}
		}
		if (paramCounter-1 < func.pdl.pdList.size()) {
			error("Too few parameters in call on " + name);
		} else if (paramCounter-1 > func.pdl.pdList.size()) {
			error("Too many parameters in call on " + name);
		}
	}

	@Override void genCode(CodeFile f) {
		if(!eList.isEmpty()){
			for (int i = eList.size()-1; i >= 0; i--) {				
				eList.get(i).genCode(f);
				f.genInstr("", "pushl", "%eax", "Push next param.");
			}
		}
		f.genInstr("", "call", "func$" + func.progProcFuncName, "");
		f.genInstr("", "addl", "$" + 4*eList.size() + ",%esp", "Pop param.");
	}

	@Override public void prettyPrint() {
		Main.log.prettyPrint(name);
		if(e != null) {
			Main.log.prettyPrint("(");
			int i = 0;
			for(Expression eTmp: eList) {
				eTmp.prettyPrint();
				if (i < eList.size()-1) {
					Main.log.prettyPrint(", ");
					i++;
				}
			}
			Main.log.prettyPrint(")");
		}
	}

	static FuncCall parse(Scanner s) {
		enterParser("func call");

		FuncCall fc = new FuncCall(s.curLineNum());
		fc.name = s.curToken.id;
		s.skip(nameToken);
		if (s.curToken.kind == leftParToken) {
			s.skip(leftParToken);
			while(true){
				fc.e = Expression.parse(s);
				fc.eList.add(fc.e);
				if (s.curToken.kind == rightParToken) {
					s.skip(rightParToken);
					break;
				}
				s.skip(commaToken);
			}
		}

		leaveParser("func call");
		return fc;
	}
}
