/**
 * File: Item.java
 *
 * @author Casey Jones
 *
 * This file is part of JBlux
 * JBlux is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * JBlux is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.jblux.common.items;

import java.io.Serializable;

public class Item implements Serializable {
    private static final long serialVersionUID = 1L;

    public int m_id;
    public short m_class;
    public short m_subclass;
    public String m_name;
    public String m_description;
    public String m_image;
    public short m_quality;
    public int m_sellPrice;
    public int m_buyPrice;
    public short m_allowableClass;
    public short m_allowableRace;
    public short m_itemLevel;
    public short m_requiredLevel;
    public short m_requiredFaction;
    public short m_requiredFactionStanding;
    public short m_isUnique;
    public short m_maxStack;
    public short m_itemStatsCount;
    public short m_stat_type1;
    public short m_stat_value1;
    public short m_stat_type2;
    public short m_stat_value2;
    public short m_stat_type3;
    public short m_stat_value3;
    public short m_stat_type4;
    public short m_stat_value4;
    public short m_stat_type5;
    public short m_stat_value5;
    public short m_stat_type6;
    public short m_stat_value6;
    public short m_stat_type7;
    public short m_stat_value7;
    public short m_stat_type8;
    public short m_stat_value8;
    public short m_stat_type9;
    public short m_stat_value9;
    public short m_stat_type10;
    public short m_stat_value10;
    public float m_dmg_min;
    public float m_dmg_max;
    public short m_dmg_type;
    public short m_armor;
    public short m_water_resistance;
    public short m_fire_resistance;
    public short m_nature_resistance;
    public short m_frost_resistance;
    public short m_shadow_resistance;
    public short m_arcane_resistance;
    public float m_delay;
    public short m_ammo_type;
    public float m_range;
    public short m_spellid;
    public short m_spellTrigger;
    public short m_spellCharges;
    public short m_spellCooldown;
    public short m_quest_item;
    public short m_quest_id;
    public short m_block_amount;
    public short m_itemset;
    public short m_maxDurability;
    public short m_requiredDisenchantSkill;
    public short m_armorDamageModifier;

    public Item() {
    }

    public void use() {
    }
    
    public void destroy() {
    }
}
