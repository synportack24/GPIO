#include <jni.h>
#include <sysfs.h>
#include <i2c.h>

JNIEXPORT void JNICALL
Java_com_example_andrew_gpio_com_example_android_gpio_fragments_i2c_1fragment_updateSensors(
        JNIEnv *env, jobject instance) {

    updateSensorValues();

}

JNIEXPORT jfloat JNICALL
Java_com_example_andrew_gpio_com_example_android_gpio_fragments_i2c_1fragment_getTemperature(
        JNIEnv *env, jobject instance) {

    return getTemperature();

}

JNIEXPORT jfloat JNICALL
Java_com_example_andrew_gpio_com_example_android_gpio_fragments_i2c_1fragment_getHumidity(
        JNIEnv *env, jobject instance) {

    return getHumidity();

}

JNIEXPORT jfloat JNICALL
Java_com_example_andrew_gpio_com_example_android_gpio_fragments_i2c_1fragment_getPressure(
        JNIEnv *env, jobject instance) {

    return getPressure();
}

JNIEXPORT jfloat JNICALL
Java_com_example_andrew_gpio_com_example_android_gpio_fragments_i2c_1fragment_getUV(JNIEnv *env,
                                                                                    jobject instance) {

    return getUV();

}

JNIEXPORT jfloat JNICALL
Java_com_example_andrew_gpio_com_example_android_gpio_fragments_i2c_1fragment_getVisableLUX(
        JNIEnv *env, jobject instance) {

    return  getVisableLUX();

}

JNIEXPORT jfloat JNICALL
Java_com_example_andrew_gpio_com_example_android_gpio_fragments_i2c_1fragment_getIRLUX(JNIEnv *env,
                                                                                       jobject instance) {

    return getIRLUX();

}