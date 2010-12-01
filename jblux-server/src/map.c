/*
 * File: map.c
 * Author: Casey Jones
 */

#include "map.h"

static struct map_list *maps = NULL;

struct map_list* init_maps()
{
    db_get_all_maps(&maps);

    struct map_list *curr = maps;
    while(curr)
    {
        struct map_t *map = curr->map;
        read_map_data(map);
        curr = curr->next;
    }
    
    return maps;
}

struct map_t* get_map_for_id(int id)
{
    struct map_list *ml = maps;
    struct map_t *map = NULL;
    
    while(ml)
    {
        map = ml->map;
        if(map->id == id)
        {
            break;
        }
        ml = ml->next;
    }

    return map;
}

struct map_t* get_map_for_name(char* name)
{
    struct map_list *ml = maps;
    struct map_t *map = NULL;

    while(ml)
    {
        map = ml->map;
        if(strcmp(map->name, name) == 0)
        {
            break;
        }
        ml = ml->next;
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

/* The Relation argument tells the function which direction the
 * player is coming from.  So if we're coming from the left, we
 * really want the right entrance coords */
struct coordinates_t get_map_entrance(struct map_t *map, enum Relation rel)
{
    struct coordinates_t coords;
    switch(rel)
    {
        case LEFT:
            coords = map->right_ent;
            break;
        case RIGHT:
            coords = map->left_ent;
            break;
        case ABOVE:
            coords = map->bottom_ent;
            break;
        case BELOW:
            coords = map->top_ent;
            break;
        default:
            break;
    }

    return coords;
}

struct npc_data* get_enemy_on_map(int npc_id, struct map_t *map)
{
    struct npc_list *en = map->enemies;
    struct npc_data *npc = NULL;

    while(en)
    {
        npc = en->npc;
        if(npc->map_id == npc_id)
        {
            break;
        }
        en = en->next;
    }

    return npc;
}

void read_map_data(struct map_t* map)
{
    FILE* f;
    size_t s;
    /* "maps/" + name + "bw.jmb\0" */
    int name_size = 5 + strlen(map->name) + 7;
    char* name = malloc(name_size);
    sprintf(name, "maps/%sbw.jbm", map->name);
    f = fopen(name, "rb");
    printf("%s %d\n", name, f == NULL);
    free(name);
    if(!f)
    {
        /* So we can free it later without worrying
         * about if it's NULL or not */
        map->walk_area = malloc(1);
        return;
    }

    /* Read in map dimensions */
    int size = 0;
    s = fread(&size, 4, 1, f);
    map->walk_area_size = size;

    map->walk_area = malloc(map->walk_area_size);
    s = fread(map->walk_area, 1, map->walk_area_size, f);

    fclose(f);
}

void cleanup_maps()
{
    delete_map_list(&maps);
}

void add_map(struct map_list **maps, struct map_t *map)
{
    struct map_list* new = malloc(sizeof(struct map_list));
    new->map = map;
    new->next = *maps;
    *maps = new;
}

void delete_map_list(struct map_list **maps)
{
    struct map_list *curr = *maps;
    struct map_list *next = NULL;
    while(curr)
    {
        struct map_t *map = curr->map;
        next = curr->next;
        free(map->name);
        free(map->walk_area);
        free(map);
        free(curr);
        curr = next;
    }
}

