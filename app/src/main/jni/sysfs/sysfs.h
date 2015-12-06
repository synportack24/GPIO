#ifndef SYSFS_H_
#define SYSFS_H_
#include <string.h>

#define ZONE_COUNT 2 // Zone0 and Zone1
#define VERSION_BUFFER 128

float getTempuratureFromZone(int zone);

void getLinuxVersion(char* versionPntr);

#endif /* SYSFS_H_ */
