/*
 * File: combat.c
 * Author: Casey Jones
 */

#include "combat.h"

struct combattant get_player_combattant(struct player_data *player)
{
    struct combattant cbt;
    cbt.strength = player->strength;
    cbt.agility = player->agility;
    cbt.stamina = player->stamina;
    cbt.intelligence = player->intelligence;
    cbt.spirit = player->spirit;
    return cbt;
}

struct combattant get_npc_combattant(struct npc_data *npc)
{
    struct combattant cbt;
    cbt.strength = npc->strength;
    cbt.agility = npc->agility;
    cbt.stamina = npc->stamina;
    cbt.intelligence = npc->intelligence;
    cbt.spirit = npc->spirit;
    return cbt;
}

int calc_dmg(struct combattant attacker, struct combattant defender)
{
    /* Really basic formula */
    int damage = attacker.strength - defender.stamina;
    if(damage == 0)
    {
        damage = 1;
    }

    return damage;
}

