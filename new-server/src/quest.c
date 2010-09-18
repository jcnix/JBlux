/*
 * File: quest.c
 * Author: Casey Jones
 */

#include "quest.h"

void quest_to_json(yajl_gen gen, struct quest *q)
{
    const char* id_field =                      "id";
    const char* name_field =                    "name";
    const char* details_field =                 "details";
    const char* objectives_field =              "objectives";
    const char* completion_field =              "completion_text";
    const char* min_level_field =               "min_level";
    const char* type_field =                    "type";
    const char* quest_item_field =              "quest_item_id";
    const char* reward_xp_field =               "reward_xp";
    const char* reward_money_field =            "reward_money";
    const char* reward_item1_field =            "reward_item1_id";
    const char* reward_item2_field =            "reward_item2_id";
    const char* reward_item3_field =            "reward_item3_id";
    const char* reward_item1_count_field =      "reward_item1_count";
    const char* reward_item2_count_field =      "reward_item2_count";
    const char* reward_item3_count_field =      "reward_item3_count";
    const char* required_item1_field =          "required_item1_id";
    const char* required_item2_field =          "required_item2_id";
    const char* required_item3_field =          "required_item3_id";
    const char* required_item1_count_field =    "required_item1_count";
    const char* required_item2_count_field =    "required_item2_count";
    const char* required_item3_count_field =    "required_item3_count";
    const char* required_npc1_field =           "required_npc1_id";
    const char* required_npc2_field =           "required_npc2_id";
    const char* required_npc3_field =           "required_npc3_id";
    const char* required_npc1_count_field =     "required_npc1_count";
    const char* required_npc2_count_field =     "required_npc2_count";
    const char* required_npc3_count_field =     "required_npc3_count";

    yajl_gen_map_open(gen);
    
    json_insert_int(gen, id_field, q->id);
    json_insert_str(gen, name_field, q->name);
    json_insert_str(gen, details_field, q->details);
    json_insert_str(gen, objectives_field, q->objectives);
    json_insert_str(gen, completion_field, q->completion_text);
    json_insert_int(gen, min_level_field, q->min_level);
    json_insert_int(gen, type_field, q->type);
    json_insert_int(gen, quest_item_field, q->quest_item_id);

    json_insert_int(gen, reward_xp_field, q->reward_xp);
    json_insert_int(gen, reward_money_field, q->reward_money);
    json_insert_int(gen, reward_item1_field, q->reward_item1_id);
    json_insert_int(gen, reward_item2_field, q->reward_item2_id);
    json_insert_int(gen, reward_item3_field, q->reward_item3_id);
    json_insert_int(gen, reward_item1_count_field, q->reward_item1_count);
    json_insert_int(gen, reward_item2_count_field, q->reward_item2_count);
    json_insert_int(gen, reward_item3_count_field, q->reward_item3_count);
    
    json_insert_int(gen, required_item1_field, q->required_item1_id);
    json_insert_int(gen, required_item2_field, q->required_item2_id);
    json_insert_int(gen, required_item3_field, q->required_item3_id);
    json_insert_int(gen, required_item1_count_field, q->required_item1_count);
    json_insert_int(gen, required_item2_count_field, q->required_item2_count);
    json_insert_int(gen, required_item3_count_field, q->required_item3_count);
    
    json_insert_int(gen, required_npc1_field, q->required_npc1_id);
    json_insert_int(gen, required_npc2_field, q->required_npc2_id);
    json_insert_int(gen, required_npc3_field, q->required_npc3_id);
    json_insert_int(gen, required_npc1_count_field, q->required_npc1_count);
    json_insert_int(gen, required_npc2_count_field, q->required_npc2_count);
    json_insert_int(gen, required_npc3_count_field, q->required_npc3_count);

    yajl_gen_map_close(gen);
}

