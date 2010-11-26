/*
 * File: coords.h
 * Author: Casey Jones
 */

#ifndef _COORDS_H
#define _COORDS_H

#include <stdio.h>
#include <stdlib.h>
#include <math.h>
#include <yajl/yajl_gen.h>
#include "jmath.h"
#include "json.h"

struct coordinates_t
{
    int x;
    int y;
};

double distance(struct coordinates_t a, struct coordinates_t b);
void coordinates_to_json(yajl_gen gen, struct coordinates_t coords);

#endif

