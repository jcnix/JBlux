/*
 * File: quest.h
 * Author: Casey Jones
 */

#ifndef _QUEST_H
#define _QUEST_H

#include <stdio.h>
#include <stdio.h>
#include <yajl/yajl_gen.h>
#include "json.h"

struct quest
{
    int id;
    char* name;
    char* details;
    char* objectives;
    char* completion_text;
    int min_level;
    int type;
    int quest_item_id;

    int reward_xp;
    int reward_money;
    int reward_item1_id;
    int reward_item2_id;
    int reward_item3_id;
    int reward_item1_count;
    int reward_item2_count;
    int reward_item3_count;

    int required_item1_id;
    int required_item2_id;
    int required_item3_id;
    int required_item1_count;
    int required_item2_count;
    int required_item3_count;

    int required_npc1_id;
    int required_npc2_id;
    int required_npc3_id;
    int required_npc1_count;
    int required_npc2_count;
    int required_npc3_count;
};

void quest_to_json(yajl_gen gen, struct quest q);

#endif

