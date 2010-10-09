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

void race_to_json(yajl_gen gen, struct race_t race)
{
    /* Field names */
    const char* id_field =              "id";
    const char* name_field =            "name";
    const char* sprite_sheet_field =    "sprite_sheet";
    const char* sprite_height_field =   "sprite_height";
    
    yajl_gen_map_open(gen);
    json_insert_int(gen, id_field, race.id);
    json_insert_str(gen, name_field, race.name);
    json_insert_str(gen, sprite_sheet_field, race.sprite_sheet);
    json_insert_int(gen, sprite_height_field, race.sprite_height);
    yajl_gen_map_close(gen);
}

void class_to_json(yajl_gen gen, struct class_t c)
{
    const char* id_field =      "id";
    const char* name_field =    "name";
    
    yajl_gen_map_open(gen);
    json_insert_int(gen, id_field, c.id);
    json_insert_str(gen, name_field, c.name);
    yajl_gen_map_close(gen);
}

void inventory_to_json(yajl_gen gen, struct inventory_t inv)
{
    yajl_gen_map_open(gen);
    yajl_gen_array_open(gen);
    yajl_gen_array_close(gen);
    yajl_gen_map_close(gen);
}

