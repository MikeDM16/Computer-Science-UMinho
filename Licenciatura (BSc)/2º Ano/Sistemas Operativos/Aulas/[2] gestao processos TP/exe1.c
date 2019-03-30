#include <unistd.h>
#include <stdio.h>
#include <stdlib.h>

int main(){
	printf("PID processo = %d, PPID do processo = %d\n", getpid(), getppid());	return 0;
}

