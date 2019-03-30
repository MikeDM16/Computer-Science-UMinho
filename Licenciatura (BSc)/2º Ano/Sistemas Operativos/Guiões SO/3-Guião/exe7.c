#include <unistd.h>
#include <fcntl.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <sys/wait.h>


void bash(){
	int n, i, pid;
	char buffer[1024];
	char* executar[512];
	char* s;

	for(i=0; i!=sizeof(buffer); buffer[i++]='\0');

	while((n=read(0, buffer, 1024)) >0 ){
		
		s = strtok(buffer, " \n\0");
		for(i=0; s!=NULL; i++){
			//executar[i] = strdup(s);
			executar[i] = (char*) malloc(n*sizeof(char));
			strcpy(executar[i], s);
			printf("executar[%d]= %s\ns = %s\n",i+1, executar[i], s);
			s=strtok(NULL, " \n\0");
		}
		executar[i]=NULL;

		if(strcmp(executar[0], "exit\n")==0){
			execlp("exit","exit", NULL);
			perror("exit");
		}

		if((pid=fork())==0){
			execvp(executar[0], executar);
			perror(executar[0]);
		}else wait(NULL);

		for(i=0; i!=sizeof(buffer); buffer[i++]='\0');

	}
}

int main(void){

	bash();

	return 0;
}