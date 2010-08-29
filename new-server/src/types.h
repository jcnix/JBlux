/*
 * File: types.h
 * Author: Casey Jones
 */

#ifndef _TYPES_H
#define _TYPES_H

struct coordinates_t
{
    int x;
    int y;
};

struct map_t
{
    int id;
    char *name;
    struct item_t *items;
    struct npc_data_t *npcs;
    int map_left;
    int map_right;
    int map_above;
    int map_below;
};

struct inventory_t
{
    int id;
};

struct race_t
{
    int id;
    char* name;
    char* sprite_sheet;
    int sprite_height;
};

struct class_t
{
    int id;
    char* name;
};

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
    char* map;
    struct inventory_t inventory;
    struct coordinates_t coords;
};

struct npc_data
{
    int npc_id;
    int job;
    char* sprite_sheet;
    char* direction;
    struct quest *quests;

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

#endif

