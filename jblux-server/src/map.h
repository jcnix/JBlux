/*
 * File: map.h
 * Author: Casey Jones
 */

#ifndef _MAP_H
#define _MAP_H

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "map_t.h"
#include "db/map_tbl.h"
#include "util/relation.h"

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

