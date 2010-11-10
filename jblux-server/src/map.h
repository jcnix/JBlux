/*
 * File: map.h
 * Author: Casey Jones
 */

#ifndef _MAP_H
#define _MAP_H

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "relation.h"
#include "types.h"
#include "db/db_map_tbl.h"

struct map_t
{
    int id;
    char *name;
    struct item_t *items;
    struct npc_list *enemies;

    int map_left;
    int map_right;
    int map_above;
    int map_below;

    struct coordinates_t left_ent;
    struct coordinates_t right_ent;
    struct coordinates_t top_ent;
    struct coordinates_t bottom_ent;
};

struct map_list
{
    struct map_t *map;
    struct map_list *next;
};

void init_maps();
struct map_t* get_map_for_id(int id);
struct map_t* get_map_for_name(char* name);
struct map_t* get_adjacent_map(struct map_t *map, enum Relation rel);
struct coordinates_t get_map_entrance(struct map_t *map, enum Relation rel);
struct npc_data* get_enemy_on_map(int npc_id, struct map_t *map);

void cleanup_maps();
void add_map(struct map_list **maps, struct map_t *map);
void delete_map_list(struct map_list **maps);

#endif

