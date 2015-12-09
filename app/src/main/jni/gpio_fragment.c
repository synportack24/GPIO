#include <jni.h>
#include "gpio.h"


JNIEXPORT void JNICALL
Java_com_example_andrew_gpio_com_example_android_gpio_fragments_gpio_1fragment_exportPin(
        JNIEnv *env, jobject instance, jint Pin) {

    exportPin( Pin );

}


JNIEXPORT void JNICALL
Java_com_example_andrew_gpio_com_example_android_gpio_fragments_gpio_1fragment_setPinMode(
        JNIEnv *env, jobject instance, jint Pin, jint direction) {

    if(direction > 1)
        direction = 1;

    if(direction < 0)
        direction = 0;

    pinMode(Pin, direction);

}

JNIEXPORT void JNICALL
Java_com_example_andrew_gpio_com_example_android_gpio_fragments_gpio_1fragment_pinWrite(JNIEnv *env,
                                                                                        jobject instance,
                                                                                        jint Pin,
                                                                                        jint value) {

    if(value > 1)
        value = 1;

    if(value < 0)
        value = 0;

    digitalWrite(Pin, value);

}