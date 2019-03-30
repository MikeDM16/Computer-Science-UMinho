#ifndef ARRAYS_DINAMICO
#define	ARRAYS_DINAMICO

typedef struct array_dinamico* ARRAY_DINAMICO;

ARRAY_DINAMICO inicializaAD(int capacidade);
void freeAD(ARRAY_DINAMICO arrayD);
int getTamanhoAD(ARRAY_DINAMICO arrayD);
int getCapacidadeAD(ARRAY_DINAMICO arrayD);

void insereElementoAD(ARRAY_DINAMICO arrayD, void *elemento);
void *getElementoPosAD(ARRAY_DINAMICO arrayD, int pos);

#endif

