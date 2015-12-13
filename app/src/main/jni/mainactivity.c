#include <jni.h>
#include <stdio.h>
#include "i2c.h"
#include "gpio.h"
#include <android/log.h>

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




    // Java JNI Fun stuff
    // -------------------------------------------------------------------
    jclass thisClass = (*env)->GetObjectClass(env, instance);
    jmethodID callBackMethodID = (*env)->GetMethodID(env, instance, "IRQ_CallBack", "()V");

    if(callBackMethodID == NULL) {
        __android_log_print(ANDROID_LOG_WARN, "GPIO", "Call Back function can't be found =(");
        return;
    }

    (*env)->CallVoidMethod(env, instance, callBackMethodID);


}

//JNIEXPORT jstring JNICALL
//Java_com_example_andrew_gpio_MainActivity_testFunction(JNIEnv *env, jobject instance) {
//
//    return (*env)->NewStringUTF(env, "string");
//}