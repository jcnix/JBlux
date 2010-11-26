/*
 * File: coords.h
 * Author: Casey Jones
 */

#ifndef _COORDS_H
#define _COORDS_H

#include <stdio.h>
#include <stdlib.h>
#include <math.h>
#include "jmath.h"

struct coordinates_t
{
    int x;
    int y;
};

double distance(struct coordinates_t a, struct coordinates_t b);

#endif

