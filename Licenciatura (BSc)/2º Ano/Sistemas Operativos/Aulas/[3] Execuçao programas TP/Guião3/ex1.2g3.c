#include <unistd.h>
#include <stdlib.h>
#include <fcntl.h>
#include <stdio.h>
#include <string.h>
#include <sys/wait.h>

int main(){
puts("em antes:-");
exec("ls","ls", "-l", NULL);
puts("depois:");
return 1;
}