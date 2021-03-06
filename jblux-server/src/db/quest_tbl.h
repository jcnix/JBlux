/*
 * File: quest_tbl.h
 * Author: Casey Jones
 */

#ifndef _QUEST_TBL_H
#define _QUEST_TBL_H

#define _GNU_SOURCE
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <libpq-fe.h>
#include "db.h"
#include "../quest.h"

struct quest* db_get_quest(int id);
int db_get_num_active_quests(int player_id);
void db_add_quest_to_log(int player_id, int quest_id);
void db_complete_quest_in_log(int player_id, int quest_id);
int have_quest(int player_id, int quest_id);

#endif
