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

    return json;
}

char* player_data_to_json(struct player_data *data)
{
    char* json;
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

    const char* race_json = race_to_json(data->race);
    stat = yajl_gen_string(gen, race_field, strlen((char*) race_field));
    stat = yajl_gen_string(gen, (unsigned char*) race_json, strlen(race_json));
    
    const char* class_json = class_to_json(data->player_class);
    stat = yajl_gen_string(gen, class_field, strlen((char*) class_field));
    stat = yajl_gen_string(gen, (unsigned char*) class_json, strlen(class_json));
    
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
    const char* coords_json = coordinates_to_json(data->coords);
    stat = yajl_gen_string(gen, coords_field, strlen((char*) coords_field));
    stat = yajl_gen_string(gen, (unsigned char*) coords_json, strlen(coords_json));

    /* Close JSON structure */
    yajl_gen_map_close(gen);
    json = get_json_str(gen);
    yajl_gen_free(gen);
    return json;
}

char* coordinates_to_json(struct coordinates_t coords)
{
    char* json;
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
    json = get_json_str(gen);
    yajl_gen_free(gen);
    return json;
}

char* race_to_json(struct race_t race)
{
    char* json;
    yajl_gen_config conf = { 0 };
    yajl_gen gen;
    yajl_gen_status stat;
    gen = yajl_gen_alloc(&conf, NULL);

    /* Field names */
    const unsigned char* id_field =  (unsigned char*) "id";
    const unsigned char* name_field =  (unsigned char*) "name";
    const unsigned char* sprite_sheet_field =  (unsigned char*) "sprite_sheet";
    const unsigned char* sprite_height_field =  (unsigned char*) "sprite_height";
    
    yajl_gen_map_open(gen);

    stat = yajl_gen_string(gen, id_field, strlen((char*) id_field));
    stat = yajl_gen_integer(gen, race.id);
    
    const unsigned char* name = (unsigned char*) race.name;
    stat = yajl_gen_string(gen, name_field, strlen((char*) name_field));
    stat = yajl_gen_string(gen, name, strlen(race.name));
    
    const unsigned char* sheet = (unsigned char*) race.sprite_sheet;
    stat = yajl_gen_string(gen, sprite_sheet_field, strlen((char*) sprite_sheet_field));
    stat = yajl_gen_string(gen, sheet, strlen(race.sprite_sheet));
    
    stat = yajl_gen_string(gen, sprite_height_field, strlen((char*) sprite_height_field));
    stat = yajl_gen_integer(gen, race.sprite_height);
   
    yajl_gen_map_close(gen);
    json = get_json_str(gen);
    yajl_gen_free(gen);
    return json;
}

char* class_to_json(struct class_t c)
{
    char* json;
    yajl_gen_config conf = { 0 };
    yajl_gen gen;
    yajl_gen_status stat;
    gen = yajl_gen_alloc(&conf, NULL);

    /* Field names */
    const unsigned char* id_field =  (unsigned char*) "id";
    const unsigned char* name_field =  (unsigned char*) "name";
    
    yajl_gen_map_open(gen);

    stat = yajl_gen_string(gen, id_field, strlen((char*) id_field));
    stat = yajl_gen_integer(gen, c.id);
    
    const unsigned char* name = (unsigned char*) c.name;
    stat = yajl_gen_string(gen, name_field, strlen((char*) name_field));
    stat = yajl_gen_string(gen, name, strlen(c.name));
    
    yajl_gen_map_close(gen);
    json = get_json_str(gen);
    yajl_gen_free(gen);
    return json;
}

