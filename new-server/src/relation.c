/*
 * File: relation.c
 * Author: Casey Jones
 */

#include "relation.h"

enum Relation str_to_rel(char* str)
{
    /* Lets just use some sane-ish default */
    enum Relation rel = LEFT;

    if(strcmp(str, "left") == 0)
    {
        rel = LEFT;
    }
    else if(strcmp(str, "right") == 0)
    {
        rel = RIGHT;
    }
    else if(strcmp(str, "top") == 0 ||
            strcmp(str, "up") == 0)
    {
        rel = ABOVE;
    }
    else if(strcmp(str, "bottom") == 0 ||
            strcmp(str, "below") == 0)
    {
        rel = BELOW;
    }

    return rel;
}

enum Relation rel_get_opposite(enum Relation rel)
{
    enum Relation r;

    switch(rel)
    {
        case LEFT:
            r = RIGHT;
            break;
        case RIGHT:
            r = LEFT;
            break;
        case ABOVE:
            r = BELOW;
            break;
        case BELOW:
            r = ABOVE;
            break;
        default:
            r = LEFT;
            break;
    }

    return r;
}

