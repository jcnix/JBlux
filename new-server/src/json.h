/*
 * File: json.h
 * Author: Casey Jones
 */

#ifndef _JSON_H
#define _JSON_H

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <yajl/yajl_gen.h>
#include "types.h"

char* get_json_str(yajl_gen gen);
char* player_data_to_json(struct player_data *data);
void coordinates_to_json(yajl_gen gen, struct coordinates_t coords);
void race_to_json(yajl_gen gen, struct race_t race);
void class_to_json(yajl_gen gen, struct class_t c);
void inventory_to_json(yajl_gen gen, struct inventory_t inv);

#endif

