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

    char* q = "SELECT name, job. race_id, class_t_id, sprite_sheet, level, "
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
    npc->level = db_get_int(db_get_int(res, 0, column));
    
    column++;
    npc->strength = db_get_int(db_get_int(res, 0, column));
    
    column++;
    npc->agility = db_get_int(db_get_int(res, 0, column));
    
    column++;
    npc->stamina = db_get_int(db_get_int(res, 0, column));
    
    column++;
    npc->intelligence = db_get_int(db_get_int(res, 0, column));
    
    column++;
    npc->spirit = db_get_int(db_get_int(res, 0, column));
    
    npc->quests = NULL;

    PQclear(res);
    db_disconnect(conn);
    return npc;
}

