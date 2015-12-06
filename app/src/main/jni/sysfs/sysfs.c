#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <fcntl.h>
#include "sysfs.h"

#ifdef ANDROID
//TODO, use this over perror
#include <android/log.h>
#endif


int fd_temps[2];

float getTempuratureFromZone(int zone){
	float tempurature = -1.0f;

	if(zone < 0 || zone > 2)
		zone = 0; // default this to 0

	char tempPath[256];
	memset(tempPath, 0, 256);

	char rdbuf[20];
	memset(rdbuf, 0, 20);

	sprintf(tempPath, "/sys/devices/virtual/thermal/thermal_zone%d/temp", zone);


	if( (fd_temps[zone] = open(tempPath, O_RDONLY)) == -1 ){
		printf("Get Temperature Error\n");
		perror("=(");
	} else {
		lseek(fd_temps[zone], 0, SEEK_SET);
		read(fd_temps[zone], &rdbuf, sizeof(rdbuf));
		tempurature = atof(rdbuf);
		tempurature = tempurature/1000;
	}

	return tempurature;
}

void getLinuxVersion(char* versionPntr){

	memset(versionPntr, '\0', VERSION_BUFFER);
	int fd_version;

	if( (fd_version = open("/proc/version", O_RDONLY)) == -1){
		printf("Proc Version Error\n");
		perror("Proc Version: ");
		versionPntr[0] = 'n';
		versionPntr[1] = 'a';
		versionPntr[2] = '\n';
		versionPntr[3] = 0;
	} else {
		lseek(fd_version, 0, SEEK_SET);
		read(fd_version, versionPntr, VERSION_BUFFER);

	}

}

int findChar(char* text, char searchFor){
	int pos = -1;
	int i = 0;	// in loop initializtion only allowed for C99 and beyond... Should be fine... but don't want to deal with it
	for(i = 0; i < strlen(text); i++){
		char v = *text;
		if(v == searchFor)
			pos = i;
		text++;
	}
	return pos;
}

float getUpTimeSinceBoot(){
	float uptime = -1.0f;

	int fd;

	char rdbuf[32];
	memset(rdbuf, 0, 32);

	char boot[16];
	memset(boot, 0, 16);

	if((fd = open("/proc/uptime", O_RDONLY)) == -1){
		printf("get uptime Error\n");
		perror("uptime error");
	} else {
		lseek(fd, 0, SEEK_SET);
		read(fd, &rdbuf, sizeof(rdbuf));
		int pos = findChar(rdbuf, ' ');
		if(pos != -1){
			strncpy(boot, rdbuf, pos);
			uptime = atof(boot);
		}
	}

	return uptime;
}

float getUpTimeSinceIdle() {
	float uptime = -1.0f;

	int fd;

	char rdbuf[32];
	memset(rdbuf, 0, 32);

	char boot[16];
	memset(boot, 0, 16);

	if((fd = open("/proc/uptime", O_RDONLY)) == -1){
		printf("get uptime Error\n");
		perror("uptime error");
	} else {
		lseek(fd, 0, SEEK_SET);
		read(fd, &rdbuf, sizeof(rdbuf));
		int pos = findChar(rdbuf, ' ');
		if(pos != -1){
			char* rdbuf2 = rdbuf;
			rdbuf2 = rdbuf2 + pos + 1;
			strncpy(boot, rdbuf2, strlen(rdbuf2));
			uptime = atof(boot);
		}
	}

	return uptime;
}
