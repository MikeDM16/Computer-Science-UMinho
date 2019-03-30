#include <unistd.h>
#include <sys/wait.h>
#include <stdio.h>
#include <stdlib.h>

//void procura(int p, int mat, int l, int c){}
int main(void){
	int l = 6, c = 100000;
	int mat[l][c];

	int i,j;
	for(i=0; i<l; i++)
		for(j=0;j<c; j++)
			mat[i][j]=0;
	mat[2][134]=mat[5][12]=1;
	mat[1][6221]=mat[4][92]=mat[2][92323]=1;
		
	pid_t pid;
	for(i=0; i!=l; i++){
		pid = fork();
		if(pid==0){
			for(j=0; j!=c; j++){
				if(mat[i][j]==1){
					printf("Encontrei pos[%d][%d]\n", i,j);
				}
			}
			_exit(0); //Encontrou;
		}
	}
	for(i=0; i!=l; i++) wait(NULL);
	return 0;
}


