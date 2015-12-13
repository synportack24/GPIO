#include <jni.h>
#include <stdio.h>
#include "i2c.h"
#include "gpio.h"



#include <fcntl.h>
#include <unistd.h>
#include <string.h>
#include <stdlib.h>
#include <errno.h>
#include <poll.h>
#include <sched.h>

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
    //IRQ(33);


    // Java JNI Fun stuff
    // -------------------------------------------------------------------
    jclass thisClass = (*env)->GetObjectClass(env, instance);
    jmethodID callBackMethodID = (*env)->GetMethodID(env, thisClass, "IRQ_CallBack", "()V");

    if(callBackMethodID == NULL) {
        //__android_log_print(ANDROID_LOG_WARN, "GPIO", "Call Back function can't be found =(");
        return;
    }

    (*env)->CallVoidMethod(env, instance, callBackMethodID);


}


JNIEXPORT jobject JNICALL
Java_com_example_andrew_gpio_InputPinService_setup_1Interrupt(JNIEnv *env, jobject instance,
                                                              jint pin) {


    // Java JNI Fun stuff
    // -------------------------------------------------------------------
    jclass thisClass = (*env)->GetObjectClass(env, instance);
    jmethodID callBackMethodID = (*env)->GetMethodID(env, thisClass, "toastmessage", "()V");

    if(callBackMethodID == NULL) {
        //__android_log_print(ANDROID_LOG_WARN, "GPIO", "Call Back function can't be found =(");
        return false;
    }

//    (*env)->CallVoidMethod(env, instance, callBackMethodID);



    // Wait For Pin Change
    // -------------------------------------------------------------------
    char path[256];
    char pVal[16];
    int timeoutTime = -1; // Never timeout
    int fd_pinValue;

    exportPin(pin); // Export Pin
    pinMode(pin, PIN_INPUT);
    setEdge(pin);


    memset(path, 0 , sizeof(path));
    sprintf(path, "%s/gpio%d/value", GPIO_PATH, pin);

    if ((fd_pinValue = open(path, O_RDONLY | O_NONBLOCK)) == -1 ){
        perror("IRQ getting value fd: ");
    }

    struct pollfd fdset[1];

    struct sched_param sched;
    memset (&sched, 0, sizeof(sched));

    sched.sched_priority = 55;  // High Priority
    sched_setscheduler (0, SCHED_RR, &sched);


    while (1) {
        memset((void*)fdset, 0, sizeof(fdset));

        fdset[0].fd = fd_pinValue;
        fdset[0].events = POLLPRI;

        int rc = poll(fdset, 1, timeoutTime);    // BLOCK 4EVA~!

        if (rc < 0) {
            // lets just reset everything and try again
            printf("poll(): got a -1... something broke!\n");
        }

        if (rc == 0) {
            // we should *NEVER* hit this...
            printf("poll(): got a timeout... should be -1?\n");
        }

        if (fdset[0].revents & POLLPRI) {
            memset(pVal, 0, 16);
            int size = read(fdset[0].fd, pVal, 16);

            // go to function pointer
//            printf("\npoll() GPIO %d interrupt occurred\n", pin);
            (*env)->CallVoidMethod(env, instance, callBackMethodID);
        }

    }

    close(fd_pinValue);

}



//JNIEXPORT jstring JNICALL
//Java_com_example_andrew_gpio_MainActivity_testFunction(JNIEnv *env, jobject instance) {
//
//    return (*env)->NewStringUTF(env, "string");
//}