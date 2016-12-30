package parser;
import main.*;
import scanner.*;
import static scanner.TokenKind.*;
import java.util.ArrayList;

						   /*|----------   ^*/
/* <numeric literal> ::= '('   <param decl>  ')'*/

public class ParamDeclList extends PascalSyntax {
	ArrayList<ParamDecl> pdList = new ArrayList<ParamDecl>();
	ParamDecl pd;

	ParamDeclList(int lNum) {
		super(lNum);
	}

	@Override public String identify() {
		return "<param decl list> on line " + lineNum;
	}

	@Override void check(Block curScope, Library lib) {
		for (ParamDecl pdTmp : pdList) {
			pdTmp.check(curScope, lib);
		}
	}

	@Override void genCode(CodeFile f) {
		for (ParamDecl pdTmp : pdList) {
			pdTmp.genCode(f);
		}
	}

	@Override public void prettyPrint() {
		Main.log.prettyPrint("(");
		int i = 0;
		for(ParamDecl pdTmp : pdList) {
			pdTmp.prettyPrint();
			if (i < pdList.size()-1) {
				Main.log.prettyPrint("; ");	
				i++;			
			}
		}

		Main.log.prettyPrint(")");
	}

	public static ParamDeclList parse(Scanner s) {
		enterParser("param decl list");

		ParamDeclList pdl = new ParamDeclList(s.curLineNum());
		s.skip(leftParToken);
		
		while(true){
			pdl.pd = ParamDecl.parse(s);
			pdl.pdList.add(pdl.pd);
			if (s.curToken.kind == rightParToken) {
				s.skip(rightParToken);
				break;
			}
			s.skip(semicolonToken);
		}
		leaveParser("param decl list");
		return pdl;
	}
}