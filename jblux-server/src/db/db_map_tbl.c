/*
 * File: db_map_tbl.c
 * Author: Casey Jones
 */

#include "db_map_tbl.h"

void db_get_all_maps(struct map_list **maps)
{
    PGconn *conn = db_connect();
    PGresult *res = NULL;

    char* q = "SELECT id, name, map_left_id, map_right_id, map_top_id, "
        "map_bottom_id FROM jblux_map;";
    int nParams = 0;
    res = db_exec(conn, q, nParams, NULL);
    int rows = PQntuples(res);
    
    int i;
    for(i = 0; i < rows; i++)
    {
        struct map_t *map = malloc(sizeof(struct map_t));
        
        int column = 0;
        map->id = db_get_int(res, i, column);
       
        column++;
        map->name = db_get_str(res, i, column);
    
        column++;
        map->map_left = db_get_int(res, i, column);
        
        column++;
        map->map_right = db_get_int(res, i, column);
        
        column++;
        map->map_above = db_get_int(res, i, column);
        
        column++;
        map->map_below = db_get_int(res, i, column);
   
        /* Get entrances for all four sides */
        map->left_ent    =   db_get_map_entrance(map->id, LEFT);
        map->right_ent   =   db_get_map_entrance(map->id, RIGHT);
        map->top_ent     =   db_get_map_entrance(map->id, ABOVE);
        map->bottom_ent  =   db_get_map_entrance(map->id, BELOW);

        db_get_npcs_on_map(map);
        /* TODO: initialize map->items */
        map->items = NULL;
        add_map(maps, map);
    }

    PQclear(res);
    db_disconnect(conn);
}

char* db_get_map_name_for_id(int id)
{
    char* name = NULL;
    PGconn *conn = db_connect();
    PGresult *res = NULL;

    char* q = "SELECT name FROM jblux_map WHERE id=$1;";
    int nParams = 1;
    char *cid = NULL;
    
    if(asprintf(&cid, "%d", id) < 0)
    {
        db_disconnect(conn);
        return NULL;
    }

    const char* params[1] = { cid };
    res = db_exec(conn, q, nParams, params);
    name = db_get_str(res, 0, 0);

    free(cid);
    PQclear(res);
    db_disconnect(conn);
    return name;
}

int db_get_map_id_for_name(char* name)
{
    int id;
    PGconn *conn = db_connect();
    PGresult *res = NULL;

    char* q = "SELECT id FROM jblux_map WHERE name=$1;";
    int nParams = 1;
    const char* params[1] = { name };
    res = db_exec(conn, q, nParams, params);
    id = db_get_int(res, 0, 0);

    PQclear(res);
    db_disconnect(conn);
    return id;
}

void db_get_items_on_map(struct map_t *map)
{
    /*PGconn *conn = db_connect();
    PGresult *res = NULL;

    PQclear(res);
    db_disconnect(conn);*/
}

void db_get_npcs_on_map(struct map_t *map)
{
    PGconn *conn = db_connect();
    PGresult *res = NULL;

    char* q = "SELECT npc_id, direction, x_coord, y_coord FROM jblux_mapnpcs "
        "WHERE map_t_id=$1;";
    int nParams = 1;
    char* cid = NULL;
    if(asprintf(&cid, "%d", map->id) < 0)
    {
        map->npcs = NULL;
        return;
    }
    const char* params[1] = { cid };
    res = db_exec(conn, q, nParams, params);

    int num_npcs = PQntuples(res);
    struct npc_list *npcs = NULL;

    int i;
    for(i = 0; i < num_npcs; i++)
    {
        int column = 0;
        int npc_id = db_get_int(res, i, column);
        struct npc_data *data = db_get_npc(npc_id);
        
        column++;
        data->direction = db_get_str(res, i, column);

        column++;
        data->coords.x = db_get_int(res, i, column);
        column++;
        data->coords.y = db_get_int(res, i, column);

        add_npc(&npcs, data);
    }

    map->npcs = npcs;
    PQclear(res);
    db_disconnect(conn);
}

struct coordinates_t db_get_map_entrance(int map_id, enum Relation r)
{
    struct coordinates_t coords;
    PGconn *conn = db_connect();
    PGresult *res = NULL;

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
    int y_column = 1;
    
    coords.x = db_get_int(res, 0, x_column);
    coords.y = db_get_int(res, 0, y_column);

    free(cid);
    PQclear(res);
    db_disconnect(conn);
    return coords;
}

