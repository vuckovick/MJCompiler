// generated with ast extension for cup
// version 0.8
// 26/6/2025 18:20:58


package rs.ac.bg.etf.pp1.ast;

public class DoWhile extends Statement {

    private BeginDoWhile BeginDoWhile;
    private Statement Statement;
    private While While;
    private OptionalCondition OptionalCondition;

    public DoWhile (BeginDoWhile BeginDoWhile, Statement Statement, While While, OptionalCondition OptionalCondition) {
        this.BeginDoWhile=BeginDoWhile;
        if(BeginDoWhile!=null) BeginDoWhile.setParent(this);
        this.Statement=Statement;
        if(Statement!=null) Statement.setParent(this);
        this.While=While;
        if(While!=null) While.setParent(this);
        this.OptionalCondition=OptionalCondition;
        if(OptionalCondition!=null) OptionalCondition.setParent(this);
    }

    public BeginDoWhile getBeginDoWhile() {
        return BeginDoWhile;
    }

    public void setBeginDoWhile(BeginDoWhile BeginDoWhile) {
        this.BeginDoWhile=BeginDoWhile;
    }

    public Statement getStatement() {
        return Statement;
    }

    public void setStatement(Statement Statement) {
        this.Statement=Statement;
    }

    public While getWhile() {
        return While;
    }

    public void setWhile(While While) {
        this.While=While;
    }

    public OptionalCondition getOptionalCondition() {
        return OptionalCondition;
    }

    public void setOptionalCondition(OptionalCondition OptionalCondition) {
        this.OptionalCondition=OptionalCondition;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(BeginDoWhile!=null) BeginDoWhile.accept(visitor);
        if(Statement!=null) Statement.accept(visitor);
        if(While!=null) While.accept(visitor);
        if(OptionalCondition!=null) OptionalCondition.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(BeginDoWhile!=null) BeginDoWhile.traverseTopDown(visitor);
        if(Statement!=null) Statement.traverseTopDown(visitor);
        if(While!=null) While.traverseTopDown(visitor);
        if(OptionalCondition!=null) OptionalCondition.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(BeginDoWhile!=null) BeginDoWhile.traverseBottomUp(visitor);
        if(Statement!=null) Statement.traverseBottomUp(visitor);
        if(While!=null) While.traverseBottomUp(visitor);
        if(OptionalCondition!=null) OptionalCondition.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("DoWhile(\n");

        if(BeginDoWhile!=null)
            buffer.append(BeginDoWhile.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(Statement!=null)
            buffer.append(Statement.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(While!=null)
            buffer.append(While.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(OptionalCondition!=null)
            buffer.append(OptionalCondition.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [DoWhile]");
        return buffer.toString();
    }
}
