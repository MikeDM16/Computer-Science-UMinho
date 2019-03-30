#include "stdio.h"
#include "string.h"
#include "stdlib.h"

typedef struct slist *LInt;
typedef struct slist {
	int valor;
	struct slist *prox;
} Nodo, *Lint;

Lint newLint(Lint fim, Lint corpo){
	if (corpo==NULL) return fim;
	else{
		Lint iterar = corpo;
		while(iterar->prox){
			iterar = iterar->prox;
		}
		iterar->prox = fim;
		fim->prox = NULL;
	}
	return corpo;
}

Lint reverse(Lint rev){
	Lint head=rev;
	if (rev==NULL) return NULL;
	while(head){
		Lint aux = head->valor;
		head = head->prox;
		rev=newLint(aux, head);
	}
	return rev;
}

void imprime(Lint p){
	Lint m = p;
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
	imprime(m);
	m = reverse(m);
	imprime(m);
	return 0;
}
