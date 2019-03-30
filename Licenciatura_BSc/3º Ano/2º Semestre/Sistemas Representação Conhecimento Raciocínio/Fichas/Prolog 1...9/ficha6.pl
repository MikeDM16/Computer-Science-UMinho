%--------------------------------- - - - - - - - - - -  -  -  -  -   -
% SIST. REPR. CONHECIMENTO E RACIOCINIO - MiEI/3

%--------------------------------- - - - - - - - - - -  -  -  -  -   -
% Programacao em logica estendida

%--------------------------------- - - - - - - - - - -  -  -  -  -   -
% SICStus PROLOG: Declaracoes iniciais

:- set_prolog_flag( discontiguous_warnings,off ).
:- set_prolog_flag( single_var_warnings,off ).
:- set_prolog_flag( unknown,fail ).

:- dynamic vem_automovel/0.
:- dynamic vem_comboio/0.
:- dynamic '-'/1.
%--------------------------------- - - - - - - - - - -  -  -  -  -   -
% Extensao do predicado par: Número -> {V,F}

par( 0 ).
par( X ) :- Z is X-2,
			X >= 0,
			par( Z ).
-par( X ) :- nao( par( X ) ).

%--------------------------------- - - - - - - - - - -  -  -  -  -   -
% Extensao do predicado impar: Número -> {V,F}

impar( 1 ).
impar( X ) :-
			N is X-2,
			N >= 1,
			impar( N ).
-impar( 0 ).
-impar( X ) :- N is X-2,
			   N >= 0,
			   -impar( N ).
%--------------------------------- - - - - - - - - - -  -  -  -  -   -
% Extensao do predicado arcoiris: cor -> {V,F}

arcoiris( vermelho ).
arcoiris( violeta ).
%etc...

-arcoiris( preto ).
-arcoiris( branco ).

%--------------------------------- - - - - - - - - - -  -  -  -  -   -
% Extensao do predicado passar a estrada: Estrada -> {V,F}

vem_automovel. 

atravessar( estrada ) :-
	nao( vem_automovel ).

% Extensao do predicado passar a ferrovia: ferrovia -> {V,F}

atravessar( ferrovia ) :-
	-vem_comboio.


%--------------------------------- - - - - - - - - - -  -  -  -  -   -
% Extensao do predicado arco: N -> {V,F}
nodo( a ).
nodo( b ).
nodo( c ).
nodo( d ).
nodo( e ).
nodo( f ).
nodo( g ).

%Extensao do predicado arco: O,D -> {V,F}
arco( b,a ).
arco( b,c ).
arco( c,a ).
arco( c,d ).

arco( f,g ).

%Extensao do predicado terminal: N -> {V,F}

-terminal( X ):-
			nodo( X ),
			arco( X,Y ).
terminal( X) :-
			nodo( X ),
			nao( -terminal( X ) ).


%--------------------------------- - - - - - - - - - -  -  -  -  -   -
%				FICHA 6
%--------------------------------- - - - - - - - - - -  -  -  -  -   -

ave( pomba ).
mamifero( esm ).
mamifero( morcego ).
%--------------------------------- - - - - - - - - - -  -  -  -  -   -
% Extensao do predicado voa: Animal -> {V,F}

voa( X ) :- ave( X ),
			nao( execao( voa(X) ) ).
-voa( tweety ).
-voa( X ) :- mamifero( X ),
			 nao( execao( -voa(X) ) ).
-voa( X ) :- execao( voa( X ) ).
voa( X ) :- execao( -voa( X ) ).

execao( voa( X ) ) :- avestruz( X ).
execao( voa( X ) ) :- pinguim( X ).
execao( -voa( X ) ) :- morcego( X ).

ave( X ) :- canario( X ).
ave( X ) :- avestruz( X ).
ave( pitigui ).

canario( pitigui ).
avestruz( trux ).
pinguim( pingo ).
mamifero( silvestre ).
mamifero( X ) :- cao( X ).
mamifero( X ) :- gato( X ).
mamifero( X ) :- morcego( X ).

morcego( morce ).
%--------------------------------- - - - - - - - - - -  -  -  -  -   -
% Extensao do meta-predicado demo: Questao,Resposta -> {V,F}

demo( Questao,verdadeiro ) :-
    Questao.
demo( Questao,falso ) :-
    -Questao.
demo( Questao,desconhecido ) :-
    nao( Questao ),
    nao( -Questao ).

%--------------------------------- - - - - - - - - - -  -  -  -  -   -
% Extensao do meta-predicado nao: Questao -> {V,F}

nao( Questao ) :-
    Questao, !, fail.
nao( Questao ).
