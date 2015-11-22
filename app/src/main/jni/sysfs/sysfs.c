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
		printf("Something bad happed");
		perror("=(");
	} else {
		lseek(fd_temps[zone], 0, SEEK_SET);
		read(fd_temps[zone], &rdbuf, sizeof(rdbuf));
		tempurature = atof(rdbuf);
		tempurature = tempurature/1000;
	}

	return tempurature;
}

