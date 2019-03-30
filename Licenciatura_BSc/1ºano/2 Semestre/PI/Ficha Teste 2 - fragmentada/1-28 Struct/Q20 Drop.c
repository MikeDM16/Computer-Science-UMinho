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

int drop(int n, Lint *l){
	Lint iterar = (*l); Lint *aux = NULL;
	int lenght = 0; int count = 0;
	while ((iterar)){
		iterar = (iterar)->prox;
		lenght++;
	}

	if (lenght < n ){
		while ((*l)){
			aux = (*l);
			l = (*l)->prox;
			free(aux);
			count ++;
		}
	}
	else {
		for(lenght=0; lenght<n;lenght++){
			aux = (*l);
			l = (*l)->prox;
			free(aux);
			count ++;
		}
	}
	return count;
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
	p->prox->prox->prox=(Lint)malloc(sizeof(Nodo));
	p->prox->prox->prox->valor=3;
	p->prox->prox->prox->prox=NULL;
	imprime(m);
	drop(2,&m);
	imprime(m);
	printf("TESTES 2 \n");
	imprime(p);
	drop(3,&p);
	imprime(p);
	return 0;
}
