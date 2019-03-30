#include <unistd.h>
#include <stdlib.h>
#include <fcntl.h>
#include <stdio.h>
#include <string.h>
#include <sys/wait.h>

int main(int argc, char** argv){
	int i;
	for(i=0;i<argc;i++){
		write(1,argv[i], strlen(argv[i]));  //printf("argv[%d]= %s\n", i,argv[i]")
		write(1,"\n",1);
	}
	return 0;
}