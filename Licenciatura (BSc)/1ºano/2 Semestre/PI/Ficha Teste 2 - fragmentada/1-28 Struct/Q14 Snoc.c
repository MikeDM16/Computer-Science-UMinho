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

void snoc(Lint *m, int x){
	Lint aux = (*m);
	
	while (aux) {
		aux = aux->prox;
	}

	aux = (Lint)malloc(sizeof(Nodo));
	aux->valor = x;
	aux->prox = NULL;
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
	snoc(&m, 7);
	imprime(m);
	return 0;
}
