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
#include "db_user_tbl.h"
#include "../types.h"

#define NPC_TABLE "jblux_npc"

struct npc_data* get_npc(int id);

#endif

