#include <sys/types.h>
#include <sys/stat.h>
#include <fcntl.h>
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>

int main(){
	int fd, n;
	puts("open para escrita"); 
	fd = open("fifo", O_WRONLY);
	char buffer[512];
	puts("suesso");

	while((n=read(0, buffer, sizeof(buffer)))>0)
		write(fd,buffer,n);
	close(fd);
	return 0;	
}
