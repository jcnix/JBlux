/*
 * File: db_user_tbl.c
 * Author: Casey Jones
 */

#include "db_user_tbl.h"

int db_authenticate(char* username, char* password, char* character_name)
{
    int auth = 0;
    PGconn *conn = db_connect();
    PGresult *res;

    char* q = "SELECT id FROM $1 WHERE username='$2' and password='$3';";
    int nParams = 3;
    const char* params_1[3] = { USER_TABLE, username, character_name };
    res = db_exec(conn, q, nParams, params_1);

    int id;
    if(PQntuples(res) > 0)
    {
        int id_column = PQfnumber(res, "id");
        char* cid = PQgetvalue(res, 0, id_column);
        id = atoi(cid);
        PQclear(res);
        auth = 1;
    }
    else
    {
        auth = 0;
    }

    if(auth)
    {
        q = "SELECT id FROM $1 WHERE name='$2' AND user_id=$3;";
        nParams = 3;
        
        /* Lets use glibc's asprintf because it's safer than sprintf */
        char* cid = NULL;
        if(asprintf(&cid, "%d", id) < 0)
        {
            db_disconnect(conn);
            return;
        }

        const char* params_2[3] = { CHARACTER_TABLE, character_name, cid };
        res = db_exec(conn, q, nParams, params_2);

        if(PQntuples(res) > 0)
            auth = 1;
        else
            auth = 0;
    }

    PQclear(res);
    db_disconnect(conn);
    return auth;
}

struct player_data db_get_player(char* character_name)
{
}

void db_set_map(int char_id, int map_id, struct coordinates_t coords)
{
    PGconn *conn = db_connect();
    char* q = "UPDATE $1 SET current_map_id=$2, x_coord=$3, y_coord=$4, WHERE id=$5;";
    int nParams = 5;
    char *cmap_id, *cx, *cy, *cid;
    if( (asprintf(&cmap_id, "%d", map_id) < 0) ||
        (asprintf(&cx, "%d", coords.x) < 0) ||
        (asprintf(&cy, "%d", coords.y) < 0) ||
        (asprintf(&cid, "%d", char_id) < 0))
    {
        /* saving the map location isn't _that_ important */
        db_disconnect();
        return;
    }

    const char* params[5] = { CHARACTER_TABLE, cmap_id, cx,
        cy, cid };

    db_exec(conn, q, nParams, params);
    db_disconnect(conn);
}

int get_map_for_player(char* character)
{
}

struct race_t get_race(int id)
{
}

struct class_t get_class(int id)
{
}

