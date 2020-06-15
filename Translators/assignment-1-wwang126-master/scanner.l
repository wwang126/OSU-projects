/*Flex Scanner for Scanning Python Code */

%{
  #include <stdlib.h>
%}

%option noyywrap

%%
 /*Whitespace Removal*/
" "       {}
 /*Keywords*/
"and"     { printf("AND %s\n", yytext); }
"break"   { printf("BREAK %s\n", yytext);}
"def"     { printf("DEF %s\n", yytext); }
"elif"    { printf("ELIF %s\n", yytext);}
"else"    { printf("ELSE %s\n", yytext);}
"for"     { printf("FOR %s\n", yytext);}
"if"      { printf("IF %s\n", yytext);}
"not"     { printf("NOT %s\n", yytext);}
"or"      { printf("OR %s\n", yytext);}
"return"  { printf("RETURN %s\n", yytext);}
"while"   { printf("WHILE %s\n", yytext);}
 /*Operators*/
"="   { printf("ASSIGN %s\n", yytext);}
"+"   { printf("PLUS %s\n", yytext);}
"-"   { printf("MINUS %s\n", yytext);}
"*"   { printf("TIMES %s\n", yytext);}
"/"   { printf("DIVIDEDBY %s\n", yytext);}
"=="  { printf("EQ %s\n", yytext);}
"!="  { printf("NEQ %s\n", yytext);}
">"   { printf("GT %s\n", yytext);}
">="  { printf("GTE %s\n", yytext);}
"<"   { printf("LT %s\n", yytext);}
"<="  { printf("LTE %s\n", yytext);}
 /*Punctuation*/
"("   { printf("LPAREN %s\n", yytext);}
")"   { printf("RPAREN %s\n", yytext);}
","   { printf("COMMA %s\n", yytext);}
":"   { printf("COLON %s\n", yytext);}
 /*Boolean checks*/
"True"  { printf("BOOLEAN 1\n", yytext);}
"False" { printf("BOOLEAN 0\n", yytext);}
 /*Comment check*/
\n#.*   {}
#.*\n    {}
 /*Identifier check*/
[A-Za-z_][A-Za-z_]* { printf("IDENTIFIER %s\n", yytext);}
 /*Float check*/
-?[0-9]*\.[0-9]+    { printf("FLOAT %f\n", atof(yytext));}
  /*Integer check*/
-?[0-9]+             { printf("INTEGER %d\n", atoi(yytext));}
 /*New Line reader*/
\n {printf("NEWLINE\n");}
%%
int main (int argc, char **argv){
  yylex();
  return 0;
}
