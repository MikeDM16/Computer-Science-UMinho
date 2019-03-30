#include <unistd.h>
#include <stdio.h>
#include <stdlib.h>

int main(){
	pid_t pid;
	pid = fork();
	if(pid==0){
	/*processo filho*/
	printf("PID (do filho) = %d, PPID (do filho) = %d\n", getpid(), getppid());
	}
	else{
	/*processo pai*/
	printf("PID (do pai) = %d, PPID (do pai) = %d, PID do filho criando = %d\n", getpid(), getppid(), pid);
	}
	_exit(0);
}

