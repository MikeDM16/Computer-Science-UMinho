%{
#include <stdio.h>

void yyerror(char* s);
int yylex();
%}

%token ID NUM SExp
%union { char* s; int i; }
%type <s> ID
%type <i> NUM 
%type <i> sexp
%type <i> lista
%%
Lisp: sexp {printf("Profundidade: %d\n", $1); }
	;
sexp: '(' lista ')'  { $$ = $2 + 1; }
	| NUM { $$ = 0; }
	| ID  { $$ = 0; }
	;
lista: { $$ = 0; }
	 | sexp lista { $$ = ($1 > $2)? $1 : $2; }
	 ;

%%
#include "lex.yy.c"
void yyerror(char* s){
	fprintf(stderr, "%d: %s\n\t%s\n", yylineno, yytext, s);
}


int main(int argc, char* argv[]){
	yyparse();

	return 0;
}