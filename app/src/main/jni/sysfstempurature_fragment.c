#include <jni.h>
#include <sysfs.h>
#include <i2c.h>

JNIEXPORT jfloat JNICALL
Java_com_example_andrew_gpio_com_example_android_gpio_fragments_sysfstempurature_1fragment_getsysfstempurature(
        JNIEnv *env, jobject instance, jint zone) {

        return getTempuratureFromZone(zone);

}


JNIEXPORT jstring JNICALL
Java_com_example_andrew_gpio_com_example_android_gpio_fragments_sysfstempurature_1fragment_getLinuxVersion(
        JNIEnv *env, jobject instance) {

    char version[VERSION_BUFFER];
    memset(version, '\0', VERSION_BUFFER);
    getLinuxVersion(&version);

    return (*env)->NewStringUTF(env, version);

        if(version != NULL)
            return (*env)->NewString(env,version, strlen(version) );
//            return (*env)->NewStringUTF(env, version);
        else
            return (*env)->NewString(env,"HERP DERP", strlen("HERP DERP") );
}

JNIEXPORT jfloat JNICALL
Java_com_example_andrew_gpio_com_example_android_gpio_fragments_sysfstempurature_1fragment_getTimeSinceBoot(
        JNIEnv *env, jobject instance) {

        return getUpTimeSinceBoot();
}

JNIEXPORT jfloat JNICALL
Java_com_example_andrew_gpio_com_example_android_gpio_fragments_sysfstempurature_1fragment_getTimeSinceIdle(
        JNIEnv *env, jobject instance) {

        return  getUpTimeSinceIdle();

}
