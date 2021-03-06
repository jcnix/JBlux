/*
 * File: npc.c
 * Author: Casey Jones
 */

#include "npc.h"

void npc_json(yajl_gen gen, struct npc_data *data)
{
    /* Field names */
    const char* npc_id_field =          "npc_id";
    const char* map_id_field =          "map_id";
    const char* unique_id_field =       "unique_id";
    const char* job_field =             "job";
    const char* character_name_field =  "character_name";
    const char* sprite_sheet_field =    "sprite_sheet";
    const char* direction_field =       "direction";
    const char* level_field =           "level";
    const char* hp_field =              "hp";
    const char* race_field =            "race";
    const char* class_field =           "player_class";
    const char* strength_field =        "strength";
    const char* agility_field =         "agility";
    const char* stamina_field =         "stamina";
    const char* intelligence_field =    "intelligence";
    const char* spirit_field =          "spirit";
    const char* coords_field =          "coords";
    
    yajl_gen_map_open(gen);
    json_insert_int(gen, npc_id_field, data->npc_id);
    json_insert_int(gen, map_id_field, data->map_id);
    json_insert_int(gen, unique_id_field, data->unique_id);
    json_insert_int(gen, job_field, data->job);
    json_insert_str(gen, character_name_field, data->character_name);
    json_insert_str(gen, sprite_sheet_field, data->sprite_sheet);
    json_insert_str(gen, direction_field, data->direction);
    
    quest_list_to_json(gen, data->quests);
    json_insert_int(gen, level_field, data->level);
    json_insert_int(gen, hp_field, data->hp);
    
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
    yajl_gen_map_close(gen);
}

char* npc_list_to_json(struct npc_list *npcs)
{
    char* json;
    yajl_gen_config conf = { 0 };
    yajl_gen gen;
    gen = yajl_gen_alloc(&conf, NULL);

    yajl_gen_array_open(gen);
    while(npcs)
    {
        struct npc_data *data = npcs->npc;
        npc_json(gen, data);
        npcs = npcs->next;
    }
    yajl_gen_array_close(gen);

    json = strdup(get_json_str(gen));
    yajl_gen_free(gen);
    return json;
}

char* npc_to_json(struct npc_data *npc)
{
    char* json;
    yajl_gen_config conf = { 0 };
    yajl_gen gen;
    gen = yajl_gen_alloc(&conf, NULL);
    npc_json(gen, npc);
    json = strdup(get_json_str(gen));
    yajl_gen_free(gen);
    return json;
}

void attack_npc(int npc_id, int map_id, struct player_data *player)
{
    struct map_t *map = get_map_for_id(map_id);
    struct npc_data *npc = get_enemy_on_map(npc_id, map);
    if(npc->hp <= 0)
    {
        /* Npc is already dead */
        return;
    }
    
    /* Aggro npc */
    if(npc->target == NULL)
    {
        npc->target = player;
        add_aggro_npc(npc);
    }

    /* TODO: calculate damage */
    struct combattant attack = get_player_combattant(player);
    struct combattant defend = get_npc_combattant(npc);
    int damage = calc_dmg(attack, defend);
    npc->hp -= damage;
    
    /* Npc is dead */
    if(npc->hp <= 0)
    {
        npc->respawn_time = time(NULL) + NPC_RESPAWN_TIME;
        deaggro_npc(npc);

        char* command = NULL;
        if(!asprintf(&command, "npc rm %d", npc->unique_id))
        {
            return;
        }
        add_dead_npc(npc);

        tell_all_players_on_map(0, npc->map_id, command);
        free(command);
        int xp = npc->level * NPC_BASE_XP;
        give_player_xp(player, xp);
    }
}

void npc_attack(struct npc_data *npc)
{
    struct player_data *player = npc->target;
    
    /* Can only attack if within 32 pixels */
    if(distance(npc->coords, player->coords) < 32)
    {
        player->hp -= 1;
        printf("player %d hp: %d\n", player->character_id, player->hp);
    }
    
    char* command = NULL;
    if(!asprintf(&command, "player %d hp %d", player->character_id,
                player->hp))
    {
        return;
    }
    tell_all_players_on_map(0, npc->map_id, command);
    free(command);

    if(player->hp <= 0) {
        printf("player died\n");
        deaggro_npc(npc);
        player->hp = player->max_hp;
    }
}

void npc_move(struct npc_data *npc)
{
    if(npc->target)
    {
        int dx = npc->target->coords.x - npc->coords.x;
        int dy = npc->target->coords.y - npc->coords.y;

        int move_right = 0;
        int move_down = 0;
        if(dx > 0)
        {
            move_right = 1;
        }
        if(dy > 0)
        {
            move_down = 1;
        }
        
        dx = abs(dx);
        dy = abs(dy);
        
        /* duplicate npc->coords so we can revert to the original */
        struct coordinates_t c;
        c.x = npc->coords.x;
        c.y = npc->coords.y;

        if(dx >= MOVE_STEP)
        {
            if(move_right)
            {
                c.x += MOVE_STEP;
            }
            else
            {
                c.x -= MOVE_STEP;
            }
        }
        if(dy >= MOVE_STEP)
        {
            if(move_down)
            {
                c.y += MOVE_STEP;
            }
            else
            {
                c.y -= MOVE_STEP;
            }
        }

        struct map_t *map = get_map_for_id(npc->map_id);
        if(jbm_can_walk(map, c.x, c.y))
        {
            npc->coords = c;
            char* command = NULL;
            if(!asprintf(&command, "npc %d coords %d %d", npc->unique_id,
                        npc->coords.x, npc->coords.y))
            {
                return;
            }
            tell_all_players_on_map(0, npc->map_id, command);
            free(command);
        }
    }
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
        struct npc_data *npc = curr->npc;
        next = curr->next;
        free(npc->sprite_sheet);
        free(npc->direction);
        delete_quest_list(&npc->quests);
        free(npc->character_name);
        free(npc->race.name);
        free(npc->race.sprite_sheet);
        free(npc->player_class.name);
        free(npc);
        free(curr);
        curr = next;
    }
}

void rm_npc(struct npc_list **npcs, struct npc_data *npc)
{
    struct npc_list *curr = *npcs;
    struct npc_list *prev = NULL;
    while(curr)
    {
        /* We're only going to determine equality by socket */
        if(curr->npc->unique_id == npc->unique_id)
        {
            if(prev == NULL)
            {
                /* We're at the head of the list */
                *npcs = curr->next;
            }
            else
            {
                prev->next = curr->next;
            }
           
            struct npc_data *npc = curr->npc;
            npc->target = NULL;
            free(curr);
            return;
        }
        prev = curr;
        curr = curr->next;
    }
}

