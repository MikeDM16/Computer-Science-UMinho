#include <unistd.h>
#include <fcntl.h>
#include <stdlib.h>
#include <stdio.h>

int main(int argc, char* args[]){
	int NBytes = 2;
	char buffer[NBytes];
	int fim = 1, lidos;
	while((lidos=read(0,buffer,NBytes)) != NBytes ){
		read(0, buffer,NBytes-lidos);
		printf("%d", lidos);
	}
	write(1,buffer,NBytes);
	return 0;
}

/*	char buffer[8*atoi(args[1])];
	int fim = 0;
	while(fim!=1){
		read(0,&buffer,1);
		if( sizeof(buffer)==8*(atoi(args[1])) ){fim =1; }
	}

	write(1, buffer, 8*atoi(args[1]));
	return 0;
}
*/

