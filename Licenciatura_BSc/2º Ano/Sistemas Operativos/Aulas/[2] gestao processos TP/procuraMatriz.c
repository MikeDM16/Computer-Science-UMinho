#include <unistd.h>
#include <stdio.h>
#include <stdlib.h>
#include <sys/wait.h>
#include <stdbool.h>

bool procuraMatriz(int elem, int  matriz, int linhas, int colunas){
	pid_t pid;
	int i, j ;
	for(i=0; i<linhas; i++){
		pid=fork();
		if(pid==0){
			for(j=0; j<colunas; j++){
				if(elem==matriz[i][j]){ 
					break;
				 }
			}
		}
	}
	if(elem==matriz[i][j]){ return true; }else{ return false; }
}

/* int criaMatriz(int N, int M){
	int i, j;
	int matriz[N][M];
	for(i=0;i<N;i++)
		for(j=0; j<M;j++){
			matriz[i][j] = i*j + 2*j +i ;			
		}
	return matriz; 
}*/

int main(){
	int linhas = 5, colunas = 100;
	int matriz[linhas][colunas];
	int i, j;
	//preencher matriz "aleatoriamente"
	for(i=0; i<linhas; i++)
		for(j=0; j<colunas; j++){
			matriz[i][j] = 3*j + i*j +i*i; 
		}
	
	int elem = 15;
	bool r = (procuraMatriz(elem, matriz, linhas, colunas));
	return 0;
}
