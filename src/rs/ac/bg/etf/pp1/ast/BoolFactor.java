// generated with ast extension for cup
// version 0.8
// 18/6/2025 15:55:44


package rs.ac.bg.etf.pp1.ast;

public class BoolFactor extends Factor {

    private Boolean B1;

    public BoolFactor (Boolean B1) {
        this.B1=B1;
    }

    public Boolean getB1() {
        return B1;
    }

    public void setB1(Boolean B1) {
        this.B1=B1;
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
        buffer.append("BoolFactor(\n");

        buffer.append(" "+tab+B1);
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [BoolFactor]");
        return buffer.toString();
    }
}
