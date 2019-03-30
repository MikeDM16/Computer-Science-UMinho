#include <unistd.h>
#include <sys/wait.h>
#include <stdio.h>
#include <stdlib.h>
#include <fcntl.h>

int main(void){
	int saida, error,  imput;
        char c; 
		
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
	
	if(fork()==0){ /*filho*/
		while(( read(0, &c, 1) == 1)){
			write(1,&c,1);
			write(2,&c,1);
		}
	} 
	 
	return 0;
}
