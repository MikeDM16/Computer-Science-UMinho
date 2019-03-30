#include <unistd.h>
#include <stdlib.h>
#include <fcntl.h>
#include <stdio.h>
#include <string.h>
#include <sys/wait.h>

int main(int argc, char** argv){
	int fork_ret, exec_ret, wait_ret, status;
	char* exec_args[]={"/bin/ls", "-l", NULL};
	fork_ret=fork();
	if (fork_ret==0){
		printf("Sou o filho %d\n", getpid());
		//sleep(10);
		exec_ret=execvp("ls", exec_args);
		perror("reached return");
		_exit(exec_ret);
	} else {
		printf("Sou o pai %d\n", getpid());
		wait_ret= wait(&status);
		if(WIFEXITED(status))
			printf("o filho retornou %d\n", WEXITSTATUS(status));
		else printf("o filho terminou \n");
	}
	return 0;
}