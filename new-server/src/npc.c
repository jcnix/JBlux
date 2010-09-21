/*
 * File: npc.c
 * Author: Casey Jones
 */

#include "npc.h"

char* npc_list_to_json(struct npc_list *npcs)
{
    char* json;
    yajl_gen_config conf = { 0 };
    yajl_gen gen;
    gen = yajl_gen_alloc(&conf, NULL);

    /* Field names */
    const char* npc_id_field =         "npc_id";
    const char* job_field =            "job";
    const char* character_name_field = "character_name";
    const char* sprite_sheet_field =   "sprite_sheet";
    const char* direction_field =      "direction";
    const char* quests_field =         "quests";
    const char* level_field =          "level";
    const char* race_field =           "race";
    const char* class_field =          "player_class";
    const char* strength_field =       "strength";
    const char* agility_field =        "agility";
    const char* stamina_field =        "stamina";
    const char* intelligence_field =   "intelligence";
    const char* spirit_field =         "spirit";
    const char* coords_field =         "coords";
    
    int i = 0;
    struct npc_data *data = npcs->npc;
    yajl_gen_map_open(gen);
    while(npcs)
    {
        printf("i: %d\n", i);
        json_insert_int(gen, npc_id_field, data->npc_id);
        json_insert_int(gen, job_field, data->job);
        json_insert_str(gen, character_name_field, data->character_name);
        json_insert_str(gen, sprite_sheet_field, data->sprite_sheet);
        json_insert_str(gen, direction_field, data->direction);
        
        /* Quests */
        yajl_gen_string(gen, (unsigned char*) quests_field, strlen(quests_field));
        yajl_gen_array_open(gen);
        int j = 0;
        struct quest_list *quests = data->quests;
        while(quests = NULL)
        {
            printf("j: %d %s\n", j, quests->quest->name);
            quest_to_json(gen, quests->quest);
            j++;
            quests = data->quests->next;
        }
        printf("done\n");
        yajl_gen_array_close(gen);

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
        
        yajl_gen_string(gen, (unsigned char*) coords_field, strlen(coords_field));
        coordinates_to_json(gen, data->coords);

        i++;
        npcs = npcs->next;
    }
    yajl_gen_map_close(gen);
    json = strdup(get_json_str(gen));
    yajl_gen_free(gen);
    return json;
}

void add_npc(struct npc_list **npcs, struct npc_data *npc)
{
    struct npc_list *new = malloc(sizeof(struct npc_list));
    new->npc = npc;
    new->next = *npcs;
    *npcs = new;
}

void delete_npcs(struct npc_list **npcs)
{
    struct npc_list *curr = *npcs;
    struct npc_list *next = NULL;
    while(curr)
    {
        next = curr->next;
        free(curr);
        curr = next;
    }
}

