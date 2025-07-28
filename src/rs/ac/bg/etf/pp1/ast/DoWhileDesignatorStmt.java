// generated with ast extension for cup
// version 0.8
// 28/6/2025 14:37:6


package rs.ac.bg.etf.pp1.ast;

public class DoWhileDesignatorStmt extends OptionalDesignatorStatement {

    private DesignatorStatementWhile DesignatorStatementWhile;

    public DoWhileDesignatorStmt (DesignatorStatementWhile DesignatorStatementWhile) {
        this.DesignatorStatementWhile=DesignatorStatementWhile;
        if(DesignatorStatementWhile!=null) DesignatorStatementWhile.setParent(this);
    }

    public DesignatorStatementWhile getDesignatorStatementWhile() {
        return DesignatorStatementWhile;
    }

    public void setDesignatorStatementWhile(DesignatorStatementWhile DesignatorStatementWhile) {
        this.DesignatorStatementWhile=DesignatorStatementWhile;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(DesignatorStatementWhile!=null) DesignatorStatementWhile.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(DesignatorStatementWhile!=null) DesignatorStatementWhile.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(DesignatorStatementWhile!=null) DesignatorStatementWhile.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("DoWhileDesignatorStmt(\n");

        if(DesignatorStatementWhile!=null)
            buffer.append(DesignatorStatementWhile.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [DoWhileDesignatorStmt]");
        return buffer.toString();
    }
}
