/*
 * File: map.c
 * Author: Casey Jones
 */

#include "map.h"

static struct map_t *maps;

void init_maps()
{
    maps = db_get_all_maps();
}

struct map_t* get_map_for_id(int id)
{
    struct map_t *map = maps;
    int i = 0;
    while(map != NULL)
    {
        if(map->id == id)
        {
            break;
        }
        i++;
        *map = *(map + i);
    }
}

struct map_t* get_map_for_name(char* name)
{
    struct map_t *map = maps;
    int i = 0;
    while(map != NULL)
    {
        if(strcmp(map->name, name) == 0)
        {
            break;
        }
        i++;
        *map = *(map + i);
    }

    return map;
}

struct map_t* get_adjacent_map(struct map_t *map, enum Relation rel)
{
    struct map_t *new_map = NULL;
    int id = 0;
    switch(rel)
    {
        case LEFT:
            id = map->map_left;
            break;
        case RIGHT:
            id = map->map_right;
            break;
        case ABOVE:
            id = map->map_above;
            break;
        case BELOW:
            id = map->map_below;
            break;
        default:
            break;
    }

    if(id != 0)
    {
        new_map = get_map_for_id(id);
    }

    return new_map;
}

struct coordinates_t get_map_entrance(struct map_t *map, enum Relation rel)
{
    struct coordinates_t coords;
    switch(rel)
    {
        case LEFT:
            coords = map->left_ent;
            break;
        case RIGHT:
            coords = map->right_ent;
            break;
        case ABOVE:
            coords = map->top_ent;
            break;
        case BELOW:
            coords = map->bottom_ent;
            break;
        default:
            break;
    }

    return coords;
}

