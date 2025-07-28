// generated with ast extension for cup
// version 0.8
// 28/6/2025 14:37:6


package rs.ac.bg.etf.pp1.ast;

public class GlobalVarDecl implements SyntaxNode {

    private SyntaxNode parent;
    private int line;
    private Type Type;
    private GlobalVarList GlobalVarList;

    public GlobalVarDecl (Type Type, GlobalVarList GlobalVarList) {
        this.Type=Type;
        if(Type!=null) Type.setParent(this);
        this.GlobalVarList=GlobalVarList;
        if(GlobalVarList!=null) GlobalVarList.setParent(this);
    }

    public Type getType() {
        return Type;
    }

    public void setType(Type Type) {
        this.Type=Type;
    }

    public GlobalVarList getGlobalVarList() {
        return GlobalVarList;
    }

    public void setGlobalVarList(GlobalVarList GlobalVarList) {
        this.GlobalVarList=GlobalVarList;
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
        if(Type!=null) Type.accept(visitor);
        if(GlobalVarList!=null) GlobalVarList.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(Type!=null) Type.traverseTopDown(visitor);
        if(GlobalVarList!=null) GlobalVarList.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(Type!=null) Type.traverseBottomUp(visitor);
        if(GlobalVarList!=null) GlobalVarList.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("GlobalVarDecl(\n");

        if(Type!=null)
            buffer.append(Type.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(GlobalVarList!=null)
            buffer.append(GlobalVarList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [GlobalVarDecl]");
        return buffer.toString();
    }
}
