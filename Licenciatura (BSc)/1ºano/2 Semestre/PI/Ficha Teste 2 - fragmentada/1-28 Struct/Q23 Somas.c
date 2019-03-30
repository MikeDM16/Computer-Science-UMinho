#include "stdio.h"
#include "string.h"
#include "stdlib.h"

typedef struct slist *LInt;
typedef struct slist {
	int valor;
	struct slist *prox;
} Nodo, *Lint;

int imprime(Lint m){
	if (m == NULL) printf("NULL \n");
	while(m){
		if (m->prox==NULL){
			printf("%d -> NULL \n",m->valor);
			m=m->prox;
		}
		else {
			printf("%d -> ",m->valor);
			m=m->prox;
		}
	}
	return 0;
}

Lint somas(Lint m){
	if (m==NULL) return NULL;

	int somas = m->valor;
	m = m->prox;
	
	Lint resultado =(Lint)malloc(sizeof(Nodo));
	resultado->valor = somas;
	Lint iterar = resultado;

	while(m!=NULL){
		resultado=(Lint)malloc(sizeof(Nodo));
		somas += m->valor;
		resultado->valor = somas;
		resultado = resultado->prox;
		m = m->prox;
	}

	resultado = NULL;
	return iterar;

}
int main(){
	Lint m=(Lint)malloc(sizeof(Nodo));
	m->valor=1;
	m->prox=(Lint)malloc(sizeof(Nodo));
	m->prox->valor=2;
	m->prox->prox=(Lint)malloc(sizeof(Nodo));
	m->prox->prox->valor=3;
	m->prox->prox->prox=(Lint)malloc(sizeof(Nodo));
	m->prox->prox->prox->valor=4;
	m->prox->prox->prox->prox= NULL;
	Lint p=(Lint)malloc(sizeof(Nodo));
	p->valor=4;
	p->prox=(Lint)malloc(sizeof(Nodo));
	p->prox->valor=5;
	p->prox->prox=(Lint)malloc(sizeof(Nodo));
	p->prox->prox->valor=7;
	p->prox->prox->prox=NULL;
	Lint r = somas(m);
	imprime(r);
	imprime(p);
	return 0;
}
