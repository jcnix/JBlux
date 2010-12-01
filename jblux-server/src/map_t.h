/*
 * File: map_t.h
 * Author: Casey Jones
 */

#ifndef _MAP_T_H
#define _MAP_T_H

#include "util/coords.h"

struct map_t
{
    int id;
    char *name;
    struct item_t *items;
    struct npc_list *enemies;

    int map_left;
    int map_right;
    int map_above;
    int map_below;

    struct coordinates_t left_ent;
    struct coordinates_t right_ent;
    struct coordinates_t top_ent;
    struct coordinates_t bottom_ent;

    /* Map walk area data */
    int walk_area_size;
    char* walk_area;
};

struct map_list
{
    struct map_t *map;
    struct map_list *next;
};

#endif

