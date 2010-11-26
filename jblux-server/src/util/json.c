/*
 * File: json.c
 * Author: Casey Jones
 */

#include "json.h"

char* get_json_str(yajl_gen gen)
{
    char* json;
    unsigned int len = 0;
    yajl_gen_get_buf(gen, (const unsigned char**) &json, &len);

    return json;
}

void json_insert_str(yajl_gen gen, const char* key, char* value)
{
    yajl_gen_string(gen, (unsigned char*) key, strlen(key));
    yajl_gen_string(gen, (unsigned char*) value, strlen(value));
}

void json_insert_int(yajl_gen gen, const char* key, int value)
{
    yajl_gen_string(gen, (unsigned char*) key, strlen(key));
    yajl_gen_integer(gen, value);
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

void inventory_to_json(yajl_gen gen, struct inventory_t inv)
{
    yajl_gen_map_open(gen);
    yajl_gen_array_open(gen);
    yajl_gen_array_close(gen);
    yajl_gen_map_close(gen);
}

