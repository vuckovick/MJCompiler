package rs.ac.bg.etf.pp1;

import rs.ac.bg.etf.pp1.ast.*;
import rs.etf.pp1.mj.runtime.Code;
import rs.etf.pp1.symboltable.Tab;
import rs.etf.pp1.symboltable.concepts.Obj;
import rs.etf.pp1.symboltable.concepts.Struct;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class CodeGenerator extends VisitorAdaptor {
    private int mainPc;
    private final int printSetMeth;
    private final Stack<Integer> skipCondFact = new Stack<>();
    private final Stack<Integer> skipCondition = new Stack<>();
    private final Stack<Integer> skipThen = new Stack<>();
    private final Stack<Integer> skipElse = new Stack<>();
    private final Stack<Integer> doBegin = new Stack<>();
    private final Stack<List<Integer>> breakJumps = new Stack<>();
    private final Stack<List<Integer>> continueJumps = new Stack<>();
    private final Struct setStruct;
    private final Obj addMeth;
    private boolean returnHappend = false;

    CodeGenerator(){
        //setStruct
        Obj setObj = Tab.find("set");
        setStruct = setObj.getType();

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        // chr i ord
        Obj chrMeth = Tab.find("chr");
        Obj ordMeth = Tab.find("ord");
        chrMeth.setAdr(Code.pc);
        ordMeth.setAdr(Code.pc);
        Code.put(Code.enter);
        Code.put(1);
        Code.put(1);
        Code.put(Code.load_n);
        Code.put(Code.exit);
        Code.put(Code.return_);
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        // len
        Obj lenMeth = Tab.find("len");
        lenMeth.setAdr(Code.pc);
        Code.put(Code.enter);
        Code.put(1);
        Code.put(1);
        Code.put(Code.load_n);
        Code.put(Code.arraylength);
        Code.put(Code.exit);
        Code.put(Code.return_);
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        // add
        addMeth = Tab.find("add");
        addMeth.setAdr(Code.pc);
        Code.put(Code.enter);
        Code.put(2);    // fp: set a, int b
        Code.put(2);    // lp:

        // provera da li ima mesta
        Code.put(Code.load_n);      // Stek: adr(a)
        Code.put(Code.arraylength); // Stek: size(a) + 1
        Code.loadConst(1);       // Stek: size(a) + 1, 1
        Code.put(Code.sub);         // Stek: size(a)
        Code.put(Code.load_n);      // Stek: size(a), adr(a)
        Code.loadConst(0);       // Stek: size(a), adr(a), 0
        Code.put(Code.aload);       // Stek: size(a), len(a)
        Code.putFalseJump(Code.ne, 0);
        int jumpNoSpace = Code.pc - 2;

        // provera da li je prazan set
        Code.put(Code.load_n);      // Stek: adr(a)
        Code.loadConst(0);       // Stek: adr(a), 0
        Code.put(Code.aload);       // Stek: len(a)
        Code.loadConst(0);       // Stek: len(a), 0
        Code.putFalseJump(Code.eq, 0);
        int jumpHaveElem = Code.pc - 2;

        // dodavanje prvog elem
        Code.put(Code.load_n);      // Stek: adr(a)
        Code.loadConst(1);       // Stek: adr(a), 1
        Code.putJump(0);
        int jumpAddedFirstElem = Code.pc - 2;

        // provera da li se element nalazi vec u listi
        Code.fixup(jumpHaveElem);
        Code.put(Code.load_n);      // Stek: adr(a)
        Code.loadConst(1);       // Stek: adr(a), i = 1
        int continueLoopInSet = Code.pc;
        Code.put(Code.dup_x1);      // Stek: i, adr(a), i
        Code.put(Code.aload);       // Stek: i, a[i]
        Code.put(Code.load_1);      // Stek: i, a[i], b
        Code.putFalseJump(Code.ne, 0);
        int jumpAlreadyInSet = Code.pc - 2;

        // provera da li smo dosli do kraja
        Code.loadConst(1);       // Stek: i, 1
        Code.put(Code.add);         // Stek: new-i
        Code.put(Code.load_n);      // Stek: new-i, adr(a)
        Code.put(Code.dup_x1);      // Stek: adr(a), new-i, adr(a)
        Code.put(Code.pop);         // Stek: adr(a), new-i
        Code.put(Code.dup);         // Stek: adr(a), i, i
        Code.put(Code.load_n);      // Stek: adr(a), i, i, adr(a)
        Code.loadConst(0);       // Stek: adr(a), i, i, adr(a), 0
        Code.put(Code.aload);       // Stek: adr(a), i, i, len(a)
        Code.putFalseJump(Code.gt, continueLoopInSet);

        // dodavanje elementa u listu
        Code.fixup(jumpAddedFirstElem);
        Code.put(Code.load_1);      // Stek: adr(a), i, b
        Code.put(Code.astore);      // Stek:
        Code.put(Code.load_n);      // Stek: adr(a)
        Code.loadConst(0);       // Stek: adr(a), 0
        Code.put(Code.dup2);        // Stek: adr(a), 0, adr(a), 0
        Code.put(Code.aload);       // Stek: adr(a), 0, len(a)
        Code.loadConst(1);       // Stek: adr(a), 0, len(a), 1
        Code.put(Code.add);         // Stek: adr(a), 0, new-len(a)
        Code.put(Code.astore);      // Stek:
        Code.putJump(0);
        int jumpElemAddedInSet = Code.pc - 2;

        // izlazak iz funkcije
        Code.fixup(jumpAlreadyInSet);
        Code.put(Code.pop);
        Code.fixup(jumpNoSpace);
        Code.fixup(jumpElemAddedInSet);
        Code.put(Code.exit);
        Code.put(Code.return_);
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        // addAll
        Obj addAllMeth = Tab.find("addAll");
        addAllMeth.setAdr(Code.pc);
        Code.put(Code.enter);
        Code.put(2);    // fp: set a, int b
        Code.put(2);    // lp:

        // prolazak kroz niz
        Code.loadConst(0);       // Stek: i = 0
        int continueLoopInArr = Code.pc;
        Code.put(Code.dup);         // Stek: i, i
        Code.put(Code.load_1);      // Stek: i, i, adr(b)
        Code.put(Code.arraylength); // Stek: i, i, len(b)
        Code.putFalseJump(Code.lt, 0);
        int jumpEndOfArr = Code.pc - 2;

        // poziv add za b[i]
        Code.put(Code.dup);         // Stek: i, i
        Code.put(Code.load_n);      // Stek: i, i, adr(a)
        Code.put(Code.dup_x1);      // Stek: i, adr(a), i, adr(a)
        Code.put(Code.pop);         // Stek: i, adr(a), i
        Code.put(Code.load_1);      // Stek: i, adr(a), i, adr(b)
        Code.put(Code.dup_x1);      // Stek: i, adr(a), adr(b), i, adr(b)
        Code.put(Code.pop);         // Stek: i, adr(a), adr(b), i
        Code.put(Code.aload);       // Stek: i, adr(a), b[i]
        int offset = addMeth.getAdr() - Code.pc;
        Code.put(Code.call);
        Code.put2(offset);
        Code.loadConst(1);       // Stek: i, 1
        Code.put(Code.add);         // Stek: new-i
        Code.putJump(continueLoopInArr);

        Code.fixup(jumpEndOfArr);
        Code.put(Code.pop);
        Code.put(Code.exit);
        Code.put(Code.return_);

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        // printSet
        printSetMeth = Code.pc;
        Code.put(Code.enter);
        Code.put(1);    // fp: set a
        Code.put(1);

        // provera da li ima elemenata
        Code.put(Code.load_n);      // Stek: adr(a)
        Code.loadConst(0);       // Stek: adr(a), 0
        Code.put(Code.aload);       // Stek: len(a)
        Code.loadConst(0);       // Stek: len(a), 0
        Code.putFalseJump(Code.ne, 0);
        int  jumpNoElem = Code.pc - 2;

        // prolazak kroz sve elemente
        Code.put(Code.load_n);      // Stek: adr(a)
        Code.loadConst(1);       // Stek: adr(a), i = 1
        int continuePrintLoopInSet = Code.pc;
        Code.put(Code.dup_x1);      // Stek: i, adr(a), i
        Code.put(Code.aload);       // Stek: i, a[i]

        //print a[i]
        Code.loadConst(0);       // Stek: i, a[i], 0
        Code.put(Code.print);       // Stek: i

        //print ' '
        Code.loadConst(' ');    // Stek: i, ' '
        Code.loadConst(0);      // Stek: i, ' ', 0
        Code.put(Code.bprint);     // Stek: i

        // provera da li smo dosli do kraja
        Code.loadConst(1);       // Stek: i, 1
        Code.put(Code.add);         // Stek: new-i
        Code.put(Code.load_n);      // Stek: new-i, adr(a)
        Code.put(Code.dup_x1);      // Stek: adr(a), new-i, adr(a)
        Code.put(Code.pop);         // Stek: adr(a), new-i
        Code.put(Code.dup);         // Stek: adr(a), i, i
        Code.put(Code.load_n);      // Stek: adr(a), i, i, adr(a)
        Code.loadConst(0);       // Stek: adr(a), i, i, adr(a), 0
        Code.put(Code.aload);       // Stek: adr(a), i, i, len(a)
        Code.putFalseJump(Code.gt, continuePrintLoopInSet);

        // izlazak iz funkcije
        Code.put(Code.pop);
        Code.put(Code.pop);
        Code.fixup(jumpNoElem);
        Code.put(Code.exit);
        Code.put(Code.return_);
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    }

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
        if(!methodDecl.obj.getType().equals(Tab.noType) && !returnHappend){
            Code.put(Code.trap);
            Code.put(1);
        }

        Code.put(Code.exit);
        Code.put(Code.return_);
        returnHappend = false;
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

    public void visit(ConditionTerm condTerm){
        // tacne
        Code.putJump(0 ); // tacne bacamo na THEN
        skipCondition.push(Code.pc - 2);
        // ovde vracamo netacne
        while(!skipCondFact.empty()){
            Code.fixup(skipCondFact.pop());
        }
    }

    public void visit(Condition condition){
        // netacni
        Code.putJump(0); // netacne bacamo na else
        skipThen.push(Code.pc - 2);
        // then
        while(!skipCondition.empty()){
            Code.fixup(skipCondition.pop());
        }
    }



    public void visit(NoElse noElse){
        //tacne
        Code.fixup(skipThen.pop());
        //tacne + netacne
    }

    public void visit(Else else_){
        //tacne
        Code.putJump(0); // tacne bacamo na kraj ELSE
        skipElse.push(Code.pc - 2);
        Code.fixup(skipThen.pop());
        //netacne
    }

    public void visit(WithElse withElse) {
        //netacne
        Code.fixup(skipElse.pop()); //vracamo tacne koje su preskocili ELSE
        //netacne + tacne
    }



    public void visit(BeginDoWhile beginDoWhile){
        doBegin.push(Code.pc);
        breakJumps.push(new ArrayList<>());
        continueJumps.push(new ArrayList<>());
    }

    public void visit(DoWhile doWhile){
        Code.putJump(doBegin.pop());
        if(!skipThen.empty()){
            Code.fixup(skipThen.pop());
        }
        
        while(!breakJumps.peek().isEmpty()){
            Code.fixup(breakJumps.peek().remove(0));
        }
        breakJumps.pop();
    }

    public void visit(Break breakStmt){
        Code.putJump(0);
        breakJumps.peek().add(Code.pc - 2);
    }

    public void visit(Continue continueStmt){
        Code.putJump(0);
        continueJumps.peek().add(Code.pc - 2);
    }

    public void visit(While whileStmt){
        while(!continueJumps.peek().isEmpty()){
            Code.fixup(continueJumps.peek().remove(0));
        }
        continueJumps.pop();
    }

////////////  Designator  ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public void visit(Assignement assignment) {
        if(assignment.getDesignatorOrExpr() instanceof DesignatorExpr) {
            Code.store(assignment.getDesignator().obj);
        }
    }

    public void addForAllElemInSet(Obj dest, Obj a){
        Code.load(a);               // Stek: adr(a)
        Code.loadConst(0);       // Stek: adr(a), 0
        Code.put(Code.aload);       // Stek: len(a)
        Code.loadConst(0);       // Stek: len(a), 0
        Code.putFalseJump(Code.ne, 0);
        int  jumpNoElem = Code.pc - 2;

        // prolazak kroz sve elemente
        Code.load(dest);               // Stek: adr(dest)
        Code.load(a);                  // Stek: adr(dest), adr(a)
        Code.loadConst(1);          // Stek: adr(dest), adr(a), i = 1
        int continuePrintLoopInSet = Code.pc;
        Code.put(Code.dup_x2);         // Stek: i, adr(dest), adr(a), i
        Code.put(Code.aload);          // Stek: i, adr(dest), a[i]

        //add a[i]
        int offset = addMeth.getAdr() - Code.pc;
        Code.put(Code.call);
        Code.put2(offset);             // Stek: i

        // provera da li smo dosli do kraja
        Code.load(dest);            // Stek: i, adr(dest)
        Code.put(Code.dup_x1);      // Stek: adr(dest), i, adr(dest)
        Code.put(Code.pop);         // Stek: adr(dest), i
        Code.loadConst(1);       // Stek: adr(dest), i, 1
        Code.put(Code.add);         // Stek: adr(dest), new-i
        Code.load(a);               // Stek: adr(dest), new-i, adr(a)
        Code.put(Code.dup_x1);      // Stek: adr(dest), adr(a), new-i, adr(a)
        Code.put(Code.pop);         // Stek: adr(dest), adr(a), new-i
        Code.put(Code.dup);         // Stek: adr(dest), adr(a), i, i
        Code.load(a);               // Stek: adr(dest), adr(a), i, i, adr(a)
        Code.loadConst(0);       // Stek: adr(dest), adr(a), i, i, adr(a), 0
        Code.put(Code.aload);       // Stek: adr(dest), adr(a), i, i, len(a)
        Code.putFalseJump(Code.gt, continuePrintLoopInSet);

        // izlazak iz funkcije
        Code.put(Code.pop);
        Code.put(Code.pop);
        Code.fixup(jumpNoElem);
    }

    public void visit(UnionStmt unionStmt) {
        Obj dest = ((Assignement) unionStmt.getParent()).getDesignator().obj;
        Obj a = unionStmt.getDesignator().obj;
        Obj b = unionStmt.getDesignator1().obj;
        if(unionStmt.getSetop() instanceof Union){
            // brisanje dest
            Code.load(dest);        // Stek: dest
            Code.loadConst(0);   // Stek: dest, 0
            Code.loadConst(0);   // Stek: dest, 0, 0
            Code.put(Code.astore);

            // dodavanje elemenata iz a
            addForAllElemInSet(dest, a);

            // dodavanje elemenata iz b
            addForAllElemInSet(dest, b);
        }
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

    public void visit(MapExpretion mapExpretion){
        Obj methObj = mapExpretion.getDesignator().obj;
        Obj arrayObj = mapExpretion.getDesignator1().obj;

        Code.loadConst(0);      // Stek: sum
        Code.loadConst(0);      // Stek: sum, i

        // prolazak kroz niz
        int jumpLoopArr = Code.pc;
        Code.put(Code.dup);        // Stek: sum, i, i
        Code.load(arrayObj);       // Stek: sum, i, i, arr
        Code.put(Code.arraylength);// Stek: sum, i, i, len(arr)
        Code.putFalseJump(Code.ne, 0);
        int jumpOutOfLoop = Code.pc - 2;
        Code.put(Code.dup);        // Stek: sum, i, i
        Code.load(arrayObj);       // Stek: sum, i, i, arr
        Code.put(Code.dup_x1);     // Stek: sum, i, arr, i, arr
        Code.put(Code.pop);        // Stek: sum, i, arr, i
        Code.put(Code.aload);      // Stek: sum, i, arr[i]
        int offset = methObj.getAdr() - Code.pc;
        Code.put(Code.call);
        Code.put2(offset);
        Code.put(Code.dup_x2);     // Stek: ret, sum, i, ret
        Code.put(Code.pop);        // Stek: ret, sum, i
        Code.loadConst(1);      // Stek: ret, sum, i, 1
        Code.put(Code.add);        // Stek: ret, sum, new-i
        Code.put(Code.dup_x2);     // Stek: i, ret, sum, i
        Code.put(Code.pop);        // Stek: i, ret, sum
        Code.put(Code.add);        // Stek: i, new-sum
        Code.put(Code.dup_x1);     // Stek: sum, i, sum
        Code.put(Code.pop);        // Stek: sum, i
        Code.putJump(jumpLoopArr);

        Code.fixup(jumpOutOfLoop);
        Code.put(Code.pop);        // Stek: sum
    }

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
        if(newFactor.getType().struct.equals(setStruct)){
            Code.loadConst(1);
            Code.put(Code.add);
        }

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
        if(print.getExpr().struct.equals(setStruct)){
            Code.put(Code.pop);      // Stek: .., a

            int offset = printSetMeth - Code.pc;
            Code.put(Code.call);
            Code.put2(offset);

        } else {
            if (print.getExpr().struct.equals(Tab.charType)) {
                Code.put(Code.bprint);
            } else {
                Code.put(Code.print);
            }
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
        returnHappend = true;
    }

}
