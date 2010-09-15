/*
 * File: db_map_tbl.h
 * Author: Casey Jones
 */

#ifndef _DB_MAP_TBL_H
#define _DB_MAP_TBL_H

#define _GNU_SOURCE
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <libpq-fe.h>
#include "db.h"
#include "../map.h"
#include "../types.h"

#define MAP_TABLE "jblux-map"

struct map_t* db_get_all_maps();
int db_get_adjacent_map(enum Relation r, int map_id);
char* get_map_name_for_id(int id);
int get_map_id_for_name(char* name);
struct item_t* db_get_items_on_map(int map_id);
struct npc_data_t* db_get_npcs_on_map(int map_id);
struct coordinates_t db_get_map_entrance(int map_id, enum Relation r);

#endif

