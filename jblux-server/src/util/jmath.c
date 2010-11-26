/*
 * File: jmath.c
 * Author: Casey Jones
 */

#include "jmath.h"

/* Iterative pow() function that works with ints */
long powi(int base, int exp)
{
    long result = 1;
    int i;
    for(i = exp; i >= 1; i--)
    {
        result *= base;
    }

    return result;
}

