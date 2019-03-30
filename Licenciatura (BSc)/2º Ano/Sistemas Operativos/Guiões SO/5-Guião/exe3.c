#include <unistd.h>
#include <fcntl.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <sys/wait.h>
#include <time.h>

int main(){
	int pd[2], i, n, fd;
	char buffer[1024];
	char *s, *fileName = "aux.txt";
	pipe(pd);

	if(fork()==0){
		dup2(pd[0],0);
		close(pd[0]);
		for(i=0; i!=sizeof(buffer); buffer[i++]='\0');

		while((n=read(0,buffer, sizeof(buffer)))>0){
			s = (char*) malloc(n*sizeof(char));
			strcpy(s, buffer);
			fd = open(fileName, O_CREAT | O_TRUNC | O_RDWR, 0666);
			write(fd, buffer, n);
			if(fork()==0){
				execlp("wc", "wc", fileName, NULL);
				perror("wc");
				_exit(1);
			}else wait(NULL);
		}

	}

	close(pd[0]);
	while((n=read(0,buffer, sizeof(buffer)))>0){
		write(pd[1], buffer, n);
	}

	return 0;
}