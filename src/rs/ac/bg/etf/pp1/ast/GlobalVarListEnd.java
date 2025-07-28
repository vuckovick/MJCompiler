// generated with ast extension for cup
// version 0.8
// 28/6/2025 14:37:6


package rs.ac.bg.etf.pp1.ast;

public class GlobalVarListEnd extends GlobalVarList {

    private GlobalVarItem GlobalVarItem;

    public GlobalVarListEnd (GlobalVarItem GlobalVarItem) {
        this.GlobalVarItem=GlobalVarItem;
        if(GlobalVarItem!=null) GlobalVarItem.setParent(this);
    }

    public GlobalVarItem getGlobalVarItem() {
        return GlobalVarItem;
    }

    public void setGlobalVarItem(GlobalVarItem GlobalVarItem) {
        this.GlobalVarItem=GlobalVarItem;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(GlobalVarItem!=null) GlobalVarItem.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(GlobalVarItem!=null) GlobalVarItem.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(GlobalVarItem!=null) GlobalVarItem.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("GlobalVarListEnd(\n");

        if(GlobalVarItem!=null)
            buffer.append(GlobalVarItem.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [GlobalVarListEnd]");
        return buffer.toString();
    }
}
