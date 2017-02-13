#ifndef __ENCODE_DES_H____
#define __ENCODE_DES_H____

int des_encrypt(unsigned char *source, int srcLen, unsigned char *dest, unsigned char *key);
int des_decrypt(unsigned char *source, int srclen, unsigned char *dest, unsigned char *key);

#endif