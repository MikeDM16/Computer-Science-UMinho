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
	else printf("%d\n", m->valor );
}

Lint Nforward(Lint l, int N){
	while (N>0){
		l = l->prox;
		N--;
	}
	return l;
}

int main(){
	Lint m=(Lint)malloc(sizeof(Nodo));
	m->valor=2;
	Lint inicio =m;
	m->prox=(Lint)malloc(sizeof(Nodo));
	m->prox->valor=5;
	m->prox->prox=(Lint)malloc(sizeof(Nodo));
	m->prox->prox->valor=6;
	m->prox->prox->prox=(Lint)malloc(sizeof(Nodo));
	m->prox->prox->prox->valor=8;
	m->prox->prox->prox->prox= inicio;
	Lint p=(Lint)malloc(sizeof(Nodo));
	p->valor=4;
	Lint first = p;
	p->prox=(Lint)malloc(sizeof(Nodo));
	p->prox->valor=5;
	p->prox->prox=(Lint)malloc(sizeof(Nodo));
	p->prox->prox->valor=7;
	p->prox->prox->prox=(Lint)malloc(sizeof(Nodo));
	p->prox->prox->prox->valor=3;
	p->prox->prox->prox->prox=first;
	imprime(Nforward(m,2));
	imprime(Nforward(p,10));
	return 0;
}
