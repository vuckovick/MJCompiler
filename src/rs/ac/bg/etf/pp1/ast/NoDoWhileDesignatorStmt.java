// generated with ast extension for cup
// version 0.8
// 18/6/2025 15:55:44


package rs.ac.bg.etf.pp1.ast;

public class NoDoWhileDesignatorStmt extends OptionalDesignatorStatement {

    public NoDoWhileDesignatorStmt () {
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("NoDoWhileDesignatorStmt(\n");

        buffer.append(tab);
        buffer.append(") [NoDoWhileDesignatorStmt]");
        return buffer.toString();
    }
}
