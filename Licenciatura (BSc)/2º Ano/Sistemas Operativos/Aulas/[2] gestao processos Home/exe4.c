#include <unistd.h>
#include <sys/wait.h>
#include <stdio.h>
#include <stdlib.h>

int main(void){
	pid_t pid;
	int i,x;
	for(i=0; i!=10; i++){
	 	pid = fork();
		if(pid==0) {
			printf("Processo filho. Pid = %d, PPID = %d.\n", getpid(),getppid());
			_exit(i);		
		}
	}
	for(i=0; i!=10;i++){
		wait(&x);
		printf("Processo Pai. PID = %d, PPID = %d.\n", getpid(), getppid());
		printf("Pid do filho = %d, retornou %d.\n\n", pid,x);	
	}
	return EXIT_SUCCESS; 
}
