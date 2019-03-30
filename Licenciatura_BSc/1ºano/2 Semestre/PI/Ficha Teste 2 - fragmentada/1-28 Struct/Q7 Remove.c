#include "stdio.h"
#include "string.h"
#include "stdlib.h"

typedef struct slist *LInt;
typedef struct slist {
	int valor;
	struct slist *prox;
} Nodo, *Lint;

int remover (Lint *l, int x){
	Lint *anterior;
	if ((*l)==NULL) return 1;
	if ((*l)->valor > x) return 1;

	while ((*l) && ((*l)->valor < x)){
		*anterior = (*l);
		l = &((*l)->prox);
	}

	// apos o ciclo while, ou x n pertence e se precorreu toda a lista,
	// ou encontramos o x
	if ((*l)==NULL || ((*l)->valor > x)) return 1;
	else if ((*l)->valor == x){
		(*anterior)->prox = &((*l)->prox);
		free (l);
	}

	return 0;
}

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
	int r = remover(&m,4);
	printf("%d\n", r);
	return 0;
}
