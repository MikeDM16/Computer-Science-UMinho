#include <unistd.h>
#include <fcntl.h>

int main(int argc, char* args[]){
	int file = open("10mb.txt", O_CREAT |O_TRUNC | O_RDWR,0640);
	int i; char s[10] = "aaaaaaaaa\n";
	for(i=0; i!=(1024*1024); i++){
		write(file, s, 10); 
	}
	return 0;
}

/*
	int i; char s = 'a';
	for(i=0; i!=(8*1024*1024); i++){
                write(file, s, 1);  
        }

*/
