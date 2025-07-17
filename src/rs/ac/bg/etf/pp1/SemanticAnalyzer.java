package rs.ac.bg.etf.pp1;

import com.sun.org.apache.xalan.internal.xsltc.compiler.util.VoidType;
import org.apache.log4j.Logger;
import rs.ac.bg.etf.pp1.ast.*;
import rs.etf.pp1.symboltable.Tab;
import rs.etf.pp1.symboltable.concepts.Obj;
import rs.etf.pp1.symboltable.concepts.Scope;
import rs.etf.pp1.symboltable.concepts.Struct;

import java.util.ArrayList;
import java.util.List;


public class SemanticAnalyzer extends VisitorAdaptor {
    Logger log = Logger.getLogger(this.getClass());

    private boolean errorDetected = false, returnFound = false;
    private final Struct boolStruct, setStruct;
    private Struct currTypeStruct;
    private Obj currMethodObj, mainMethodObj;
    private int doWhileCnt = 0;
    int nVars;

    public SemanticAnalyzer() {
        //dopuna tabele simbola
        Tab.init();
        boolStruct = new Struct(Struct.Bool);
        Scope universe = Tab.currentScope();
        Tab.insert(Obj.Type, "bool", boolStruct);

        for(Obj o: Tab.find("chr").getLocalSymbols()){
            o.setFpPos(1);
        }

        for(Obj o: Tab.find("ord").getLocalSymbols()){
            o.setFpPos(1);
        }

        for(Obj o: Tab.find("len").getLocalSymbols()){
            o.setFpPos(1);
        }

        setStruct = new Struct(Struct.Enum);
        setStruct.setElementType(Tab.intType);
        Tab.insert(Obj.Type, "set", setStruct);

        Obj addObj, addAllObj;
        universe.addToLocals(addObj = new Obj(Obj.Meth, "add", setStruct, 0, 2));
        {
            Tab.openScope();
            Tab.currentScope().addToLocals(new Obj(Obj.Var, "a", setStruct, 0, 1));
            Tab.currentScope().addToLocals(new Obj(Obj.Var, "b", Tab.intType, 0, 1));
            addObj.setLocals(Tab.currentScope().getLocals());
            Tab.closeScope();
        }
        for(Obj o: addObj.getLocalSymbols()){
            o.setFpPos(1);
        }

        universe.addToLocals(addAllObj = new Obj(Obj.Meth, "addAll", setStruct, 0, 2));
        {
            Tab.openScope();
            Tab.currentScope().addToLocals(new Obj(Obj.Var, "a", setStruct, 0, 1));
            Tab.currentScope().addToLocals(new Obj(Obj.Var, "b", new Struct(Struct.Array, Tab.intType), 0, 1));
            addAllObj.setLocals(Tab.currentScope().getLocals());
            Tab.closeScope();
        }
        for(Obj o: addAllObj.getLocalSymbols()){
            o.setFpPos(1);
        }
    }

    public void report_error(String var1, SyntaxNode var2) {
        this.errorDetected = true;
        StringBuilder var3 = new StringBuilder(var1);
        int var4 = var2 == null ? 0 : var2.getLine();
        if (var4 != 0) {
            var3.append(" na liniji ").append(var4);
        }

        this.log.error(var3.toString());
    }

    public void report_info(String var1, SyntaxNode var2) {
        StringBuilder var3 = new StringBuilder(var1);
        int var4 = var2 == null ? 0 : var2.getLine();
        if (var4 != 0) {
            var3.append(" na liniji ").append(var4);
        }

        this.log.info(var3.toString());
    }

    public boolean passed() {
        return !this.errorDetected;
    }

    private List<Struct> getFormalParams(Obj methodObj) {
        List<Struct> formalParams = new ArrayList<>();
        if (methodObj.getKind() == Obj.Meth) {
            for (Obj local : methodObj.getLocalSymbols()) {
                if (local.getFpPos() == 1) {
                    formalParams.add(local.getType());
                }
            }
        }
        return formalParams;
    }

    private List<Struct> getActualParams(OptionalActPars optionalActPars) {
        List<Struct> actualParams = new ArrayList<>();
        if (optionalActPars instanceof FuncPars) {
            ActPars actPars = ((FuncPars) optionalActPars).getActPars();
            if (actPars != null) {
                actualParams.add(actPars.getExpr().struct);
                collectActParsList(actPars.getActParsList(), actualParams);
            }
        }
        return actualParams;
    }

    private void collectActParsList(ActParsList actParsList, List<Struct> actualParams) {
        if (actParsList instanceof ActParams) {
            ActParams actParams = (ActParams) actParsList;
            collectActParsList(actParams.getActParsList(), actualParams);
            actualParams.add(actParams.getExpr().struct);
        }
    }

    private void reportSymbolUsage(Obj obj, SyntaxNode node) {
        String usageType;
        String objStr;
        if (obj.getKind() == Obj.Con) {
            usageType = "Koriscen simbolicka konstanta na liniji " + node.getLine() + "  ";
            objStr = getKindString(obj) + " " + getTypeString(obj.getType()) + " " + obj.getName() + ", " + obj.getAdr() + ", " + obj.getLevel();
        } else if (obj.getKind() == Obj.Var) {
            if (obj.getLevel() == 0) {
                usageType = "Koriscena globalna promenljiva na liniji " + node.getLine() + " ";
            } else if (obj.getFpPos() > 0) {
                usageType = "Koriscen formalni argument na liniji " + node.getLine() + "     ";
            } else {
                usageType = "Koriscena lokalna promenljiva na liniji " + node.getLine() + "  ";
            }
            objStr = getKindString(obj) + " " + getTypeString(obj.getType()) + " " + obj.getName() + ", " + obj.getAdr() + ", " + obj.getLevel();
        } else if (obj.getKind() == Obj.Meth) {
            usageType = "Poziv funkcije na liniji " + node.getLine() + "                 ";
            objStr = getKindString(obj) + " " + getTypeString(obj.getType()) + " " + obj.getName() + "(" + getFormParamsString(obj) + ")" + ", " + obj.getAdr() + ", " + obj.getLevel();
        } else if (obj.getKind() == Obj.Elem) {
            usageType = "Pristup elementu ";
            if (obj.getLevel() == 0) {
                usageType += "globalnog";
            } else if (obj.getFpPos() > 0) {
                usageType += "formalnog";
            } else {
                usageType += "lokalnog ";
            }
            usageType += " niza na liniji " + node.getLine();
            objStr = getKindString(obj) + " " + getTypeString(obj.getType()) + " " + obj.getName() + ", " + obj.getAdr() + ", " + obj.getLevel();
        } else {
            return;
        }
        String message = usageType + " : [ "
                + objStr + " ]";
        report_info(message, null);
    }

    private String getKindString(Obj obj) {
        switch (obj.getKind()) {
            case Obj.Con:   return "Con";
            case Obj.Var:    return "Var";
            case Obj.Meth:    return "Meth";
            case Obj.Elem:    return "Elem";
        }
        return "";
    }

    private String getTypeString(Struct struct) {
        switch (struct.getKind()) {
            case Struct.Int:  return "int";
            case Struct.Char:  return "char";
            case Struct.Array:  return getTypeString(struct.getElemType()) + "[]";
            case Struct.Bool:  return "bool";
            case Struct.Enum:  return "set";
        }
        return "";
    }

    private String getFormParamsString(Obj obj){
        if(obj.getLevel() == 0){
            return "";
        }

        List<Struct> formalParams = getFormalParams(obj);
        StringBuilder formalParamsStr = new StringBuilder();
        for (Struct formalParam : formalParams) {
            formalParamsStr.append(getTypeString(formalParam)).append(", ");
        }

        return formalParamsStr.substring(0, formalParamsStr.length() - 2);
    }

////////////  PROGRAM  ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public void visit(ProgramName programName) {
        programName.obj = Tab.insert(Obj.Prog, programName.getProgName(), Tab.noType);
        programName.obj.setLevel(0);
        Tab.openScope();
    }

    public void visit(Program program) {
        nVars = Tab.currentScope().getnVars();
        Tab.chainLocalSymbols(program.getProgramName().obj);
        Tab.closeScope();

        if(mainMethodObj == null) {
            report_error("Greska na liniji " + program.getLine() + ": Ne postoji main metoda!", null);
        }
        else if(mainMethodObj.getLevel() > 0){
            report_error("Greska na liniji " + program.getLine() + ": Main metoda ne sme da ima parametre!", null);
        }
    }



////////////  TABEL OF SIMBOLS  ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    ////////////  Vars  ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public void visit(Type type) {
        Obj typeObj = Tab.find(type.getType());
        if (typeObj == Tab.noObj) {
            this.report_error("Greska na liniji " + type.getLine() + ": Nije pronadjen tip " + type.getType() + " u tabeli simbola! ", (SyntaxNode)null);
            type.struct = Tab.noType;
        } else if (Obj.Type == typeObj.getKind()) {
            type.struct = typeObj.getType();
        } else {
            this.report_error("Greska na liniji " + type.getLine() + ": Ime " + type.getType() + " ne predstavlja tip!", type);
            type.struct = Tab.noType;
        }

        currTypeStruct = type.struct;
    }

    public void visit(VarItem varItem){
        Obj varObj;

        varObj = Tab.currentScope().findSymbol(varItem.getVarName());
//        nema potrebe za ovim
//        if(currMethodObj == null){
//            varObj = Tab.find(varItem.getVarName());
//        }
//        else{
//            varObj = Tab.currentScope().findSymbol(varItem.getVarName());
//        }

        if (varObj == null || varObj == Tab.noObj) {
            if (varItem.getArrayOpt() instanceof NoArray) {
                Tab.insert(Obj.Var, varItem.getVarName(), currTypeStruct);
            } else {
                Struct type = new Struct(Struct.Array, currTypeStruct);
                Tab.insert(Obj.Var, varItem.getVarName(), type);
            }
        } else {
            report_error("Greska na liniji " + varItem.getLine() + ": Lokalna promenljiva " + varItem.getVarName()
                    + " je vec deklarisana!", null);
        }
    }

    public void visit(GlobalVar globalVar) {
        Obj varObj;
        varObj = Tab.find(globalVar.getVarName());

        if (varObj == null || varObj == Tab.noObj) {
            if (globalVar.getArrayOpt() instanceof NoArray) {
                Tab.insert(Obj.Var, globalVar.getVarName(), currTypeStruct);
            } else {
                Struct type = new Struct(Struct.Array, currTypeStruct);
                Tab.insert(Obj.Var, globalVar.getVarName(), type);
            }
        } else {
            report_error("Greska na liniji " + globalVar.getLine() + ": Globalna promenljiva " + globalVar.getVarName()
                    + " je vec deklarisana!", null);
        }
    }

    ////////////  Consts  ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public void visit(NumConst value){
        value.struct = Tab.intType;
    }

    public void visit(BoolConst value){
        value.struct = boolStruct;
    }

    public void visit(CharConst value){
        value.struct = Tab.charType;
    }

    public void visit(ConstItem constItem){
        if (Tab.currentScope().findSymbol(constItem.getConstName()) != null) {
            report_error("Greska na liniji " + constItem.getLine() + ": Konstanta " + constItem.getConstName() + " je vec deklarisana!", null);
        } else if (!currTypeStruct.assignableTo(constItem.getConsts().struct)) {
            report_error("Greska na liniji " + constItem.getLine() + ": Konstanta "
                    + constItem.getConstName() + "nije kompatibilna sa zadatim tipom!", null);

        } else {
            int value = 0;
            Obj con = Tab.insert(Obj.Con, constItem.getConstName(), constItem.getConsts().struct);
            if (constItem.getConsts() instanceof NumConst) {
                value = ((NumConst) constItem.getConsts()).getValue();
            } else if (constItem.getConsts() instanceof CharConst) {
                value = ((CharConst) constItem.getConsts()).getValue();
            } else if (constItem.getConsts() instanceof BoolConst) {
                value = ((BoolConst) constItem.getConsts()).getValue() ? 1 : 0;
            }
            con.setAdr(value);
        }
    }

    ////////////  Method  ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public void visit(FormPar formPar){
        if (Tab.currentScope().findSymbol(formPar.getFormName()) != null) {
            report_error("Greska na liniji " + formPar.getLine() + ": Parametar " + formPar.getFormName() + " je vec deklarisan!", null);
        } else {
            if (formPar.getArrayOpt() instanceof NoArray) {
                Obj parNode = Tab.insert(Obj.Var, formPar.getFormName(), formPar.getType().struct);
                parNode.setFpPos(1);
                currMethodObj.setLevel(currMethodObj.getLevel() + 1);
            } else {
                Struct type = new Struct(Struct.Array, formPar.getType().struct);
                Obj parNode = Tab.insert(Obj.Var, formPar.getFormName(), type);
                parNode.setFpPos(1);
                currMethodObj.setLevel(currMethodObj.getLevel() + 1);
            }
        }
    }

    public void visit(MethodDecl methodDecl) {
        if (!returnFound && currMethodObj.getType() != Tab.noType) {
            report_error("Greska na liniji " + methodDecl.getLine() + ": Funcija " + currMethodObj.getName()
                    + " nema return iskaz!", null);
        }

        Tab.chainLocalSymbols(currMethodObj);
        Tab.closeScope();

        returnFound = false;
        currMethodObj = null;
    }

    public void visit(MethodSignature methodSignature) {
        if (Tab.currentScope().findSymbol(methodSignature.getMethodName()) != null) {
            report_error("Greska na liniji " + methodSignature.getLine() + ": Ime funkcije " + methodSignature.getMethodName() + " je vec deklarisano!", null);
        }
        currMethodObj = Tab.insert(Obj.Meth, methodSignature.getMethodName(), methodSignature.getMethodType().struct);

        if (methodSignature.getMethodName().equalsIgnoreCase("main")) {
            mainMethodObj = currMethodObj;
            if (currMethodObj.getType() != Tab.noType) {
                report_error("Greska na liniji " + methodSignature.getLine() + ": Main metoda mora da ima povratni tip VOID!", null);
            }
        }

        methodSignature.obj = currMethodObj;
        Tab.openScope();
    }

    public void visit(ReturnType returnType) {
        returnType.struct = returnType.getType().struct;
    }

    public void visit(ReturnVoid returnVoid) {
        returnVoid.struct = Tab.noType;
    }



////////////  CONTEXT CONDITIONS //////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    ////////////  FACTOR  ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public void visit(NumConstFactor numConstFactor){
        numConstFactor.struct = Tab.intType;
    }

    public void visit(CharFactor charFactor){
        charFactor.struct = Tab.charType;
    }

    public void visit(BoolFactor boolFactor){
        boolFactor.struct = boolStruct;
    }

    public void visit(UnaryMinusFactor unaryMinusFactor){
        if(unaryMinusFactor.getFactor().struct.equals(Tab.intType)){
            unaryMinusFactor.struct = Tab.intType;
        }
        else{
            report_error("Greska na liniji " + unaryMinusFactor.getLine() + ": Ne mozemo negirati vrednost koja nije int", null);
            unaryMinusFactor.struct = Tab.noType;
        }
    }

    public void visit(NoUnaryFactor noUnaryFactor){
        noUnaryFactor.struct = noUnaryFactor.getFactor().struct;
    }

    public void visit(NewFactor newFactor){
        if(!newFactor.getExpr().struct.equals(Tab.intType)){
            report_error("Greska na liniji " + newFactor.getLine() + ": Velicina nije int vrednosti.", newFactor);
            newFactor.struct = Tab.noType;
        }else{
            Struct type = newFactor.getType().struct;
            if (type == setStruct) {
                // Ako je Type skup, kreira se skup
                newFactor.struct = setStruct;
            } else {
                // U suprotnom, kreira se niz
                newFactor.struct = new Struct(Struct.Array, type);
            }
        }
    }

    public void visit(ParenFactor parenFactor){
        parenFactor.struct = parenFactor.getExpr().struct;
    }

    public void visit(DesignatorFactor designatorFactor){
        designatorFactor.struct = designatorFactor.getDesignator().obj.getType();
    }

    public void visit(DesignatorMethFactor designatorMethFactor) {
        Obj methodObj = designatorMethFactor.getDesignator().obj;
        if (methodObj.getKind() != Obj.Meth) {
            report_error("Greska na liniji " + designatorMethFactor.getLine() + ": Poziv ne adekvatne metode.", designatorMethFactor);
            designatorMethFactor.struct = Tab.noType;
            return;
        }
        if (methodObj.getType() == Tab.noType) {
            report_error("Greska na liniji " + designatorMethFactor.getLine() + ": Metoda nema povratni tip, ne moze se koristiti u izrazu.", designatorMethFactor);
            designatorMethFactor.struct = Tab.noType;
            return;
        }

        List<Struct> actualParams = getActualParams(designatorMethFactor.getOptionalActPars());
        List<Struct> formalParams = getFormalParams(methodObj);
        if (actualParams.size() != formalParams.size()) {
            report_error("Greska na liniji " + designatorMethFactor.getLine() + ": Broj argumenata se ne poklapa. Ocekivano " + formalParams.size() + ", dobijeno " + actualParams.size() + ".", designatorMethFactor);
        } else {
            for (int i = 0; i < actualParams.size(); i++) {
                if (!actualParams.get(i).assignableTo(formalParams.get(i))) {
                    report_error("Greska na liniji " + designatorMethFactor.getLine() + ": Tip argumenta " + (i + 1) + " nije kompatibilan.", designatorMethFactor);
                }
            }
        }
        designatorMethFactor.struct = methodObj.getType();
    }

    ////////////  DESIGNATOR ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public void visit(DesignatorVar designator){
        Obj ident = Tab.find(designator.getName());
        if (ident == Tab.noObj) {
            report_error("Greska na liniji " + designator.getLine() + ": Promenljiva " + designator.getName() + " nije deklarisana!", null);
            designator.obj = Tab.noObj;
        }
        else if(ident.getKind() != Obj.Var && ident.getKind() != Obj.Con && ident.getKind() != Obj.Meth) {
            report_error("Greska na liniji " + designator.getLine() + ": Promenljiva " + designator.getName() + " nije adekvatna!", null);
            designator.obj = Tab.noObj;
        }
        else {
            designator.obj = ident;

            reportSymbolUsage(ident, designator);
        }
    }

    public void visit(DesignatorIdent designatorIdent){
        Obj ident = Tab.find(designatorIdent.getName());
        if (ident == Tab.noObj) {
            report_error("Greska na liniji " + designatorIdent.getLine() + ": Promenljiva " + designatorIdent.getName() + " nije deklarisana!", null);
            designatorIdent.obj = Tab.noObj;
        }
        else if(ident.getKind() != Obj.Var || ident.getType().getKind() != Struct.Array){
            report_error("Greska na liniji " + designatorIdent.getLine() + ": Promenljiva " + designatorIdent.getName() + " nije niz!", null);
            designatorIdent.obj = Tab.noObj;
        }
        else {
            designatorIdent.obj = ident;
        }
    }

    public void visit(DesignatorArr designatorArr){
        Obj ident = designatorArr.getDesignatorIdent().obj;
        if(ident == Tab.noObj)
            designatorArr.obj = Tab.noObj;
        else if(!designatorArr.getExpr().struct.equals(Tab.intType)) {
            report_error("Greska na liniji " + designatorArr.getLine() + ": Indeksiranje sa ne int vrednosti.", designatorArr);
            designatorArr.obj = Tab.noObj;
        }
        else {
            designatorArr.obj = new Obj(Obj.Elem, ident.getName() + "[]", ident.getType().getElemType());
            designatorArr.obj.setLevel(ident.getLevel());
            designatorArr.obj.setFpPos(ident.getFpPos());
            designatorArr.obj.setAdr(ident.getAdr());
            reportSymbolUsage(designatorArr.obj, designatorArr);
        }
    }

    ////////////  TERM && EXP  ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public void visit(TermFactor termFactor){
        termFactor.struct = termFactor.getFactorOpt().struct;
    }

    public void visit(TermMulFactors termMulFactors){
        if(termMulFactors.getFactorList().struct != Tab.intType || termMulFactors.getFactorOpt().struct != Tab.intType){
            report_error("Greska na liniji " + termMulFactors.getLine() + ": Obe vrednosti moraju da budu tipa int prilikom mnozenja.", termMulFactors);
            termMulFactors.struct = Tab.noType;
        }
        else {
            termMulFactors.struct = Tab.intType;
        }
    }

    public void visit(Term term){
        term.struct = term.getFactorList().struct;
    }

    public void visit(AddTerm addTerm){
        addTerm.struct = addTerm.getTerm().struct;
    }

    public void visit(AddTerms addTerms){
        if(addTerms.getTermList().struct != Tab.intType || addTerms.getTerm().struct != Tab.intType){
            report_error("Greska na liniji " + addTerms.getLine() + ": Obe vrednosti moraju da budu tipa int prilikom sabiranja.", addTerms);
            addTerms.struct = Tab.noType;
        }
        else {
            addTerms.struct = Tab.intType;
        }
    }

    public void visit(Expretion expretion){
        expretion.struct = expretion.getTermList().struct;
    }

    public void visit(MapExpretion mapExpr) {
        Designator left = mapExpr.getDesignator();
        Designator right = mapExpr.getDesignator1();

        Obj leftObj = left.obj;
        Obj rightObj = right.obj;

        if (leftObj == Tab.noObj || rightObj == Tab.noObj) {
            mapExpr.struct = Tab.noType;
            return;
        }

        if (leftObj.getKind() != Obj.Meth) {
            report_error("Greska na liniji " + mapExpr.getLine() + ": Levi operand map operacije mora biti funkcija.", mapExpr);
            mapExpr.struct = Tab.noType;
            return;
        }

        Struct returnType = leftObj.getType();
        if (!returnType.equals(Tab.intType)) {
            report_error("Greska na liniji " + mapExpr.getLine() + ": Funkcija mora vratiti int.", mapExpr);
            mapExpr.struct = Tab.noType;
            return;
        }

         List<Struct> formalParams = getFormalParams(leftObj);
        if (formalParams.size() != 1 || !formalParams.get(0).equals(Tab.intType)) {
            report_error("Greska na liniji " + mapExpr.getLine() + ": Funkcija mora imati tacno jedan parametar tipa int.", mapExpr);
            mapExpr.struct = Tab.noType;
            return;
        }

        Struct rightType = rightObj.getType();
        if (rightType.getKind() != Struct.Array || !rightType.getElemType().equals(Tab.intType)) {
            report_error("Greska na liniji " + mapExpr.getLine() + ": Desni operand map operacije mora biti niz int vrednosti.", mapExpr);
            mapExpr.struct = Tab.noType;
            return;
        }

        mapExpr.struct = Tab.intType;
    }

    ////////////  Statement  ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public void visit(DesignatorExpr designatorExpr){
        designatorExpr.struct = designatorExpr.getExpr().struct;
    }

    public void visit(Assignement assignement){
        Obj designatorObj = assignement.getDesignator().obj;
        int designatorKind = designatorObj.getKind();
        if(designatorKind != Obj.Var && designatorKind != Obj.Elem){
            report_error("Greska na liniji " + assignement.getLine() + ": Dodela u neadekvatnu promenljivu.", assignement);
        } else if(!assignement.getDesignatorOrExpr().struct.assignableTo(designatorObj.getType())){
            report_error("Greska na liniji " + assignement.getLine() + ": Tip koji hocemo da dodelimo nije adekvatan.", assignement);
        }
    }

    public void visit(FunctionCall functionCall) {
        Obj methodObj = functionCall.getDesignator().obj;
        if (methodObj.getKind() != Obj.Meth) {
            report_error("Greska na liniji " + functionCall.getLine() + ": Poziv ne adekvatne metode.", functionCall);
            return;
        }
        List<Struct> actualParams = getActualParams(functionCall.getOptionalActPars());
        List<Struct> formalParams = getFormalParams(methodObj);
        if (actualParams.size() != formalParams.size()) {
            report_error("Greska na liniji " + functionCall.getLine() + ": Broj argumenata se ne poklapa. Ocekivano " + formalParams.size() + ", dobijeno " + actualParams.size() + ".", functionCall);
        } else {
            for (int i = 0; i < actualParams.size(); i++) {
                if (!actualParams.get(i).assignableTo(formalParams.get(i))) {
                    report_error("Greska na liniji " + functionCall.getLine() + ": Tip argumenta " + (i + 1) + " nije kompatibilan.", functionCall);
                }
            }
        }
    }

    public void visit(Inc inc){
        Obj designatorObj = inc.getDesignator().obj;
        int designatorKind = designatorObj.getKind();
        if(designatorKind != Obj.Var && designatorKind != Obj.Elem){
            report_error("Greska na liniji " + inc.getLine() + ": Inkrement neadekvatne promenljive.", inc);
        } else if(!designatorObj.getType().equals(Tab.intType)){
            report_error("Greska na liniji " + inc.getLine() + ": Inkrement ne int promenljive.", inc);
        }
    }

    public void visit(Dec dec){
        Obj designatorObj = dec.getDesignator().obj;
        int designatorKind = designatorObj.getKind();
        if(designatorKind != Obj.Var && designatorKind != Obj.Elem){
            report_error("Greska na liniji " + dec.getLine() + ": Dekrement neadekvatne promenljive.", dec);
        } else if(!designatorObj.getType().equals(Tab.intType)){
            report_error("Greska na liniji " + dec.getLine() + ": Dekrement ne int promenljive.", dec);
        }
    }

    public void visit(UnionStmt unionStmt){
        Obj des1 = unionStmt.getDesignator().obj;
        Obj des2 = unionStmt.getDesignator1().obj;

        if (des1 == Tab.noObj || des2 == Tab.noObj) {
            unionStmt.struct = Tab.noType;
            return;
        }

        Struct type1 = des1.getType();
        Struct type2 = des2.getType();

        if (!type1.equals(setStruct) || !type2.equals(setStruct)) {
            report_error("Greska na liniji " + unionStmt.getLine() + ": Oba operanda u uniji moraju biti skupovi.", unionStmt);
            unionStmt.struct = Tab.noType;
        } else {
            unionStmt.struct = setStruct;
        }
    }

    public void visit(NoReturnExpr noReturnExpr){
        noReturnExpr.struct = Tab.noType;
    }

    public void visit(ReturnExpr returnExpr){
        returnExpr.struct = returnExpr.getExpr().struct;
    }

    public void visit(Break beakStmt){
        if(doWhileCnt == 0){
            report_error("Greska na liniji " + beakStmt.getLine() + ": Break naredba van tela doWhile petlje.", beakStmt);
        }
    }

    public void visit(Continue continueStmt){
        if(doWhileCnt == 0){
            report_error("Greska na liniji " + continueStmt.getLine() + ": Continue naredba van tela doWhile petlje.", continueStmt);
        }
    }

    public void visit(Return returnStmt){
        if(currMethodObj == null){
            report_error("Greska na liniji " + returnStmt.getLine() + ": Poziv return naredbe van funkcije.", returnStmt);
        } else {
            returnFound = true;
            if(!returnStmt.getOptionalExpr().struct.equals(currMethodObj.getType())){
                report_error("Greska na liniji " + returnStmt.getLine() + ": Return vraca ne ekvivalentni tip sa povratnim tipom funkcije.", returnStmt);
            }
        }
    }

    public void visit(Read read){
        Obj designatorObj = read.getDesignator().obj;
        int designatorKind = designatorObj.getKind();
        Struct designatorType = designatorObj.getType();

        if(designatorKind != Obj.Var && designatorKind != Obj.Elem){
            report_error("Greska na liniji " + read.getLine() + ": Read neadekvatne promenljive.", read);
        } else if(!designatorType.equals(Tab.intType) && !designatorType.equals(Tab.charType) && !designatorType.equals(boolStruct)){
            report_error("Greska na liniji " + read.getLine() + ": Read ne int,char ili bool promenljive.", read);
        }
    }

    public void visit(Print print){
        Struct expretionType = print.getExpr().struct;

        if(!expretionType.equals(Tab.intType) && !expretionType.equals(Tab.charType) && !expretionType.equals(boolStruct) && !expretionType.equals(setStruct)){
            report_error("Greska na liniji " + print.getLine() + ": Print ne int,char,bool ili set izraza.", print);
        }
    }

    public void visit(BeginDoWhile beginDoWhile){
        doWhileCnt++;
    }

    public void visit(DoWhile doWhile){
        doWhileCnt--;
    }

    ////////////  Statement  ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public void visit(NoRelops noRelops){
        if(noRelops.getExpr().struct != boolStruct){
            report_error("Greska na liniji " + noRelops.getLine() + ": Logicki operand nije tipa bool.", noRelops);
            noRelops.struct = Tab.noType;
        } else{
            noRelops.struct = boolStruct;
        }
    }

    public void visit(Relops relops){
        if(!relops.getExpr().struct.compatibleWith(relops.getExpr1().struct)){
            report_error("Greska na liniji " + relops.getLine() + ": Operandi nisu kompatibilni za relacione operacije.", relops);
            relops.struct = Tab.noType;
        } else{
            if(relops.getExpr().struct.isRefType() || relops.getExpr1().struct.isRefType()){
                Relop r = relops.getRelop();
                if(r instanceof Eq || r instanceof Ne){
                    relops.struct = boolStruct;
                }
                else{
                    report_error("Greska na liniji " + relops.getLine() + ": Za operande tipa Array mogu da se koriste samo != i == relacioni operatori.", relops);
                    relops.struct = Tab.noType;
                }
            }else {
                relops.struct = boolStruct;
            }
        }
    }

    public void visit(CondFact condFact){
        condFact.struct = condFact.getOptionalRelop().struct;
    }

    public void visit(ConditionFacts conditionFacts){
        conditionFacts.struct = conditionFacts.getCondFact().struct;
    }

    public void visit(AndConditions andConditions){
        if(andConditions.getCondFact().struct != boolStruct || andConditions.getCondFactList().struct != boolStruct){
            report_error("Greska na liniji " + andConditions.getLine() + ": AND operacija ne bool tipa.", andConditions);
            andConditions.struct = Tab.noType;
        } else{
            andConditions.struct = boolStruct;
        }
    }

    public void visit(ConditionTerm conditionTerm){
        conditionTerm.struct = conditionTerm.getCondFactList().struct;
    }

    public void visit(ConditionTerms conditionTerms){
        conditionTerms.struct = conditionTerms.getCondTerm().struct;
    }

    public void visit(OrConditions orConditions){
        if(orConditions.getCondTerm().struct != boolStruct || orConditions.getCondTermList().struct != boolStruct){
            report_error("Greska na liniji " + orConditions.getLine() + ": OR operacija ne bool tipa.", orConditions);
            orConditions.struct = Tab.noType;
        } else{
            orConditions.struct = boolStruct;
        }
    }

    public void visit(Condition condition){
        condition.struct = condition.getCondTermList().struct;
        if(condition.getCondTermList().struct != boolStruct){
            report_error("Greska na liniji " + condition.getLine() + ": Uslov nije bool tipa.", condition);

        }
    }
}
