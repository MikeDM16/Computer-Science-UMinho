

int main(){
	int fdi = open("teste.txt", O_RONLY);
	int fdo = open("teste1.txt", O_CREAT|O_RWONLY, 0644);

	if(fork()==0){
		dup2(fdi,0);
		close(fdi);
		dup2(fdo,1);
		close(fdo);
	//
		execlp("rev","rev",NULL);
		perror("rev");
		_exit(1);
	}else{
		wait(NULL);	
	}

	close(fdi); //fechar sempre os ficheiros abertos
	close(fdo);
}
