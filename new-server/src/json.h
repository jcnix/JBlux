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

const unsigned char* get_json_str(yajl_gen gen);
yajl_gen player_data_to_json(struct player_data data);

#endif

