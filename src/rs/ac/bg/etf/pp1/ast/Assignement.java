// generated with ast extension for cup
// version 0.8
// 26/6/2025 18:20:58


package rs.ac.bg.etf.pp1.ast;

public class Assignement extends DesignatorStatement {

    private Designator Designator;
    private Assignop Assignop;
    private DesignatorOrExpr DesignatorOrExpr;

    public Assignement (Designator Designator, Assignop Assignop, DesignatorOrExpr DesignatorOrExpr) {
        this.Designator=Designator;
        if(Designator!=null) Designator.setParent(this);
        this.Assignop=Assignop;
        if(Assignop!=null) Assignop.setParent(this);
        this.DesignatorOrExpr=DesignatorOrExpr;
        if(DesignatorOrExpr!=null) DesignatorOrExpr.setParent(this);
    }

    public Designator getDesignator() {
        return Designator;
    }

    public void setDesignator(Designator Designator) {
        this.Designator=Designator;
    }

    public Assignop getAssignop() {
        return Assignop;
    }

    public void setAssignop(Assignop Assignop) {
        this.Assignop=Assignop;
    }

    public DesignatorOrExpr getDesignatorOrExpr() {
        return DesignatorOrExpr;
    }

    public void setDesignatorOrExpr(DesignatorOrExpr DesignatorOrExpr) {
        this.DesignatorOrExpr=DesignatorOrExpr;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(Designator!=null) Designator.accept(visitor);
        if(Assignop!=null) Assignop.accept(visitor);
        if(DesignatorOrExpr!=null) DesignatorOrExpr.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(Designator!=null) Designator.traverseTopDown(visitor);
        if(Assignop!=null) Assignop.traverseTopDown(visitor);
        if(DesignatorOrExpr!=null) DesignatorOrExpr.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(Designator!=null) Designator.traverseBottomUp(visitor);
        if(Assignop!=null) Assignop.traverseBottomUp(visitor);
        if(DesignatorOrExpr!=null) DesignatorOrExpr.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("Assignement(\n");

        if(Designator!=null)
            buffer.append(Designator.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(Assignop!=null)
            buffer.append(Assignop.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(DesignatorOrExpr!=null)
            buffer.append(DesignatorOrExpr.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [Assignement]");
        return buffer.toString();
    }
}
