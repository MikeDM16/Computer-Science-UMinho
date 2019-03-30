#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include <unistd.h>
#include <sys/wait.h>

int main(void){

	int fd[2];

	char string[] = "Hello, world!\n";
	char readbuffer[80];

	pipe(fd);
	
	write(fd[1], string, strlen(string)+1);
	
	read(fd[0], readbuffer, sizeof(readbuffer));
	printf("%s", readbuffer);
	
	return 0;
}
