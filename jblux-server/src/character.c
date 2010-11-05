/*
 * File: character.c
 * Author: Casey Jones
 */

#include "character.h"

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

