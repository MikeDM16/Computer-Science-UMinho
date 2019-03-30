#include <sys/wait.h>
#include <unistd.h>
#include <stdio.h>
#include <stdlib.h>


int main(void)
{
	puts("ini-");

	printf("%s\n", getenv("USERNAME"));
	
	puts("ola");
	if(fork()==0){
		puts("criar diretoria" );
		execlp("mkdir", "mkdir", "/home/user/.Backup", NULL);
		perror("mkdir");
	}
	return 0;
}
