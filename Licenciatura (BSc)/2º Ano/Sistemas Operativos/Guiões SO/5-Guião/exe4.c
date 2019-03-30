#include <unistd.h>
#include <fcntl.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <sys/wait.h>
#include <time.h>

int main(){
	int pd[2], n, i;
	char buffer[1024];
	//char instrucao[512] = "ls /etc | wx -l";
	char *s;
	//char *comandos[512];

	pipe(pd);
	
	/*
	s = strtok(instrucao, "|");
	for(i=0; s!=NULL; i++){
		comandos[i] = (char*) malloc((strlen(s)+1)*sizeof(char));
		strcpy(comandos[i], s);
		s = strtok(NULL, "|");
	}
	comandos[i]=NULL;

	for(i=0; comandos[i]!=NULL; i++){
			write(1, comandos[i], strlen(comandos[i]));
			putchar('\n');
	}*/

	if(fork()==0){
		close(pd[0]);
		dup2(pd[1],1);
		close(pd[1]);
		execlp("ls", "ls", "/etc", NULL);
		perror("ls");
		_exit(1);
	}

	close(pd[1]);
	dup2(pd[0],0);
	
	execlp("wc", "wc", "-l", NULL);
	perror("wc");
	
	return 0;
}