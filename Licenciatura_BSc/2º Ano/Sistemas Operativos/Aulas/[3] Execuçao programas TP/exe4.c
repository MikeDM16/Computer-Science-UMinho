#include <unistd.h>
#include <stdlib.h>
#include <stdio.h>
#include <fcntl.h>
#include <sys/wait.h>

int main(void){
	if(fork()==0){
		char * args[] = {"exe3","ls", "-l",NULL};
		execvp("./exe3", args);
		perror("./exe3");
		return 1;
	}
	wait(NULL);
	return 0;
}
