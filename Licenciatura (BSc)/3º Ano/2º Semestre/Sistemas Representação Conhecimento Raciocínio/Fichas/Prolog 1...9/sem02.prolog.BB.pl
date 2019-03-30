%--------------------------------- - - - - - - - - - -  -  -  -  -   -
% SIST. REPR. CONHECIMENTO E RACIOCINIO - MiEI/3

%--------------------------------- - - - - - - - - - -  -  -  -  -   -
% Base de Conhecimento com informacao genealogica.

%--------------------------------- - - - - - - - - - -  -  -  -  -   -
% Extensao do predicado filho: Filho,Pai -> {V,F}

:- dynamic filho/2.
:- dynamic pai/2.

filho( joao,jose ).
filho( jose,manuel ).
filho( carlos,jose ).

pai( paulo,filipe ).
pai( paulo,maria ).

avo( antonio,nadia ).

sexo( joao,masculino ).
sexo( jose, masculino ).
sexo( maria,feminino ).
sexo( joana,feminino ).


%--------------------------------- - - - - - - - - - -  -  -  -  -   -
% Extensao do predicado pai: Pai,Filho -> {V,F}

pai( P,F ) :-
    filho( F,P ).

%--------------------------------- - - - - - - - - - -  -  -  -  -   -
% Extensao do predicado avo: Avo,Neto -> {V,F}

avo( A,N ) :-
	filho( N,P ), 
	filho( P,A).

neto( N,A) :- avo(A,N).
%--------------------------------- - - - - - - - - - -  -  -  -  -   -
% Extensao do predicado bisavo: Bisavo,Bisneto -> {V,F}

% bisavo(B,N) :- 
%	filho(A,B),
%	filho(P,A),
%	filho(N,P).

bisavo( B,N ) :- 
	filho(A,B),
	avo(A,N).
bisneto( N,B ) :- bisavo( B,N ). 


%--------------------------------- - - - - - - - - - -  -  -  -  -   -
% Extensao do predicado descendente: Descendente,Ascendente -> {V,F}

&descendente( A,B) :-
&	filho(A,B); 
&	neto(A,B);
&	bisneto( A,B ).

descendente( D,A ) :-
	filho(D,X);
	descendente( X,A).

ascendente( B,A ) :- descendente( A,B ).



%--------------------------------- - - - - - - - - - -  -  -  -  -   -
% Extensao do predicado descendente: Descendente,Ascendente,Grau -> {V,F}

descendente( D,A,1 ) :-
	filho( D,A ).
descendente( D,A,G ) :-
	filho( D,X),
	descendente( X,A,N ),
	G is N+1.




