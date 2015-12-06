#ifndef SYSFS_H_
#define SYSFS_H_
#include <string.h>

#define ZONE_COUNT 2 // Zone0 and Zone1
#define VERSION_BUFFER 256

float getTempuratureFromZone(int zone);

void getLinuxVersion(char* versionPntr);

float getUpTimeSinceBoot();

float getUpTimeSinceIdle();

#endif /* SYSFS_H_ */
