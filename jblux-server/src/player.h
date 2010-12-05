/*
 * File: player.h
 * Author: Casey Jones
 */

#ifndef _PLAYER_H
#define _PLAYER_H

#include <stdio.h>
#include <stdlib.h>
#include "player_data.h"
#include "quest.h"
#include "util/json.h"
#include "db/quest_tbl.h"
#include "db/user_tbl.h"

#define MAX_QUESTS 25

char* player_data_to_json(struct player_data *data);
int player_accept_quest(struct player_data *player, int quest_id);
int player_complete_quest(struct player_data *player, int quest_id);
void give_quest_reward(struct player_data *player, struct quest *quest);
void give_player_xp(struct player_data *player, int xp);

#endif

