package parser;
import main.*;
import scanner.*;
import static scanner.TokenKind.*;

public abstract class Statement extends PascalSyntax { 
	static Statement st; 
	
	Statement(int lNum) {
		super(lNum); 
	}

	@Override public String identify() {
		return "<Statement> on line " + lineNum;
	}

	@Override void check(Block curScope, Library lib) {
		if(st instanceof AssignStatm){
			AssignStatm as = (AssignStatm)st;
			as.check(curScope, lib);
		} else if(st instanceof CompoundStatm){
			CompoundStatm cs = (CompoundStatm)st;
			cs.check(curScope, lib);
		} else if(st instanceof EmptyStatm){
			EmptyStatm es = (EmptyStatm)st;
			es.check(curScope, lib);
		} else if(st instanceof IfStatm){
			IfStatm ifs = (IfStatm)st;
			ifs.check(curScope, lib);
		} else if(st instanceof ProcCallStatm){
			ProcCallStatm pc = (ProcCallStatm)st;
			pc.check(curScope, lib);
		} else if(st instanceof WhileStatm){
			WhileStatm ws = (WhileStatm)st;
			ws.check(curScope, lib);
		}
	}

	@Override void genCode(CodeFile f) {
		st.genCode(f);
	}

	@Override public void prettyPrint() {
		st.prettyPrint();
	}

	static Statement parse(Scanner s) { 
		enterParser("statement");
		
		if (s.curToken.kind == nameToken) {
			if(s.nextToken.kind == leftBracketToken){
				st = AssignStatm.parse(s);
			} else if (s.nextToken.kind == assignToken) {
				st = AssignStatm.parse(s);
			} else if(s.nextToken.kind == leftParToken){
				st = ProcCallStatm.parse(s);
			} else {
				st = ProcCallStatm.parse(s);
			}
		} else if(s.curToken.kind == beginToken){
			st = CompoundStatm.parse(s);
		} else if(s.curToken.kind == ifToken){
			st = IfStatm.parse(s);
		} else if(s.curToken.kind == whileToken) {
			st = WhileStatm.parse(s);
		} else {
			st = EmptyStatm.parse(s);
		}

		leaveParser("statement");
		return st; 
	}
}