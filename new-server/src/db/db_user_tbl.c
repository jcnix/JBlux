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
    if(PQntuples(res) < 1)
    {
        auth = 0;
    }
    else
    {
        char* cid = NULL;
        id = atoi(PQgetvalue(res, 0, 0));
        PQclear(res);
        
        q = "SELECT id FROM jblux_character WHERE name=$1 AND user_id=$2;";
        nParams = 2;
        
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

        free(cid);
    }

    PQclear(res);
    db_disconnect(conn);
    return auth;
}

struct player_data* db_get_player(char* character_name)
{
    struct player_data *data = malloc(sizeof(struct player_data));
    PGconn *conn = db_connect();
    PGresult *res;

    char* q = "SELECT user_id, id, name, level, strength, agility, stamina,"
        "intelligence, spirit, current_map_id, race_id, class_t_id, x_coord,"
        "y_coord, inventory_id FROM jblux_character WHERE name=$1;";
    int nParams = 1;
    const char* params[1] = { character_name };
    res = db_exec(conn, q, nParams, params);
    
    int column = 0;
    data->user_id = atoi(PQgetvalue(res, 0, column));
    
    column++;
    data->character_id = atoi(PQgetvalue(res, 0, column));
    
    column++;
    data->character_name = PQgetvalue(res, 0, column);
   
    column++;
    data->level = atoi(PQgetvalue(res, 0, column));
   
    column++;
    data->strength = atoi(PQgetvalue(res, 0, column));
   
    column++;
    data->agility = atoi(PQgetvalue(res, 0, column));
   
    column++;
    data->stamina = atoi(PQgetvalue(res, 0, column));
   
    column++;
    data->intelligence = atoi(PQgetvalue(res, 0, column));
   
    column++;
    data->spirit = atoi(PQgetvalue(res, 0, column));
   
    column++;
    int map_id = atoi(PQgetvalue(res, 0, column));
    data->map_id = map_id;
    data->map = get_map_name_for_id(map_id);

    column++;
    data->race = get_race(atoi(PQgetvalue(res, 0, column)));

    column++;
    data->player_class = get_class(atoi(PQgetvalue(res, 0, column)));

    column++;
    data->coords.x = atoi(PQgetvalue(res, 0, column));
    column++;
    data->coords.y = atoi(PQgetvalue(res, 0, column));
    /* TODO: get inventory */
    data->inventory.id = 0;

    PQclear(res);
    db_disconnect(conn);
    return data;
}

void db_set_map_for_player(int char_id, int map_id, struct coordinates_t coords)
{
    PGconn *conn = db_connect();
    PGresult *res;

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
    res = db_exec(conn, q, nParams, params);
    
    free(cmap_id);
    free(cx);
    free(cy);
    free(cid);
    PQclear(res);
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
    column = PQfnumber(res, "sprite_height");
    race.sprite_height = atoi(PQgetvalue(res, 0, column));

    free(cid);
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

    free(cid);
    PQclear(res);
    db_disconnect(conn);
    return class;
}

