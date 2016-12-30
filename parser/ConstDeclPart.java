package parser;

import java.util.ArrayList;
import main.*;
import scanner.*;
import static scanner.TokenKind.*;

							   /*|-------------^*/
/* <const decl part> ::= 'const'   <const decl>    */

public class ConstDeclPart extends PascalSyntax {
	ConstDecl cd = null;
	ArrayList<ConstDecl> cdList;

	ConstDeclPart(int lNum) {
		super(lNum);
		cdList = new ArrayList<ConstDecl>();
	}

	@Override public String identify() {
		return "<const decl part> on line " + lineNum;
	}
	@Override void check(Block curScope, Library lib) {
		for (ConstDecl constD : cdList) {
			constD.check(curScope, lib);
		}
	}

	@Override void genCode(CodeFile f) {
		for (ConstDecl constD : cdList) {
			constD.genCode(f);
		}
	}

	@Override void prettyPrint() {
		Main.log.prettyPrint("const ");
		for (ConstDecl cdl : cdList) {
			cdl.prettyPrint();
			//Main.log.prettyPrintLn("");
		}
	}

	static ConstDeclPart parse(Scanner s) {
		enterParser("const decl part");
		ConstDeclPart cdp = new ConstDeclPart(s.curLineNum());

		s.skip(constToken);

		while(s.curToken.kind == nameToken){
			cdp.cd = ConstDecl.parse(s);
			cdp.cdList.add(cdp.cd);
		}

		leaveParser("const decl part");
		return cdp;
	}
}
