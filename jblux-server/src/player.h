/*
 * File: player.h
 * Author: Casey Jones
 */

#ifndef _PLAYER_H
#define _PLAYER_H

#include <stdio.h>
#include <stdlib.h>
#include "json.h"
#include "quest.h"
#include "types.h"
#include "db/quest_tbl.h"

#define MAX_QUESTS 25

struct player_data
{
    int user_id;
    int character_id;
    char* character_name;
    int level;
    struct race_t race;
    struct class_t player_class;
    int strength;
    int agility;
    int stamina;
    int intelligence;
    int spirit;
    int map_id;
    char* map;
    struct quest_list *quest_log;
    struct inventory_t inventory;
    struct coordinates_t coords;
};

char* player_data_to_json(struct player_data *data);
int player_accept_quest(struct player_data *player, int quest_id);

#endif

