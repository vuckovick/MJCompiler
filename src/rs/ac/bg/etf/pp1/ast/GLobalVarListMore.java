// generated with ast extension for cup
// version 0.8
// 28/6/2025 14:37:6


package rs.ac.bg.etf.pp1.ast;

public class GLobalVarListMore extends GlobalVarList {

    private GlobalVarList GlobalVarList;
    private GlobalVarItem GlobalVarItem;

    public GLobalVarListMore (GlobalVarList GlobalVarList, GlobalVarItem GlobalVarItem) {
        this.GlobalVarList=GlobalVarList;
        if(GlobalVarList!=null) GlobalVarList.setParent(this);
        this.GlobalVarItem=GlobalVarItem;
        if(GlobalVarItem!=null) GlobalVarItem.setParent(this);
    }

    public GlobalVarList getGlobalVarList() {
        return GlobalVarList;
    }

    public void setGlobalVarList(GlobalVarList GlobalVarList) {
        this.GlobalVarList=GlobalVarList;
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
        if(GlobalVarList!=null) GlobalVarList.accept(visitor);
        if(GlobalVarItem!=null) GlobalVarItem.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(GlobalVarList!=null) GlobalVarList.traverseTopDown(visitor);
        if(GlobalVarItem!=null) GlobalVarItem.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(GlobalVarList!=null) GlobalVarList.traverseBottomUp(visitor);
        if(GlobalVarItem!=null) GlobalVarItem.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("GLobalVarListMore(\n");

        if(GlobalVarList!=null)
            buffer.append(GlobalVarList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(GlobalVarItem!=null)
            buffer.append(GlobalVarItem.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [GLobalVarListMore]");
        return buffer.toString();
    }
}
