%--------------------------------- - - - - - - - - - -  -  -  -  -   -
% SIST. REPR. CONHECIMENTO E RACIOCINIO - MiEI/3

%--------------------------------- - - - - - - - - - -  -  -  -  -   -
% Programacao em logica estendida
%
% Representacao de conhecimento imperfeito

%--------------------------------- - - - - - - - - - -  -  -  -  -   -
% SICStus PROLOG: Declaracoes iniciais

:- set_prolog_flag( discontiguous_warnings,off ).
:- set_prolog_flag( single_var_warnings,off ).
:- set_prolog_flag( unknown,fail ).

%--------------------------------- - - - - - - - - - -  -  -  -  -   -
% SICStus PROLOG: definicoes iniciais

:- op( 900,xfy,'::' ).
:- op( 900,xfy,'<' ).
:- op( 900,xfy,'>' ).
:- dynamic filho/2.
:- dynamic pai/2.

%--------------------------------- - - - - - - - - - -  -  -  -  -   -
% Extensao do predicado filho: Filho,Pai -> {V,F,D}

filho( joao,jose ).
filho( jose,manuel ).
filho( carlos,jose ).

-filho( F,P ) :-
    nao( filho( F,P ) ),
    nao( excecao( filho( F,P ) ) ).

% Invariante Estrutural:  nao permitir a insercao de conhecimento
%                         repetido

+filho( F,P ) :: (solucoes( (F,P),(filho( F,P )),S ),
                  comprimento( S,N ), N == 1
                  ).

% Invariante Referencial: nao admitir mais do que 2 progenitores
%                         para um mesmo individuo

+filho( F,P ) :: (solucoes( (Ps),(filho( F,Ps )),S ),
                  comprimento( S,N ), N =< 2
                  ).

%--------------------------------- - - - - - - - - - -  -  -  -  -   -
% Explicitacao das situacoes de excecao

% A Belem é filha de uma pessoa de que se desconhece a identidade

filho( belem,xpto023 ).
excecao( filho( F,P ) ) :-
    filho( F,xpto023 ).

% A Maria é filha do Faria ou do Garcia

excecao( filho( maria,faria ) ).
excecao( filho( maria,garcia ) ).

% O Julio tem um filho que ninguem pode conhecer

filho( xpto732,julio ).
excecao( filho( F,P ) ) :-
    filho( xpto732,P ).
nulo( xpto732 ).
+filho( F,P ) :: (solucoes( (Fs,P),(filho(Fs,julio),nao(nulo(Fs))),S ),
                  comprimento( S,N ), N == 0 
                  ).
%--------------------------------- - - - - - - - - - -  -  -  -  -   -
idade( joao,21 ).
%conhecimento de valor desconhecido e desconhecido,mas dentro de um 
%conjnto de valores conhecidos

idade( andre,xpto).
excecao( idade(P,I) ) :- idade(P,xpto).

-idade(adriana,30).
excecao( idade(adriana,23)).
excecao( idade(adriana,21)).


-idade( P,I ):-
	nao( idade( P,I ) ),
	nao( excecao( idade( P,I ) ) ).
%--------------------------------- - - - - - - - - - -  -  -  -  -   -
%Ficha 7
%negação por falha na prova e negação por excecao
-jogo( Id,N,C) :- nao( jogo(Id,N,C) ),
				 nao( excecao( jogo(Id,N,C) ) ).

%Casos totalmente desconhecidos
excecao( jogo( Id,N,C) ) :- jogo(Id,N, xpto). 

jogo( 1,almeida,500 ).
jogo( 2,baltazar,xpto).
excecao( jogo( 3,costa,500 ) ).
excecao( jogo( 3,costa,2000 ) ).
%Valor recebido entre aqueles valores
excecao( jogo( 4,duarte,V) )  :- V >= 250,
			    	 		   	 V =< 750.
jogo( 5,edgar,xpto2).
excecao( jogo(Id,N,C) ) :-
		jogo(Id,N,xpto2).
nulo( xpto2 ). 
+jogo(Id,N,C) :: (solucoes((Id,N,C), (jogo(Id,N,C) ,nao(nulo(C))), S),
                  comprimento( S,N ), N == 0 ).

jogo( 6,francisco,250 ).
excecao( jogo( 6,francisco,V) ) :- V>= 5000. 

-jogo( 7,guerra,2500 ).
jogo( 7,guerra,xpto3).
excecao( jogo(Id,N,C) ) :-
		jogo(Id,N,xpto3).

excecao( jogo(8,helder,C) ) :-
		cerca( 1000, Csup, Cinf),
		C >= Cinf, C =< Csup.

excecao( jogo(9,ivo,C) ) :-
		proximo( 3000, Csup, Cinf),
		C >= Cinf, C =< Csup.

cerca( X,Csup,Cinf ) :- Csup is X*1.25, Cinf is X*0.75.
proximo( X,Csup,Cinf ) :- Csup is X*1.1, Cinf is X*0.9.

%Invariantes
+jogo( Id,A,C ) :: solucoes(Id, jogo( Id,_,_ ), S),
				   comprimento(S,N), 
				   N == 1.


%--------------------------------- - - - - - - - - - -  -  -  -  -   -

evolucao( Termo ) :-
    solucoes( Invariante,+Termo::Invariante,Lista ),
    insercao( Termo ),
    teste( Lista ).

insercao( Termo ) :-
    assert( Termo ).
insercao( Termo ) :-
    retract( Termo ),!,fail.

teste( [] ).
teste( [R|LR] ) :-
    R,
    teste( LR ).

%--------------------------------- - - - - - - - - - -  -  -  -  -   -
% Extensao do meta-predicado demo: Questao,Resposta -> {V,F}

demo( Questao,verdadeiro ) :-
    Questao.
demo( Questao, falso ) :-
    -Questao.
demo( Questao,desconhecido ) :-
    nao( Questao ),
    nao( -Questao ).

%--------------------------------- - - - - - - - - - -  -  -  -  -   -
% Extensao do meta-predicado nao: Questao -> {V,F}

nao( Questao ) :-
    Questao, !, fail.
nao( Questao ).

%--------------------------------- - - - - - - - - - -  -  -  -  -   -

solucoes( X,Y,Z ) :-
    findall( X,Y,Z ).

comprimento( S,N ) :-
    length( S,N ).
