package rs.ac.bg.etf.pp1;

import rs.ac.bg.etf.pp1.ast.*;
import rs.etf.pp1.mj.runtime.Code;
import rs.etf.pp1.symboltable.Tab;
import rs.etf.pp1.symboltable.concepts.Obj;
import rs.etf.pp1.symboltable.concepts.Struct;

import java.util.Stack;

public class CodeGenerator extends VisitorAdaptor {
    private int mainPc, skipThen;
    private Stack<Integer> skipCondFact = new Stack<>();
    private Stack<Integer> skipCondition = new Stack<>();

    private int getRelopCode(Relop relop) {
        if(relop instanceof Eq)
            return  Code.eq;
        if(relop instanceof Ne)
            return  Code.ne;
        if(relop instanceof Gt)
            return  Code.gt;
        if(relop instanceof Lt)
            return  Code.lt;
        if(relop instanceof Ge)
            return  Code.ge;
        if(relop instanceof Le)
            return  Code.le;
        return 0;
    }

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

////////////  Condition  ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public void visit(NoRelops noRelops) {
        Code.loadConst(0);
        Code.putFalseJump(Code.ne, 0); // netacna
        skipCondFact.push(Code.pc - 2);
        // tacna
    }

    public void visit(Relops relops) {
        Code.putFalseJump(getRelopCode(relops.getRelop()), 0); // netacna
        skipCondFact.push(Code.pc - 2);
        // tacna
    }

    public void visit(CondTerm condTerm){
        // tacne
        Code.putJump(0 ); // tacne bacamo na THEN
        skipCondFact.push(Code.pc - 2);
        // ovde vracamo netacne
        while(!skipCondFact.empty()){
            Code.fixup(skipCondFact.pop());
        }
    }

    public void visit(Condition condition){
        // netacni
        Code.putJump(0); // netacne bacamo na else
        skipThen = Code.pc - 2;
        // then
        while(!skipCondition.empty()){
            Code.fixup(skipCondition.pop());
        }
    }

////////////  Designator  ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public void visit(Assignement assignment) {
        Code.store(assignment.getDesignator().obj);
    }

    public void visit(FunctionCall functionCall) {
        int offset = functionCall.getDesignator().obj.getAdr() - Code.pc;
        Code.put(Code.call);
        Code.put2(offset);

        if(functionCall.getDesignator().obj.getType() != Tab.noType){
            Code.put(Code.pop);
        }
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

    public void visit(DesignatorMethFactor designatorMethFactor) {
        int offset = designatorMethFactor.getDesignator().obj.getAdr() - Code.pc;
        Code.put(Code.call);
        Code.put2(offset);
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
