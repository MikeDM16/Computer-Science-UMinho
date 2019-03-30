#include <unistd.h>
#include <fcntl.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <sys/wait.h>

int main(int argn, char *args[]){
	int i=0;

	while(args[i]!=NULL){
			write(1, args[i], strlen(args[i]));
			putchar('\n');
			i++;
	}

	return 0;
}