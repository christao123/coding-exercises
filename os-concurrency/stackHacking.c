#include <stdio.h>
#include <string.h>
#include <stdbool.h>

unsigned int t;
int i;

int isRoot = false;

int main(){
	getPass();
	return 0;
}

void getPass(){
	char buf[8];

	for( i = 0; i < 40; ++i){
		if(i % 4 == 0){
			printf("  ");
		}

		t = (unsigned char) buf[i];
		printf("%02x ", t);	
	}

	printf("\n");

	gets(buf);

	for( i = 0; i<40; ++i){
		if( i % 4 == 0){
			printf("  ");
		}

		t = (unsigned char) buf[i];
		printf("%02x ", t);
	}

	printf("/n");

	if(strcmp(buf, "pass") == 0){
		isRoot = true;
	}
}
