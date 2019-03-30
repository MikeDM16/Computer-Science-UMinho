#include <unistd.h>
#include <stdlib.h>
#include <stdio.h>
#include <fcntl.h>

int main(void){
	char * args[] = {"ls","-l",NULL};
	//raramente se usam array para passar as instrucoes
	execvp("ls", args);

	perror("ls");
	return 0;
	
	
}
