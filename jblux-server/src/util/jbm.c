/*
 * File: jbm.c
 * Author: Casey Jones
 */

#include "jbm.h"

int jbm_can_walk(struct map_t *map, int x, int y)
{
    /* Each byte of the walk map covers 32 pixels horizontally */
    int bytes_per_row = map->dim_x >> 5; /* divide by 32 */
    if(!divisible_by_base2(map->dim_x, 32))
    {
        bytes_per_row++;
    }
    /* Move to the byte where our bit is */
    int dx = x >> 5;
    int index = (bytes_per_row * (y>>2)) + (dx);
    int byte = map->walk_area[index];
    int pix = x - (dx << 5);
    
    int bit = 1;
    if(pix < 4)
        bit = 1; 
    else if(pix < 8)
        bit = 2;
    else if(pix < 12)
        bit = 3;
    else if(pix < 16)
        bit = 4;
    else if(pix < 20)
        bit = 5;
    else if(pix < 24)
        bit = 6;
    else if(pix < 28)
        bit = 7;
    else
        bit = 8;
    return (byte >> (bit-1)) & 0x1;
}

void jbm_read(struct map_t* map)
{
    FILE* f;
    size_t s;
    /* "maps/" + name + "bw.jmb\0" */
    int name_size = 5 + strlen(map->name) + 7;
    char* name = malloc(name_size);
    sprintf(name, "maps/%sbw.jbm", map->name);
    f = fopen(name, "rb");
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
    int x = 0;
    int y = 0;
    s = fread(&x, 4, 1, f);
    s = fread(&y, 4, 1, f);
    s = fread(&size, 4, 1, f);
    map->dim_x = x;
    map->dim_y = y;
    map->walk_area_size = size;

    map->walk_area = malloc(map->walk_area_size);
    s = fread(map->walk_area, 1, map->walk_area_size, f);

    fclose(f);
}

