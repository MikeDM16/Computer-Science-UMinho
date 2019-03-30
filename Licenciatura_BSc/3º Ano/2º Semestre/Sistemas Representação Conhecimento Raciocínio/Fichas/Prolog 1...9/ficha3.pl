%--------------------------------- - - - - - - - - - -  -  -  -  -   -
% SIST. REPR. CONHECIMENTO E RACIOCINIO - MiEI/

%--------------------------------- - - - - - - - - - -  -  -  -  -   -
% Extensao do predicado descendente: Descendente,Ascendente,Grau -> {V,F}

descendente( D,A,1 ) :-
	filho( D,A ).
descendente( D,A,G ) :-
	filho( D,X),
	descendente( X,A,N ),
	G is N+1.

%---------------------------------------------------------------------
% Soma de dois valores: X, Y, R ->  {V,F}

soma( X,Y,R )	:-
	R is (X + Y).

%---------------------------------------------------------------------
% Soma de tres valores: X, Y, Z, R ->  {V,F}

soma( X,Y,Z,R )	:-
	R is (X + Y + Z).

%---------------------------------------------------------------------
% Aplicaçao opeação aritmética: O, X, Y, R ->  {V,F}

operacao( +,X,Y,R ) :-
	R is (X + Y).
operacao( -,X,Y,R ) :-
	R is (X - Y).
operacao( /,X,Y,R ) :-
	Y \= 0,
	R is (X / Y).
operacao( *,X,Y,R ) :-
	R is (X * Y).

%---------------------------------------------------------------------
% somatório: L lista, S -> {V,F}

somatorio( [X],X ).
somatorio( [X|Y], N) :-
	somatorio( Y,R ),
	N is (R+X).

%---------------------------------------------------------------------
% operação de conjuntos: O, L,, S -> {V,F}

operacao( +,[X],X ).
operacao( +,[X],Y ) :- operacao( +,Y,R ), N is (R+X).
operacao( -,[X],X ).
operacao( -,[X],Y ) :- operacao( -,Y,R ), N is (R-X).
operacao( *,[X],X ).
operacao( *,[X],Y ) :- operacao( *,Y,R ), N is (R*X).
operacao( /,[X],X ).
operacao( /,[X],Y ) :- y /=0, operacao( /,Y,R ), N is (R/X).

%---------------------------------------------------------------------
% determinar o máximo de uma lista: L,Max -> {V,F}	

maximo([X],X).
maximo( [X|L],M ) :- 
	maximo( L,R ), R>X.
maximo( [X|L],M ) :-
	maximo( L,R ), X<=R.


%---------------------------------------------------------------------
% determinar se um elemento pertence a uma lista:X, L -> {V,F}
pertence( X,[X|L] ).
pertence( X,[Y|L] ) :-	X \= Y,
			pertence( X,L ).
%---------------------------------------------------------------------
% determinar o comprimento de uma lista: L, X -> {V,F}
compri( [], 0 ).
compri( [X|L],R) :-
	compri( L,N ),
	R is N+1. 


%---------------------------------------------------------------------
%Concatenar com o operador + : L, X -> {V,F}
concat( [],0 ).
concat( [X|L], N+1) :- concat(L,N).

%---------------------------------------------------------------------
% Determinar quandos elementos diferentes tem uma lista: L, X -> {V,F}
quantos( [],0 ).
quantos( [X|L], N) :-
	pertence( X,L ),
	qantos( L, N),
quantos( [X|L], N1) :-
	nao( pertence( X.L) ),
	quantos( L, N),
	N1 is N+1.