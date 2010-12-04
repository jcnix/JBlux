/*
 * File: npc.h
 * Author: Casey Jones
 */

#ifndef _NPC_H
#define _NPC_H

#define _GNU_SOURCE
#include <stdio.h>
#include <stdlib.h>
#include <math.h>
#include <time.h>
#include <yajl/yajl_gen.h>
#include "client.h"
#include "character.h"
#include "map.h"
#include "quest.h"
#include "world.h"
#include "rules/combat.h"
#include "util/coords.h"
#include "util/json.h"

#define NPC_RESPAWN_TIME 5
#define MOVE_STEP 7

struct npc_data
{
    int npc_id;
    int map_id;
    int unique_id;
    int job;
    char* sprite_sheet;
    char* direction;
    struct coordinates_t coords;
    struct quest_list *quests;

    char* character_name;
    int level;
    int max_hp;
    int hp;
    struct race_t race;
    struct class_t player_class;
    int strength;
    int agility;
    int stamina;
    int intelligence;
    int spirit;
    
    time_t respawn_time;
    struct player_data *target;
};

struct npc_list
{
    struct npc_data *npc;
    struct npc_list *next;
};

char* npc_list_to_json(struct npc_list *npcs);
char* npc_to_json(struct npc_data *npc);
void attack_npc(int id, int map_id, struct player_data *player);
void npc_move(struct npc_data *npc);
void add_quests_to_npcs(struct npc_list **npcs);

/* struct npc_list functions */
void add_npc(struct npc_list **npcs, struct npc_data *npc);
void rm_npc(struct npc_list **npcs, struct npc_data *npc);
void delete_npcs(struct npc_list **npcs);

#endif

