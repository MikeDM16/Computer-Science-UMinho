#include <signal.h>
#include <sys/types.h>
#include <stdio.h>
#include <unistd.h>
typedef void (*sighandler_t)(int);

void f(int signal){
	static int segundos = 0, controlcs = 0;
	switch (signal){
		case SIGALRM: 	segundos++;
				alarm(1);
				break;
		case SIGINT: 	controlcs++; 
				printf("segundos = %d\n", segundos);
				break;
		case SIGQUIT:	printf("control-c = %d\n", controlcs);
				_exit(0);
		default: 	puts("heim??");
	}
	alarm(1);	
	puts("nao morre com crtl-c !");
}

int main(void){
	signal(SIGINT, f);
	signal(SIGQUIT,f);
	signal(SIGALRM, f);
	alarm(1);
	while(1) pause();
	return 0;
}
