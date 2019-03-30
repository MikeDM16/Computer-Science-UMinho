#include <unistd.h>
#include <stdlib.h>
#include <fcntl.h>
#include <stdio.h>
#include <string.h>

int main(int argc, char** argv){
	int ret;
	char* exec_args[]= {"/bin/ls", "-l", NULL};
	ret= execl ("/bin/ls", "/bin/ls", "-l", NULL);
	perror("reached return");
	return 0;
}