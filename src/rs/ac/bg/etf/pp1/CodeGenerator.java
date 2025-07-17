package rs.ac.bg.etf.pp1;

import rs.ac.bg.etf.pp1.ast.*;
import rs.etf.pp1.mj.runtime.Code;
import rs.etf.pp1.symboltable.Tab;
import rs.etf.pp1.symboltable.concepts.Obj;
import rs.etf.pp1.symboltable.concepts.Struct;

public class CodeGenerator extends VisitorAdaptor {
    private int mainPc;

    public int getMainPc() {
        return mainPc;
    }

////////////  Method  ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public void visit(MethodSignature signature) {
        signature.obj.setAdr(Code.pc);
        if(signature.getMethodName().equals("main")){
            mainPc = Code.pc;
        }

        Code.put(Code.enter);
        Code.put(signature.obj.getLevel());
        Code.put(signature.obj.getLocalSymbols().size());
    }

    public void visit(MethodDecl methodDecl) {
        Code.put(Code.exit);
        Code.put(Code.return_);
    }

////////////  Designator  ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public void visit(Assignement assignment) {
        Code.store(assignment.getDesignator().obj);
    }

    public void visit(Inc inc){
        if(inc.getDesignator().obj.getKind() == Obj.Elem){
            Code.put(Code.dup2);
        }

        Code.load(inc.getDesignator().obj);
        Code.loadConst(1);
        Code.put(Code.add);
        Code.store(inc.getDesignator().obj);
    }

    public void visit(Dec dec){
        if(dec.getDesignator().obj.getKind() == Obj.Elem){
            Code.put(Code.dup2);
        }

        Code.load(dec.getDesignator().obj);
        Code.loadConst(1);
        Code.put(Code.sub);
        Code.store(dec.getDesignator().obj);
    }

////////////  Factor  ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public void visit(AddTerms addTerms) {
        if(addTerms.getAddop() instanceof Plus){
            Code.put(Code.add);
        }
        else{
            Code.put(Code.sub);
        }
    }

    public void visit(TermMulFactors  termMulFactors) {
        if(termMulFactors.getMulop() instanceof Mull){
            Code.put(Code.mul);
        } else if(termMulFactors.getMulop() instanceof Div){
            Code.put(Code.div);
        } else {
            Code.put(Code.rem);
        }
    }

    public void visit(UnaryMinusFactor unaryMinusFactor) {
        Code.put(Code.neg);
    }

    public void visit(DesignatorFactor designatorFactor) {
        Code.load(designatorFactor.getDesignator().obj);
    }

    public void visit(DesignatorIdent designatorIdent) {
        Code.load(designatorIdent.obj);
    }

    public void visit(NumConstFactor numConstFactor){
        Code.loadConst(numConstFactor.getN1());
    }

    public void visit(CharFactor charFactor){
        Code.loadConst(charFactor.getC1());
    }

    public void visit(BoolFactor boolFactor){
        Code.loadConst(boolFactor.getB1()? 1 : 0);
    }

    public void visit(NewFactor newFactor) {
        Code.put(Code.newarray);
        if(newFactor.getType().struct.equals(Tab.charType)){
            Code.put(0);
        } else {
            Code.put(1);
        }
    }

////////////  Statement  ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public void visit(NoPrintNumConst noPrintNumConst){
        Code.loadConst(0);
    }

    public void visit(PrintNumConst printNumConst){
        Code.loadConst(printNumConst.getN1());
    }

    public void visit(Print print){
        if(print.getExpr().struct.equals(Tab.charType)){
            Code.put(Code.bprint);
        }
        else{
            Code.put(Code.print);
        }
    }

    public void visit(Read read){
        if(read.getDesignator().obj.getType().equals(Tab.charType)){
            Code.put(Code.bread);
        } else {
            Code.put(Code.read);
        }

        Code.store(read.getDesignator().obj);
    }

    public void visit(Return returnStmt){
        Code.put(Code.exit);
        Code.put(Code.return_);
    }

}
