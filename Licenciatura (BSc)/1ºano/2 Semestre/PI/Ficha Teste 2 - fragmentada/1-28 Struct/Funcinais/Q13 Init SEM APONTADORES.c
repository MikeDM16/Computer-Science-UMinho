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

void init (Lint m){
	Lint ant = NULL;
	while ((m)->prox){
		ant = m;
		m = (m)->prox;
	}
	free(m);
	ant->prox  =NULL;

}

/*void init (Lint *m){
	Lint *aux = m;
	while ( &((*aux)->prox) ){
		aux = &((*aux)->prox);
	}
	free(*aux); */

int main(){
	Lint m=(Lint)malloc(sizeof(Nodo));
	m->valor=2;
	m->prox=(Lint)malloc(sizeof(Nodo));
	m->prox->valor=8;
	m->prox->prox=(Lint)malloc(sizeof(Nodo));
	m->prox->prox->valor=5;
	m->prox->prox->prox=(Lint)malloc(sizeof(Nodo));
	m->prox->prox->prox->valor=8;
	m->prox->prox->prox->prox=NULL;
	imprime(m);
	init(&m);
	imprime(m);
	init(&m);
	imprime(m);
	return 0;
}
