#include <jni.h>

JNIEXPORT jstring JNICALL
Java_com_example_andrew_gpio_MainActivity_testFunction(JNIEnv *env, jobject instance) {

    // TODO


    return (*env)->NewStringUTF(env, "string");
}