// generated with ast extension for cup
// version 0.8
// 18/6/2025 15:55:44


package rs.ac.bg.etf.pp1.ast;

public class UnmatchedStmt extends Statement {

    private Unmatched Unmatched;

    public UnmatchedStmt (Unmatched Unmatched) {
        this.Unmatched=Unmatched;
        if(Unmatched!=null) Unmatched.setParent(this);
    }

    public Unmatched getUnmatched() {
        return Unmatched;
    }

    public void setUnmatched(Unmatched Unmatched) {
        this.Unmatched=Unmatched;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(Unmatched!=null) Unmatched.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(Unmatched!=null) Unmatched.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(Unmatched!=null) Unmatched.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("UnmatchedStmt(\n");

        if(Unmatched!=null)
            buffer.append(Unmatched.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [UnmatchedStmt]");
        return buffer.toString();
    }
}
