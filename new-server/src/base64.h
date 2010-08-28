/*
 * File: base64.h
 * Author: Casey Jones
 */

#ifndef _BASE64_H
#define _BASE64_H

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <openssl/sha.h>
#include <openssl/hmac.h>
#include <openssl/evp.h>
#include <openssl/bio.h>
#include <openssl/buffer.h>

char* base64_encode(unsigned char *input, int length);
char* base64_decode(unsigned char *input, int length);

#endif

