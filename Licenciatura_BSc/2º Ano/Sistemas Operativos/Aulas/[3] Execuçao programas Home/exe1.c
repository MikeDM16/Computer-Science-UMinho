#include <unistd.h>
#include <sys/wait.h>
#include <stdio.h>
#include <stdlib.h>

int main(void){
	printf("Antes do execlp\n");
	execl("/bin/ls", "ls","-l",  NULL);
	perror("ls");
	printf("Depois do execlp\n"); /*Nao deve chegar até aqui */
	return 0;	
}
