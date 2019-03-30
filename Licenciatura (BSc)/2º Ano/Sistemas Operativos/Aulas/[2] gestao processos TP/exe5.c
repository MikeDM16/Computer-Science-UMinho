#include <unistd.h>
#include <stdio.h>
#include <stdlib.h>
#include <sys/wait.h>

int main(){
	pid_t pid;
	int x, i;
	pid = fork();
	for(i=1; i<=10;i++){
		if(pid!=0){
			wait(NULL);
			
		}else{
			printf("PID filho gerado =%d, PPID = %d\n", getpid(), getppid());
			pid = fork();	
		}


	}
	_exit(1);
	
}
