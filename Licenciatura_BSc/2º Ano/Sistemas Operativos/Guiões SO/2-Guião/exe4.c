#include <unistd.h>
#include <fcntl.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <sys/wait.h>
/*

int main(){
	int status, i;
	pid_t pid;

	for(i=0; i!=10; i++){
		if((pid=fork())==0){
			printf("Sou o filho Nº%d\n pid = %d",i+1,getpid());
			_exit(i+1);
		}
	}

	for(i=0; i!=10; i++){
		wait(&status);
		if(WIFEXITED(status))
			printf("O filho retornou %d\n",WEXITSTATUS(status));
	}

	return 0;
}*/

int main(){
	int pid[10], i, status;

	for(i=0; i!=10; i++){
		if((pid[i]=fork())==0){
			printf("Sou o filho Nº%d pid = %d\n",i+1,getpid());
			_exit(i+1);
		}
	}

	for(i=0; i!=10; i++){
		waitpid(pid[i], &status, 0);
		if(WIFEXITED(status))
			printf("O filho %d retornou %d\n",pid[i], WEXITSTATUS(status));
	}


}