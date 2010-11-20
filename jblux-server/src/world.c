/*
 * File: world.c
 * Author: Casey Jones
 */

#include "world.h"

static struct npc_list *dead_npcs;
static pthread_mutex_t dead_npcs_mutex = PTHREAD_MUTEX_INITIALIZER;

static struct npc_list *aggro_npcs;
static pthread_mutex_t aggro_npcs_mutex = PTHREAD_MUTEX_INITIALIZER;

static void respawn_npcs();
static void npcs_attack_target();

void* init_world()
{
    while(1)
    {
        sleep(1);
        respawn_npcs();
        npcs_attack_target();
    }
    return NULL;
}

void add_dead_npc(struct npc_data *npc)
{
    pthread_mutex_lock(&dead_npcs_mutex);
    add_npc(&dead_npcs, npc);
    pthread_mutex_unlock(&dead_npcs_mutex);
}

void add_aggro_npc(struct npc_data *npc)
{
    pthread_mutex_lock(&aggro_npcs_mutex);
    add_npc(&aggro_npcs, npc);
    pthread_mutex_unlock(&aggro_npcs_mutex);
}

void respawn_npcs()
{
    time_t currtime = time(NULL);
    struct npc_list *curr = dead_npcs;
    while(curr)
    {
        struct npc_data *npc = curr->npc;
        printf("%s", ctime(&currtime));
        if(currtime >= npc->respawn_time)
        {
            printf("npc: %d\n", npc->npc_id);
            npc->hp = npc->max_hp;
            /* De-aggro the NPC  */
            if(npc->target)
            {
                pthread_mutex_lock(&aggro_npcs_mutex);
                remove_npc(&aggro_npcs, npc);
                pthread_mutex_unlock(&aggro_npcs_mutex);
            }

            char* npc_json = npc_to_json(npc);
            char* command = NULL;
            if(!asprintf(&command, "npc add %s", npc_json))
            {
                continue;
            }

            tell_all_players_on_map(0, npc->map_id, command);
            pthread_mutex_lock(&dead_npcs_mutex);
            remove_npc(&dead_npcs, npc);
            pthread_mutex_unlock(&dead_npcs_mutex);
            free(npc_json);
            free(command);
        }
        curr = curr->next;
    }
}

void npcs_attack_target()
{
    struct npc_list *curr = aggro_npcs;

    while(curr)
    {
        struct npc_data *npc = curr->npc;
        struct player_data *player = npc->target;
        player->hp -= 1;
        printf("player %d hp: %d\n", player->character_id, player->hp);
        
        char* command = NULL;
        if(!asprintf(&command, "player %d hp %d", player->character_id,
                    player->hp))
        {
            return;
        }

        tell_all_players_on_map(0, npc->map_id, command);
        free(command);

        if(player->hp <= 0) {
            printf("player died\n");
            pthread_mutex_lock(&aggro_npcs_mutex);
            remove_npc(&aggro_npcs, npc);
            pthread_mutex_unlock(&aggro_npcs_mutex);
            player->hp = player->max_hp;
        }

        curr = curr->next;
    }
}

