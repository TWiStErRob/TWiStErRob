#define JPEG_INTERNALS

extern "C" {
#include "libjpeg/jinclude.h"
#include "libjpeg/jpeglib.h"
};

#include <cerrno>
#include <cmath>
#include <string>

using namespace std;

#include <android/log.h>

#define  LOG_TAG    "LibJPEG_Wrapper"
#define  printf(...)  __android_log_print(ANDROID_LOG_FATAL, LOG_TAG, __VA_ARGS__)

#include <jni.h>
// https://github.com/android/platform_frameworks_base/blob/master/native/graphics/jni/bitmap.cpp
#include <android/bitmap.h>

bool dumpFile(const string &fileName, void *bytes, size_t size) {
    printf("Dumping file %s", fileName.c_str());
    FILE *output = fopen(fileName.c_str(), "wb");

    if (!output) {
        printf("Error opening output file %s (%d = %s)\n!", fileName.c_str(), errno, strerror(errno));
        return false;
    }

    fwrite(bytes, 1, size, output);
    fclose(output);
    return true;
}

unsigned long compressJPEG(JSAMPLE *image, JDIMENSION width, JDIMENSION height, unsigned char *&buf, uint8_t quality) {
    struct jpeg_compress_struct cinfo;
    struct jpeg_error_mgr jerr;
    cinfo.err = jpeg_std_error(&jerr);
    jpeg_create_compress(&cinfo);

    cinfo.in_color_space = JCS_RGB;
    cinfo.jpeg_color_space = JCS_YCbCr;
    jpeg_set_defaults(&cinfo);

    unsigned long size = 0;
    jpeg_mem_dest(&cinfo, &buf, &size);
    cinfo.image_width = width;
    cinfo.image_height = height;
    cinfo.input_components = RGB_PIXELSIZE;
    jpeg_set_quality(&cinfo, quality, TRUE);
    jpeg_start_compress(&cinfo, TRUE);
    while (cinfo.next_scanline < cinfo.image_height) {
        JSAMPROW row_pointer = &image[cinfo.next_scanline * width * RGB_PIXELSIZE];
        jpeg_write_scanlines(&cinfo, &row_pointer, 1);
    }
    jpeg_finish_compress(&cinfo);
    jpeg_destroy_compress(&cinfo);
    return size;
}

int write_jpeg_file(JSAMPLE *image, JDIMENSION width, JDIMENSION height, const string &fileName, uint8_t quality) {
    unsigned char *buf = NULL;
    printf("Compressing %s with quality=%d", fileName.c_str(), quality);
    unsigned long size = compressJPEG(image, width, height, buf, quality);

    if (!dumpFile(fileName, buf, size)) {
        return -1;
    }
    // The standard library functions malloc/free are used for allocating larger memory, see jdatadst.c
    free(buf);

    return 1; /* success code is 1! */
}


JSAMPLE *toJPEGInput(const unsigned char *const pixels, const AndroidBitmapInfo &info) {
    JSAMPLE *result;
    switch (info.format) {
        case ANDROID_BITMAP_FORMAT_RGBA_8888: {
            const unsigned char *end = pixels + info.width * info.height * ((8 + 8 + 8 + 8) / 8);
            const unsigned char *source = pixels;
            JSAMPLE *target = result = new JSAMPLE[info.width * info.height * RGB_PIXELSIZE];
            while (source < end) {
                *target++ = *source++;
                *target++ = *source++;
                *target++ = *source++;
                source++; // ignored alpha
            }
            break;
        }
        case ANDROID_BITMAP_FORMAT_RGB_565: {
            const unsigned char *end = pixels + info.width * info.height * ((5 + 6 + 5) / 8);
            const unsigned char *source = pixels;
            JSAMPLE *target = result = new JSAMPLE[info.width * info.height * RGB_PIXELSIZE];
            while (source < end) {
                auto half1 = *source++;
                auto half2 = *source++;
                auto R5 = (half1 & 0b11111000) >> 3;
                auto G6 = (half1 & 0b00000111) << 3 | (half2 & 0b11100000) >> 5;
                auto B5 = (half2 & 0b00011111) >> 0;
                *target++ = (R5 * 527 + 23) >> 6;
                *target++ = (G6 * 259 + 33) >> 6;
                *target++ = (B5 * 527 + 23) >> 6;
            }
            break;
        }
        default:
            result = nullptr;
            break;
    }
    return result;
}

// JNI methods need to be declared inside extern C for them to link properly
extern "C" {

static jint throwResult(JNIEnv *env, jint result) {
    const char *message;
    const char *clazz = "java/lang/RuntimeException";
    switch (result) {
        case ANDROID_BITMAP_RESULT_SUCCESS:
            message = NULL; // don't throw
            break;
        case ANDROID_BITMAP_RESULT_ALLOCATION_FAILED:
            message = "Allocation Failed";
            clazz = "java/lang/OutOfMemoryError";
            break;
        case ANDROID_BITMAP_RESULT_BAD_PARAMETER:
            message = "Bad parameter";
            clazz = "java/lang/IllegalArgumentException";
            break;
        case ANDROID_BITMAP_RESULT_JNI_EXCEPTION:
            message = "JNI Exception";
            break;
        default:
            message = "Unknown code";
            break;
    }
    if (message) {
        return env->ThrowNew(env->FindClass(clazz), message);
    } else {
        return 0;
    }
}

JNIEXPORT JNICALL void Java_net_twisterrob_jpegtest_MainActivity_dumpPixels(
        JNIEnv *env, jclass type, jobject bitmap, jstring fileName_) {
    AndroidBitmapInfo info;
    if (throwResult(env, AndroidBitmap_getInfo(env, bitmap, &info))) {
        return;
    }

    const unsigned char *bitmapPixels;
    if (throwResult(env, AndroidBitmap_lockPixels(env, bitmap, (void **) &bitmapPixels))) {
        return;
    }

    const char *fileName = env->GetStringUTFChars(fileName_, 0);
    JSAMPLE *pixels = toJPEGInput(bitmapPixels, info);
    if (pixels) {
        dumpFile(fileName, pixels, info.width * info.height * RGB_PIXELSIZE);
        delete[] pixels;
    }
    env->ReleaseStringUTFChars(fileName_, fileName);

    if (throwResult(env, AndroidBitmap_unlockPixels(env, bitmap))) {
        return;
    }
}

JNIEXPORT JNICALL jbyteArray Java_net_twisterrob_jpegtest_MainActivity_compress(
        JNIEnv *env, jclass type, jobject bitmap, jint quality) {
    AndroidBitmapInfo info;
    if (throwResult(env, AndroidBitmap_getInfo(env, bitmap, &info))) {
        return NULL;
    }

    const unsigned char *bitmapPixels;
    if (throwResult(env, AndroidBitmap_lockPixels(env, bitmap, (void **) &bitmapPixels))) {
        return NULL;
    }

    JSAMPLE *pixels = toJPEGInput(bitmapPixels, info);
    if (pixels) {
        jbyte *buf = NULL;
        jsize size = (jsize) compressJPEG(pixels, info.width, info.height, (unsigned char *&) buf, (uint8_t) quality);
        delete[] pixels;
        jbyteArray result = env->NewByteArray(size);
        env->SetByteArrayRegion(result, 0, size, buf);
        return result;
    } else {
        env->ThrowNew(env->FindClass("java/lang/IllegalArgumentException"), "Unsupported format");
        return NULL;
    }

    if (throwResult(env, AndroidBitmap_unlockPixels(env, bitmap))) {
        return NULL;
    }
}


#define RGB(r, g, b) (0xFF000000 | (r) << 16 | (g) << 8 | (b))
#define RED(color) ((unsigned char)(color >> 16 & 0xFF))
#define GREEN(color) ((unsigned char)(color >> 8 & 0xFF))
#define BLUE(color) ((unsigned char)(color & 0xFF))

unsigned int RGBFromHSV(unsigned char hue, unsigned char saturation, unsigned char value) {
    if (saturation == 0) return RGB(value, value, value);

    unsigned char region = (unsigned char) (hue / 43);
    u_int16_t remainder = (u_int16_t) ((hue - (region * 43)) * 6);
    unsigned char v = value;
    unsigned char p = (unsigned char) ((value * (255 - saturation)) >> 8);
    unsigned char q = (unsigned char) ((value * (255 - ((saturation * remainder) >> 8))) >> 8);
    unsigned char t = (unsigned char) ((value * (255 - ((saturation * (255 - remainder)) >> 8))) >> 8);

    // @formatter:off
    switch (region) {
        case 0: return RGB(v, t, p);
        case 1: return RGB(q, v, p);
        case 2: return RGB(p, v, t);
        case 3: return RGB(p, q, v);
        case 4: return RGB(t, p, v);
        default: return RGB(v, p ,q);
    }
    // @formatter:on
}

JNIEXPORT JNICALL void Java_net_twisterrob_jpegtest_MainActivity_test(
        JNIEnv *env, jclass type, jstring fileName_, jint width_, jint height_, jint quality) {
    JDIMENSION width = (JDIMENSION) width_;
    JDIMENSION height = (JDIMENSION) height_;
    JSAMPLE *generated = new JSAMPLE[width * height * RGB_PIXELSIZE];
    for (JDIMENSION y = 0; y < height; ++y) {
        for (JDIMENSION x = 0; x < width; ++x) {
            JSAMPLE *pixel = generated + (y * width + x) * RGB_PIXELSIZE;
            unsigned char hue = (unsigned char) (x * 255 / width);
            unsigned char saturation = (unsigned char) (255 - y * 255 / height);
            int color = RGBFromHSV(hue, saturation, 255);
            pixel[RGB_RED] = (JSAMPLE) RED(color);
            pixel[RGB_GREEN] = (JSAMPLE) GREEN(color);
            pixel[RGB_BLUE] = (JSAMPLE) BLUE(color);
        }
    }
    const char *fileName = env->GetStringUTFChars(fileName_, 0);
    write_jpeg_file(generated, width, height, fileName, (uint8_t) quality);
    delete[] generated;
    env->ReleaseStringUTFChars(fileName_, fileName);
}

}
