#include "stdio.h"
#include "string.h"
#include "stdlib.h"

typedef struct slist *LInt;
typedef struct slist {
	int valor;
	struct slist *prox;
} Nodo, *Lint;

void imprime(int v[], int N){
	int i ;
	for (i=0; i<N; i++){
		if (i ==(N-1)) printf("%d\n", v[i]);
		else printf("%d ", v[i]);
	}
}

int dumpL(Lint l, int v[], int N){
	int i=0;
	while(l){
		if (i<N){
			v[i] = l->valor;
			i++;
			l = l->prox;
		}else break;
	}
	return (i);
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
	m->prox->prox->prox->prox= NULL;
	Lint p=(Lint)malloc(sizeof(Nodo));
	p->valor=4;
	p->prox=(Lint)malloc(sizeof(Nodo));
	p->prox->valor=5;
	p->prox->prox=(Lint)malloc(sizeof(Nodo));
	p->prox->prox->valor=7;
	p->prox->prox->prox=NULL;
	int v[10];
	int r = dumpL(m,v,6);
	printf(" foram copiados %d elementos\n", r);
	imprime(v,r);
	printf("%d\n", dumpL(p,v,5));
	return 0;
}
