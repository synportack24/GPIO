
#ifndef GPIO_H_
#define GPIO_H_
#include <stdbool.h>

// GPIO
// /sys/class/aml_gpio
//#define SYSFS_PATH		"/sys/class/aml_gpio"
#define SYSFS_PATH          "/sys/class"
#define GPIO_MODE_PATH		"/sys/devices/virtual/gpio/mode/"
#define GPIO_PIN_PATH		"/sys/devices/virtual/gpio/pin"
#define GPIO_PATH            SYSFS_PATH "/gpio"
#define GPIO_EXPORT          GPIO_PATH "/export"
#define GPIO_UNEXPORT        GPIO_PATH "/unexport"




// ADC
// ch0 and ch1 sample rate 8kSPS 0~0124
#define ADC_PATH0 "/sys/class/saradc/saradc_ch0"
#define ADC_PATH1 "/sys/class/saradc/saradc_ch0"


bool exportPin(int pin);
bool unexportPin(int pin);

bool pinMode(int pin, int state);
int analogRead(int pin);

int digitalRead(int pin);
bool digitalWrite(int pin, int state);


#endif /* GPIO_H_ */
