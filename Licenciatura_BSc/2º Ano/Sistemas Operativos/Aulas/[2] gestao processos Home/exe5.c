#include <unistd.h>
#include <sys/wait.h>
#include <stdio.h>
#include <stdlib.h>

int main(void){
	pid_t pid; 
	int i,x; 
	pid = fork();
	for(i=0; i<10; i++){
	/*	if(pid!=0){
			
			wait(NULL);
		}else{
			printf("PID = %d, PPID = %d.\n", getpid(), getppid());
			pid =fork();
		}
	}*/

		if(pid==0){
			printf("Pid = %d, PPID= %d.\n", getpid(), getppid());
			pid = fork();
		}else{
			wait(NULL);
		}
	}
	return EXIT_SUCCESS; 
}
