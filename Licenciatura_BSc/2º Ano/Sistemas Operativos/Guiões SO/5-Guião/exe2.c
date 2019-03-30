#include <unistd.h>
#include <fcntl.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <sys/wait.h>
#include <time.h>

int main(){
	int pd[2], n;
	char * s = "tentativa\n";
	char buffer[1024];
	pipe(pd);

	if(fork()==0){
		close(pd[1]);
		while((n=read(pd[0], buffer, sizeof(buffer)))>0){
			write(1, buffer, n);
		}
		_exit(0);
	}
	close(pd[0]);
	for(n=0; n!=4; n++){
		sleep(2);
		write(pd[1], s, strlen(s));
	}
	
	//wait(NULL);

	return 0;
}