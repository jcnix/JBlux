/*
 * File: player_data.h
 * Author: Casey Jones
 */

#ifndef _PLAYER_DATA_H
#define _PLAYER_DATA_H

#include "quest.h"
#include "character.h"
#include "inventory.h"
#include "util/coords.h"

struct player_data
{
    int user_id;
    int character_id;
    char* character_name;
    int level;
    int xp;
    int money;
    int max_hp;
    int hp;
    struct race_t race;
    struct class_t player_class;
    int strength;
    int agility;
    int stamina;
    int intelligence;
    int spirit;
    int map_id;
    char* map;
    struct quest_list *quests;
    struct inventory_t inventory;
    struct coordinates_t coords;
};

#endif

