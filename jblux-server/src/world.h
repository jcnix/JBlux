/*
 * File: world.h
 * Author: Casey Jones
 */

#ifndef _WORLD_H
#define _WORLD_H

#define _GNU_SOURCE
#include <stdio.h>
#include <stdlib.h>
#include <pthread.h>
#include <time.h>
#include <unistd.h>
#include "client.h"
#include "npc.h"
#include "player_data.h"
#include "util/coords.h"

void* init_world();
void add_dead_npc(struct npc_data *npc);
void add_aggro_npc(struct npc_data *npc);
void deaggro_npc(struct npc_data *npc);

#endif

