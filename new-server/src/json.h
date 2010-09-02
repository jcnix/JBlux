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
char* coordinates_to_json(struct coordinates_t coords);
char* race_to_json(struct race_t race);
char* class_to_json(struct class_t c);
char* inventory_to_json(struct inventory_t inv);

#endif

