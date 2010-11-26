/*
 * File: character.h
 * Author: Casey Jones
 */

#ifndef _CHARACTER_H
#define _CHARACTER_H

#include <stdio.h>
#include <stdlib.h>
#include "util/json.h"

struct race_t
{
    int id;
    char* name;
    char* sprite_sheet;
    int sprite_height;
};

struct class_t
{
    int id;
    char* name;
};

void race_to_json(yajl_gen gen, struct race_t race);
void class_to_json(yajl_gen gen, struct class_t c);

#endif

