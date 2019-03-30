#include <unistd.h>
#include <stdlib.h>
#include <fcntl.h>
#include <stdio.h>
#include <string.h>
#include <sys/wait.h>


int main() {
	  pid_t pid; filho, status;
	  pid= fork();
	  if (pid==0) { /*filho*/
          printf("filho: pid = %d, ppid = %d\n", getpid(),getppid());
          _exit(0);
      } 
      else { /*pai*/
      	  printf("pai: pid = %d, ppid = %d\n, filho=%d\n" getpid(),getppid(), pid);
      	  filho= wait(&status);
      	  printf("pai:morreu filho=%d\n", filho);
      	  if (WIFEXITED(status)) {
      	  	       printf("pai: filho terminou = %d\n", WEXITSTATUS(status));
      	  }
      	  else {
      	  	printf("pai: filho sinal = %d\n", WTERMSIG(status));
      	  }
      }
	  return 0;
}