package parser;
import main.*;
import scanner.*;
import types.*;
import static scanner.TokenKind.*;
import java.util.HashMap;


/* <library> ::= */
public class Library extends Block {
	int level = 0;

	TypeDecl bt = new TypeDecl("boolean", -1);
	TypeDecl ct = new TypeDecl("char", -1);
	TypeDecl it = new TypeDecl("integer", -1);

	ConstDecl cdeol = new ConstDecl("eol", -1);
	ConstDecl cdf = new ConstDecl("false", -1);
	ConstDecl cdt = new ConstDecl("true", -1); 
	ProcDecl pdwrite = new ProcDecl("write", -1);

	static HashMap<String, PascalDecl> decls = new HashMap<String, PascalDecl>();

	public Library() {
		super(-1);
		bt.type = new types.BoolType();
		ct.type = new types.CharType();
		it.type = new types.IntType();

		cdeol.type = ct.type;
		cdf.type = bt.type;
		cdt.type = bt.type;
		//cdf.type = new types.BoolType();
		//cdt.type = new types.BoolType();

		decls.put("boolean", bt);
		decls.put("char", ct);
		decls.put("eol", cdeol);
		decls.put("false", cdf);
		decls.put("integer", it);
		decls.put("true", cdt);
		decls.put("write", pdwrite);
	}
	//public Library() {}

	@Override public String identify() {
		return "<library> on line " + lineNum;
	}

	@Override public void genCode(CodeFile f) {
		// f.genInstr("", ".extern write_char", "", "");
		// f.genInstr("", ".extern write_int", "", "");
		// f.genInstr("", ".extern write_bool", "", "");
	}

	PascalDecl findDecl(String id, PascalSyntax source){
		PascalDecl pascalDecl = decls.get(id);
		if (pascalDecl != null) {
			Main.log.noteBinding(id, source, pascalDecl);
			return pascalDecl;
		}
		//if (outerScope != null) {
		//	return outerScope.findDecl(id, source);
		//}
		source.error("Unknown id: " + id);
		return null;
	}

	@Override public void prettyPrint() {}

	static Library parse(Scanner s) {
		return null;
	}
}
