package rs.ac.bg.etf.pp1;

import java_cup.runtime.Symbol;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import rs.ac.bg.etf.pp1.ast.Program;
import rs.ac.bg.etf.pp1.util.Log4JUtils;
import rs.etf.pp1.mj.runtime.Code;
import rs.etf.pp1.symboltable.Tab;

import java.io.*;
import java.nio.file.Files;

public class Compiler {
    static {
        DOMConfigurator.configure(Log4JUtils.instance().findLoggerConfigFile());
        Log4JUtils.instance().prepareLogFile(Logger.getRootLogger());
    }

    public static void main(String[] args) throws Exception {

        Logger log = Logger.getLogger(Compiler.class);

        Reader br = null;
        try {
            File sourceCode = new File("test/program.mj");

            log.info("Compiling source file: " + sourceCode.getAbsolutePath());
//			log.info("====================================================================");
//			log.info("========================= LEKSICKA ANALIZA =========================");
//			log.info("====================================================================");

            log.info("=====================================================================");
            log.info("========================= SINTAKSNA ANALIZA =========================");
            log.info("=====================================================================");

            br = new BufferedReader(new FileReader(sourceCode));
            Yylex lexer = new Yylex(br);

            MJParser p = new MJParser(lexer);
            Symbol s = p.parse();  //pocetak parsiranja

            Program prog = (Program)(s.value);

            // ispis sintaksnog stabla
            log.info("========================= SINTAKSNO STABLO =========================");
            log.info(prog.toString(""));


            log.info("=====================================================================");
            log.info("========================= SEMANTICKA ANALIZA =========================");
            log.info("=====================================================================");

            // ispis prepoznatih programskih konstrukcija
            SemanticAnalyzer v = new SemanticAnalyzer();
            prog.traverseBottomUp(v);

            MyDumpSymbolTableVisitor visitor = new MyDumpSymbolTableVisitor();
            Tab.dump(visitor);

            log.info(!p.errorDetected);
            log.info(v.passed());

            if(!p.errorDetected && v.passed()){
                //generisanje koda
                File objFile = new File("test/program.obj");
                if(objFile.exists()){
                    objFile.delete();
                }

                CodeGenerator cg = new CodeGenerator();
                prog.traverseBottomUp(cg);
                Code.dataSize = v.nVars;
                Code.mainPc = cg.getMainPc();
                Code.write(Files.newOutputStream(objFile.toPath()));
                //Code.write(new FileOutputStream(objFile));

                log.info("Generisanje uspesno zavrseno!");
            }else{
                log.error("Parsiranje NIJE uspesno zavrseno!");
            }

        }
        finally {
            if (br != null) try { br.close(); } catch (IOException e1) { log.error(e1.getMessage(), e1); }
        }

    }
}
