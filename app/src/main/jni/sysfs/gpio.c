#include <fcntl.h>
#include <unistd.h>
#include <stdio.h>
#include <string.h>
#include "gpio.h"
#include <stdlib.h>
#include <errno.h>
#include <poll.h>
#include <sched.h>


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



bool pinMode(int pin, int state){
	// Clear out pinPath
	memset(pinPath, 0 , sizeof(pinPath));
	sprintf(pinPath, "%s/gpio%d%s", GPIO_PATH, pin, "/direction");
//	sprintf(pinPath, "%s%s%d", GPIO_MODE_PATH, GPIO_NAME, pin);

//	if (state != 0 || state != 1)
//		return false;	// only setting for

	int fd_pin;

	if( (fd_pin = open(pinPath, O_RDWR)) == -1) {
		printf("pinMode: failed to set pin %d, to %d\n", pin, state);
		perror("=( pinMode: ");
	} else {
		// Some day I'll support multiple state types, for now just the 2
		switch(state){
		case 0: // IN
			write(fd_pin, "in", 3);
			close(fd_pin);
			break;
		case 1:	//OUT
			write(fd_pin, "out", 4);
			close(fd_pin);
			break;
		default:
			printf("set state %d not allowable for pin %d", state, pin);
			break;
		}
	}

}

int analogRead(int pin) {
	int analogValue = -1;
	return analogValue;
}

bool digitalWrite(int pin, int state) {
	memset(pinPath, 0 , sizeof(pinPath));
	sprintf(pinPath, "%s/gpio%d%s", GPIO_PATH, pin, "/value");

//	if (state != 0 || state != 1)
//		return false;	// only setting for

	int fd_pin;

	if( (fd_pin = open(pinPath, O_WRONLY)) == -1) {
		printf("digitalWrite: failed to set pin %d, to %d\n", pin, state);
		perror("=(");
		return false;
	} else {
		switch(state){
		case 0: // IN
			write(fd_pin, "0", 2);
			close(fd_pin);
			break;
		case 1:	//OUT
			write(fd_pin, "1", 2);
			close(fd_pin);
			break;
		default:
			printf("set state %d not allowable for pin %d", state, pin);
			return false;
			break;
		}
	}
	return true;
}

int digitalRead(int pin) {

	memset(pinPath, 0 , sizeof(pinPath));
	sprintf(pinPath, "%s/gpio%d%s", GPIO_PATH, pin, "/value");

	int fd_pin;

	if( (fd_pin = open(pinPath, O_RDONLY)) == -1) {
		printf("digitalRead: failed to read pin %d\n", pin);
		perror("=(");
		return false;
	} else {
		char pinChar;
		read(fd_pin, &pinChar, 1);
		close(fd_pin);

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

bool setEdge(int pin) {

    // For now we just going to default to both edges

	memset(pinPath, 0 , sizeof(pinPath));
	sprintf(pinPath, "%s/gpio%d%s", GPIO_PATH, pin, "/edge");
    int fd_pin;

	if( (fd_pin = open(pinPath, O_WRONLY)) == -1 ){
        printf("setEdge: unable to set edge for pin %d\n", pin);
		perror("=( setEdge: ");
		return false;
	}

	write(fd_pin, "both", 5);
	close(fd_pin);

	return true;
}

bool exportPin(int pin){
	char tempPath[256];
	memset(tempPath, 0, 256);

	int fd_exportPath;

	if( (fd_exportPath = open(GPIO_EXPORT, O_WRONLY)) == -1 ){
		printf("exportPin: unable to unexport pin %d\n", pin);
		perror("=( exportPin: ");
		return false;
	} else {
        lseek(fd_exportPath, 0, SEEK_SET);
        char buffer[4]; // should be more than enough for simple sets
        memset((void *)buffer, 0, sizeof(buffer));
        int size = sprintf(buffer, "%d", pin);
        write(fd_exportPath, buffer, size);
        close(fd_exportPath);
	}
	return true;
}

bool unexportPin(int pin){
	char tempPath[256];
	memset(tempPath, 0, 256);

	int fd_exportPath;

	if( (fd_exportPath = open(GPIO_UNEXPORT, O_WRONLY)) == -1 ){
		printf("unexportPin: unable to unexport pin %d\n", pin);
		perror("=( unexportPin: ");
		return false;
	} else {
        lseek(fd_exportPath, 0, SEEK_SET);
        char buffer[4]; // should be more than enough for simple sets
        memset((void *)buffer, 0, sizeof(buffer));
        int size = sprintf(buffer, "%d", pin);
        write(fd_exportPath, buffer, size);
        close(fd_exportPath);
	}
	return true;
}

void IRQ(int pin) {

    char pVal[16];
    int timeoutTime = -1; // Never timeout
    int fd_pinValue;

    exportPin(pin); // Export Pin
    pinMode(pin, PIN_INPUT);
    setEdge(pin);


    memset(pinPath, 0 , sizeof(pinPath));
	sprintf(pinPath, "%s/gpio%d/value", GPIO_PATH, pin);

	if ((fd_pinValue = open(pinPath, O_RDONLY | O_NONBLOCK)) == -1 ){
		perror("IRQ getting value fd: ");
	}

    struct pollfd fdset[1];

    struct sched_param sched;
    memset (&sched, 0, sizeof(sched));

    sched.sched_priority = 55;  // High Priority
    sched_setscheduler (0, SCHED_RR, &sched);


	while (1) {
		memset((void*)fdset, 0, sizeof(fdset));

		fdset[0].fd = fd_pinValue;
		fdset[0].events = POLLPRI;

		int rc = poll(fdset, 1, timeoutTime);    // BLOCK 4EVA~!

		if (rc < 0) {
			printf("poll(): got a -1... something broke!\n");
		}

		if (rc == 0) {
			printf("poll(): got a timeout... should be -1?\n");
		}

		if (fdset[0].revents & POLLPRI) {
            memset(pVal, 0, 16);
			int size = read(fdset[0].fd, pVal, 16);

            // go to function pointer
			printf("\npoll() GPIO %d interrupt occurred\n", pin);
		}

	}

	close(fd_pinValue);
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

