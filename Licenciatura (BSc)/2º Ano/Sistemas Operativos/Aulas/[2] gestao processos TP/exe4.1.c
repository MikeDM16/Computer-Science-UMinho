#include <unistd.h>
#include <sys/wait.h>
#include <stdio.h>
#include <stdlib.h>

int main(){
	int i, status;
	pid_t pid, filho;
	for(i=0; i!=10; i++){
		if(fork()==0){
		printf("filho = %d, %d\n", i, getpid());
		_exit(1);
		}
	}

	for(i=0;  i!=10; i++){
		filho = wait(&status);
		if(WIFEXITED(status)){
			printf("filho = %d, exit = %d\n",
				 filho, WIFEXITED(status));
			}
		}
	return EXIT_SUCCESS;
}
