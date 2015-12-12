#include <bme280-i2c.h>
#include <bme280.h>
#include <si1132.h>
#include "i2c.h"

static int pressure;
static int temperature;
static int humidity;
static float altitude;

float SEALEVELPRESSURE_HPA = 1024.25;

void i2c_init() {
    char* i2c = "/dev/i2c-1";

    si1132_begin(i2c);
    bme280_begin(i2c);

}


void updateSensorValues() {
    // Get Values
    bme280_read_pressure_temperature_humidity(&pressure, &temperature, &humidity);
    bme280_readAltitude(pressure, SEALEVELPRESSURE_HPA);
}


float getTemperature() {
    return (float)(temperature/100.0);
}

float getHumidity() {
    return (float)(humidity/1024.0);
}

float getPressure() {
    return (float)(pressure/100.0);
}

float getUV() {
    return Si1132_readUV()/100.0;
}

float getVisableLUX() {
    return Si1132_readVisible();
}

float getIRLUX() {
    return Si1132_readIR();
}