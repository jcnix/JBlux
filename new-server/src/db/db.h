/*
 * File: db.h
 * Author: Casey Jones
 */

#ifndef _DB_H
#define _DB_H

#include <stdio.h>
#include <stdlib.h>
#include <libpq-fe.h>

PGconn* db_connect();
void db_disconnect(PGconn* conn);
PGresult* db_exec(PGconn* conn, char* command, int nParams,
        const char* const *paramValues);

#endif

