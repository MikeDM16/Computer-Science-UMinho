%--------------------------------- - - - - - - - - - -  -  -  -  -   -
% SIST. REPR. CONHECIMENTO E RACIOCINIO - MiEI/3

%--------------------------------- - - - - - - - - - -  -  -  -  -   -
% Invariantes

%--------------------------------- - - - - - - - - - -  -  -  -  -   -
% SICStus PROLOG: Declaracoes iniciais

:- set_prolog_flag( discontiguous_warnings,off ).
:- set_prolog_flag( single_var_warnings,off ).
:- set_prolog_flag( unknown,fail ).

%--------------------------------- - - - - - - - - - -  -  -  -  -   -
% SICStus PROLOG: definicoes iniciais

:- op( 900,xfy,'::' ).
:- dynamic filho/2.
:- dynamic pai/2.

%--------------------------------- - - - - - - - - - -  -  -  -  -   -
% Extensao do predicado pai: Filho,Pai -> {V,F,D}

pai( P,F ) :- filho( F,P ).
pai( paulo,filipe ).
pai( paulo, maria ).

%--------------------------------- - - - - - - - - - -  -  -  -  -   -
% Extensao do predicado filho: Filho,Pai -> {V,F,D}

filho( joao,jose ).
filho( jose,manuel ).
filho( carlos,jose ).

%--------------------------------- - - - - - - - - - -  -  -  -  -   -
% Extensao do predicado avo: Avo,Neto -> {V,F}

avo( A,N ) :- filho( N,X ) , filho( X,A ). 
neto(N,A) :- avo(A,N).
avo( antonio,nadia ).

%--------------------------------- - - - - - - - - - -  -  -  -  -   -
% Extensao do predicado descendente: Descendente,Ascendente -> {V,F}

descendente(D,A) :- filho(D,A).
descendente(D,A) :- filho(D,X), descendente(X,A).

%--------------------------------- - - - - - - - - - -  -  -  -  -   -
% Extensao do predicado solucoes
solucoes(X,Y,Z) :- findall(X,Y,Z).

%---------------------------------- - - - - - - - - - -  -  -  -  -
% Predicado «comprimento» que calcula o número de elementos
% existentes numa lista;

comprimento([],0).
comprimento([H|T],R) :- comprimento(T,M), R is M+1.

%---------------------------------- - - - - - - - - - -  -  -  -  -
% Testa

testa([]).
testa([H|T]) :- H,testa(T).

%--------------------------------- - - - - - - - - - -  -  -  -  -   -
% Invariante Estrutural:  nao permitir a insercao de conhecimento
%                         repetido

+filho( F,P ) :: (solucoes( (F,P),(filho( F,P )),S ),
                  comprimento( S,N ), 
				  N == 1 ).


% Invariante Referencial: nao admitir mais do que 2 progenitores
%                         para um mesmo individuo

+filho( F,P ) :: ( solucoes((Ps),(filho(F,Ps)),S), 
			comprimento(S,N), N=<2).

%--------------------------------- - - - - - - - - - -  -  -  -  -   -
% Extensão do predicado que permite a evolucao do conhecimento

evolucao( Termo ) :- (solucoes(INV, +Termo::INV, LINV),
				assert(Termo),
				testa(LINV)).
evolucao( Termo ) :- retract( Termo ).


evolucao1( Termo ):- 
	(solucoes(INV, +Termo::INV, LINV),
	insercao(Termo),
	testa(LINV)).

insercao( Termo ) :-
	assert( Termo ).
insercao( Termo ) :-
	retract( Termo ), !, fail.