%{
#include <iostream>
#include <map>
#include <list>

#include "parser.hpp"

bool _error = false;
std::map<std::string, float> symbols;
std::list<std::string> output;
extern int yylex();
void yyerror (YYLTYPE* loc, const char* err){
  std::cerr << "Error (line " << loc->first_line << "): " << err << std::endl;
}
%}

%locations
%define parse.error verbose

%define api.pure full
%define api.push-pull push

%union {
  std::string* str;
  int category;
}

/* define identifiers and numbers */
%token <str> IDENTIFIER
%token <str> NUMBER

/* define categories */

%token <category> INDENT DEDENT NEWLINE
%token <category> AND BREAK DEF ELIF ELSE FOR IF NOT OR RETURN WHILE
%token <category> TRUE FALSE
%token <category> ASSIGN PLUS MINUS TIMES DIVIDEBY
%token <category> EQ NEQ GT GTE LT LTE
%token <category> LPAREN RPAREN
%token <category> COMMA COLON

/* set types of expression */
%type <str> expression
%type <str> statement
%type <str> conditional
%left PLUS MINUS
%left TIMES DIVIDEBY

%start program

%%
program
  : program statement {output.push_back(*$2);}
  | statement { output.push_back(*$1);}
  ;
statement
  : IDENTIFIER ASSIGN expression NEWLINE { symbols [*$1] = $3; delete $1;}
  | error NEWLINE { std::cerr << "Error: bad statement on line " << @1.first_line << std::endl; _error = true; }
  | IF LPAREN conditional RPAREN
  | ELIF LPAREN conditional RPAREN
  | WHILE LPAREN conditional RPAREN
  | IF conditional
  | ELIF conditional
  | WHILE conditional
  | BREAK NEWLINE
  ;
conditional
  : conditional EQ condtional
  | conditional NEQ condtional
  | conditional GT condtional
  | conditional GTE condtional
  | conditional LT condtional
  | conditional LTE condtional
  | expression
  ;
expression
  : expression PLUS expression { $$ = $1 + $3;}
  | expression MINUS expression { $$ = $1 + $3;}
  | expression TIMES expression { $$ = $1 * $3;}
  | expression DIVIDEBY expression {$$ = $1 / $3;}
  | LPAREN expression RPAREN {}
  | NUMBER {$$ = symbols[*$1]; delete $1; }
  | IDENTIFIER {$$ = symbols[*$1]; delete $1; }
  ;
%%

/*Epilogue*/
