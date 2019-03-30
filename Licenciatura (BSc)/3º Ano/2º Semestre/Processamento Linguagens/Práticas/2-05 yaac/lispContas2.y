%{
#define _GNU_SOURCE
#include <stdio.h>
void yyerror(char* s);
int yylex();
%}

%token ID NUM SExp
%union { char* s; int i; void* l; }
%type <s> ID
%type <i> NUM 
%type <s> sexp
%type <s> lista
%%
Lisp: sexp { printf(" = %s\n", $1); }
	;
sexp: '(' ID lista ')'  { $$ = calc($2, $3); }
	| NUM { $$ = $1; }
//	| ID  { $$ = $1; }
	;
lista:  { $$ = NULL; }
	 | sexp lista { $$ = cons($1, $2); }
	 ;
// cons faz o alloc e preenche
%%


#include "lex.yy.c"
void yyerror(char* s){
	fprintf(stderr, "%d: %s\n\t%s\n", yylineno, yytext, s);
}

int main(int argc, char* argv[]){
	yyparse();

	return 0;
}