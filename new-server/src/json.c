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

char* player_data_to_json(struct player_data *data)
{
    char* json;
    yajl_gen_config conf = { 0 };
    yajl_gen gen;
    gen = yajl_gen_alloc(&conf, NULL);

    /* Field names */
    const char* user_id_field =        "user_id";
    const char* character_id_field =   "character_id";
    const char* character_name_field = "character_name";
    const char* level_field =          "level";
    const char* race_field =           "race";
    const char* class_field =          "player_class";
    const char* strength_field =       "strength";
    const char* agility_field =        "agility";
    const char* stamina_field =        "stamina";
    const char* intelligence_field =   "intelligence";
    const char* spirit_field =         "spirit";
    const char* map_field =            "map";
    const char* inventory_field =      "inventory";
    const char* coords_field =         "coords";

    /* Open JSON structure */
    yajl_gen_map_open(gen);
    
    json_insert_int(gen, user_id_field, data->user_id);
    json_insert_int(gen, character_id_field, data->character_id);
    json_insert_str(gen, character_name_field, data->character_name);
    json_insert_int(gen, level_field, data->level);

    yajl_gen_string(gen, (unsigned char*) race_field, strlen(race_field));
    race_to_json(gen, data->race);
    
    yajl_gen_string(gen, (unsigned char*) class_field, strlen(class_field));
    class_to_json(gen, data->player_class);
    
    json_insert_int(gen, strength_field, data->strength);
    json_insert_int(gen, agility_field, data->agility);
    json_insert_int(gen, stamina_field, data->stamina);
    json_insert_int(gen, intelligence_field, data->intelligence);
    json_insert_int(gen, spirit_field, data->spirit);
    json_insert_str(gen, map_field, data->map);
  
    yajl_gen_string(gen, (unsigned char*) coords_field, strlen(coords_field));
    coordinates_to_json(gen, data->coords);
    
    /* Close JSON structure */
    yajl_gen_map_close(gen);
    /* stdup string because yajl_gen_free will mess things up */
    json = strdup(get_json_str(gen));
    yajl_gen_free(gen);
    return json;
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

