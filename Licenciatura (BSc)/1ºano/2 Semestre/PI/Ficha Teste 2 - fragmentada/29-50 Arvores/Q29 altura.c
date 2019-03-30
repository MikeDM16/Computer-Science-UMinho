#include "stdio.h"
#include "string.h"
#include "stdlib.h"

typedef struct nodo *ABin;

struct nodo
{
	int valor;
	ABin esq,dir;
};

void imprime(ABin a){
	if (a!=NULL){
		printf("%d\n", a->valor);
	}
	imprime(a->esq);
	imprime(a->dir);
}

int main(){
	ABin m =(ABin)malloc(sizeof(struct nodo));
	m->valor = 7;
	m->esq = (ABin)malloc(sizeof(struct nodo));
	m->esq->valor = 6;
	m->dir = (ABin)malloc(sizeof(struct nodo));
	m->dir->valor = 8;
	m->esq->esq = (ABin)malloc(sizeof(struct nodo));
	m->esq->esq->valor = NULL;
	m->esq->dir = (ABin)malloc(sizeof(struct nodo));
	m->esq->dir->valor = NULL;
	m->dir->esq = (ABin)malloc(sizeof( struct nodo));
	m->dir->esq->valor = NULL;
	m->dir->dir = (ABin)malloc(sizeof(struct nodo));
	m->dir->dir->valor = NULL;
	imprime(m);
	return 0;
}