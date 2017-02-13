#include "helloas_kt_com_jni_dynamicword_DynamicWord.h"
#include <jni.h>

#include <android/log.h>
#include <errno.h>
#include <stdlib.h>
#include "global.h"
#include "rsaeuro.h"
#include "r_random.h"
#include "simpledes.h"
#include "md5.h"
#include "rsa.h"
#include "nn.h"
#include "base64.h"

#define DEBUG(args...) \
	__android_log_print(ANDROID_LOG_DEBUG, "kzsecprotect", args)

#define RUNTIME_EXCEPTION "java/lang/RuntimeException"

void throw_exception(JNIEnv *env, char *message)
{
    jthrowable new_exception = (*env)->FindClass(env, RUNTIME_EXCEPTION);
    if(new_exception == 0) {
        return;
    } else {
        DEBUG("Exception '%s', Message: '%s'", RUNTIME_EXCEPTION, message);
    }
    (*env)->ThrowNew(env, new_exception, message);
}

char *getcharbychar(char c, char *pRes) {
    int i = (c &0xF0) >> 4;
    if (i >= 10)
        pRes[0] = 'a' + (i - 10);
    else
        pRes[0] = '0' + i;
    i = c & 0x0F;
    if (i >= 10)
        pRes[1] = 'a' + (i - 10);
    else
        pRes[1] = '0' + i;
    pRes[2] = '\0';
    return pRes;
}


void GetKeyString_1(char *p)
{
    strcat(p, "8f8f14bddd3b9eb932bb69be6ffb3a7f0fc8fe09b8280eecc56ab10508729e48b459b64324ad9ed8");
}
void GetKeyString_2(char *p)
{
    strcat(p, "398a2982e6b5a7bdbdaeacebe0e47da6847f40d4bb5aa0c0b4c2333d57fd142c4e040687131033d2af");
}
unsigned char GetUCByChars(const char *chars)
{
    unsigned char res = 0;
    const char *p = chars;
    while (*p != '\0')
    {
        res *= 16;
        if (((*p) > 'a') && ((*p) <= 'f'))
        {
            res += (10 + (*p) - 'a');
        } else
        {
            res += (*p - '0');
        }
        p ++;
    }
    return res;
}
void GetPublicKey(R_RSA_PUBLIC_KEY *rsa)
{
    //memmove(rsa, &PUBLIC_KEY1, sizeof(R_RSA_PUBLIC_KEY));
    //return;
    char key[512] = {0};
    GetKeyString_1(key);

    rsa->bits = 1024;
    rsa->exponent[MAX_RSA_MODULUS_LEN - 3] = 1;
    rsa->exponent[MAX_RSA_MODULUS_LEN - 1] = 1;
    GetKeyString_2(key);
    //module
    NN_DIGIT nn[256] = {0};
    strcat(key, "0903b44227461f673175d70b8dd6deee3c7bac56ad51e5e75e003764e4334d18016e64c3dfd79a0bba2822c0d467d7");
    int count = NN_Hex2NN(nn, (unsigned char *)key);
    NN_Encode(rsa->modulus, MAX_RSA_MODULUS_LEN, nn, count);

/*	char Tmp[4] = {0};
	char pTmp[4] = {0};
	char AllStr[2048] = {0};
	int modulusPos = MAX_RSA_MODULUS_LEN - 1;

	int iPos;
	for (iPos = 0; iPos < MAX_RSA_MODULUS_LEN; iPos ++)
	{
	    sprintf(Tmp, "%0.2x", rsa->modulus[iPos]);
		strcat(AllStr, Tmp);
	}
	DEBUG("Modulus:%s", AllStr);
	memset(AllStr, 0, 2048);
	for (iPos = 0; iPos < MAX_RSA_MODULUS_LEN; iPos ++)
	{
	    sprintf(Tmp, "%0.2x", rsa->exponent[iPos]);
		strcat(AllStr, Tmp);
	}
	DEBUG("exponent:%s", AllStr);	*/
}
R_RANDOM_STRUCT *InitRandomStruct(void)
{
    static unsigned char seedByte = 0;
    unsigned int bytesNeeded;
    static R_RANDOM_STRUCT randomStruct;

    R_RandomInit(&randomStruct);

    /* Initialize with all zero seed bytes, which will not yield an actual
             random number output. */

    while (1) {
        R_GetRandomBytesNeeded(&bytesNeeded, &randomStruct);
        if(bytesNeeded == 0)
            break;

        R_RandomUpdate(&randomStruct, &seedByte, 1);
    }

    return(&randomStruct);
}
int RSADecryptSubString(unsigned char *pSrc, int iSrcLen, unsigned char *pDst, int *iDstLen)
{
    unsigned char decrypt[512] = {0};
    int decryptLen = 512;
    int err = R_DecodePEMBlock(decrypt, &decryptLen, pSrc, iSrcLen);
    //DEBUG("Decrypt Base64 error:%d Len:%d src:%s", err, decryptLen, (char *)pSrc);
    if (err == 0) {
        R_RSA_PUBLIC_KEY rsaKey = {0};
        GetPublicKey(&rsaKey);
        int error = RSAPublicDecrypt(pDst, iDstLen, decrypt, decryptLen, &rsaKey);
        //DEBUG("decrypt:%d rsa:%s", error, pDst);
        return error;
    }
    return -1;
}
int IsBase64Chars(const unsigned char *pSrc, int iSrcLen)
{
    int res = 0;
    int i;
    //DEBUG("Decrypt String:%s, size:%d", (char *)pSrc, iSrcLen);
    for (i = 0; i < iSrcLen; i ++)
    {
        if (!(((pSrc[i] >= 'A') && (pSrc[i] <= 'Z'))
              || ((pSrc[i] >= 'a') && (pSrc[i] <= 'z'))
              || ((pSrc[i] >= '0') && (pSrc[i] <= '9'))
              || (pSrc[i] == '+')
              || (pSrc[i] == '/')
              || (pSrc[i] == ' ')
              || (pSrc[i] == ',')
              || (pSrc[i] == '\r')
              || (pSrc[i] == '\n')
              || (pSrc[i] == '=')))
        {
            DEBUG("IS NOT BASE64 CHAR %c, index:%d", pSrc[i], i);
            res = -1;
            break;
        }
    }
    return res;
}
int RSADecrypt(const unsigned char *pSrc, int iSrcLen, unsigned char *pDst, int iDstLen)
{
    int res = 0;
    if (IsBase64Chars(pSrc, iSrcLen) == 0)
    {
        unsigned char tmp[512] = {0};
        int iPos = 0;
        const unsigned char *p = pSrc;
        //DEBUG("decrypt:%s", pSrc);
        while (*p != '\0')
        {
            if (*p == ',')
            {
                if ((iPos > 0) && (iPos < 256))
                {
                    char tmpDst[512] = {0};
                    int iTmpDst = 512;
                    if (RSADecryptSubString(tmp, iPos, tmpDst, &iTmpDst) != 0)
                    {
                        res = -1;
                        break;
                    }
                    strcat(pDst, tmpDst);
                    iPos = 0;
                    memset(tmp, 0, 512);
                } //end if (iPos > 0)
            } else if (*p > 32)
            {
                tmp[iPos] = *p;
                iPos ++;
            }
            p ++;
        }
        if ((iPos > 0) && (iPos < 256))
        {
            char tmpDst[512] = {0};
            int iTmpDst = 512;
            if (RSADecryptSubString(tmp, iPos, tmpDst, &iTmpDst) != 0)
            {
                res = -1;
            } else {
                strcat(pDst, tmpDst);
            }
        }
    } else
    {
        DEBUG("Decode String invalied, size:%d",  iSrcLen);
        res = -1;
    }
    return res;
}
int RSAEncryptSubString(unsigned char *pSrc, int iSrcLen, unsigned char *pDst, int *iDstLen)
{
    R_RANDOM_STRUCT *randomStruct;
    randomStruct = InitRandomStruct();
    R_RSA_PUBLIC_KEY rsaKey = {0};
    GetPublicKey(&rsaKey);
    char encrypt[512] = {0};
    int encryptLen = 512;
    RSAPublicEncrypt(encrypt, &encryptLen, pSrc, iSrcLen, &rsaKey, randomStruct);
    R_EncodePEMBlock(pDst, iDstLen, encrypt, encryptLen);
    //DEBUG("encrypt base64 len:%d Str:%s", *iDstLen, pDst);
}
int RSAEncrypt(const unsigned char *pSrc, int iSrcLen,  unsigned char *pDst, int iDstLen)
{
    const int PER_SUB_STRING_LEN = 80;
    int iLeave = iSrcLen;
    char Tmp[PER_SUB_STRING_LEN + 1];
    char pTmpDst[PER_SUB_STRING_LEN * 3];
    int iPos = 0;
    int iCpyLen;
    int iTmpDstLen;
    int iDstPos = 0;
    while (iLeave > 0)
    {
        memset(Tmp, 0, PER_SUB_STRING_LEN + 1);
        if (iLeave > PER_SUB_STRING_LEN)
        {
            iCpyLen = PER_SUB_STRING_LEN;
        } else
        {
            iCpyLen = iLeave;
        }
        memmove(Tmp, pSrc + iPos, iCpyLen);
        memset(pTmpDst, 0, PER_SUB_STRING_LEN * 3);
        iTmpDstLen = PER_SUB_STRING_LEN * 3;
        RSAEncryptSubString(Tmp, iCpyLen, pTmpDst, &iTmpDstLen);
        strcat(pDst, pTmpDst);
        strcat(pDst, ",");
        iPos += iCpyLen;
        iLeave -= iCpyLen;
    }
}
jstring   CharsToJString(JNIEnv*   env,   char*   str)
{
    jsize   len   =   strlen(str);
    jclass   clsstring   =   (*env)->FindClass(env, "java/lang/String");
    jstring   strencode   =   (*env)->NewStringUTF(env,"UTF8");
    jmethodID   mid   =   (*env)->GetMethodID(env,clsstring,   "<init>",   "([BLjava/lang/String;)V");
    jbyteArray   barr   =   (*env)-> NewByteArray(env,len);
    (*env)-> SetByteArrayRegion(env,barr,0,len,(jbyte*)str);
    return (jstring)(*env)-> NewObject(env,clsstring, mid, barr, strencode);
}
JNIEXPORT jstring JNICALL Java_helloas_kt_com_jni_1dynamicword_DynamicWord_createWord
        (JNIEnv *env, jclass clazz, jstring key, jstring letters, jlong now_time, jlong dx_time, jlong c2s_dx_time)
{
    //debug
    const char *pKey = (*env)->GetStringUTFChars(env, key, 0);
    const char *pLetters = (*env)->GetStringUTFChars(env, letters, 0);
    long long lNow = now_time;
    long long lDxTime = dx_time;
    long long lC2SDxTime = c2s_dx_time;
    lNow += c2s_dx_time;
    lNow -= dx_time;
    lNow /=30000; //
    char timeKey[8] = {0};
    unsigned char keydigest[16] = {0};
    unsigned char keyNow[8] = {0};
    int i = 0;
    char szData[256] = {0};
    //DEBUG("KEY INT:%lld", lNow);
    for (i = 0; i < 8; i ++)
    {
        keyNow[i] = (lNow >> (8* i)) & (0xFF);
    }
    for (i = 0; i < 8; i ++) {
        getcharbychar(keyNow[i], szData + (i * 2));
    }
    //DEBUG("key:%s", szData);
    md5((char *)keyNow, 8, keydigest);
    memset(szData, 0, 256);
    for (i = 0; i < 16; i ++) {
        getcharbychar(keydigest[i], szData + (i * 2));
    }
    //DEBUG("key md5:%s", szData);


    for (i = 0; i < 8; i ++)
    {
        timeKey[i] = keydigest[i] | keydigest[i + 8];
    }

    memset(szData, 0, 256);
    for (i = 0; i < 8; i ++) {
        getcharbychar(keyNow[i], szData + (i * 2));
    }
    //DEBUG("key:%s", szData);
    int decryptlen = strlen(pKey) / 8;
    if ((strlen(pKey) % 8) != 0) {
        decryptlen ++;
    }


    memset(szData, 0, 256);
    for (i = 0; i < 8; i ++) {
        getcharbychar(timeKey[i], szData + (i * 2));
    }
    //DEBUG("long size:%d now_time:%lld Key:%s", sizeof(lNow), lNow, szData);
    memset(szData, 0, 256);
    for (i = 0; i < strlen(pKey); i ++) {
        getcharbychar(pKey[i], szData + (i * 2));
    }
    //DEBUG("source:%s   %s", pKey, szData);
    //DEBUG("Letter Table:%s", pLetters);
    decryptlen *= 8;
    char dst[1024] = {0};
    des_encrypt((unsigned char *)pKey, strlen(pKey), (unsigned char *)dst, (unsigned char *)timeKey);
    memset(szData, 0, 256);
    for (i = 0; i < decryptlen; i ++) {
        getcharbychar(dst[i], szData + (i * 2));
    }
    //DEBUG("des:%s", szData);
    unsigned char digest[16] = {0};
    md5(dst, decryptlen, digest);
    memset(szData, 0, 256);
    for (i = 0; i < 16; i ++) {
        getcharbychar(digest[i], szData + (i * 2));
    }
    //DEBUG("md5:%s", szData);
    unsigned int code[6] = {0};
    code[0] = digest[0] + digest[1] + digest[2] + digest[3];
    code[1] = digest[4] + digest[5] + digest[6] + digest[7];
    code[2] = digest[8] + digest[9] + digest[10] + digest[11];
    code[3] = digest[12] + digest[13] + digest[14] + digest[15];
    code[4] = digest[0] + digest[1] + digest[4] + digest[5];
    code[5] = digest[8] + digest[9] + digest[12] + digest[13];
    //
    int letterLen = strlen(pLetters);
    //DEBUG("Code:%d   %d  %d %d %d %d Letter Len:%d", code[0], code[1], code[2], code[3], code[4], code[5], letterLen);

    char word[7] = {0};
    for (i = 0; i < 6; i ++ ) {
        word[i] = pLetters[code[i] % letterLen];
    }
    //DEBUG("Dynamic:%s", word);
    jstring res = (*env)->NewStringUTF(env, word);
    return res;
}

/*
 * Class:     helloas_kt_com_jni_dynamicword_DynamicWord
 * Method:    nextWordLeaveInterval
 * Signature: (JJJ)I
 */
JNIEXPORT jint JNICALL Java_helloas_kt_com_jni_1dynamicword_DynamicWord_nextWordLeaveInterval
        (JNIEnv *env, jclass clazz, jlong now_time, jlong dx_time, jlong c2s_dx_time)
{
    long long lNow = now_time;
    long long lDxTime = dx_time;
    long long lC2SDxTime = c2s_dx_time;
    lNow += c2s_dx_time;
    lNow -= dx_time;
    lNow %= 30000;
    lNow /= 1000;
    jint n = lNow;
    return n;
}

/*
 * Class:     helloas_kt_com_jni_dynamicword_DynamicWord
 * Method:    encryptData
 * Signature: (Ljava/lang/String;)Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_helloas_kt_com_jni_1dynamicword_DynamicWord_encryptData
        (JNIEnv *env, jclass clazz, jstring params){
    const int NUMBER_OF_PARAMS  = 6;
    const char *pKey = (*env)->GetStringUTFChars(env, params, 0);
    int iSrcLen = strlen(pKey);
    if (iSrcLen > 0)
    {
        //DEBUG("Key size:%d", iSrcLen);
        char *pDst = malloc(iSrcLen * NUMBER_OF_PARAMS);
        memset(pDst, 0, iSrcLen * NUMBER_OF_PARAMS);
        int iDstLen = iSrcLen * NUMBER_OF_PARAMS;
        RSAEncrypt(pKey, strlen(pKey), pDst, iDstLen);
        (*env)->ReleaseStringUTFChars(env, params, pKey);
        jstring res = CharsToJString(env, pDst);
        free(pDst);
        return res;
    } else
    {
        jstring res = CharsToJString(env, "");
        return res;
    }
}

/*
 * Class:     helloas_kt_com_jni_dynamicword_DynamicWord
 * Method:    decryptData
 * Signature: (Ljava/lang/String;)Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_helloas_kt_com_jni_1dynamicword_DynamicWord_decryptData
        (JNIEnv *env, jclass clazz, jstring params)
{
    const int NUMBER_OF_PARAMS_DECRYPT  = 6;
    const char *pParams = (*env)->GetStringUTFChars(env, params, 0);
    int iSrcLen = strlen(pParams);
    //DEBUG("decrypt size:%d", iSrcLen);
    if (iSrcLen > 0)
    {
        char *pDst = malloc(iSrcLen * NUMBER_OF_PARAMS_DECRYPT);
        memset(pDst, 0, iSrcLen * NUMBER_OF_PARAMS_DECRYPT);
        int iDstLen = iSrcLen * NUMBER_OF_PARAMS_DECRYPT;
        int error = RSADecrypt(pParams, iSrcLen, pDst, iDstLen);
        (*env)->ReleaseStringUTFChars(env, params, pParams);
        jstring res;
        if (error == 0)
        {
            res = CharsToJString(env, pDst);
        } else
        {
            res = CharsToJString(env, "");
        }
        free(pDst);
        return res;
    } else
    {
        jstring res = CharsToJString(env, "");
        return res;
    }
}
void md5(const char *pSrc, int iLen, unsigned char *digest)
{
    MD5_CTX ctx;
    MD5Init(&ctx);
    MD5Update(&ctx, (unsigned char *)pSrc, iLen);
    MD5Final(digest, &ctx);
}

