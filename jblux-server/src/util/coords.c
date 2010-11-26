/*
 * File: coords.c
 * Author: Casey Jones
 */

#include "coords.h"

double distance(struct coordinates_t a, struct coordinates_t b)
{
    double distance = 0.0f;
    long d = powi(a.x - b.x, 2) + powi(a.y - b.y, 2);
    distance = sqrt(d);

    return distance;
}

