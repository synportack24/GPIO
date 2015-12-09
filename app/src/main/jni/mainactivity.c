#include <jni.h>
#include "i2c.h"
#include "gpio.h"

JNIEXPORT void JNICALL
Java_com_example_andrew_gpio_MainActivity_gpio_1init(JNIEnv *env, jobject instance) {

    // Start
    i2c_init();

    // Get some starting values
    updateSensorValues();


}

JNIEXPORT void JNICALL
Java_com_example_andrew_gpio_MainActivity_setupInterrupt(JNIEnv *env, jobject instance) {

    // TODO
    IRQ(33);

}

JNIEXPORT jstring JNICALL
Java_com_example_andrew_gpio_MainActivity_testFunction(JNIEnv *env, jobject instance) {

    return (*env)->NewStringUTF(env, "string");
}