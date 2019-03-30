%{
#define _GNU_SOURCE
#include <stdio.h>
void trim(char* s);
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
Lisp: sexp { printf("%s\n", $1); }
	;
sexp: '(' ID lista ')'  { trim($3); asprintf(&$$, "%s(%s)", $2, $3); }
	| NUM { asprintf(&$$, "%d", $1); }
	| ID  { asprintf(&$$, "%s", $1); }
	;
lista:  { asprintf(&$$, " "); }
	 | sexp lista { asprintf(&$$, "%s, %s", $1, $2); }
	 ;

%%
#include "lex.yy.c"
void yyerror(char* s){
	fprintf(stderr, "%d: %s\n\t%s\n", yylineno, yytext, s);
}

void trim(char* s){
	s[strlen(s)-2] = '\0';	
}

int main(int argc, char* argv[]){
	yyparse();

	return 0;
}