#include <unistd.h>
#include <fcntl.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <sys/wait.h>
#include <time.h>

/* Pai comunica com o filho */
int paiFilho(){
	int pd[2], n;
	char buffer[512];

	pipe(pd);

	if(fork()==0){
		puts("sou o filho");
		n = read(pd[0], buffer, sizeof(buffer));
		write(1, buffer, n);
		_exit(0);
	}else{wait(NULL);}

	char* s = "ola filho\n";
	sleep(5);
	write(pd[1], s, strlen(s));

	return 0;
}

/*filho comunica com pai */
int filhoPai(){
	int pd[2], n;
	char buffer[512];
	pipe(pd);

	if(fork()==0){
		puts("Sou o filho");
		sleep(5);
		char *s = "ola Pai\n";
		write(pd[1], s, strlen(s));
		_exit(0);
	}else{wait(NULL);}

	puts("Sou o pai");
	n = read(pd[0], buffer, sizeof(buffer));
	write(1, buffer, n);

	return 0;
}

int main(int argc, char const *argv[]){

	//paiFilho();
	filhoPai();

	return 0;
}