%{
#include <stdio.h>

void yyerror(char* s);
int yylex();
int soma = 0;
%}

%token NOME NUM
%union { char* s; int i; }
%type <s> NOME
%type <i> NUM

%%
cla: NOME notas '.'   {printf("%s -> %d.\n",$1, soma);}
   ;
notas: NUM    { soma += $1; }
	 | NUM ',' notas   { soma += $1; }
	 ;
%%
#include "lex.yy.c"
void yyerror(char* s){
	fprintf(stderr, "%d: %s", yylineno, s);
}


int main(int argc, char* argv[]){
	yyparse();

	return 0;
}