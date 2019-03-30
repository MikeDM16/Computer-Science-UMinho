#include <unistd.h>
#include <stdlib.h>
#include <stdio.h>
#include <fcntl.h>
#include <sys/wait.h>

int main(int argn, char* args[]){
	int i; 
	for(i=0; i!=argn; i++){	
		if(fork()==0){
			execlp(args[i],args[i], NULL);
			perror(args[i]);
			return 1; //_exit(1);
		}	
	}
	//colocar wait dentro do ciclo quebra com a eventual recorrencia
	for(i=0; i!=argn; i++)
		wait(NULL);

	return 0;
}
