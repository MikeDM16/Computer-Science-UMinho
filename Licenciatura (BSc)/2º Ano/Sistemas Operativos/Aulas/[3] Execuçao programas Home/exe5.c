#include <unistd.h>
#include <sys/wait.h>
#include <stdio.h>
#include <stdlib.h>

int main(int argn, char* args[]){
	printf("Processo Pai\n");
	int i;	
	for(i=1; i!=argn; i++){
	
		if(fork()==0){
			printf("Processo filho %d\n", i);
			execlp(args[i], args[i],NULL);
			perror(args[i]);
			_exit(1);
			}
		else{printf("depois do filho %d\n", i); }
	}

	for(i=1; i!=argn; i++) wait(NULL);
	
	return 0;	
}
