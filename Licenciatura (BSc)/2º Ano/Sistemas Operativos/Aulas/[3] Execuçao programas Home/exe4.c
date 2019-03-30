#include <unistd.h>
#include <sys/wait.h>
#include <stdio.h>
#include <stdlib.h>

int main(int argn, char* args[]){
	printf("Processo Pai\n");
	
	if(fork()==0){
		printf("Processo filho");
		execv("./exe3", args);
		perror("./exe3");
		}
	
	printf("Depois do Processo filho\n"); 
	wait(NULL);
	
	return 0;	
}
