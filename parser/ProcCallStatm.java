package parser;
import main.*;
import scanner.*;
import types.*;
import static scanner.TokenKind.*;
import java.util.ArrayList;

public class ProcCallStatm extends Statement {
	String name;
	int selfDeclaredWrite = 0;
	ArrayList<Expression> ex = new ArrayList<Expression>();

	ProcCallStatm(int lNum) {
		super(lNum);
	}
	
	ProcDecl proc = null;

	@Override public String identify() {
		return "<ProcCallStatm> on line " + lineNum;
	}

	@Override void check(Block curScope, Library lib) {
		proc = (ProcDecl)curScope.findDecl(name, this);
		//type = proc.type;
		//System.out.println("ProcCallStatm " + type + ", " + proc);
		proc.checkWhetherProcedure(this);
		int paramCounter = 1;
		if (!ex.isEmpty()) {
			for (Expression eTmpCheck : ex) {
				eTmpCheck.check(curScope, lib);
				//System.out.println("PROC.lineNum " + proc.lineNum);
				//System.out.println(proc.id);
				if ((!proc.id.equals("write") || proc.lineNum != -1) && proc.pdl != null) {
					if (paramCounter > proc.pdl.pdList.size()) {
						error("Too many parameters in call on " + name);
					}
				} else if ((!proc.id.equals("write") || proc.lineNum != -1) && proc.pdl == null) {
					error("Too many parameters in call on " + name);
				}
				if ((!proc.id.equals("write") || proc.lineNum != -1) && proc.pdl != null) {
					types.Type tmpType = proc.pdl.pdList.get(paramCounter - 1).type;
					//System.out.println("\neTmpCheck " + eTmpCheck.type + "\ntmpType " + tmpType);
					eTmpCheck.type.checkType(tmpType, "Param #" + paramCounter, this, "Param #" + paramCounter + " is not " + tmpType.identify());
				}
				paramCounter++;
			}
		}
		//System.out.println("PROC.ID " + proc.id);
		if ((!proc.id.equals("write") || proc.lineNum != -1) && proc.pdl != null) {
			if (paramCounter-1 < proc.pdl.pdList.size()) {
				error("Too few parameters in call on " + name);
			} else if (paramCounter-1 > proc.pdl.pdList.size()) {
				error("Too many parameters in call on " + name);
			}
			if (proc.id.equals("write") && proc.lineNum != -1) {
				selfDeclaredWrite = 1;
			}
		}
	}

	@Override void genCode(CodeFile f) {
		if (name.equals("write") && selfDeclaredWrite == 0) {
			if(!ex.isEmpty()){
				for (int i = 0; i < ex.size(); i++) {
					ex.get(i).genCode(f);
					if (ex.get(i).se1.t.get(0).f.get(0) instanceof UnsignedConstant) {
						f.genInstr("", "pushl", "%eax", "Push next param.");
						if (ex.get(i).se1.t.get(0).f.get(0) instanceof NumberLiteral) {
							f.genInstr("", "call", "write_int", "");
						} else if (ex.get(i).se1.t.get(0).f.get(0) instanceof CharLiteral) {
							f.genInstr("", "call", "write_char", "");
						} else if (((NamedConst)ex.get(i).se1.t.get(0).f.get(0)).name.equals("true") || ((NamedConst)ex.get(i).se1.t.get(0).f.get(0)).name.equals("false")) {
							f.genInstr("", "call", "write_bool", "");
						}
					} else if (ex.get(i).se1.t.get(0).f.get(0) instanceof Variable) {
						if (((Variable)ex.get(i).se1.t.get(0).f.get(0)).var.name.equals("eol")) {
							//System.out.println("\n\nHva sOren vil du??\n");
							f.genInstr("", "movl", "$" + 10 + ",%eax", "char " + 10);
							f.genInstr("", "pushl", "%eax", "Push next param.");
							f.genInstr("", "call", "write_char", "");
						} else if (((Variable)ex.get(i).se1.t.get(0).f.get(0)).var.type instanceof types.IntType) {
							f.genInstr("", "pushl", "%eax", "Push next param.");
							f.genInstr("", "call", "write_int", "");
						} else if (((Variable)ex.get(i).se1.t.get(0).f.get(0)).var.type instanceof types.CharType) {
							f.genInstr("", "pushl", "%eax", "Push next param.");
							f.genInstr("", "call", "write_char", "");
						} else if (((Variable)ex.get(i).se1.t.get(0).f.get(0)).var.type instanceof types.BoolType) {
							f.genInstr("", "pushl", "%eax", "Push next param.");
							f.genInstr("", "call", "write_bool", "");
						}
					} else if (ex.get(i).se1.t.get(0).f.get(0) instanceof Negation) {
						f.genInstr("", "pushl", "%eax", "Push next param.");
						f.genInstr("", "call", "write_bool", "");
					}
					f.genInstr("", "addl", "$4,%esp", "Pop param.");
				}
			}
			//f.genInstr("", "addl", "$" + 4*ex.size() + ",%esp", "Pop param.");
		} else {
			if(!ex.isEmpty()){
				for (int i = ex.size()-1; i >= 0; i--) {				
					ex.get(i).genCode(f);
					f.genInstr("", "pushl", "%eax", "Push next param.");
				}
			}
			f.genInstr("", "call", "proc$" + proc.progProcFuncName, "");
			if(!ex.isEmpty()){
				f.genInstr("", "addl", "$" + 4*ex.size() + ",%esp", "");
			}
		}
	}

	@Override public void prettyPrint() {
		Main.log.prettyPrint(name);
		if(!ex.isEmpty()){
			Main.log.prettyPrint("(");
			for (int i = 0; i < ex.size(); i++){
				ex.get(i).prettyPrint();
				if(i < ex.size()-1){
					Main.log.prettyPrint(", ");
				}
			}
			Main.log.prettyPrint(")");
		}
	}

	static ProcCallStatm parse(Scanner s) {
		enterParser("proc call");

		ProcCallStatm pcs = new ProcCallStatm(s.curLineNum());
		
		pcs.name = s.curToken.id;
		s.skip(nameToken);

		if (s.curToken.kind == leftParToken){
			s.skip(leftParToken);

			pcs.ex.add(Expression.parse(s));

			while(s.curToken.kind == commaToken){
				s.skip(commaToken);

				pcs.ex.add(Expression.parse(s));
			}

			s.skip(rightParToken);
		}

		leaveParser("proc call");
		return pcs;
	}
}