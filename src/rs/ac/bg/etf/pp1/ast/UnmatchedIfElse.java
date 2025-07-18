// generated with ast extension for cup
// version 0.8
// 18/6/2025 15:55:44


package rs.ac.bg.etf.pp1.ast;

public class UnmatchedIfElse extends Unmatched {

    private IfConstruction IfConstruction;
    private Matched Matched;
    private Unmatched Unmatched;

    public UnmatchedIfElse (IfConstruction IfConstruction, Matched Matched, Unmatched Unmatched) {
        this.IfConstruction=IfConstruction;
        if(IfConstruction!=null) IfConstruction.setParent(this);
        this.Matched=Matched;
        if(Matched!=null) Matched.setParent(this);
        this.Unmatched=Unmatched;
        if(Unmatched!=null) Unmatched.setParent(this);
    }

    public IfConstruction getIfConstruction() {
        return IfConstruction;
    }

    public void setIfConstruction(IfConstruction IfConstruction) {
        this.IfConstruction=IfConstruction;
    }

    public Matched getMatched() {
        return Matched;
    }

    public void setMatched(Matched Matched) {
        this.Matched=Matched;
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
        if(IfConstruction!=null) IfConstruction.accept(visitor);
        if(Matched!=null) Matched.accept(visitor);
        if(Unmatched!=null) Unmatched.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(IfConstruction!=null) IfConstruction.traverseTopDown(visitor);
        if(Matched!=null) Matched.traverseTopDown(visitor);
        if(Unmatched!=null) Unmatched.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(IfConstruction!=null) IfConstruction.traverseBottomUp(visitor);
        if(Matched!=null) Matched.traverseBottomUp(visitor);
        if(Unmatched!=null) Unmatched.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("UnmatchedIfElse(\n");

        if(IfConstruction!=null)
            buffer.append(IfConstruction.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(Matched!=null)
            buffer.append(Matched.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(Unmatched!=null)
            buffer.append(Unmatched.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [UnmatchedIfElse]");
        return buffer.toString();
    }
}
