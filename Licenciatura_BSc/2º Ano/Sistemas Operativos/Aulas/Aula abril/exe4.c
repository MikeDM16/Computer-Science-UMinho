#include <stdio.h>
#include <unistd.h>
#include <sys/wait.h>
#include <stdio.h>
#include <string.h>

#define MSG "exemplo"

int main(void){
	int pd[2];
	pipe(pd);
	
	if(fork()==0){
		close(pd[1]);
		dup2(pd[0], 0);
		close(pd[0]);
		execlp("grep","grep","-v","^#", "/etc/passwd",NULL);
		perror("grep");
		return 1;
	}
	else{
		int pd1[2];
		pipe(pd1);
		if(fork()==0){
			close(pd1[1]);
			dup2(pd1[0],0);
			close(pd[0]);

			execlp("cut", "cut", "-f7","-d:", NULL);
			perror("cut");
		}
		else{
			int pd2[2];
			pipe(pd2);
			if(fork()==0){
				close(pd2[1]);
				dup2(pd2[0],0);
				close(pd2[0]);
				execlp("uniq","uniq",NULL);
				perror("uniq");		
				}
			}		
		}
	execlp("wc","wc","-l",NULL);
	perror("wc");
	return 1;
	} 

