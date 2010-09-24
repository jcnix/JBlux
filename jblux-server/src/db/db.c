/*
 * File: db.c
 * Author: Casey Jones
 */

#include "db.h"

PGconn* db_connect()
{
    PGconn *conn;

    conn = PQconnectdb("dbname=tmuo");
    if(PQstatus(conn) != CONNECTION_OK)
    {
        fprintf(stderr, "Connection to DB failed: %s\n",
                PQerrorMessage(conn));
        PQfinish(conn);
        return NULL;
    }

    return conn;
}

int db_is_connected(PGconn* conn)
{
    if(PQstatus(conn) == CONNECTION_OK)
    {
        return 1;
    }
    else
    {
        return 0;
    }
}

void db_disconnect(PGconn* conn)
{
    PQfinish(conn);
}

PGresult* db_exec(PGconn* conn, char* command, int nParams,
        const char* const *paramValues)
{
    PGresult *res = PQexecParams(   conn,
                                    command,
                                    nParams,
                                    NULL,
                                    paramValues,
                                    NULL,
                                    NULL,
                                    0);
    if(PQresultStatus(res) == PGRES_COMMAND_OK ||
        PQresultStatus(res) == PGRES_TUPLES_OK)
    {
        return res;
    }
    else
    {
        fprintf(stderr, "Command: %s failed.  %s\n", command,
                PQerrorMessage(conn));
        return res;
    }
}

int db_get_int(PGresult* res, int row, int column)
{
    return atoi(PQgetvalue(res, row, column));
}

char* db_get_str(PGresult* res, int row, int column)
{
    return strdup(PQgetvalue(res, row, column));
}

