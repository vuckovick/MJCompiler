// generated with ast extension for cup
// version 0.8
// 28/6/2025 14:37:7


package rs.ac.bg.etf.pp1.ast;

public class TermFactor extends FactorList {

    private FactorOpt FactorOpt;

    public TermFactor (FactorOpt FactorOpt) {
        this.FactorOpt=FactorOpt;
        if(FactorOpt!=null) FactorOpt.setParent(this);
    }

    public FactorOpt getFactorOpt() {
        return FactorOpt;
    }

    public void setFactorOpt(FactorOpt FactorOpt) {
        this.FactorOpt=FactorOpt;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(FactorOpt!=null) FactorOpt.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(FactorOpt!=null) FactorOpt.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(FactorOpt!=null) FactorOpt.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("TermFactor(\n");

        if(FactorOpt!=null)
            buffer.append(FactorOpt.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [TermFactor]");
        return buffer.toString();
    }
}
