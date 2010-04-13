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

public class Item {
    public int id;
    public short classs;
    public short subclass;
    public String name;
    public String image;
    public String description;
    public int sellPrice;
    public int buyPrice;
    public short allowableClass;
    public short allowableRace;
    public short itemLevel;
    public short requiredLevel;
    public short requiredFaction;
    public short requiredFactionStanding;
    public short isUnique;
    public short maxStack;
    public short itemStatsCount;
    public short stat_type1;
    public short stat_value1;
    public short stat_type2;
    public short stat_value2;
    public short stat_type3;
    public short stat_value3;
    public short stat_type4;
    public short stat_value4;
    public short stat_type5;
    public short stat_value5;
    public short stat_type6;
    public short stat_value6;
    public short stat_type7;
    public short stat_value7;
    public short stat_type8;
    public short stat_value8;
    public short stat_type9;
    public short stat_value9;
    public short stat_type10;
    public short stat_value10;
    public float dmg_min;
    public float dmg_max;
    public short dmg_type;
    public short armor;
    public short water_resistance;
    public short fire_resistance;
    public short nature_resistance;
    public short frost_resistance;
    public short shadow_resistance;
    public short arcane_resistance;
    public short delay;
    public short ammo_type;
    public float range;
    public short spellid;
    public short spellTrigger;
    public short spellCharges;
    public short spellCooldown;
    public short quest_item;
    public short quest_id;
    public short block_amount;
    public short itemset;
    public short maxDurability;
    public short requiredDisenchantSkill;
    public short armorDamageModifier;

    public void use() {
    }
    
    public void destroy() {
    }
}
