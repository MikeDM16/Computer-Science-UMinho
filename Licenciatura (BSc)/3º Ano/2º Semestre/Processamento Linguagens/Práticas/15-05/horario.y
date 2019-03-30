%{
#define _GNU_SOURCE
#include <stdio.h>
void yyerror(char* s);
void validar_duracao(int i, int f);
void validar_sobreposicao(int f, int i);
int yylex();
%}

%token DIA HORA DISC
%union { char* s; int i; struct{ int ini, fim;}h; }
%type <s> DISC
%type <i> DIA HORA
%type <h> aula aulas

%%
horario : hdia horario
		| '\n' horario
		|
		;
hdia :  DIA '\n' aulas
	 ;
aulas : aulas aula   { validar_sobreposicao($1.fim, $2.ini);
					   $$.ini = $1.ini; $$.fim = $2.fim;}
	  | 			 { $$.ini = 24; $$.fim = 24; }
	  ;

aula : HORA HORA DISC '\n'    { validar_duracao($1,$2); 
								$$.ini = $1; $$.fim = $2; 
							  }
	 ; 
%%

#include "lex.yy.c"
void yyerror(char* s){
	fprintf(stderr, "%d: %s\n\t%s\n", yylineno, yytext, s);
}

void validar_sobreposicao(int fim, int ini){	
	if( fim > ini)
		yyerror("Sobreposição atividades");
}
void validar_duracao(int inicio, int fim){
	if(inicio > fim)
		yyerror("Duração inválida");	
}

int main(int argc, char* argv[]){
	yyparse();

	return 0;
}