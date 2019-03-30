#include <sys/types.h>
#include <sys/stat.h>
#include <fcntl.h>
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>

int main(){
	int fd, n;
	puts("open para leitura"); 
	fd = open("fifo", O_RDONLY);
	char buffer[512];
	puts("suesso");

	while((n=read(fd, buffer, sizeof(buffer)))>0)
		write(1,buffer,n);
	close(fd);
	return 0;	
}
