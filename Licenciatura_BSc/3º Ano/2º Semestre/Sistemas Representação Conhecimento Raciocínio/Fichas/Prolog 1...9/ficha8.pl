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
%(a)
%predicado pais: Filho, pai, mae -> {V,F,D}
-pais( F,P,M ) :- nao( pais( F,P,M ) ),
				 nao( excecao( pais( F,P,M) ) ).

filho( F,P ) :- pais(F,P,M).
filho( F,M ) :- pais(F,P,M).

pais( ana,abel,alice ).
nascimento( ana,data( 1,1,2010 ) ).

%(b)
pais( anibal, antonio, alberta ).
nascimento( anibal, data( 2,1,2010 ) ).
 
 %(c)
pais( berta,bras,belem ).
pais( berto,bras,belem ).
nascimento( berta, data(2,2,2010) ).
nascimento( berto, data(2,2,2010) ).

%(d)
nascimento( catia, data(3,3,2010) ).

%(e)
excecao( pais( crespim,celso,catia ) ).
excecao( pais( crespim,caio,catia ) ).

%(f)
nascimento( danilo,data(4,4,2010 ) ).
pais( danilo,daniel,xpto ).
excecao( pais(F,P,M) ) :- pais(F,P,xpto).
nulo(xpto).

%(g)
pais( eurico,elias,elsa ).
excecao( nascimento( eurico, data(D,5,2010) ) ) :- D = 5.
excecao( nascimento( eurico, data(D,5,2010) ) ) :- D = 15.
excecao( nascimento( eurico, data(D,5,2010) ) ) :- D = 25.

-data(D,M,A) :- nao( data(D,M,A) ),
				nao( excecao( data(D,M,A))).
-nascimento( P,data(D,M,A) ) :- nao( nascimento( P, data(D,M,A) ) ),
								nao( excecao( nascimento( P, data(D,M,A) ) ) ).


%(h)
pais( fabia,fausto,xpto2 ).
excecao( pais( F,P,M ) ) :- pais(F,P,xpto2) .
pais( otavia,fausto,xpto2 ).

%(i)
pais( golias,guido,guida ).
nascimento( golias, data(xptod,xptom,xptoa)).
excecao( nascimento(P, data(D,M,A) ) ) :-  nascimento( P, data(xptod,xptom,xptoa) ).

%j
nascimento( helder, data(xptod,xptom,xptoa)).
-nascimento( helder,data(8,8,2010)).
%excecao( nascimento(P, data(D,M,A) ) ) :-  nascimento( P, data(xptod,xptom,xptoa). 



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
