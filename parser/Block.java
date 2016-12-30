package parser;

import java.util.HashMap;
import java.util.ArrayList;

import main.*;
import scanner.*;
import static scanner.TokenKind.*;

											  /*|-----------^*/
/* <block> ::= <const decl part> <var decl part> <func decl> 'begin' <stat list> 'end'*/
				/*<> */				/*<> */		/*<proc decl> */
												/*<> */

public class Block extends PascalSyntax {
	ConstDeclPart cdp = null;
	VarDeclPart vdp = null;
	FuncDecl fd = null;
	ProcDecl pd = null;
	StatmList sl = null;
	ArrayList<ProcDecl> procDeclList;

	HashMap<String,PascalDecl> decls = new HashMap<>();

	//Block curScope = this;
	Block outerScope = null;
	int nextOffset = 0;
	int level = 0;
	//static int tmplevel = 0;

	Block(int lNum) {
		super(lNum);
		procDeclList = new ArrayList<ProcDecl>();
	}

	@Override public String identify() {
		return "<block> on line " + lineNum;
	}

	@Override void genCode(CodeFile f) {
		//tmplevel++;
		//level;
		// if (cdp != null) {
		// 	cdp.genCode(f);
		// }
		// if (vdp != null) {
		// 	vdp.genCode(f);
		// }
		for (ProcDecl prdl : procDeclList) {
			if (prdl instanceof FuncDecl) {
				FuncDecl fdTmp = (FuncDecl)prdl;
				fdTmp.genCode(f);
			} else if (prdl instanceof ProcDecl) {
				ProcDecl pdTmp = prdl;
				pdTmp.genCode(f);
			}
		}
		if (level != 1) {
			sl.genCode(f);
		}
	}

	void addDecl(String id, PascalDecl d) {
		if (decls.containsKey(id))
			d.error(id + " declared twice in same block!");
		decls.put(id, d);
	}

	PascalDecl findDecl(String id, PascalSyntax source){
		PascalDecl pascalDecl = decls.get(id);
		if (pascalDecl != null) {
			Main.log.noteBinding(id, source, pascalDecl);
			return pascalDecl;
		}
		if (outerScope != null) {
			return outerScope.findDecl(id, source);
		}
		source.error("Unknown id: " + id);
		return null;
	}

	@Override void check(Block curScope, Library lib) {
		this.outerScope = curScope;
		this.level = outerScope.level + 1;
		if (cdp != null) {
			for (ConstDecl cd : cdp.cdList) {
				addDecl(cd.name, cd);
			}
			cdp.check(this, lib);
		}
		if (vdp != null) {
			for (VarDecl varD : vdp.vd) {
				addDecl(varD.name, varD);
			}
			vdp.check(this, lib);
		}
		if(!procDeclList.isEmpty()){
			for (ProcDecl prdl : procDeclList) {
				if (prdl instanceof FuncDecl) {
					FuncDecl fdTmp = (FuncDecl)prdl;
					addDecl(fdTmp.name, fdTmp);
					fdTmp.check(this, lib);
				} else if (prdl instanceof ProcDecl) {
					ProcDecl pdTmp = prdl;
					addDecl(pdTmp.name, pdTmp);
					pdTmp.check(this, lib);
				}
			}
		}
		if (sl != null) {
			sl.check(this, lib);
		}
	}

	@Override void prettyPrint() {

		if (cdp != null) {
			cdp.prettyPrint();
			Main.log.prettyPrintLn("");
		}
		if (vdp != null) {
			vdp.prettyPrint();
			Main.log.prettyPrintLn("");
		}
		for (ProcDecl prdl : procDeclList) {
			if (prdl instanceof FuncDecl) {
				FuncDecl fdTmp = (FuncDecl)prdl;
				fdTmp.prettyPrint();
				Main.log.prettyPrintLn("");
			} else if (prdl instanceof ProcDecl) {
				ProcDecl pdTmp = prdl;
				pdTmp.prettyPrint();
				Main.log.prettyPrintLn("");
			}
		}

		Main.log.prettyPrintLn("begin");
		Main.log.prettyIndent();
		sl.prettyPrint();
		Main.log.prettyOutdent();
		Main.log.prettyPrint("end");
	}

	static Block parse(Scanner s) {
		enterParser("block");

		Block bl = new Block(s.curLineNum());

		if (s.curToken.kind == constToken) {
			bl.cdp = ConstDeclPart.parse(s);
		}
		if (s.curToken.kind == varToken) {
			bl.vdp = VarDeclPart.parse(s);
		}

		while(s.curToken.kind == functionToken || s.curToken.kind == procedureToken){
			if (s.curToken.kind == functionToken) {
				bl.fd = FuncDecl.parse(s);
				bl.procDeclList.add(bl.fd);
			} else if (s.curToken.kind == procedureToken) {
				bl.pd = ProcDecl.parse(s);
				bl.procDeclList.add(bl.pd);
			}
		}

		s.skip(beginToken);

		bl.sl = StatmList.parse(s);

		s.skip(endToken);

		leaveParser("block");
		return bl;
	}
}