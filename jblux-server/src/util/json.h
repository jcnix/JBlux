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
#include "coords.h"
#include "../inventory.h"

char* get_json_str(yajl_gen gen);
void json_insert_str(yajl_gen gen, const char* key, char* value);
void json_insert_int(yajl_gen gen, const char* key, int value);

void inventory_to_json(yajl_gen gen, struct inventory_t inv);

#endif

