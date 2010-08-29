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
    maps = malloc(sizeof(map_t) * rows);

    int i;
    for(i = 0; i < rows; i++)
    {
        struct map_t;
        
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

        *(maps + i) = map;
    }

    PQclear(res);
    db_disconnect(conn);
    return maps;
}

int db_get_adjacent_map(Relation r, int map_id);
char* get_map_name_for_id(int id);
int get_map_id_for_name(char* name);
struct item_t* db_get_items_on_map(int map_id);
struct npc_data_t db_get_npcs_on_map(int map_id);
struct coordinates_t db_get_map_entrance(int map_id, Relation r);

