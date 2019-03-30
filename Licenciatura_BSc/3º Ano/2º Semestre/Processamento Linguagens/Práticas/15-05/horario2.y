%{
#define _GNU_SOURCE
#include <stdio.h>
void yyerror(char* s);
void validar_duracao(int i, int f);
void validar_sobreposicao(int f, int i);
void escrever_html();
int yylex();
int finais[12] = { [0...11] = 8  };
char tds[12][200]  = { [0...11] = "" };
%}

%token DIA HORA DISC
%union { char* s; int i; struct{ int ini, fim;}h; }
%type <s> DISC
%type <i> DIA HORA
%type <h> aula aulas

%%
axioma: horarios    {escrever_html();}
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

aula : HORA HORA DISC '\n'    { addtd($1, $2, $3); 
								validar_duracao($1,$2); 
								$$.ini = $1; $$.fim = $2; 
							  }
	 ; 
%%

#include "lex.yy.c"
void yyerror(char* s){
	fprintf(stderr, "%d: %s\n\t%s\n", yylineno, yytext, s);
}

void escrever_html(){
	
}

void addtd(int ini, int fim, char* disc){
	char* tdsrt[100];
	sprintf(tdstr, "<td spanrow='%d'>%s</td>", fim-ini, disc);
	strcat(tds[ini-8], tdstr);
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