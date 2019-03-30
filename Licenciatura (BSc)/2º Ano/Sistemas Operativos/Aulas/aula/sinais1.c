#include <signal.h>
#include <unistd.h>
#include <sys/types.h>
#include <stdio.h>

void f1(){
	int  t = alarm(10);
	printf("tempo = %d", t);
	
}

int main(){
	
	char buff[100];
	int n;
	//signal(SIGINT, f1); 
	while((n=read(0,buff, 100))>0){
		signal(SIGINT, f1);	
		write(1,buff,n);
	}
	return 0;
}
