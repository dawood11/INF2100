package parser;
import main.*;
import scanner.*;
import static scanner.TokenKind.*;
import java.util.ArrayList;

public class StatmList extends PascalSyntax {
	ArrayList<Statement> s = new ArrayList<Statement>();

	StatmList(int lNum){
		super(lNum);
	}

	@Override public String identify() {
		return "<StatmList> on line " + lineNum;
	}

	@Override void check(Block curScope, Library lib) {
		for (Statement st : s) {
			st.check(curScope, lib);
		}
	}

	@Override void genCode(CodeFile f) {
		for (Statement st : s) {
			st.genCode(f);
		}
	}

	@Override public void prettyPrint() {
		int i = 0;
		for (Statement stat : s){
			stat.prettyPrint();
			if(i < s.size()-1){
				Main.log.prettyPrintLn(";");
				i++;
			} else {
				Main.log.prettyPrintLn("");
			}
		}
	}

	static StatmList parse(Scanner s) {
		enterParser("statm list");

		StatmList sl = new StatmList(s.curLineNum());
		
		sl.s.add(Statement.parse(s));

		while(s.curToken.kind == semicolonToken){
			s.skip(semicolonToken);
			sl.s.add(Statement.parse(s));
		}
		
		leaveParser("statm list");
		return sl;
	}
}