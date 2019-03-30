#include <unistd.h>
#include <sys/wait.h>
#include <stdio.h>
#include <stdlib.h>

int main(void){
	pid_t pid = fork();
	if(pid==0){
		printf("Processo filho\n--->Pid= %d, Pid pai = %d.\n", getpid(), getppid());
		_exit(0);	
	}
	wait(NULL);
	printf("Processo Pai\n--> Pid= %d.\n", getpid());
	return EXIT_SUCCESS;
}
