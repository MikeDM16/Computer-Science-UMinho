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
}

int main(){
	Lint m=(Lint)malloc(sizeof(Nodo));
	m->valor=4;
	m->prox=(Lint)malloc(sizeof(Nodo));
	m->prox->valor=3;
	m->prox->prox=(Lint)malloc(sizeof(Nodo));
	m->prox->prox->valor=5;
	m->prox->prox->prox=(Lint)malloc(sizeof(Nodo));
	m->prox->prox->prox->valor=8;
	m->prox->prox->prox->prox=NULL;
	Lint apontador = m ;
	Lint p = NULL;
	imprime(p);
	imprime (apontador);
	return 0;
}
