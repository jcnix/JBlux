/*
 * File: world.c
 * Author: Casey Jones
 */

#include "world.h"

static struct npc_list *dead_npcs;
static pthread_mutex_t dead_npcs_mutex = PTHREAD_MUTEX_INITIALIZER;
static void respawn_npcs();

void* init_world()
{
    while(1)
    {
        sleep(5);
        respawn_npcs();
    }
    return NULL;
}

void add_dead_npc(struct npc_data *npc)
{
    pthread_mutex_lock(&dead_npcs_mutex);
    add_npc(&dead_npcs, npc);
    pthread_mutex_unlock(&dead_npcs_mutex);
}

void respawn_npcs()
{
    struct npc_list *curr = dead_npcs;
    pthread_mutex_lock(&dead_npcs_mutex);
    while(curr)
    {
        struct npc_data *npc = curr->npc;
        printf("npc: %d\n", npc->npc_id);
        npc->hp = npc->max_hp;
        char* npc_json = npc_to_json(npc);
        char* command = NULL;
        if(!asprintf(&command, "npc add %s", npc_json))
        {
            continue;
        }

        tell_all_players_on_map(0, npc->map_id, command);
        struct npc_list *next = curr->next;
        free(curr);
        free(npc_json);
        free(command);
        curr = next;
    }
    dead_npcs = NULL;
    pthread_mutex_unlock(&dead_npcs_mutex);
}

