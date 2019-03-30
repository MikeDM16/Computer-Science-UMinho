#include <unistd.h>
#include <sys/wait.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
int main(int argn, char* args[]){
	char* s = "\n";
	for(int i=1; args[i]; i++){
		write(1, args[i], strlen(args[i])+1);
		write(1,s,strlen(s)+1);
	}

	return 0;	
}
