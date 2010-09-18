/*
 * File: db_npc_tbl.c
 * Author: Casey Jones
 */

#include "db_npc_tbl.h"

struct npc_data* db_get_npc(int id)
{
    struct npc_data *npc = malloc(sizeof(struct npc_data));
    if(!npc)
    {
        return NULL;
    }

    PGconn *conn = db_connect();
    PGresult *res = NULL;

    char* q = "SELECT name, job, race_id, class_t_id, sprite_sheet, level, "
        "strength, agility, stamina, intelligence, spirit "
        "FROM jblux_npc WHERE id=$1;";
    int nParams = 1;

    char* cid = NULL;
    if(asprintf(&cid, "%d", id) < 0)
    {
        db_disconnect(conn);
        return NULL;
    }

    const char* params[1] = { cid };
    res = db_exec(conn, q, nParams, params);
    npc->npc_id = id;
   
    int column = 0;
    npc->character_name = db_get_str(res, 0, column);

    column++;
    npc->job = db_get_int(res, 0, column);

    column++;
    npc->race = get_race(db_get_int(res, 0, column));

    column++;
    npc->player_class = get_class(db_get_int(res, 0, column));

    column++;
    npc->level = db_get_int(res, 0, column);
    
    column++;
    npc->strength = db_get_int(res, 0, column);
    
    column++;
    npc->agility = db_get_int(res, 0, column);
    
    column++;
    npc->stamina = db_get_int(res, 0, column);
    
    column++;
    npc->intelligence = db_get_int(res, 0, column);
    
    column++;
    npc->spirit = db_get_int(res, 0, column);
    
    npc->quests = db_get_quests_for_npc(id);

    PQclear(res);
    db_disconnect(conn);
    return npc;
}

struct quest* db_get_quests_for_npc(int npc_id)
{
    PGconn *conn = db_connect();
    PGresult *res = NULL;

    char *q = "SELECT id, name, details, objectives, completion_text, "
         "min_level, flag, quest_item_id, reward_xp, reward_money, "
         "rewardItem1_id, rewardItem2_id, rewardItem3_id, rewardItem1_count, "
         "rewardItem2_count, rewardItem3_count, reqItem1_id, reqItem2_id, "
         "reqItem3_id, reqItem1_count, reqItem2_count, reqItem3_count, "
         "reqNpc1_id, reqNpc2_id, reqNpc3_id, reqNpc1_count, reqNpc2_count, "
         "reqNpc3_count FROM jblux_quest WHERE npc_id=$1;";

    int nParams = 1;
    char* cid = NULL;
    if(asprintf(&cid, "%d", npc_id) < 0)
    {
        db_disconnect(conn);
        return NULL;
    }
    const char* params[1] = { cid };
    res = db_exec(conn, q, nParams, params);

    int num_quests = PQntuples(res);
    struct quest *quests = malloc(sizeof(struct quest) * num_quests);

    int i = 0;
    for(i = 0; i < num_quests; i++)
    {
        struct quest *q = malloc(sizeof(struct quest));
        if(!q)
        {
            continue;
        }

        int column = 0;
        q->id = db_get_int(res, i, column);

        column++;
        q->name = db_get_str(res, i, column);

        column++;
        q->details = db_get_str(res, i, column);

        column++;
        q->objectives = db_get_str(res, i, column);

        column++;
        q->completion_text = db_get_str(res, i, column);

        column++;
        q->min_level = db_get_int(res, i, column);

        column++;
        q->type = db_get_int(res, i, column);

        column++;
        q->quest_item_id = db_get_int(res, i, column);

        column++;
        q->reward_xp = db_get_int(res, i, column);

        column++;
        q->reward_money = db_get_int(res, i, column);

        column++;
        q->reward_item1_id = db_get_int(res, i, column);
        column++;
        q->reward_item2_id = db_get_int(res, i, column);
        column++;
        q->reward_item3_id = db_get_int(res, i, column);
        column++;
        q->reward_item1_count = db_get_int(res, i, column);
        column++;
        q->reward_item2_count = db_get_int(res, i, column);
        column++;
        q->reward_item3_count = db_get_int(res, i, column);

        column++;
        q->required_item1_id = db_get_int(res, i, column);
        column++;
        q->required_item2_id = db_get_int(res, i, column);
        column++;
        q->required_item3_id = db_get_int(res, i, column);
        column++;
        q->required_item1_count = db_get_int(res, i, column);
        column++;
        q->required_item2_count = db_get_int(res, i, column);
        column++;
        q->required_item3_count = db_get_int(res, i, column);
        
        column++;
        q->required_npc1_id = db_get_int(res, i, column);
        column++;
        q->required_npc2_id = db_get_int(res, i, column);
        column++;
        q->required_npc3_id = db_get_int(res, i, column);
        column++;
        q->required_npc1_count = db_get_int(res, i, column);
        column++;
        q->required_npc2_count = db_get_int(res, i, column);
        column++;
        q->required_npc3_count = db_get_int(res, i, column);

        *(quests + i) = *q;
    }

    PQclear(res);
    db_disconnect(conn);

    return quests;
}

