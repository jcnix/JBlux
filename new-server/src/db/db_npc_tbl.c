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

    char* q = "SELECT * FROM jblux_npc WHERE id=$1;";
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
    
    int column = PQfnumber(res, "job");
    npc->job = atoi(PQgetvalue(res, 0, column));

    column = PQfnumber(res, "race_id");
    npc->race = get_race(atoi(PQgetvalue(res, 0, column)));

    column = PQfnumber(res, "class_t_id");
    npc->player_class = get_class(atoi(PQgetvalue(res, 0, column)));

    npc->quests = NULL;

    PQclear(res);
    db_disconnect(conn);
    return npc;
}

