/*
 * File: db_user_tbl.c
 * Author: Casey Jones
 */

#include "user_tbl.h"

int db_authenticate(char* username, char* password, char* character_name)
{
    int user_id = 0;
    PGconn *conn = db_connect();
    PGresult *res = NULL;

    char* q = "SELECT id FROM jblux_user WHERE username=$1 and password=$2"
        "AND is_active AND NOT online;";
    int nParams = 2;
    const char* params_1[2] = { username, password };
    res = db_exec(conn, q, nParams, params_1);

    if(PQntuples(res) < 1)
    {
        return 0;
    }
    user_id = db_get_int(res, 0, 0);
    PQclear(res);

    q = "SELECT id FROM jblux_character WHERE name=$1 AND user_id=$2;";
    nParams = 2;

    char* cid = NULL;
    if(asprintf(&cid, "%d", user_id) < 0)
    {
        db_disconnect(conn);
        return 0;
    }

    const char* params_2[2] = { character_name, cid };
    res = db_exec(conn, q, nParams, params_2);

    if(PQntuples(res) < 1)
        return 0;

    free(cid);
    PQclear(res);

    q = "UPDATE jblux_user SET online=true WHERE id=$1;";
    nParams = 1;
    cid = NULL;
    if(asprintf(&cid, "%d", user_id) < 0)
    {
        db_disconnect(conn);
        return 0;
    }
    const char* params_3[1] = { cid };
    res = db_exec(conn, q, nParams, params_3);
    free(cid);
    PQclear(res);

    db_disconnect(conn);
    return user_id;
}

void db_set_user_offline(int user_id)
{
    PGconn *conn = db_connect();
    PGresult *res = NULL;

    char* q = "UPDATE jblux_user SET online=false WHERE id=$1;";
    int nParams = 1;
    char* cid = NULL;
    if(asprintf(&cid, "%d", user_id) < 0)
    {
        db_disconnect(conn);
        return;
    }
    const char* params[1] = { cid };
    res = db_exec(conn, q, nParams, params);
    PQclear(res);
    db_disconnect(conn);
}

void db_set_all_users_offline()
{
    PGconn *conn = db_connect();
    PGresult *res = NULL;

    char* q = "UPDATE jblux_user SET online=false;";
    int nParams = 0;
    res = db_exec(conn, q, nParams, NULL);
    PQclear(res);
    db_disconnect(conn);
}

struct player_data* db_get_player(char* character_name)
{
    struct player_data *data = malloc(sizeof(struct player_data));
    if(!data)
    {
        return NULL;
    }

    PGconn *conn = db_connect();
    PGresult *res = NULL;

    char* q = "SELECT user_id, id, name, level, xp, money, max_hp, strength, "
        "agility, stamina, intelligence, spirit, current_map_id, race_id, "
        "class_t_id, x_coord, y_coord, inventory_id FROM jblux_character "
        "WHERE name=$1;";
    int nParams = 1;
    const char* params[1] = { character_name };
    res = db_exec(conn, q, nParams, params);

    if(PQntuples(res) > 0)
    {
        int column = 0;
        data->user_id = db_get_int(res, 0, column);

        column++;
        data->character_id = db_get_int(res, 0, column);

        column++;
        data->character_name = db_get_str(res, 0, column);

        column++;
        data->level = db_get_int(res, 0, column);
        
        column++;
        data->xp = db_get_int(res, 0, column);
        
        column++;
        data->money = db_get_int(res, 0, column);

        column++;
        data->max_hp = db_get_int(res, 0, column);
        data->hp = data->max_hp;

        column++;
        data->strength = db_get_int(res, 0, column);

        column++;
        data->agility = db_get_int(res, 0, column);

        column++;
        data->stamina = db_get_int(res, 0, column);

        column++;
        data->intelligence = db_get_int(res, 0, column);

        column++;
        data->spirit = db_get_int(res, 0, column);

        column++;
        data->map_id = db_get_int(res, 0, column);
        struct map_t *map = get_map_for_id(data->map_id);
        data->map = map->name;

        column++;
        data->race = get_race(db_get_int(res, 0, column));

        column++;
        data->player_class = get_class(db_get_int(res, 0, column));

        column++;
        data->coords.x = db_get_int(res, 0, column);
        column++;
        data->coords.y = db_get_int(res, 0, column);
        /* TODO: get inventory */
        data->inventory.id = 0;
        data->quests = db_get_quests_for_player(data->character_id);
    }
    else
    {
        data = NULL;
    }

    PQclear(res);
    db_disconnect(conn);
    return data;
}

void db_set_map_for_player(int char_id, int map_id, struct coordinates_t coords)
{
    PGconn *conn = db_connect();
    PGresult *res = NULL;

    char* q = "UPDATE jblux_character SET current_map_id=$1, "
        "x_coord=$2, y_coord=$3 WHERE id=$4;";
    int nParams = 4;
    char *cmap_id = NULL;
    char *cx = NULL;
    char *cy = NULL;
    char *cid = NULL;
    if( (asprintf(&cmap_id, "%d", map_id) < 0) ||
            (asprintf(&cx, "%d", coords.x) < 0) ||
            (asprintf(&cy, "%d", coords.y) < 0) ||
            (asprintf(&cid, "%d", char_id) < 0))
    {
        /* saving the map location isn't _that_ important */
        db_disconnect(conn);
        return;
    }
    const char* params[4] = { cmap_id, cx, cy, cid };
    res = db_exec(conn, q, nParams, params);

    free(cmap_id);
    free(cx);
    free(cy);
    free(cid);
    PQclear(res);
    db_disconnect(conn);
}

void db_save_player(struct player_data *player)
{
    PGconn *conn = db_connect();
    PGresult *res = NULL;

    char* q = "UPDATE jblux_character SET level=$1 xp=$2, money=$3, "
        "strength=$4, agility=$5, stamina=$6, intelligence=$7, spirit=$8, "
        "max_hp=$9, current_map_id=$10, x_coord=$11, y_coord=$12 "
        "WHERE id=$13;";
    int nParams = 13;
    char* cid = NULL;
    char* clevel = NULL;
    char* cxp = NULL;
    char* cmoney = NULL;
    char* cstrength = NULL;
    char* cagility = NULL;
    char* cstamina = NULL;
    char* cintelligence = NULL;
    char* cspirit = NULL;
    char* cmax_hp = NULL;
    char* cmap = NULL;
    char* cx = NULL;
    char* cy = NULL;
    char* spirit = NULL;
    if( (asprintf(&cid, "%d", player->character_id) < 0) ||
        (asprintf(&clevel, "%d", player->level) < 0) ||
        (asprintf(&cxp, "%d", player->xp) < 0) ||
        (asprintf(&cmoney, "%d", player->money) < 0) ||
        (asprintf(&cstrength, "%d", player->strength) < 0) ||
        (asprintf(&cagility, "%d", player->agility) < 0) ||
        (asprintf(&cstamina, "%d", player->stamina) < 0) ||
        (asprintf(&cintelligence, "%d", player->intelligence) < 0) ||
        (asprintf(&cspirit, "%d", player->spirit) < 0) ||
        (asprintf(&cmax_hp, "%d", player->max_hp) < 0) ||
        (asprintf(&cmap, "%d", player->map_id) < 0) ||
        (asprintf(&cx, "%d", player->coords.x) < 0) ||
        (asprintf(&cy, "%d", player->coords.y) < 0) )
    {
        db_disconnect(conn);
        return;
    }
    const char* params[3] = { cxp, cmoney, cid };
    res = db_exec(conn, q, nParams, params);

    free(cid);
    free(cxp);
    free(cmoney);
    PQclear(res);
    db_disconnect(conn);
}

int get_map_for_player(char* character)
{
    int map_id = -1;
    PGconn *conn = db_connect();
    PGresult *res = NULL;

    char* q = "SELECT current_map_id FROM jblux_character WHERE name=$1;";
    int nParams = 1;
    const char* params[1] = { character };
    res = db_exec(conn, q, nParams, params);
    map_id = db_get_int(res, 0, 0);

    PQclear(res);
    db_disconnect(conn);
    return map_id;
}

struct quest_list* db_get_quests_for_player(int character_id)
{
    struct quest_list *quests = NULL;
    PGconn *conn = db_connect();
    PGresult *res = NULL;

    char* q = "SELECT quest_id, reqitem1_count, reqitem2_count, "
        "reqitem3_count, reqnpc1_count, reqnpc2_count, reqnpc3_count "
        "FROM jblux_questlog WHERE character_id=$1 AND active;";
    int nParams = 1;
    char* cid = NULL;
    if(!asprintf(&cid, "%d", character_id))
    {
        db_disconnect(conn);
        return NULL;
    }
    const char* params[1] = { cid };
    res = db_exec(conn, q, nParams, params);
    int num_quests = PQntuples(res);

    int i;
    for(i = 0; i < num_quests; i++)
    {
        int quest_id = db_get_int(res, i, 0);
        struct quest *q = db_get_quest(quest_id);
        q->current_item1_count = db_get_int(res, i, 1);
        q->current_item2_count = db_get_int(res, i, 2);
        q->current_item3_count = db_get_int(res, i, 3);
        q->current_npc1_count = db_get_int(res, i, 4);
        q->current_npc2_count = db_get_int(res, i, 5);
        q->current_npc3_count = db_get_int(res, i, 6);
        q->complete = is_quest_complete(q);

        add_quest(&quests, q);
    }

    PQclear(res);
    db_disconnect(conn);
    return quests;
}

struct race_t get_race(int id)
{
    struct race_t race;
    race.id = id;
    PGconn *conn = db_connect();
    PGresult *res = NULL;

    char* q = "SELECT name, sprite_sheet, sprite_height "
        "FROM jblux_race WHERE id=$1;";
    int nParams = 1;
    char *cid = NULL;
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

    int column = 0;
    race.name = db_get_str(res, 0, column);

    column++;
    race.sprite_sheet = db_get_str(res, 0, column);

    column++;
    race.sprite_height = db_get_int(res, 0, column);

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
    PGresult *res = NULL;

    char* q = "SELECT name FROM jblux_class WHERE id=$1;";
    int nParams = 1;
    char *cid = NULL;
    if(asprintf(&cid, "%d", id) < 0)
    {
        db_disconnect(conn);
        class.name = "";
        return class;
    }
    const char* params[1] = { cid };
    res = db_exec(conn, q, nParams, params);

    int column = 0;
    class.name = db_get_str(res, 0, column);

    free(cid);
    PQclear(res);
    db_disconnect(conn);
    return class;
}

