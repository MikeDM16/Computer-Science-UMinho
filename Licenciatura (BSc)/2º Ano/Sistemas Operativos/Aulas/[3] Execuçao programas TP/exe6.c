#include <unistd.h>
#include <stdlib.h>
#include <stdio.h>
#include <fcntl.h>
#include <sys/wait.h>

int main(int argn, char* args[]){
	int i,s, sucesso =0;
	pid_t arrayPides[30], pid; 
	for(i=1; i!=argn; i++){	
		arrayPides[i-1]=fork();
		if(arrayPides[i-1]==0){
			execlp(args[i],args[i], NULL);
			perror(args[i]);
			return 1; //_exit(1);
		}	
	}
	//colocar wait dentro do ciclo quebra com a eventual recorrencia
	for(i=0; i!=argn; i++){
		int j;
		pid=wait(&s);
		if(WIFEXITED(s)){
			if(WEXITSTATUS(s)==0) sucesso++;
			for(j=0; j!=argn  && arrayPides[j]!=pid; j++) 
				printf("o filho executou com sucesso %d\n", arrayPides[j]);			}	
	}	
	return 0;		
}	
