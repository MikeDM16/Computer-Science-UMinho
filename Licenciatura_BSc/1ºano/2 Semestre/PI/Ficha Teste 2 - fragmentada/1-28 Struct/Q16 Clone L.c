#include "stdio.h"
#include "string.h"
#include "stdlib.h"

typedef struct slist *LInt;
typedef struct slist {
	int valor;
	struct slist *prox;
} Nodo, *Lint;

void imprime(Lint m){
	if (m==NULL) printf("NULL (printf de teste)\n");
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

Lint cloneL (Lint m){
	Lint clone = NULL;
	if (m==NULL) return clone;
	else {
		clone=(Lint)malloc(sizeof(Nodo));
		clone->valor = m->valor;
		m = m->prox;
	}

	Lint iterar = clone;
	clone = clone->prox;
	while(m){
		clone=(Lint)malloc(sizeof(Nodo));
		clone->valor = m->valor;
		clone = clone->prox;
		m = m->prox;
	}

	clone = NULL;
	clone = iterar;

	return clone;
}


int main(){
	Lint m=(Lint)malloc(sizeof(Nodo));
	m->valor=2;
	m->prox=(Lint)malloc(sizeof(Nodo));
	m->prox->valor=5;
	m->prox->prox=(Lint)malloc(sizeof(Nodo));
	m->prox->prox->valor=6;
	m->prox->prox->prox=(Lint)malloc(sizeof(Nodo));
	m->prox->prox->prox->valor=8;
	m->prox->prox->prox->prox=NULL;
	Lint p=(Lint)malloc(sizeof(Nodo));
	p->valor=4;
	p->prox=(Lint)malloc(sizeof(Nodo));
	p->prox->valor=5;
	p->prox->prox=(Lint)malloc(sizeof(Nodo));
	p->prox->prox->valor=7;
	p->prox->prox->prox=(Lint)malloc(sizeof(Nodo));
	p->prox->prox->prox->valor=3;
	p->prox->prox->prox->prox=NULL;
	Lint cl = cloneL(p);
	imprime(cl);
	return 0;
}
