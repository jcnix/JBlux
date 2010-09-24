/*
 * File: base64.c
 * Author: Casey Jones
 */

#include "base64.h"

char* base64_encode(char *input)
{
    BIO *bio = NULL;
    BIO *b64 = NULL;
    BUF_MEM *bptr = NULL;
    int length = strlen(input)+1;

    b64 = BIO_new(BIO_f_base64());
    BIO_set_flags(b64, BIO_FLAGS_BASE64_NO_NL);
    bio = BIO_new(BIO_s_mem());
    b64 = BIO_push(b64, bio);
    BIO_write(b64, input, length);
    
    if(!BIO_flush(b64))
        return NULL;

    BIO_get_mem_ptr(b64, &bptr);

    char *buf = malloc(bptr->length+1);
    if(!buf)
    {
        return "";
    }

    memcpy(buf, bptr->data, bptr->length);
    buf[bptr->length] = 0x0;
    BIO_free_all(b64);

    return buf;
}

char* base64_decode(char *input)
{
    BIO *bio;
    BIO *b64;

    int length = strlen(input) + 1;
    char* buffer = malloc(length+1);
    if(!buffer)
    {
        return "";
    }

    memset(buffer, 0, length);

    b64 = BIO_new(BIO_f_base64());
    BIO_set_flags(b64, BIO_FLAGS_BASE64_NO_NL);
    bio = BIO_new_mem_buf(input, length);
    bio = BIO_push(b64, bio);
    BIO_read(bio, buffer, length);
    BIO_free_all(bio);
    BIO_free_all(b64);

    return buffer;
}

