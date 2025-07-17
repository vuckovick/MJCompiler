// generated with ast extension for cup
// version 0.8
// 17/6/2025 12:29:1


package rs.ac.bg.etf.pp1.ast;

public class CondFact implements SyntaxNode {

    private SyntaxNode parent;
    private int line;
    public rs.etf.pp1.symboltable.concepts.Struct struct = null;

    private OptionalRelop OptionalRelop;

    public CondFact (OptionalRelop OptionalRelop) {
        this.OptionalRelop=OptionalRelop;
        if(OptionalRelop!=null) OptionalRelop.setParent(this);
    }

    public OptionalRelop getOptionalRelop() {
        return OptionalRelop;
    }

    public void setOptionalRelop(OptionalRelop OptionalRelop) {
        this.OptionalRelop=OptionalRelop;
    }

    public SyntaxNode getParent() {
        return parent;
    }

    public void setParent(SyntaxNode parent) {
        this.parent=parent;
    }

    public int getLine() {
        return line;
    }

    public void setLine(int line) {
        this.line=line;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(OptionalRelop!=null) OptionalRelop.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(OptionalRelop!=null) OptionalRelop.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(OptionalRelop!=null) OptionalRelop.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("CondFact(\n");

        if(OptionalRelop!=null)
            buffer.append(OptionalRelop.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [CondFact]");
        return buffer.toString();
    }
}
