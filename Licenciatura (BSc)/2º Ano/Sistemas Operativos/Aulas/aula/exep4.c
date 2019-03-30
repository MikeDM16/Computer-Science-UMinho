#include <unistd.h>
#include <sys/wait.h>
#include <stdio.h>
#include <stdlib.h>
#include <fcntl.h>

int main(int argn, char* args[]){
	int i;
	char* output, input;
	if(strcmp(args[1], "-i")==0){
		input = args[++i];
		i = 3;
		}
	if(strcmp(args[3], "-o")==0){
		output = args[4];
		i = 5; 
		}
	

	imput = open("/etc/passwd", O_RDONLY);
	if(imput == 1){
		perror("etc/passwd");
		_exit(1);
	}
	/* close(0); dup(fd); */
	dup2(imput, 0);
	close(imput); // está atribuido ao imput. Posso fechar o descritor criado pelo open
	saida = open("saida.txt", O_CREAT | O_WRONLY | O_APPEND | O_TRUNC, 0666);
	error = open("erros.txt", O_CREAT | O_WRONLY | O_APPEND | O_TRUNC, 0666);
	dup2(saida, 1); close(saida);	
	dup2(error, 2); close(error);

	execlp("wc", "wc", "-l", NULL);
	perror("wc");
	
	puts("Sucesso!");
	return 0;
}
