#include <unistd.h>
#include <sys/wait.h>
#include <stdio.h>
#include <stdlib.h>

#define MAX 100000000

int a[MAX];

int main(){
	int i, j, status;
	a[73875678] = 1;
	//for(i=0; i!=MAX && a[i]!=1; i++);
	//if(i!=MAX) puts("encontrei");
	for(j=0; j!=4; j++){
		if(fork()==0){
		for(i=0; i!=MAX/4 && a[i+j*MAX/4]!=1; i++)
		_exit(i== MAX ? 0 : 1);
		}
	}
	for(j= 0; j!=4; j++){
		wait(&status);
		if(WIFEXITED(status))
			if(WEXITSTATUS(status))
				puts("encontrei");
			return 0;
	}
	return EXIT_SUCCESS;
}
