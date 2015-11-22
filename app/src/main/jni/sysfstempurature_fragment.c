#include <jni.h>
#include <sysfs.h>

JNIEXPORT jfloat JNICALL
Java_com_example_andrew_gpio_com_example_android_gpio_fragments_sysfstempurature_1fragment_getsysfstempurature(
        JNIEnv *env, jobject instance, jint zone) {

        return getTempuratureFromZone(zone);

}