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
#include "db_npc_tbl.h"
#include "../map.h"
#include "../npc.h"
#include "../player_data.h"
#include "../types.h"

#define MAP_TABLE "jblux-map"

/* Forward declaring */
struct map_list;
struct map_t;

void db_get_all_maps(struct map_list **maps);
char* db_get_map_name_for_id(int id);
int db_get_map_id_for_name(char* name);
void db_get_items_on_map(struct map_t *map);
struct npc_list* db_get_npcs_on_map(int map_id, struct player_data *player);
struct npc_list* db_get_enemies_on_map(int map_id);
struct coordinates_t db_get_map_entrance(int map_id, enum Relation r);

#endif

