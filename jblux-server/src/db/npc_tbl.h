/*
 * File: db_npc_tbl.h
 * Author: Casey Jones
 */

#ifndef _DB_NPC_TBL_H
#define _DB_NPC_TBL_H

#define _GNU_SOURCE
#include <stdio.h>
#include <stdlib.h>
#include <libpq-fe.h>
#include "db.h"
#include "user_tbl.h"
#include "../player_data.h"
#include "quest_tbl.h"

#define NPC_TABLE "jblux_npc"

struct npc_data* db_get_npc(int id);
struct quest_list* db_get_quests_for_npc(int npc_id, struct player_data *player);

#endif

