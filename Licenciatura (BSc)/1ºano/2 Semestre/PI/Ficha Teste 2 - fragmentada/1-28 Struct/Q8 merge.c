#include "stdio.h"
#include "string.h"
#include "stdlib.h"

typedef struct slist *LInt;
typedef struct slist {
	int valor;
	struct slist *prox;
} Nodo, *Lint;

void imprime(Lint m){
	while(m){
		if (m->prox==NULL){
			printf("%d -> NULL (printf de teste)\n",m->valor);
			m=m->prox;
		}
		else {
			printf("%d -> ",m->valor);
			m=m->prox;
		}
	}
}

Lint merge (Lint a, Lint b){
	Lint aux;
	if (a==NULL){ return b; }
	else return a;
	Lint inicio = a;
	
	while (a && b){
		if ((a->valor < b->valor)){
			a = a->prox;
		}
		if (a->valor == b->valor){
			a = a->prox;
			b = b->prox;
		}
		if (a->valor > b->valor){
			aux = (Lint)malloc(sizeof(Nodo));
			aux->valor = b->valor;
			aux->prox = a->prox;
			a->prox = aux;
			b = b->prox;
			a = a->prox;
		}
	}
	imprime (a);
	imprime (b);
	if (b==NULL){ //caso a lista a seja > que a b
		a = inicio;
		return a;
	}
	if (a==NULL){
		while(b){
		a = (Lint)malloc(sizeof(Nodo));
		a->valor = b->valor;
		a = a->prox;
		}
		a= NULL;
	}
	a= inicio;
	return a;
}

int main(){
	Lint m=(Lint)malloc(sizeof(Nodo));
	m->valor=2;
	m->prox=(Lint)malloc(sizeof(Nodo));
	m->prox->valor=3;
	m->prox->prox=(Lint)malloc(sizeof(Nodo));
	m->prox->prox->valor=5;
	m->prox->prox->prox=(Lint)malloc(sizeof(Nodo));
	m->prox->prox->prox->valor=8;
	m->prox->prox->prox->prox=NULL;
	Lint k = (Lint)malloc(sizeof(Nodo));
	k->valor=4;
	k->prox=(Lint)malloc(sizeof(Nodo));
	k->prox->valor=5;
	k->prox->prox=(Lint)malloc(sizeof(Nodo));
	k->prox->prox->valor=6;
	k->prox->prox->prox=(Lint)malloc(sizeof(Nodo));
	k->prox->prox->prox->valor=8;
	k->prox->prox->prox->prox=NULL;
	imprime(k);
	m = merge(m,k);
	imprime(m);
	return 0;
}
