// generated with ast extension for cup
// version 0.8
// 28/6/2025 14:37:6


package rs.ac.bg.etf.pp1.ast;

public class DesignatorStmtDoWhile extends DesignatorStatementWhile {

    private DesignatorOpt DesignatorOpt;

    public DesignatorStmtDoWhile (DesignatorOpt DesignatorOpt) {
        this.DesignatorOpt=DesignatorOpt;
        if(DesignatorOpt!=null) DesignatorOpt.setParent(this);
    }

    public DesignatorOpt getDesignatorOpt() {
        return DesignatorOpt;
    }

    public void setDesignatorOpt(DesignatorOpt DesignatorOpt) {
        this.DesignatorOpt=DesignatorOpt;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(DesignatorOpt!=null) DesignatorOpt.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(DesignatorOpt!=null) DesignatorOpt.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(DesignatorOpt!=null) DesignatorOpt.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("DesignatorStmtDoWhile(\n");

        if(DesignatorOpt!=null)
            buffer.append(DesignatorOpt.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [DesignatorStmtDoWhile]");
        return buffer.toString();
    }
}
