/*
 * File: base64.c
 * Author: Casey Jones
 */

#include "base64.h"

char* base64_encode(char *input, int length)
{
    BIO *bio;
    BIO *b64;
    BUF_MEM *bptr;

    b64 = BIO_new(BIO_f_base64());
    bio = BIO_new(BIO_s_mem());
    b64 = BIO_push(b64, bio);
    BIO_write(b64, input, length);
    
    if(!BIO_flush(b64))
        return NULL;

    BIO_get_mem_ptr(b64, &bptr);

    char *buf = malloc(bptr->length);
    memcpy(buf, bptr->data, bptr->length-1);
    buf[bptr->length-1] = 0x0;
    BIO_free_all(b64);

    return buf;
}

char* base64_decode(char *input, int length)
{
    BIO *bio;
    BIO *b64;

    char* buffer = malloc(length);
    memset(buffer, 0, length);

    b64 = BIO_new(BIO_f_base64());
    bio = BIO_new_mem_buf(input, length);
    b64 = BIO_push(b64, bio);
    BIO_read(bio, buffer, length);
    BIO_free_all(bio);

    return buffer;
}

