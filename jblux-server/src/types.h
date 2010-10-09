/*
 * File: types.h
 * Author: Casey Jones
 */

#ifndef _TYPES_H
#define _TYPES_H

struct coordinates_t
{
    int x;
    int y;
};

struct inventory_t
{
    int id;
};

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

#endif

