#include <unistd.h>
#include <stdio.h>
#include <stdlib.h>
#include <sys/wait.h>

int main(){
	pid_t pid;
	int x, i;
	pid = fork();
	for(i=1; i<=10; i++){
		if(pid==0){
			printf("PID (do filho %d) = %d, PPID =%d\n",i, pid, getppid());
			printf("Processo PAI: Codigo de saida do filho = %d\n", i);
			return(i);
		}else{
		//	pid = fork();
		//	printf("Processo PAI: Codigo de saida do filho = %d\n", i);
			pid = fork();	
		}


	}
	_exit(1);
	
}
