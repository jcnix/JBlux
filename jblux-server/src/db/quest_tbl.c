/*
 * File: quest_tbl.c
 * Author: Casey Jones
 */

#include "quest_tbl.h"

struct quest* db_get_quest(int id)
{
    PGconn *conn = db_connect();
    PGresult *res = NULL;

    char *q = "SELECT id, name, details, objectives, completion_text, "
         "min_level, flag, quest_item_id, reward_xp, reward_money, "
         "rewardItem1_id, rewardItem2_id, rewardItem3_id, rewardItem1_count, "
         "rewardItem2_count, rewardItem3_count, reqItem1_id, reqItem2_id, "
         "reqItem3_id, reqItem1_count, reqItem2_count, reqItem3_count, "
         "reqNpc1_id, reqNpc2_id, reqNpc3_id, reqNpc1_count, reqNpc2_count, "
         "reqNpc3_count FROM jblux_quest WHERE id=$1;";

    int nParams = 1;
    char* cid = NULL;
    if(asprintf(&cid, "%d", id) < 0)
    {
        db_disconnect(conn);
        return NULL;
    }
    const char* params[1] = { cid };
    res = db_exec(conn, q, nParams, params);
    free(cid);

    struct quest *quest = malloc(sizeof(struct quest));

    int column = 0;
    quest->id = db_get_int(res, 0, column);

    column++;
    quest->name = db_get_str(res, 0, column);

    column++;
    quest->details = db_get_str(res, 0, column);

    column++;
    quest->objectives = db_get_str(res, 0, column);

    column++;
    quest->completion_text = db_get_str(res, 0, column);

    column++;
    quest->min_level = db_get_int(res, 0, column);

    column++;
    quest->type = db_get_int(res, 0, column);

    column++;
    quest->quest_item_id = db_get_int(res, 0, column);

    column++;
    quest->reward_xp = db_get_int(res, 0, column);

    column++;
    quest->reward_money = db_get_int(res, 0, column);

    column++;
    quest->reward_item1_id = db_get_int(res, 0, column);
    column++;
    quest->reward_item2_id = db_get_int(res, 0, column);
    column++;
    quest->reward_item3_id = db_get_int(res, 0, column);
    column++;
    quest->reward_item1_count = db_get_int(res, 0, column);
    column++;
    quest->reward_item2_count = db_get_int(res, 0, column);
    column++;
    quest->reward_item3_count = db_get_int(res, 0, column);

    column++;
    quest->required_item1_id = db_get_int(res, 0, column);
    column++;
    quest->required_item2_id = db_get_int(res, 0, column);
    column++;
    quest->required_item3_id = db_get_int(res, 0, column);
    column++;
    quest->required_item1_count = db_get_int(res, 0, column);
    column++;
    quest->required_item2_count = db_get_int(res, 0, column);
    column++;
    quest->required_item3_count = db_get_int(res, 0, column);
    
    column++;
    quest->required_npc1_id = db_get_int(res, 0, column);
    column++;
    quest->required_npc2_id = db_get_int(res, 0, column);
    column++;
    quest->required_npc3_id = db_get_int(res, 0, column);
    column++;
    quest->required_npc1_count = db_get_int(res, 0, column);
    column++;
    quest->required_npc2_count = db_get_int(res, 0, column);
    column++;
    quest->required_npc3_count = db_get_int(res, 0, column);

    PQclear(res);
    db_disconnect(conn);

    return quest;
}

int db_get_num_active_quests(int player_id)
{
    int num_quests = 0;
    PGconn *conn = db_connect();
    PGresult *res = NULL;

    char *q = "SELECT id FROM jblux_questlog WHERE character_id=$1 "
        "AND active;";
    int nParams = 1;
    char* cid = NULL;
    if(asprintf(&cid, "%d", player_id) < 0)
    {
        db_disconnect(conn);
        return -1;
    }
    const char* params[1] = { cid };
    res = db_exec(conn, q, nParams, params);
    free(cid);

    num_quests = PQntuples(res);
    PQclear(res);
    db_disconnect(conn);

    return num_quests;
}

void db_add_quest_to_log(int player_id, int quest_id)
{
    PGconn *conn = db_connect();
    PGresult *res = NULL;

    char *q = "INSERT INTO jblux_questlog(character_id, quest_id, active) "
        "VALUES($1, $2, TRUE);";
    int nParams = 2;
    char* pid = NULL;
    char* qid = NULL;
    if( asprintf(&pid, "%d", player_id) < 0 ||
        asprintf(&qid, "%d", quest_id) < 0)
    {
        db_disconnect(conn);
        return;
    }
    const char* params[2] = { pid, qid };
    res = db_exec(conn, q, nParams, params);
    free(pid);
    free(qid);
    PQclear(res);
    db_disconnect(conn);
}

int have_quest(int player_id, int quest_id)
{
    int have_quest = 1;
    PGconn *conn = db_connect();
    PGresult *res = NULL;

    char *q = "SELECT id FROM jblux_questlog WHERE character_id=$1 AND "
        "quest_id=$2;";
    int nParams = 2;
    char* pid = NULL;
    char* qid = NULL;
    if( asprintf(&pid, "%d", player_id) < 0 ||
        asprintf(&qid, "%d", quest_id) < 0)
    {
        db_disconnect(conn);
        return 1;
    }
    const char* params[2] = { pid, qid };
    res = db_exec(conn, q, nParams, params);
    free(pid);
    free(qid);
    
    if(PQntuples(res) == 0)
    {
        have_quest = 0;
    }

    PQclear(res);
    db_disconnect(conn);

    return have_quest;
}

