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

void split(Lint l, int x, Lint *mx, Lint *Mx){
	if (l==NULL) return;
	Lint menor = (*mx), maior = (*Mx); 
	while (l){
		if (l->valor < x){
			(*mx) = l;
			(mx) = &((*mx)->prox);
			l = l->prox;
		}
		if (l->valor >= x){
			(*Mx) = l;
			Mx = &((*Mx)->prox);
			l = l->prox;
		}
	}
	
	(*Mx) = NULL; (*mx) = NULL;
	(*Mx) = maior; (*mx) = menor;
	return;
}

int main(){
	Lint m=(Lint)malloc(sizeof(Nodo));
	m->valor=2;
	m->prox=(Lint)malloc(sizeof(Nodo));
	m->prox->valor=5;
	m->prox->prox=(Lint)malloc(sizeof(Nodo));
	m->prox->prox->valor=3;
	m->prox->prox->prox=(Lint)malloc(sizeof(Nodo));
	m->prox->prox->prox->valor=8;
	m->prox->prox->prox->prox=NULL;
	Lint mx=NULL; 
	Lint Mx=NULL;
	split(m,4,&mx,&Mx);
	imprime(Mx);
	return 0;
}
