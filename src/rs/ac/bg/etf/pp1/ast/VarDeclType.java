// generated with ast extension for cup
// version 0.8
// 17/6/2025 12:29:1


package rs.ac.bg.etf.pp1.ast;

public class VarDeclType extends DecType {

    private GlobalVarDecl GlobalVarDecl;

    public VarDeclType (GlobalVarDecl GlobalVarDecl) {
        this.GlobalVarDecl=GlobalVarDecl;
        if(GlobalVarDecl!=null) GlobalVarDecl.setParent(this);
    }

    public GlobalVarDecl getGlobalVarDecl() {
        return GlobalVarDecl;
    }

    public void setGlobalVarDecl(GlobalVarDecl GlobalVarDecl) {
        this.GlobalVarDecl=GlobalVarDecl;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(GlobalVarDecl!=null) GlobalVarDecl.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(GlobalVarDecl!=null) GlobalVarDecl.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(GlobalVarDecl!=null) GlobalVarDecl.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("VarDeclType(\n");

        if(GlobalVarDecl!=null)
            buffer.append(GlobalVarDecl.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [VarDeclType]");
        return buffer.toString();
    }
}
