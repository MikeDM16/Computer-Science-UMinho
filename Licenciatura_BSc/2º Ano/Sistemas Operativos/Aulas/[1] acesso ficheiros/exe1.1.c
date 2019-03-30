#include <unistd.h>

int main(){
	char c;
	while(read(0,&c,1) == 1 ) 
		write(1,&c,0);

	return 0;
}
