// generated with ast extension for cup
// version 0.8
// 28/6/2025 14:37:7


package rs.ac.bg.etf.pp1.ast;

public class Eq extends Relop {

    public Eq () {
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
        buffer.append("Eq(\n");

        buffer.append(tab);
        buffer.append(") [Eq]");
        return buffer.toString();
    }
}
