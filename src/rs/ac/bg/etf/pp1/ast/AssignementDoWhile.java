// generated with ast extension for cup
// version 0.8
// 17/6/2025 12:29:1


package rs.ac.bg.etf.pp1.ast;

public class AssignementDoWhile extends DesignatorStatementWhile {

    private Designator Designator;
    private Assignop Assignop;
    private DesignatorOrExprWhile DesignatorOrExprWhile;

    public AssignementDoWhile (Designator Designator, Assignop Assignop, DesignatorOrExprWhile DesignatorOrExprWhile) {
        this.Designator=Designator;
        if(Designator!=null) Designator.setParent(this);
        this.Assignop=Assignop;
        if(Assignop!=null) Assignop.setParent(this);
        this.DesignatorOrExprWhile=DesignatorOrExprWhile;
        if(DesignatorOrExprWhile!=null) DesignatorOrExprWhile.setParent(this);
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

    public DesignatorOrExprWhile getDesignatorOrExprWhile() {
        return DesignatorOrExprWhile;
    }

    public void setDesignatorOrExprWhile(DesignatorOrExprWhile DesignatorOrExprWhile) {
        this.DesignatorOrExprWhile=DesignatorOrExprWhile;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(Designator!=null) Designator.accept(visitor);
        if(Assignop!=null) Assignop.accept(visitor);
        if(DesignatorOrExprWhile!=null) DesignatorOrExprWhile.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(Designator!=null) Designator.traverseTopDown(visitor);
        if(Assignop!=null) Assignop.traverseTopDown(visitor);
        if(DesignatorOrExprWhile!=null) DesignatorOrExprWhile.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(Designator!=null) Designator.traverseBottomUp(visitor);
        if(Assignop!=null) Assignop.traverseBottomUp(visitor);
        if(DesignatorOrExprWhile!=null) DesignatorOrExprWhile.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("AssignementDoWhile(\n");

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

        if(DesignatorOrExprWhile!=null)
            buffer.append(DesignatorOrExprWhile.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [AssignementDoWhile]");
        return buffer.toString();
    }
}
