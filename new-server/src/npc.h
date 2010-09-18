/*
 * File: npc.h
 * Author: Casey Jones
 */

#ifndef _NPC_H
#define _NPC_H

#include <stdio.h>
#include <stdlib.h>
#include <yajl/yajl_gen.h>
#include "json.h"
#include "quest.h"

char* npc_list_to_json(struct npc_data *npcs);

#endif

