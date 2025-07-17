package rs.ac.bg.etf.pp1;

import rs.ac.bg.etf.pp1.ast.MethodDecl;
import rs.ac.bg.etf.pp1.ast.MethodSignature;
import rs.ac.bg.etf.pp1.ast.VisitorAdaptor;
import rs.etf.pp1.mj.runtime.Code;

public class CodeGenerator extends VisitorAdaptor {
    private int mainPc;

    public int getMainPc() {
        return mainPc;
    }

    public void visit(MethodSignature signature) {
        Code.put(Code.enter);
        Code.put(signature.obj.getLevel());
        Code.put(signature.obj.getLocalSymbols().size());
    }

    public void visit(MethodDecl methodDecl) {
        Code.put(Code.enter);
        Code.put(Code.return_);
    }

}
