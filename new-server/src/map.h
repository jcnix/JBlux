/*
 * File: map.h
 * Author: Casey Jones
 */

#ifndef _MAP_H
#define _MAP_H

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "db/db_map_tbl.h"

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

void init_maps();
struct map_t* get_map_for_id(int id);
struct map_t* get_map_for_name(char* name);
struct map_t* get_adjacent_map(enum Relation r, struct map_t *map);

#endif

