#include <unistd.h>
#include <stdlib.h>
#include <stdio.h>
#include <fcntl.h>

int main(void){
	execlp("ls","ls","-l", NULL);
	//o normal é o primeiro e segundo argumento se repetirem. 
	//execl terminam sempre com NULL
	//se o execlp correr bem, o resto do codigo é integralmente substituido pelo main da funcao chamada
	//o return 0 nao é executado a menos que o execlp falhe
	//--> tratamento de erro: perror("NOME");
	perror("ls");
	return 0;
	
	
}
