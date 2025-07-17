
package rs.ac.bg.etf.pp1;

import java_cup.runtime.Symbol;
import org.apache.log4j.Logger;



%%

%{
    private static final Logger log = Logger.getLogger("Yylex");

	// ukljucivanje informacije o poziciji tokena
	private Symbol new_symbol(int type) {
		return new Symbol(type, yyline+1, yycolumn);
	}
	
	// ukljucivanje informacije o poziciji tokena
	private Symbol new_symbol(int type, Object value) {
		return new Symbol(type, yyline+1, yycolumn, value);
	}

%}

%cup
%line
%column

%xstate COMMENT

%eofval{
	return new_symbol(sym.EOF);
%eofval}

%%

" " 	{ }
"\b" 	{ }
"\t" 	{ }
"\r\n" 	{ }
"\f" 	{ }

"program"   { return new_symbol(sym.PROG, yytext());}
"break"     { return new_symbol(sym.BREAK, yytext()); }
"class"     { return new_symbol(sym.CLASS, yytext()); }
"else"      { return new_symbol(sym.ELSE, yytext()); }
"const"     { return new_symbol(sym.CONST, yytext()); }
"if"        { return new_symbol(sym.IF, yytext()); }
"new"       { return new_symbol(sym.NEW, yytext()); }
"print"     { return new_symbol(sym.PRINT, yytext()); }
"read"      { return new_symbol(sym.READ, yytext()); }
"return"    { return new_symbol(sym.RETURN, yytext()); }
"void"      { return new_symbol(sym.VOID, yytext()); }
"extends"   { return new_symbol(sym.EXTENDS, yytext()); }
"continue"  { return new_symbol(sym.CONTINUE, yytext()); }
"union"     { return new_symbol(sym.UNION, yytext()); }
"do"        { return new_symbol(sym.DO, yytext()); }
"while"     { return new_symbol(sym.WHILE, yytext()); }
"map"       { return new_symbol(sym.MAP, yytext()); }
"interface" { return new_symbol(sym.INTERFACE, yytext()); }
"+"       { return new_symbol(sym.PLUS, yytext()); }
"-"       { return new_symbol(sym.MINUS, yytext()); }
"*"       { return new_symbol(sym.MULTIPLY, yytext()); }
"/"       { return new_symbol(sym.DIVIDE, yytext()); }
"%"       { return new_symbol(sym.MOD, yytext()); }
"=="      { return new_symbol(sym.EQ, yytext()); }
"!="      { return new_symbol(sym.NE, yytext()); }
">"       { return new_symbol(sym.GT, yytext()); }
">="      { return new_symbol(sym.GE, yytext()); }
"<"       { return new_symbol(sym.LT, yytext()); }
"<="      { return new_symbol(sym.LE, yytext()); }
"&&"      { return new_symbol(sym.AND, yytext()); }
"||"      { return new_symbol(sym.OR, yytext()); }
"="       { return new_symbol(sym.ASSIGN, yytext()); }
"++"      { return new_symbol(sym.INC, yytext()); }
"--"      { return new_symbol(sym.DEC, yytext()); }
";"       { return new_symbol(sym.SEMI, yytext()); }
":"       { return new_symbol(sym.COLON, yytext()); }
","       { return new_symbol(sym.COMMA, yytext()); }
"."       { return new_symbol(sym.DOT, yytext()); }
"("       { return new_symbol(sym.LPAREN, yytext()); }
")"       { return new_symbol(sym.RPAREN, yytext()); }
"["       { return new_symbol(sym.LBRACKET, yytext()); }
"]"       { return new_symbol(sym.RBRACKET, yytext()); }
"{"       { return new_symbol(sym.LBRACE, yytext()); }
"}"		 { return new_symbol(sym.RBRACE, yytext()); }

"//"         { yybegin(COMMENT); }
<COMMENT>.   { yybegin(COMMENT); }
<COMMENT>\n  { yybegin(YYINITIAL); }

[0-9]+  { return new_symbol(sym.NUMBERCONST, Integer.parseInt(yytext())); }
"true"   { return new_symbol(sym.BOOLCONST, Boolean.valueOf(yytext())); }
"false"  { return new_symbol(sym.BOOLCONST, Boolean.valueOf(yytext())); }
"'"([^'\\\\]|\\\\[btnfr\"'\\\\])"'"   { return new_symbol(sym.CHARCONST, yytext().charAt(1)); }

([a-zA-Z])([a-zA-Z0-9_])* { return new_symbol(sym.IDENT, yytext()); }

. { log.error("Leksicka greska ("+yytext()+") na liniji "+(yyline+1)+", na koloni "+(yycolumn+1)); }










