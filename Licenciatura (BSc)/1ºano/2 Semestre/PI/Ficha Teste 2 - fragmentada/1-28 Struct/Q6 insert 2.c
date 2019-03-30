#include "stdio.h"
#include "string.h"
#include "stdlib.h"

typedef struct slist *LInt;
typedef struct slist {
	int valor;
	struct slist *prox;
} Nodo, *Lint;

void insert(Lint *l, int x){
	Lint m = &(*l);
	Lint iterar = m;
	if (m==NULL){
		Lint newL=(Lint)malloc(sizeof(Nodo));
		newL->valor = x;
		newL->prox = NULL;
		m = newL;
	}
	else{
		while((iterar) && (iterar->valor < x)){
			iterar = iterar->prox;
		}
	}
	if ((iterar)==NULL){ 			//chegou ao fim da lista. x > qq elemento
		(iterar)=(Lint)malloc(sizeof(Nodo));
		(iterar)->valor = x;
		(iterar)->prox  = NULL;
				return;
		}
	if((iterar)->valor = x) return; //o x já existe.
		else {
			Lint ant = iterar;
			//neste espaço entre ant e iterar será colocado x;
			iterar = ((iterar)->prox);
			Lint newL=(Lint)malloc(sizeof(Nodo));
			newL->valor = x;
			newL->prox = iterar;
			(ant)->prox = newL;
		}
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
	Lint k = (Lint)malloc(sizeof(Nodo));
	k=NULL;
	imprime(m);
	insert(&k,78);
	insert(&m,2);
	imprime(k);
	imprime(m);
	return 0;
}
