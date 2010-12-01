/*
 * File: jbm.h
 * Author: Casey Jones
 */

#ifndef _JBM_H
#define _JBM_H

/**
 * Reads and works with data from .jbm files
 * (JBlux Map)
 */
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "../map_t.h"

int jbm_can_walk(struct map_t *map, int x, int y);
void jbm_read(struct map_t* map);

#endif

