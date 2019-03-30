#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "arraysDinamicos.h"


struct array_dinamico{
    void **elementos;
    int posicao;
    int capacidade;
};

static void reallocAD(ARRAY_DINAMICO);



/*
 * Inicialização e Free
 */


ARRAY_DINAMICO inicializaAD(int capacidade){
    ARRAY_DINAMICO arrayD = (ARRAY_DINAMICO) malloc(sizeof(struct array_dinamico));
    
    if(arrayD){
        arrayD->elementos = (void **) malloc(sizeof(void *) * capacidade);
    }else{
        return NULL;
    }
    if(arrayD->elementos != NULL){
        arrayD->posicao = 0;
        arrayD->capacidade = capacidade;
    }else{
        free(arrayD);
        return NULL;
    }
    return arrayD;
}



void freeAD(ARRAY_DINAMICO arrayD){
    if (arrayD != NULL) {
        free(arrayD->elementos);
    }
    free(arrayD);
}

/*
 * Gets do tamanho e da capacidade
 */

int getTamanhoAD(ARRAY_DINAMICO arrayD){
    return arrayD->posicao;
}

int getCapacidadeAD(ARRAY_DINAMICO arrayD){
    return arrayD->capacidade;
}

/*
 * Inserir um elemento
 */

void insereElementoAD(ARRAY_DINAMICO arrayD, void *elemento){
    reallocAD(arrayD);
    arrayD->elementos[arrayD->posicao] = elemento;
    arrayD->posicao++;
}


/*
 * Get elemento num dada posição
 */

void * getElementoPosAD(ARRAY_DINAMICO arrayD, int pos){
    if (pos < arrayD->posicao){
        return arrayD->elementos[pos];
    } 
    else {
        return NULL;
    }
}

/*
 * Função inerente ao módulo 
 */


static void reallocAD(ARRAY_DINAMICO arrayD){
    int capNova;
    void **elemsNovo;
    
    if(arrayD->posicao == arrayD->capacidade){
        capNova = arrayD->capacidade*2;
        elemsNovo = (void **) realloc(arrayD->elementos,sizeof(void *) * capNova);
        
    if (elemsNovo != NULL){
        arrayD->elementos = elemsNovo;
        arrayD->capacidade = capNova;
      }
   }
}




