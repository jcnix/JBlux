/*
 * File: db_map_tbl.c
 * Author: Casey Jones
 */

#include "db_map_tbl.h"

struct map_t* db_get_all_maps()
{
    struct map_t *maps;

    PGconn *conn = db_connect();
    PGresult *res;

    char* q = "SELECT * FROM jblux_map;";
    int nParams = 0;
    res = db_exec(conn, q, nParams, NULL);
    int rows = PQntuples(res);
    maps = malloc(sizeof(struct map_t) * rows);

    int i;
    for(i = 0; i < rows; i++)
    {
        struct map_t map;
        
        int column = PQfnumber(res, "id");
        map.id = atoi(PQgetvalue(res, i, column));
        
        column = PQfnumber(res, "name");
        map.name = PQgetvalue(res, i, column);
    
        column = PQfnumber(res, "map_left_id");
        map.map_left = atoi(PQgetvalue(res, i, column));
    
        column = PQfnumber(res, "map_right_id");
        map.map_right = atoi(PQgetvalue(res, i, column));
    
        column = PQfnumber(res, "map_top_id");
        map.map_above = atoi(PQgetvalue(res, i, column));
    
        column = PQfnumber(res, "map_below_id");
        map.map_below = atoi(PQgetvalue(res, i, column));
    
        column = PQfnumber(res, "entrance_left_x");
        map.map_below = atoi(PQgetvalue(res, i, column));

        /* TODO: initialize map.npcs and map.items */
        map.npcs = NULL;
        map.items = NULL;
        *(maps + i) = map;
    }

    PQclear(res);
    db_disconnect(conn);
    return maps;
}

int db_get_adjacent_map(enum Relation r, int map_id)
{
}

char* get_map_name_for_id(int id)
{
    char* name;
    PGconn *conn = db_connect();
    PGresult *res;

    char* q = "SELECT name FROM jblux_map WHERE id=$1;";
    int nParams = 1;
    char *cid;
    
    if(asprintf(&cid, "%d", id) < 0)
    {
        db_disconnect(conn);
        return NULL;
    }

    const char* params[1] = { cid };
    res = db_exec(conn, q, nParams, params);
    name = PQgetvalue(res, 0, 0);

    free(cid);
    PQclear(res);
    db_disconnect(conn);
    return name;
}

int get_map_id_for_name(char* name)
{
    int id;
    PGconn *conn = db_connect();
    PGresult *res;

    char* q = "SELECT id FROM jblux_map WHERE name=$1;";
    int nParams = 1;
    const char* params[1] = { name };
    res = db_exec(conn, q, nParams, params);
    id = atoi(PQgetvalue(res, 0, 0));

    PQclear(res);
    db_disconnect(conn);
    return id;
}

struct item_t* db_get_items_on_map(int map_id)
{
}

struct npc_data_t* db_get_npcs_on_map(int map_id)
{
}

struct coordinates_t db_get_map_entrance(int map_id, enum Relation r)
{
    struct coordinates_t coords;
    PGconn *conn = db_connect();
    PGresult *res;

    char* q = NULL;
    if(r == LEFT)
        q = "SELECT entrance_left_x, entrance_left_y FROM jblux_map WHERE id=$1;";
    else if(r == RIGHT)
        q = "SELECT entrance_right_x, entrance_right_y FROM jblux_map WHERE id=$1;";
    else if(r == ABOVE)
        q = "SELECT entrance_top_x, entrance_top_y FROM jblux_map WHERE id=$1;";
    else if(r == BELOW)
        q = "SELECT entrance_bottom_x, entrance_bottom_y FROM jblux_map WHERE id=$1;";

    int nParams = 1;
    char* cid = NULL;
    if(asprintf(&cid, "%d", map_id) < 0)
    {
        db_disconnect(conn);
        struct coordinates_t coords;
        coords.x = -1;
        coords.y = -1;
        return coords;
    }

    const char* params[1] = { cid };
    res = db_exec(conn, q, nParams, params);

    int x_column = 0;
    int y_column = 0;
    if(r == LEFT)
    {
        x_column = PQfnumber(res, "entrance_left_x");
        y_column = PQfnumber(res, "entrance_left_y");
    }
    else if(r == RIGHT)
    {
        x_column = PQfnumber(res, "entrance_right_x");
        y_column = PQfnumber(res, "entrance_right_y");
    }
    else if(r == ABOVE)
    {
        x_column = PQfnumber(res, "entrance_top_x");
        y_column = PQfnumber(res, "entrance_top_y");
    }
    else if(r == BELOW)
    {
        x_column = PQfnumber(res, "entrance_bottom_x");
        y_column = PQfnumber(res, "entrance_bottom_y");
    }

    coords.x = atoi(PQgetvalue(res, 0, x_column));
    coords.y = atoi(PQgetvalue(res, 0, y_column));

    free(cid);
    PQclear(res);
    db_disconnect(conn);
    return coords;
}

