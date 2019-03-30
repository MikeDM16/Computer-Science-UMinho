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

int remover(Lint *m, int x){
	if (m==NULL) return 0;
	int count =0; Lint aux;
	while ((*m)){
		if ((*m)->valor ==x){
			aux = (*m);
			(*m) = (*m)->prox;
			free(aux);
			count ++;
		}
		else (*m) = (*m)->prox;
	}
	return count;
}

int main(){
	Lint m=(Lint)malloc(sizeof(Nodo));
	m->valor=2;
	m->prox=(Lint)malloc(sizeof(Nodo));
	m->prox->valor=5;
	m->prox->prox=(Lint)malloc(sizeof(Nodo));
	m->prox->prox->valor=8;
	m->prox->prox->prox=(Lint)malloc(sizeof(Nodo));
	m->prox->prox->prox->valor=5;
	m->prox->prox->prox->prox=NULL;
	int r = remover(&m, 5);
	printf("%d\n",r);
	return 0;
}
