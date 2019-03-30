#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <sys/wait.h>
#include <string.h>

int main(int argn, char* cmd[]){
	
	int i;
	for(i=1; i!=argn; i++){
		if(fork()==0){
			printf("Filho %d\n",i);
			execlp(cmd[i],cmd[i],NULL);
			perror(cmd[i]);
		}
	}
	for(i=1; i!=argn; i++) wait(NULL);	

	return EXIT_SUCCESS;

}
