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

%token Pal Num 
%union { char* s; int i; char* imprime; struct{ int ini, fim;}h; }
%type <s> Pal
%type <i> Num
%type <h> aula aulas

%%
linguagem: Comandos
		 ;
Comandos : Comando Resto
		 ;
Resto : Comando Comandos
	  |
	  ;
Comando : Declaracao
		| Atribuição
		| Operacao
		| InOut
		;
//Declaração só de int ou arrays de int
Declaracao : Variavel
		   | Array
		   ;
Variavel : Pal
		 ;
Array : Pal '[' Num ']'
	  ;
//Atribuição
Atribuição : Variavel '=' Num ';'
		   | Pal '=' '{' Num restoArray
      	   ;
RestoArray : ',' Num RestoArray
		   | '}'
		   ;

Operacao : opAritmetica
	   	 | opRelacao
	   	 | opLogica
	   	 ;

opAritmetica: Soma 
	        | Subtracao
			| Multiplicao 
			| Divisao
		 	;
opRelacao : opIgual
		  | opDif
		  | opMenor
		  | opMaior
		  | opMenorI
		  | opMaiorI
		  ;
opLogica : opOR
		 | opAND
		 ; 
// ---- - Operacao Aritmetica -----
//	SOMA	 	
Soma : Valor '+' RestoSoma  
	 ;
RestoSoma : Valor ';'   
		  | Valor '+' RestoSoma  
		  ;
//Subtração
Subtracao : Valor '-' RestoSub 
	      ;
RestoSub : Valor ';'  
		 | Valor '-' RestoSub  
		 ;
//Multiplicação 
Multiplicao : Valor '*' RestoMult 
	 		;
RestoMult : Valor ';' 
		  | Valor '*' RestoMult  
		  ;
//Divisão
Divisao : Valor '/' RestoDiv
		;
RestoDiv : Valor ';' 
		 | Valor '/' RestoDiv  
		 ;
Valor : Pal
	  | Num
	  ;
// ---- - Operacao Relacional -----
opIgual : Valor '==' Valor ';'
		;
opDif : Valor '!=' Valor ';'
	  ;
opMenor : Valor '<' Valor ';'
	  	;
opMaior : Valor '>' Valor ';'
	  	;
opMenorI : Valor '<=' Valor ';'
	  	 ;
opMaiorI : Valor '>=' Valor ';'
	  	 ;
// ---- - Operacao Logicas -----
opAND : 
	  ;
opOR :
	 ;

InOut : Input
	  | Output
	  ;

Input : Pal;    {  atoi(Pal); } 
	  ;
Output : 
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
PRINTF ssdfsd      { escreveAssembly("pushs" + imprime);
					escreveAssembly("write"); 
					}
%%

#include "lex.yy.c"
void yyerror(char* s){
	fprintf(stderr, "%d: %s\n\t%s\n", yylineno, yytext, s);
}

void escrever_html(){
	printf("... <table>\n");
	
	printf("<tr>\n<th>horas</th>\n<th>2F</th>\n<th>3F</th>\n<th>4F</th>\n<th>5F</th>\n<th>6F</th>\n</tr>");
	for(i = 0; i<12; i++){
		printf("<tr><th>%d</th>\n%s\n</tr>", i+8, tds[i]);
	}

	printf("...</table>\n");
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