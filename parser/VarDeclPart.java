package parser;
import main.*;
import scanner.*;
import static scanner.TokenKind.*;
import java.util.ArrayList;

/* <VarDeclPart> ::= 'var' <var decl> */
						/* <var decl> */

public class VarDeclPart extends PascalSyntax {

	ArrayList<VarDecl> vd = new ArrayList<>();

	VarDeclPart(int lNum) {
		super(lNum);
	}

	@Override void check(Block curScope, Library lib) {
		for (VarDecl varD : vd) {
			varD.check(curScope, lib);
		}
	}

	@Override void genCode(CodeFile f) {
		for (VarDecl varD : vd) {
			varD.genCode(f);
		}
	}

	@Override public String identify() {
		return "<VarDeclPart> on line " + lineNum;
	}

	@Override void prettyPrint() {
		Main.log.prettyPrint("var ");
		Main.log.prettyIndent();
		Main.log.prettyIndent();
		for (VarDecl v : vd) {
			v.prettyPrint();
		}
		Main.log.prettyOutdent();
		Main.log.prettyOutdent();
	}

	static VarDeclPart parse(Scanner s) {
		enterParser("var decl part");

		VarDeclPart vdp = new VarDeclPart(s.curLineNum());
		s.skip(varToken);
		while(s.curToken.kind == nameToken){
			vdp.vd.add(VarDecl.parse(s));
		}

		leaveParser("var decl part");
		return vdp;
	}
}