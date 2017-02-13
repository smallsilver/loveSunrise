LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

	
LOCAL_MODULE    := kzdynamicword

LOCAL_SRC_FILES := \
	desc.c \
    dynamiccreate.c	\
	md2c.c \
	md4c.c \
	md5c.c \
	nn.c \
	prime.c \
	r_dh.c \
	simpledes.c \
	r_encode.c \
	r_enhanc.c \
	r_keygen.c \
	base64.c \
	r_stdlib.c \
	r_random.c \
	rsa.c \
	shsc.c 
		
LOCAL_ARM_MODE := arm

ifeq ($(TARGET_ARCH_ABI),armeabi-v7a)
    LOCAL_CFLAGS := -DHAVE_NEON=1
    LOCAL_SRC_FILES += des386.s.neon
	LOCAL_SRC_FILES += rsa68k.s.neon
	LOCAL_SRC_FILES += rsa386.s.neon
    LOCAL_ARM_NEON  := true
endif

LOCAL_LDLIBS := -llog -lz
include $(BUILD_SHARED_LIBRARY)

