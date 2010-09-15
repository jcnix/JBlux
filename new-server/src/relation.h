/*
 * File: relation.h
 * Author: Casey Jones
 */

#ifndef _RELATION_H
#define _RELATION_H

#include <stdio.h>
#include <stdlib.h>
#include <string.h>

enum Relation
{
    LEFT,
    RIGHT,
    ABOVE,
    BELOW
};

enum Relation str_to_rel(char* str);
enum Relation rel_get_opposite(enum Relation rel);

#endif

