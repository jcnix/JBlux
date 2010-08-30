/*
 * File: db_user_tbl.h
 * Author: Casey Jones
 */

#ifndef _DB_USER_TBL
#define _DB_USER_TBL

#define _GNU_SOURCE
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <postgresql/libpq-fe.h>
#include "db.h"
#include "db_map_tbl.h"
#include "../types.h"

#define USER_TABLE "jblux_user"
#define CHARACTER_TABLE "jblux_character"
#define RACE_TABLE "jblux_race"
#define CLASS_TABLE "jblux_class"

int db_authenticate(char* username, char* password, char* character_name);
struct player_data db_get_player(char* character_name);
void db_set_map_for_player(int char_id, int map_id, struct coordinates_t coords);
int get_map_for_player(char* character);
struct race_t get_race(int id);
struct class_t get_class(int id);

#endif

