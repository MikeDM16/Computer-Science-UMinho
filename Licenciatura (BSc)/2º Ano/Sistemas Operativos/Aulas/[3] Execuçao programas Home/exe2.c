#include <unistd.h>
#include <sys/wait.h>
#include <stdio.h>
#include <stdlib.h>

int main(void){
	printf("Processo Pai\n");
	
	if(fork()==0){
		printf("Processo filho");
		execl("/bin/ls", "ls","-l",  NULL);
		perror("ls");
		}
	
	printf("Depois do Processo filho\n"); /*Nao deve chegar até aqui */
	wait(NULL);
	
	return 0;	
}
