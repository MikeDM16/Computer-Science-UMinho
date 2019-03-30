%{
#define _GNU_SOURCE
#include <stdio.h>
#include <math.h>
void yyerror(char* s);
int yylex();
%}

%token NUM
%union { double d; }
%type <d> NUM exp p fat es

%%
//cal : exp '\n' cal     { printf("Resultado:%f\n", $1); }
cal : cal exp '\n'     { printf("Resultado:%f\n", $2); }
	| 
	;

exp : exp '+' p     { $$ = $1 + $3; }
	| exp '-' p     { $$ = $1 - $3; }
	| p             { $$ = $1; }
	;

p   : p '*' fat     { $$ = $1 * $3; }
	| p '/' fat     { $$ = $1 / $3; }
	| fat           { $$ = $1; }
	;

fat : es '^' fat    { $$ = pow($1, $3); }
 	| '-' es		{ $$ = $1; }
 	;

es : '(' exp ')'   { $$ = $2; }
   | NUM           { $$ = $1; }
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