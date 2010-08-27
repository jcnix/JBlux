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

    char* q = "SELECT id FROM jblux_user WHERE username=$1 and password=$2;";
    int nParams = 2;
    const char* params_1[2] = { username, password };
    res = db_exec(conn, q, nParams, params_1);

    int id;
    if(PQresultStatus(res) != PGRES_TUPLES_OK)
    {
        auth = 0;
    }
    else
    {
        id = atoi(PQgetvalue(res, 0, 0));
        PQclear(res);
        
        q = "SELECT id FROM jblux_character WHERE name=$1 AND user_id=$2;";
        nParams = 2;
        
        /* Lets use glibc's asprintf because it's safer than sprintf */
        char* cid = NULL;
        if(asprintf(&cid, "%d", id) < 0)
        {
            db_disconnect(conn);
            return 0;
        }

        const char* params_2[2] = { character_name, cid };
        res = db_exec(conn, q, nParams, params_2);

        if(PQresultStatus(res) != PGRES_TUPLES_OK)
            auth = 0;
        else
            auth = 1;
    }

    PQclear(res);
    db_disconnect(conn);
    return auth;
}

struct player_data db_get_player(char* character_name)
{
}

void db_set_map_for_player(int char_id, int map_id, struct coordinates_t coords)
{
    PGconn *conn = db_connect();
    char* q = "UPDATE jblux_character SET current_map_id=$1, x_coord=$2, y_coord=$3, WHERE id=$4;";
    int nParams = 4;
    char *cmap_id, *cx, *cy, *cid;
    if( (asprintf(&cmap_id, "%d", map_id) < 0) ||
        (asprintf(&cx, "%d", coords.x) < 0) ||
        (asprintf(&cy, "%d", coords.y) < 0) ||
        (asprintf(&cid, "%d", char_id) < 0))
    {
        /* saving the map location isn't _that_ important */
        db_disconnect(conn);
        return;
    }

    const char* params[4] = {cmap_id, cx, cy, cid };
    db_exec(conn, q, nParams, params);
    db_disconnect(conn);
}

int get_map_for_player(char* character)
{
    int map_id = -1;
    PGconn *conn = db_connect();
    PGresult *res;

    char* q = "SELECT current_map_id FROM jblux_character WHERE name=$1;";
    int nParams = 1;
    const char* params[1] = { character };
    res = db_exec(conn, q, nParams, params);
    map_id = atoi(PQgetvalue(res, 0, 0));

    PQclear(res);
    db_disconnect(conn);
    return map_id;
}

struct race_t get_race(int id)
{
    struct race_t race;
    race.id = id;
    PGconn *conn = db_connect();
    PGresult *res;

    char* q = "SELECT * FROM jblux_race WHERE id=$1;";
    int nParams = 1;
    char *cid;
    if(asprintf(&cid, "%d", id) < 0)
    {
        db_disconnect(conn);
        race.name = "";
        race.sprite_sheet = "";
        race.sprite_height = 0;
        return race;
    }
    const char* params[1] = { cid };
    res = db_exec(conn, q, nParams, params);

    int column = PQfnumber(res, "name");
    race.name = PQgetvalue(res, 0, column);
    column = PQfnumber(res, "sprite_sheet");
    race.sprite_sheet = PQgetvalue(res, 0, column);
    column = PQfnumber(res, "sprite_sheet");
    race.sprite_height = atoi(PQgetvalue(res, 0, column));

    PQclear(res);
    db_disconnect(conn);
    return race;
}

struct class_t get_class(int id)
{
    struct class_t class;
    class.id = id;
    PGconn *conn = db_connect();
    PGresult *res;

    char* q = "SELECT * FROM jblux_class WHERE id=$1;";
    int nParams = 1;
    char *cid;
    if(asprintf(&cid, "%d", id) < 0)
    {
        db_disconnect(conn);
        class.name = "";
        return class;
    }
    const char* params[1] = { cid };
    res = db_exec(conn, q, nParams, params);

    int column = PQfnumber(res, "name");
    class.name = PQgetvalue(res, 0, column);

    PQclear(res);
    db_disconnect(conn);
    return class;
}

