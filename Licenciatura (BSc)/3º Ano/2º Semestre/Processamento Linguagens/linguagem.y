%{

#define _GNU_SOURCE
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <sys/types.h>
#include <fcntl.h>
#include <unistd.h>

#include "arraysDinamicos.h"

void yyerror(char*);
int yylex();

int indicesIf;
int indicesWhile;
int contaVariaveis;
char declarados[26];
char indices[26];
int indice;
char* strErr, *aux;
ARRAY_DINAMICO assembly; 

void escreve_assembly(ARRAY_DINAMICO arr);
%}


%union { char* s; int i; char c;}
%token <s> IF ELSE PROGRAMA string WHILE PRINT RETURN READ TIPO 
%token <c> '{' '}' ';' ',' '(' ')' '[' ']' variavel
%token <i> valor
%type <s> Decls RDecls Decl DeclsVar RDeclsVar Opcao
%type <s> Atribs RAtribs Atrib Codigos RCodigos Cond exp Conj RConj 
%type <s> Prog Inicio DeclVar Codigo termo




%%    

Prog        : PROGRAMA  Inicio '{' Decls Codigos '}'                     { insereElementoAD(assembly, "pushi 0\n");
                                                                           insereElementoAD(assembly, $4);
                                                                           insereElementoAD(assembly, "start\n");
                                                                           insereElementoAD(assembly, $5);
                                                                           insereElementoAD(assembly, "stop\n");
                                                                           contaVariaveis++;
                                                                           escreve_assembly(assembly);
                                                                           freeAD(assembly);
                                                                         }             
            ;
Inicio      :                                                            { contaVariaveis=1;
                                                                           indicesIf = 0;
                                                                           indicesWhile = 0;
                                                                           for(int i=0; i<26; i++){
                                                                              declarados[i] = -1;
                                                                              indices[i] = -1;
                                                                           }
                                                                           assembly = inicializaAD(100);
                                                                         }                                                             
            ;
Decls       : Decl RDecls                                                { int t = strlen($1) + strlen($2);
                                                                           $$ = (char*) malloc( t* sizeof(char));
                                                                           strcat($$, $1); strcat($$, $2);
                                                                         }   
            |                                                            { $$ = strdup(" "); }    
			      ;
RDecls	    : Decls                                                      { $$ = strdup($1); }    
	          ;
Decl        : TIPO DeclsVar ';'                                          { $$ = strdup($2); }    
            ;
DeclsVar    : DeclVar RDeclsVar                                          { int t = strlen($1) + strlen($2);
                                                                           $$ = (char*) malloc( t* sizeof(char));
                                                                           strcat($$, $1); strcat($$, $2);
                                                                         }     
            ;
RDeclsVar   : ',' DeclsVar                                               { $$ = strdup($2); }    
            |                                                            { $$ = strdup(" "); }    
            ;
DeclVar     : variavel                                                   { int indice = (int) yylval.c;
                                                                           if(declarados[indice-97] != (-1)){
                                                                             asprintf(&strErr, "Variavel %c já declarada.\n", $1);
                                                                             yyerror(strErr); free(strErr);
                                                                             exit(0);
                                                                           }else{
                                                                            indices[indice-97] = contaVariaveis;
                                                                            declarados[indice-97] = (1);
                                                                            $$ = strdup("pushi 0\n");
                                                                            contaVariaveis++;
                                                                           }
                                                                         }                                                                             
            | variavel '[' valor ']'                                     { int tamanho = $3;
                                                                           if(tamanho <= 0){
                                                                             asprintf(&strErr, "O tamanho do array deve ser >= 0.\n");
                                                                             yyerror(strErr); free(strErr);
                                                                             exit(0);
                                                                           }
                                                                           int indice = (int) ($1);
                                                                           if(declarados[indice-97] != (-1)){
                                                                             asprintf(&strErr, "Variavel %d já declarada.\n", $1);
                                                                             yyerror(strErr); free(strErr);
                                                                             exit(0);
                                                                           }else{
                                                                            indices[indice-97] = contaVariaveis;
                                                                            declarados[indice-97] = (1);
                                                                            asprintf(&aux, "pushn %d", tamanho);
                                                                            $$ = strdup(aux);
                                                                            contaVariaveis++;
                                                                            free(aux);
                                                                           }
                                                                         }     
            ;
Atribs      : Atrib RAtribs                                              { int t = strlen($1) + strlen($2);
                                                                           $$ = (char*) malloc( t* sizeof(char));
                                                                           strcat($$, $1); strcat($$, $2);
                                                                         }
            ;
RAtribs     : Atribs                                                     { $$ = strdup($1); }   
            |                                                            { $$ = strdup(" "); }
            ;
Atrib       : variavel '=' exp ';'                                       { int indice = (int) ($1);
                                                                           if(declarados[indice-97] == (-1)){
                                                                             asprintf(&strErr, "Variavel %d usada mas não declarada.", $1);
                                                                             yyerror(strErr); free(strErr);
                                                                             exit(0);
                                                                           }else{
                                                                            int t = strlen($3) + strlen("storeg N\n");
                                                                            $$ = (char*) malloc( t* sizeof(char));
                                                                            strcat($$, $3);
                                                                            asprintf(&aux, "storeg %d\n", indices[indice-97]);
                                                                            strcat($$, aux); free(aux);
                                                                           }
                                                                         }                  
            ;
Codigos     : Codigo RCodigos                                            { int t = strlen($1) + strlen($2);
                                                                           $$ = (char*) malloc( t* sizeof(char));
                                                                           strcat($$, $1); strcat($$, $2);
                                                                         }    
            ;
RCodigos    :                                                            { $$ = strdup(" "); }    
            | Codigos                                                    { int t = strlen($1);
                                                                           $$ = (char*) malloc( t* sizeof(char));
                                                                           strcat($$, $1);
                                                                         }     
            ;                                                        
Codigo      : PRINT string Opcao ';'                                     { asprintf(&aux, "pushs %s\nwrites\n", $2);
                                                                           $$ = strdup(aux); free(aux);                                                                            
                                                                         }          
            | READ variavel ';'                                          { int indice = (int) ($2);
                                                                           if(declarados[indice-97] == (-1)){
                                                                             asprintf(&strErr, "Variavel %d usada mas não declarada.", $2);
                                                                             yyerror(strErr); free(strErr);
                                                                             exit(0);
                                                                           }else{
                                                                             asprintf(&aux, "read\natoi\nstoreg%d\n", indices[indice-97]);
                                                                             $$ = strdup(aux);  free(aux);  
                                                                           }                                                                           
                                                                         }                                      
            | IF '(' Cond ')' '{' Codigos '}'                            { asprintf(&aux, "%s\njz fim_if_%d\n%s\nfim_if_%d:\n",
                                                                                            $3, indicesIf, $6, indicesIf);
                                                                           $$ = strdup(aux);  indicesIf++;  free(aux);                                                                            
                                                                         }    
            | IF '(' Cond ')' '{' Codigos '}' ELSE '{' Codigos '}'       { asprintf(&aux, "%s\njz else_if_%d\n%s\njump fim_if_%d\nelse_if_%d: \n%s \nfim_if_%d:\n",
                                                                                            $3, indicesIf, $6, indicesIf, indicesIf, $10, indicesIf);
                                                                           $$ = strdup(aux); indicesIf++;  free(aux);                                                                            
                                                                         }       
            | RETURN exp ';'                                             { asprintf(&aux, "%s\nstoreg 0\nreturn\n", $2);
                                                                           $$ = strdup(aux); free(aux);                                                                            
                                                                         }    
            | WHILE '(' Cond ')' '{' Codigos '}'                         { asprintf(&aux, "cond_while_%d:\n%s\njz fim_while_%d\n%s\njump cond_while_%d\nfim_while_%d:\n",
                                                                                            indicesWhile, $3, indicesWhile, $6, indicesWhile, indicesWhile);
                                                                           $$ = strdup(aux);
                                                                           indicesWhile++;  free(aux);                                                                            
                                                                         } 
            | Atribs                                                     { $$ = strdup($1); }
            |                                                            { $$ = strdup(" "); }
            ;
Cond        : exp '=''=' exp                                             { asprintf(&aux, "%s%sequal\n", $1, $4);
                                                                           $$ = strdup(aux); free(aux);                                                                            
                                                                         }      
            | exp '>''=' exp                                             { asprintf(&aux, "%s%ssupeq\n", $1, $4);
                                                                           $$ = strdup(aux); free(aux);                                                                            
                                                                         }   
            | exp '<''=' exp                                             { asprintf(&aux, "%s%sinfeq\n", $1, $4);
                                                                           $$ = strdup(aux); free(aux);                                                                            
                                                                         }   
            | exp '!''=' exp                                             { asprintf(&aux, "%s%sequal\nnot\n", $1, $4);
                                                                           $$ = strdup(aux); free(aux);                                                                            
                                                                         }   
            | exp '<' exp                                                { asprintf(&aux, "%s%sinf\n", $1, $3);
                                                                           $$ = strdup(aux); free(aux);                                                                            
                                                                         }   
            | exp '>' exp                                                { asprintf(&aux, "%s%ssup\n", $1, $3);
                                                                           $$ = strdup(aux); free(aux);                                                                            
                                                                         }  
            | exp                                                        { $$ = strdup($1); }
            | Conj                                                       { $$ = strdup($1); }
            ;
Opcao       : termo                                                      { int t = strlen($1);
                                                                           $$ = (char*) malloc( t* sizeof(char));
                                                                           strcat($$, $1);
                                                                         }
            |                                                            { $$ = strdup(" "); }
            ;
exp         : termo                                                      { $$ = strdup($1); }   
            | exp '+' termo                                              { asprintf(&aux, "%s%sadd\n", $1, $3);
                                                                           $$ = strdup(aux); free(aux);                                                                            
                                                                         }    
            | exp '-' termo                                              { asprintf(&aux, "%s%ssub\n", $1, $3);
                                                                           $$ = strdup(aux); free(aux);                                                                            
                                                                         }    
            | exp '*' termo                                              { asprintf(&aux, "%s%smul\n", $1, $3);
                                                                           $$ = strdup(aux); free(aux);                                                                            
                                                                         }  
            | exp '/' termo                                              { asprintf(&aux, "%s%sdiv\n", $1, $3);
                                                                           $$ = strdup(aux); free(aux);                                                                            
                                                                         }     
            ; 
termo       : variavel                                                   { int indice = (int) ($1);
                                                                           asprintf(&aux, "pushg %d\n", indices[indice-97]);
                                                                           $$ = strdup(aux); free(aux);                                                                            
                                                                         }    
            | valor                                                      { asprintf(&aux, "pushi %d\n", $1);
                                                                           $$ = strdup(aux); free(aux);                                                                            
                                                                         }                                                                                                        
        //    |                                                            { $$ = strdup(" ");}
            ;
Conj        : '(' Cond ')' RConj                                         { int t = strlen($2) + strlen($4);
                                                                           $$ = (char*) malloc( t* sizeof(char));
                                                                           strcat($$, $2); strcat($$, $4);
                                                                         }       
            ;
RConj       :                                                            { $$ = strdup(" "); }  
            | '&''&' Conj                                                { $$ = strdup($3); }   
            ;

%%
#include "lex.yy.c"

void yyerror(char *s){
	fprintf(stderr, "%d: %s\n", yylineno, s);
}

void escreve_assembly(ARRAY_DINAMICO arr){
  int out_file = open("saida", O_CREAT | O_TRUNC | O_WRONLY, 0666);

  int tamanho = getTamanhoAD(assembly);

  char* s;
  int i = 0;
  while(i < tamanho){
    s = strdup( (char*)getElementoPosAD(assembly, i) );
    write(out_file, s, strlen(s));
    i ++;
  }

  close(out_file);
  printf("Ficheiro de saida criado.\n");
}

int main(int argc, char*argv[]){
	yyparse();

	return 0;	
}