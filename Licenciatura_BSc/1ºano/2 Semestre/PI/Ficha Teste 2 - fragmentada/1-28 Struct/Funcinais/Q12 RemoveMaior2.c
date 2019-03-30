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

int removeMaior(Lint *l){
	int maior;
	Lint m = (*l);
	maior = m->valor;
	m = m->prox;
	while (m){
		if (m->valor > maior){
			maior = m->valor;
			m = m->prox;
		} else m = m->prox;
	}
	Lint anterior=NULL;
	Lint iterar  =(*l);

	if (iterar->valor == maior) {
		Lint aux  =iterar;
		iterar = iterar->prox;
		free(aux);
		return maior;
	}else{ 
		anterior  = iterar;
		iterar  = iterar->prox;
	}
	while(iterar){
		if (iterar->valor == maior) {
			anterior->prox = iterar->prox;
			free(iterar);
			return maior;
		}else{ 
			anterior  = iterar;
			iterar  = iterar->prox;
		}
	}

}



int main(){
	Lint m=(Lint)malloc(sizeof(Nodo));
	m->valor=67;
	m->prox=(Lint)malloc(sizeof(Nodo));
	m->prox->valor=7;
	m->prox->prox=(Lint)malloc(sizeof(Nodo));
	m->prox->prox->valor=98;
	m->prox->prox->prox=(Lint)malloc(sizeof(Nodo));
	m->prox->prox->prox->valor=8;
	m->prox->prox->prox->prox=NULL;
	int r = removeMaior(&m);
	imprime(m);
	printf("%d\n", r);
	return 0;
}
