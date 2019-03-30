#include "stdio.h"
#include "string.h"
#include "stdlib.h"

typedef struct slist *LInt;
typedef struct slist {
	int valor;
	struct slist *prox;
} Nodo, *Lint;

Lint clone (Lint l){
	Lint nova;
	if (l == NULL) return NULL;
	else{
		nova=(Lint)malloc(sizeof(Nodo));
		nova->valor = l->valor;
		nova->prox = (clone (l->prox));
	}
	return nova;
}
int main(){
	Lint m=(Lint)malloc(sizeof(Nodo));
	m->valor=4;
	m->prox=(Lint)malloc(sizeof(Nodo));
	m->prox->valor=3;
	m->prox->prox=(Lint)malloc(sizeof(Nodo));
	m->prox->prox->valor=5;
	m->prox->prox->prox=NULL;
	/*while(m){
		printf("%d\n",m->valor);
		m=m->prox;
	}*/
	Lint r = clone (m);
		while(r){
		if (r->prox==NULL){
			printf("%d -> NULL \n",r->valor);
			r=r->prox;
		}
		else {
			printf("%d -> ",r->valor);
			r=r->prox;
		}
	}
	return 0;
}
