// generated with ast extension for cup
// version 0.8
// 28/6/2025 14:37:6


package rs.ac.bg.etf.pp1.ast;

public class FormParsListEnd extends FormParsList {

    private FormParItem FormParItem;

    public FormParsListEnd (FormParItem FormParItem) {
        this.FormParItem=FormParItem;
        if(FormParItem!=null) FormParItem.setParent(this);
    }

    public FormParItem getFormParItem() {
        return FormParItem;
    }

    public void setFormParItem(FormParItem FormParItem) {
        this.FormParItem=FormParItem;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(FormParItem!=null) FormParItem.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(FormParItem!=null) FormParItem.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(FormParItem!=null) FormParItem.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("FormParsListEnd(\n");

        if(FormParItem!=null)
            buffer.append(FormParItem.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [FormParsListEnd]");
        return buffer.toString();
    }
}
