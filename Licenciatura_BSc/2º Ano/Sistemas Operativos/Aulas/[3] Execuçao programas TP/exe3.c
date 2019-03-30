#include <unistd.h>
#include <stdlib.h>
#include <stdio.h>
#include <fcntl.h>
#include <sys/wait.h>

int main(void){
	if(fork()==0){
		char * args[] = {"ls","-l",NULL};
		execvp("ls", args);
		perror("ls");
		return 1;
	}
	wait(NULL);
	return 0;
}
