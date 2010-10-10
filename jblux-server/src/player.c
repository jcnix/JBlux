/*
 * File: player.c
 * Author: Casey Jones
 */

#include "player.h"

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

int player_accept_quest(struct player_data *player, int quest_id)
{
    /* Find out if we're below the quest log limit */
    int num_quests = db_get_num_active_quests(player->character_id);
    if(num_quests == MAX_QUESTS || num_quests == -1 ||
            have_quest(player->character_id, quest_id))
    {
        return 0;
    }

    struct quest *quest = db_get_quest(quest_id);
    if(quest->min_level > player->level)
    {
        return 0;
    }

    /* Add quest to log and save to Database */
    add_quest(&player->quest_log, quest);
    db_add_quest_to_log(player->character_id, quest->id);

    return 1;
}

