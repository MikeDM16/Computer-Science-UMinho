#include <unistd.h>
#include <fcntl.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <sys/wait.h>

int main(int argn, char* args[]){

	args[0] = (char*) malloc(strlen("Invoca exe3")*sizeof(char));
	strcpy(args[0], "invoca exe3");

	execvp("./exe3", args);
	perror("./exe3");

	return 0;
}