#ifndef I2C_H_
#define I2C_H_

void i2c_init();

void updateSensorValues();

float getTemperature();

float getHumidity();

float getPressure();

#endif