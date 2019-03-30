#include "stdio.h"
#include "string.h"
#include "stdlib.h"

typedef struct slist *LInt;
typedef struct slist {
	int valor;
	struct slist *prox;
} Nodo, *Lint;

int lenght (Lint l){
	int c;
	if (l==NULL) c=0;
	else while(l){
		c++;
		l=l->prox;
	}
	return c;
}
int main(){
	int i;
	Lint m=NULL;
	for(i=0;i<10;i++){
		Lint nova = (Lint)malloc(sizeof(Nodo));
		nova->valor=i;
		nova->prox=m;
		m=nova;
			}
	/*while(m){
		printf("%d\n",m->valor);
		m=m->prox;
	}*/
	int r = lenght (m);
	printf("%d\n", r);
	return 0;
}


