#include <fcntl.h>
#include <unistd.h>

#include <stdio.h>
#include <string.h>
#include "gpio.h"


#ifdef ANDROID
#include <android/log.h>
#define log_tag "gpio_Android"
#endif


//Might not be the best idea as not all 20 are used.... or
// maybe it's a great idea, I don't know I'm just a comment
int fd_pins[20];

char pinPath[256];


int allowablePins[] = {
		74,
		75,
		76,
		83,
		88,
		97,
		98,
		99,
		100,
		101,
		105,
		106,
		107,
		108,
		115,
		116
};

bool writeToFD(int fd, int value){
	lseek(fd, 0, SEEK_SET);
	char buffer[4]; // should be more than enough for simple sets
	memset((void *)buffer, 0, sizeof(buffer));
	sprintf(buffer, "%c", value);
	write(fd, buffer, sizeof(buffer));
	return true;
}


bool pinMode(int pin, int state){
	// Clear out pinPath
	memset(pinPath, 0 , sizeof(pinPath));
	sprintf(pinPath, "%s%d%s", GPIO_PATH, pin, "/direction");
//	sprintf(pinPath, "%s%s%d", GPIO_MODE_PATH, GPIO_NAME, pin);

//	if (state != 0 || state != 1)
//		return false;	// only setting for

	int fd_pin;

	if( (fd_pin = open(pinPath, O_RDWR)) == -1) {
		printf("failed to set pin %d, to %d", pin, state);
		perror("=(");
	} else {
		// Some day I'll support multiple state types, for now just the 2
		switch(state){
		case 0: // IN
			write(fd_pin, "in", 3);
			break;
		case 1:	//OUT
			write(fd_pin, "out", 4);
			break;
		default:
			printf("set state %d not allowable for pin %d", state, pin);
			break;
		}
	}

}

int analogRead(int pin){
	int analogValue = -1;
	return analogValue;
}

bool digitalWrite(int pin, int state){
	memset(pinPath, 0 , sizeof(pinPath));
	sprintf(pinPath, "%s%d%s", GPIO_PATH, pin, "/value");

//	if (state != 0 || state != 1)
//		return false;	// only setting for

	int fd_pin;

	if( (fd_pin = open(pinPath, O_RDWR)) == -1) {
		printf("failed to set pin %d, to %d", pin, state);
		perror("=(");
		return false;
	} else {
		switch(state){
		case 0: // IN
			write(fd_pin, "0", 2);
			break;
		case 1:	//OUT
			write(fd_pin, "1", 2);
			break;
		default:
			printf("set state %d not allowable for pin %d", state, pin);
			return false;
			break;
		}
	}
	return true;
}

int digitalRead(int pin){
	int pinValue = -1;

	memset(pinPath, 0 , sizeof(pinPath));
	sprintf(pinPath, "%s%d%s", GPIO_PATH, pin, "/value");

	int fd_pin;

	if( (fd_pin = open(pinPath, O_RDONLY)) == -1) {
		printf("failed to read pin %d", pin);
		perror("=(");
		return false;
	} else {
		char pinChar;
		read(fd_pin, &pinChar, 1);

		switch(pinChar){
		case '0':
			return 0;
			break;		// I think this is unreachable...
		case '1':
			return 1;
			break;
		default:
			printf("Unknown value read from pin %d", pin);
			return -1;	// ERROR
		}
	}

	return -1;
}

bool exportPin(int pin){
	char tempPath[256];
	memset(tempPath, 0, 256);

	int fd_exportPath;

	if( (fd_exportPath = open(GPIO_EXPORT, O_RDWR)) == -1 ){
		printf("unable to export pin %d", pin);
		perror("=(");
		return false;
	} else {
		return writeToFD(fd_exportPath, pin);
	}
	return true;
}

bool unexportPin(int pin){
	char tempPath[256];
	memset(tempPath, 0, 256);

	int fd_exportPath;

	if( (fd_exportPath = open(GPIO_UNEXPORT, O_RDWR)) == -1 ){
		printf("unable to unexport pin %d", pin);
		perror("=(");
		return false;
	} else {
		return writeToFD(fd_exportPath, pin);
	}
	return true;
}







// array position is pin#
// value at that pos is gpio number
//#define JUMPER_SIZE 20
//int pintoSysfs[JUMPER_SIZE] = {
//		0,
//		0,
//		0,
//		0,
//		0,
//		0,
//		0,
//		0,
//		0,
//		0,
//		0,
//		0,
//		0,
//		0,
//		0,
//		0,
//		0,
//		0,
//		0,
//		0
//};

//int getSysfsNumber(int pinNumber){
//	if(pinNumber < JUMPER_SIZE)
//		return 0;
//	else return pintoSysfs[pinNumber];
//}
//
//bool writeFS(int fd, int value){
//	char buffer[4];
//	memset((void*)buffer, 0, sizeof(buffer) );
//
//	//sprintf does not always play nice on embedded sys
//	sprintf(buffer, "%c", value);
//
//	lseek(fd, 0, SEEK_SET);
//
//	write(fd, buffer, sizeof(buffer));
//}
//



bool isPinOutput(int pin){
	return false;
}
