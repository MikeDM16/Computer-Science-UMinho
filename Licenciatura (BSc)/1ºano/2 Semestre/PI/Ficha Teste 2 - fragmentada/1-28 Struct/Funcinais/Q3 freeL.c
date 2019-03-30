#include "stdio.h"
#include "string.h"
#include "stdlib.h"

typedef struct slist *LInt;
typedef struct slist {
	int valor;
	struct slist *prox;
} Nodo, *Lint;

void freeL (Lint l){
	Lint aux;
	while(l){
		aux = l;
		l=l->prox;
		free(aux);
	}
}

int main(){
	Lint m=(Lint)malloc(sizeof(Nodo));
	m->valor=4;
	m->prox=(Lint)malloc(sizeof(Nodo));
	m->prox->valor=3;
	m->prox->prox=(Lint)malloc(sizeof(Nodo));
	m->prox->prox->valor=5;
	m->prox->prox->prox=NULL;
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
	free(m);
	if (m) printf("%d\n", m->valor); // usado para Testar se a lista ficou vazia. 
	//FICOU!!!! Pois deu erro de segmentaçao
	return 0;
}
