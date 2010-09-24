/*
 * File: db.h
 * Author: Casey Jones
 */

#ifndef _DB_H
#define _DB_H

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <libpq-fe.h>

PGconn* db_connect();
int db_is_connected(PGconn* conn);
void db_disconnect(PGconn* conn);
PGresult* db_exec(PGconn* conn, char* command, int nParams,
        const char* const *paramValues);

int db_get_int(PGresult* res, int row, int column);
char* db_get_str(PGresult* res, int row, int column);

#endif

