%{
#include "main.hpp"
#include <iostream>
#include <set>
#include <vector>


#include "parser.hpp"


extern int yylex();
void yyerror(YYLTYPE* loc, const char* err);
std::string* translate_boolean_str(std::string* boolean_str);

/*
 * Here, target_program is a string that will hold the target program being
 * generated, and symbols is a simple symbol table.
 */
Node* target_program;
std::set<std::string> symbols;
%}

/* Enable location tracking. */
%locations


/*
 * All program constructs will be represented as strings, specifically as
 * their corresponding C/C++ translation.
 */
/*%define api.value.type {union YYSTYPE}*/

/*
 * Because the lexer can generate more than one token at a time (i.e. DEDENT
 * tokens), we'll use a push parser.
 */
%define api.pure full
%define api.push-pull push

%union {
  std::string* str;
  Node* node;
  std::vector<Node*>* ch;
}

/*
 * Since we've unioned everything we can just assgn types like in piazza @57
 */

%type<node>program statement
%type<ch> statements
%type<node>assign_statement while_statement
%type<node>primary_expression BREAK
%type<str>IDENTIFIER FLOAT INTEGER BOOLEAN
%type<node>expression negated_expression
%type<node>if_statement elif_blocks else_block block break_statement
%type<ch> condition

/*
 * These are all of the terminals in our grammar, i.e. the syntactic
 * categories that can be recognized by the lexer.
 */
%token IDENTIFIER
%token FLOAT INTEGER BOOLEAN
%token INDENT DEDENT NEWLINE
%token AND BREAK DEF ELIF ELSE FOR IF NOT OR RETURN WHILE
%token ASSIGN PLUS MINUS TIMES DIVIDEDBY
%token EQ NEQ GT GTE LT LTE
%token LPAREN RPAREN COMMA COLON

/*
 * Here, we're defining the precedence of the operators.  The ones that appear
 * later have higher precedence.  All of the operators are left-associative
 * except the "not" operator, which is right-associative.
 */
%left OR
%left AND
%left PLUS MINUS
%left TIMES DIVIDEDBY
%left EQ NEQ GT GTE LT LTE
%right NOT

/* This is our goal/start symbol. */
%start program

%%

/*
 * Each of the CFG rules below recognizes a particular program construct in
 * Python and creates a new string containing the corresponding C/C++
 * translation.  Since we're allocating strings as we go, we also free them
 * as we no longer need them.  Specifically, each string is freed after it is
 * combined into a larger string.
 */
program
  : statements { target_program = new Node("", "Block");
                target_program->children.insert(target_program->children.end(), (*$1).begin(), (*$1).end());}
  ;

statements
  : statement { std::vector<Node*>* temp = new std::vector<Node*> (1, $1); $$ = temp; }
  | statements statement { (*$1).push_back($2); $$ = $1;}
  ;

statement
  : assign_statement { $$ = $1; }
  | if_statement { $$ = $1; }
  | while_statement { $$ = $1; }
  | break_statement { Node* temp = new Node("", "Break");
                        $$ = temp; }
  ;

primary_expression
  : IDENTIFIER { $$ = new Node("box", std::string("Identifier: " + *$1)); }
  | FLOAT { $$ = new Node("box", std::string("Float: " + *$1));  }
  | INTEGER { $$ = new Node("box", std::string("Integer: " + *$1)); }
  | BOOLEAN { $$ = new Node("box", std::string("Boolean: " + *$1));}
  | LPAREN expression RPAREN { $$ = $2; }
  ;

negated_expression
  : NOT primary_expression { Node* temp = new Node("", "NOT");
                             temp->children.push_back($2);
                            $$ = temp;}
  ;

expression
  : primary_expression { $$ = $1; }
  | negated_expression { $$ = $1; }
  | expression PLUS expression { Node* temp = new Node("", "PLUS");
                                temp->children.push_back($1);
                                temp->children.push_back($3);
                                $$ = temp;}
  | expression MINUS expression { Node* temp = new Node("", "MINUS");
                                temp->children.push_back($1);
                                temp->children.push_back($3);
                                $$ = temp;}
  | expression TIMES expression { Node* temp = new Node("", "TIMES");
                                temp->children.push_back($1);
                                temp->children.push_back($3);
                                $$ = temp;}
  | expression DIVIDEDBY expression { Node* temp = new Node("", "DIVIDEDBY");
                                temp->children.push_back($1);
                                temp->children.push_back($3);
                                $$ = temp;}
  | expression EQ expression { Node* temp = new Node("", "EQ");
                                temp->children.push_back($1);
                                temp->children.push_back($3);
                                $$ = temp;}
  | expression NEQ expression { Node* temp = new Node("", "NEQ");
                                temp->children.push_back($1);
                                temp->children.push_back($3);
                                $$ = temp;}
  | expression GT expression { Node* temp = new Node("", "GT");
                                temp->children.push_back($1);
                                temp->children.push_back($3);
                                $$ = temp;}
  | expression GTE expression { Node* temp = new Node("", "GTE");
                                temp->children.push_back($1);
                                temp->children.push_back($3);
                                $$ = temp;}
  | expression LT expression { Node* temp = new Node("", "LT");
                                temp->children.push_back($1);
                                temp->children.push_back($3);
                                $$ = temp;}
  | expression LTE expression { Node* temp = new Node("", "LTE");
                                temp->children.push_back($1);
                                temp->children.push_back($3);
                                $$ = temp;}
  ;

assign_statement
  : IDENTIFIER ASSIGN expression NEWLINE { Node* temp = new Node("", "Assignment");
                                            Node* idnt_temp = new Node("box", std::string("Identifier: " + *$1));
                                            temp->children.push_back(idnt_temp);
                                            temp->children.push_back($3);
                                          $$ = temp;}
  ;
block
  : INDENT statements DEDENT { Node* temp = new Node("", "Block");
                                temp->children = std::vector<Node*>((*$2).begin(),(*$2).end());
                              $$ = temp;}
  ;

condition
  : expression { $$ = new std::vector<Node*>(1, $1); }
  | condition AND condition { Node* temp = new Node("","AND");
                              temp->children = std::vector<Node*>((*$1).begin(),(*$1).end());
                              temp->children.insert(temp->children.end(), (*$3).begin(), (*$3).end());
                              $$ = new std::vector<Node*> (1,temp);}
  | condition OR condition { Node* temp = new Node("","AND");
                              temp->children = std::vector<Node*>((*$1).begin(),(*$1).end());
                              temp->children.insert(temp->children.end(), (*$3).begin(), (*$3).end());
                              $$ = new std::vector<Node*> (1,temp);}
  ;

if_statement
  : IF condition COLON NEWLINE block elif_blocks else_block { Node* temp = new Node("", "If");
                                            temp->children = *$2;
                                            temp->children.push_back($5);
                                            temp->children.push_back($6);
                                            temp->children.push_back($7);
                                            $$ = temp;
                                          }
  ;

elif_blocks
  : %empty { $$ = new Node("",""); }
  | elif_blocks ELIF condition COLON NEWLINE block { Node* temp = new Node("", "Elif");
                                            temp->children = *$3;
                                            temp->children.push_back($1);
                                            temp->children.push_back($6);
                                            $$ = temp;
                                          }
  ;

else_block
  : %empty { $$ = new Node("",""); }
  | ELSE COLON NEWLINE block { Node* temp = new Node("", "Elif");
                                            temp->children.push_back($4);
                                            $$ = temp;
                                          }
  ;

while_statement
  : WHILE condition COLON NEWLINE block {  Node* temp = new Node("", "Elif");
                                            temp->children = *$2;
                                            temp->children.push_back($5);
                                            $$ = temp;
                                          }
  ;

break_statement
  : BREAK NEWLINE { $$ = $1; }
  ;

%%

void yyerror(YYLTYPE* loc, const char* err) {
  std::cerr << "Error (line " << loc->first_line << "): " << err << std::endl;
}

/*
 * This function translates a Python boolean value into the corresponding
 * C++ boolean value.
 */
std::string* translate_boolean_str(std::string* boolean_str) {
  if (*boolean_str == "True") {
    return new std::string("true");
  } else {
    return new std::string("false");
  }
}
