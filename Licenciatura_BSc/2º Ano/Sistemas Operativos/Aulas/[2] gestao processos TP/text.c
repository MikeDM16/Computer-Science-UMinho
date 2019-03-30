#include <stdio.h>
#include <stdlib.h>

int main(){
	//printf("hello World");
	int m[5][10];
	int i,j;
	for(i=0; i<5;i++)
		for(j=0;j<10;j++){ m[i][j]=i*j + 3*j + i;  }
	
	for(i=0; i<5;i++)
                for(j=0;j<10;j++){ printf("%d\n", m[i][j]); }

	return 0;
}
