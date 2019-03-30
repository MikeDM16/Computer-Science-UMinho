#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include <unistd.h>
#include <sys/wait.h>
#include <string.h>

int main(void){
	
	int pd[2];
 	char* s = "ola ";
	char* s2 = "mundo! \n";

	write(pd[1],s, strlen(s)+1);
	write(pd[1],s2, strlen(s)+1);
	
	char buffer[80];
	read(pd[0], &buffer, strlen(buffer));
	printf("%s", buffer); 	
	
	close(pd[0]);	
	close(pd[1]);

	return 0;
}
