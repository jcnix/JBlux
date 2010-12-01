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

/*
 * This is what happens
 *    1100100000 = 800
 *  & 0000011111 =  31
 *  ------------
 *    0000000000
 *
 *  The last 5 bits need to be 0 essentially
 */
int divisible_by_base2(int number, int divisor)
{
    if((number & (divisor-1)) == 0)
    {
        return 1;
    }
    else
    {
        return 0;
    }
}

