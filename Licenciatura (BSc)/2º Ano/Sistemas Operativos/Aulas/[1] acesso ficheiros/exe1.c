#include <unistd.h>
#include <fcntl.h>

int main(void){
	char buffer;
	while(read(0,&buffer,1)==1){
		write(1,&buffer,1);	
	}
	return 0;
}
