/*
 * File: npc.h
 * Author: Casey Jones
 */

#ifndef _NPC_H
#define _NPC_H

#include <stdio.h>
#include <stdlib.h>
#include <yajl/yajl_gen.h>
#include "json.h"
#include "quest.h"

struct npc_data
{
    int npc_id;
    int job;
    char* sprite_sheet;
    char* direction;
    struct coordinates_t coords;
    struct quest_list *quests;

    char* character_name;
    int level;
    struct race_t race;
    struct class_t player_class;
    int strength;
    int agility;
    int stamina;
    int intelligence;
    int spirit;
};

struct npc_list
{
    struct npc_data *npc;
    struct npc_list *next;
};

char* npc_list_to_json(struct npc_list *npcs);
void add_quests_to_npcs(struct npc_list **npcs);
void add_npc(struct npc_list **npcs, struct npc_data *npc);
void delete_npcs(struct npc_list **npcs);

#endif

