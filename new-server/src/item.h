/*
 * File: item.h
 * Author: Casey Jones
 */

#ifndef _ITEM_H
#define _ITEM_H

enum ITEM_TYPE
{
    MELEE_WEAPON,
    RANGED_WEAPON,
    POTION,
    /* Armors */
    HELMET,
    CHEST_ARMOR,
    LEG_ARMOR,
    GLOVES,
    BOOTS,
    CAPE
};

struct item_t
{
    int id;
    ITEM_TYPE m_class;
    int m_subclass;
    char *m_name;
    char *m_description;
    char *m_image;
    int m_quality;
    int m_sellPrice;
    int m_buyPrice;
    int m_allowableClass;
    int m_allowableRace;
    int m_itemLevel;
    int m_requiredLevel;
    int m_requiredFaction;
    int m_requiredFactionStanding;
    int m_isUnique;
    int m_maxStack;
    int m_itemStatsCount;
    int m_stat_type1;
    int m_stat_value1;
    int m_stat_type2;
    int m_stat_value2;
    int m_stat_type3;
    int m_stat_value3;
    int m_stat_type4;
    int m_stat_value4;
    int m_stat_type5;
    int m_stat_value5;
    int m_stat_type6;
    int m_stat_value6;
    int m_stat_type7;
    int m_stat_value7;
    int m_stat_type8;
    int m_stat_value8;
    int m_stat_type9;
    int m_stat_value9;
    int m_stat_type10;
    int m_stat_value10;
    float m_dmg_min;
    float m_dmg_max;
    int m_dmg_type;
    int m_armor;
    int m_water_resistance;
    int m_fire_resistance;
    int m_nature_resistance;
    int m_frost_resistance;
    int m_shadow_resistance;
    int m_arcane_resistance;
    float m_delay;
    int m_ammo_type;
    float m_range;
    int m_spellid;
    int m_spellTrigger;
    int m_spellCharges;
    int m_spellCooldown;
    int m_quest_item;
    int m_quest_id;
    int m_block_amount;
    int m_itemset;
    int m_maxDurability;
    int m_requiredDisenchantSkill;
    int m_armorDamageModifier;
};

#endif

