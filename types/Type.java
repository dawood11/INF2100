package types;

import main.*;
import parser.PascalSyntax;

public abstract class Type {
    public abstract String identify();

    public void checkType(Type tx, String op, PascalSyntax where, String mess) {
	Main.log.noteTypeCheck(this, op, tx, where);
	//System.out.println("\nType " + tx  + "\nTHIS " + this + "\nWHERE lineNum " + where.lineNum + "\n MESS " + mess);
	//if (this != tx)
	if (this.getClass() != tx.getClass())
	    where.error(mess);
    }

    public abstract int size();
}
