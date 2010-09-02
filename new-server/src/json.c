/*
 * File: json.c
 * Author: Casey Jones
 */

#include "json.h"

char* get_json_str(yajl_gen gen)
{
    char* json;
    yajl_gen_status stat;
    unsigned int len = 0;
    stat = yajl_gen_get_buf(gen, (const unsigned char**) &json, &len);
    yajl_gen_clear(gen);
    yajl_gen_free(gen);

    return json;
}

yajl_gen player_data_to_json(struct player_data *data)
{
    yajl_gen_config conf = { 0 };
    yajl_gen gen;
    yajl_gen_status stat;
    gen = yajl_gen_alloc(&conf, NULL);

    /* Field names */
    const unsigned char* user_id_field =        (unsigned char*) "user_id";
    const unsigned char* character_id_field =   (unsigned char*) "character_id";
    const unsigned char* character_name_field = (unsigned char*) "character_name";
    const unsigned char* level_field =          (unsigned char*) "level";
    const unsigned char* race_field =           (unsigned char*) "race";
    const unsigned char* class_field =          (unsigned char*) "player_class";
    const unsigned char* strength_field =       (unsigned char*) "strength";
    const unsigned char* agility_field =        (unsigned char*) "agility";
    const unsigned char* stamina_field =        (unsigned char*) "stamina";
    const unsigned char* intelligence_field =   (unsigned char*) "intelligence";
    const unsigned char* spirit_field =         (unsigned char*) "spirit";
    const unsigned char* map_field =            (unsigned char*) "map";
    const unsigned char* inventory_field =      (unsigned char*) "inventory";
    const unsigned char* coords_field =         (unsigned char*) "coords";

    /* Open JSON structure */
    yajl_gen_map_open(gen);
    
    stat = yajl_gen_string(gen, user_id_field, strlen((char*) user_id_field));
    stat = yajl_gen_integer(gen, data->user_id);

    stat = yajl_gen_string(gen, character_id_field, strlen((char*) character_id_field));
    stat = yajl_gen_integer(gen, data->character_id);
    
    const unsigned char* char_name = (unsigned char*) data->character_name;
    stat = yajl_gen_string(gen, character_name_field, strlen((char*) character_name_field));
    stat = yajl_gen_string(gen, char_name, 5);
    
    stat = yajl_gen_string(gen, level_field, strlen((char*) level_field));
    stat = yajl_gen_integer(gen, data->level);

    /* TODO: Get race */
    /* TODO: Get class */
    
    stat = yajl_gen_string(gen, strength_field, strlen((char*) strength_field));
    stat = yajl_gen_integer(gen, data->strength);
    
    stat = yajl_gen_string(gen, agility_field, strlen((char*) agility_field));
    stat = yajl_gen_integer(gen, data->agility);

    stat = yajl_gen_string(gen, stamina_field, strlen((char*) stamina_field));
    stat = yajl_gen_integer(gen, data->stamina);
    
    stat = yajl_gen_string(gen, intelligence_field, strlen((char*) intelligence_field));
    stat = yajl_gen_integer(gen, data->intelligence);
    
    stat = yajl_gen_string(gen, spirit_field, strlen((char*) spirit_field));
    stat = yajl_gen_integer(gen, data->spirit);
    
    const unsigned char* map_name = (unsigned char*) data->map;
    stat = yajl_gen_string(gen, map_field, strlen((char*) map_field));
    stat = yajl_gen_string(gen, map_name, strlen((char*) map_name));
  
    /* TODO: Get Inventory */
    const char* coords_json = get_json_str(coordinates_to_json(data->coords));
    stat = yajl_gen_string(gen, coords_field, strlen((char*) coords_field));
    stat = yajl_gen_string(gen, (unsigned char*) coords_json, strlen(coords_json));

    /* Close JSON structure */
    yajl_gen_map_close(gen);
    return gen;
}

yajl_gen coordinates_to_json(struct coordinates_t coords)
{
    yajl_gen_config conf = { 0 };
    yajl_gen gen;
    yajl_gen_status stat;
    gen = yajl_gen_alloc(&conf, NULL);

    /* Field names */
    const unsigned char* x_field =  (unsigned char*) "x";
    const unsigned char* y_field =  (unsigned char*) "y";
    
    yajl_gen_map_open(gen);

    stat = yajl_gen_string(gen, x_field, strlen((char*) x_field));
    stat = yajl_gen_integer(gen, coords.x);
    
    stat = yajl_gen_string(gen, y_field, strlen((char*) y_field));
    stat = yajl_gen_integer(gen, coords.y);
    
    yajl_gen_map_close(gen);

    return gen;
}

