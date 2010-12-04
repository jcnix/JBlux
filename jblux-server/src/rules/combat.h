/*
 * File: combat.h
 * Author: Casey Jones
 */

#ifndef _COMBAT_H
#define _COMBAT_H

#include <stdio.h>
#include <stdlib.h>
#include "../player_data.h"
#include "../npc.h"

/**
 * A Struct to store data needed to calculate damage
 * This is meant to make the parameters of calc_dmg
 * more generic instead of having different functions
 * for an npc_data attackering a player_data or vice
 * versa.
 */
struct combattant
{
    int strength;
    int agility;
    int stamina;
    int intelligence;
    int spirit;
};

struct combattant get_player_combattant(struct player_data *player);
struct combattant get_npc_combattant(struct npc_data *npc);
int calc_dmg(struct combattant attacker, struct combattant defender);

#endif

