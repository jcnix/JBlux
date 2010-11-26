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

void coordinates_to_json(yajl_gen gen, struct coordinates_t coords)
{
    const char* x_field =  "x";
    const char* y_field =  "y";
    
    yajl_gen_map_open(gen);
    json_insert_int(gen, x_field, coords.x);
    json_insert_int(gen, y_field, coords.y);
    yajl_gen_map_close(gen);
}

