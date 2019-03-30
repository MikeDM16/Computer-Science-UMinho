#include <unistd.h>
#include <stdlib.h>
#include <fcntl.h>
#include <stdio.h>
#include <string.h>
#include <sys/wait.h>

int main(){
 int i;

 	for(i=0; i<10;i++){
 		if(fork()==0) { /*filho*/
 						/*fazer o que tiver que fazer*/
 						_exit(0);}
 	}
 	/*wait(NULL);*/ /*executa sequencialmente com este wait, sem ele executa concorrentemente */
}
	for(i=0;i<10;i++) wait(NULL); /*para usar este NULL é quando não se quer saber como os filhos morreram, 
	                                o que a seguir vem é para cada filho saber como morreu */

int status;

pid_t pid=wait(&status);

if (WIFEXITED (status))	printf("%d\n", WEXITSTATUS(status));	

