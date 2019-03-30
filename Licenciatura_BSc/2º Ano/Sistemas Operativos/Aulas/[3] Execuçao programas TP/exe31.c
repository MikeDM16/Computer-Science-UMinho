#include <unistd.h>
#include <stdlib.h>
#include <stdio.h>
#include <fcntl.h>
#include <sys/wait.h>

int main(int argn, char *args[]){
	for(int i=0; i!=argn; i++){
		puts(args[i]);
	}
	return 0;
}
