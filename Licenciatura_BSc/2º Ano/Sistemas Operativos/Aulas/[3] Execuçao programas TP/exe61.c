#include <unistd.h>
#include <stdlib.h>
#include <stdio.h>
#include <fcntl.h>
#include <sys/wait.h>

int mysystem(char* cmd){
	int s, i; pid_t pid;
	if((pid = fork())==0){//filho
		execlp(cmd, cmd, NULL);
		_exit(1);
	}
	waitpid(pid,&s,0);
	//while(wait(&s)!=pid);
	
	return 
}
