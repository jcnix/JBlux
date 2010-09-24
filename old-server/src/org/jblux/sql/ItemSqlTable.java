/**
 * File: ItemSqlTable.java
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

package org.jblux.sql;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.jblux.common.items.Item;

public class ItemSqlTable {
    private DBManager m_db;
    private Connection m_conn;
    private static final String TABLE = "jblux_item";

    public ItemSqlTable() {
        m_db = new DBManager();
    }

    public ResultSet getColumnNames() {
        m_db.connect();
        ResultSet rs = null;

        try {
            m_conn = m_db.getConnection();
            DatabaseMetaData meta = m_conn.getMetaData();
            rs = meta.getColumns(null, null, "items", null);
        } catch (SQLException ex) {
        }

        m_db.close();
        return rs;
    }

    public ResultSet getAllValues(int id) {
        m_db.connect();

        String q = String.format("SELECT * FROM %s WHERE id='%d';", TABLE, id);
        ResultSet item_rs = m_db.query_select(q);

        m_db.close();
        return item_rs;
    }

    public ResultSet getAllValues(String name) {
        m_db.connect();

        String q = String.format("SELECT * FROM %s WHERE name='%s';", TABLE, name);
        ResultSet item_rs = m_db.query_select(q);

        m_db.close();
        return item_rs;
    }

    public ResultSet getAllItemNames() {
        m_db.connect();
        ResultSet rs = m_db.query_select(String.format("SELECT name FROM %s", TABLE));
        m_db.close();
        return rs;
    }

    public Item getItem(String name) {
        short id = -1;
        m_db.connect();
        
        try {
            String q = String.format("SELECT id FROM %s WHERE name='%s';", TABLE, name);
            ResultSet rs = m_db.query_select(q);
            rs.next();
            id = rs.getShort("id");
        } catch(SQLException ex) {
        }

        m_db.close();
        return getItem(id);
    }

    public Item getItem(int id) {
        Item item = new Item();
        
        try {
            ResultSet rs = getAllValues(id);
            rs.next();

            item.m_id = rs.getShort("id");
            item.m_class = rs.getShort("type");
            item.m_subclass = rs.getShort("subtype");
            item.m_name = rs.getString("name");
            item.m_description = rs.getString("description");
            item.m_image = rs.getString("displayimg");
            item.m_quality = rs.getShort("quality");
            item.m_buyPrice = rs.getInt("buyprice");
            item.m_sellPrice = rs.getInt("sellprice");
            item.m_allowableClass = rs.getShort("allowableclass");
            item.m_allowableRace = rs.getShort("allowablerace");
            item.m_itemLevel = rs.getShort("itemlevel");
            item.m_requiredLevel = rs.getShort("requiredlevel");
            item.m_requiredFaction = rs.getShort("RequiredFaction");
            item.m_requiredFactionStanding = rs.getShort("RequiredFactionStanding");
            item.m_isUnique = rs.getShort("isUnique");
            item.m_maxStack = rs.getShort("maxstack");
            item.m_itemStatsCount = rs.getShort("itemstatscount");
            item.m_stat_type1 = rs.getShort("stat_type1");
            item.m_stat_value1 = rs.getShort("stat_value1");
            item.m_stat_type2 = rs.getShort("stat_type2");
            item.m_stat_value2 = rs.getShort("stat_value2");
            item.m_stat_type3 = rs.getShort("stat_type3");
            item.m_stat_value3 = rs.getShort("stat_value3");
            item.m_stat_type4 = rs.getShort("stat_type4");
            item.m_stat_value4 = rs.getShort("stat_value4");
            item.m_stat_type5 = rs.getShort("stat_type5");
            item.m_stat_value5 = rs.getShort("stat_value5");
            item.m_stat_type6 = rs.getShort("stat_type6");
            item.m_stat_value6 = rs.getShort("stat_value6");
            item.m_stat_type7 = rs.getShort("stat_type7");
            item.m_stat_value7 = rs.getShort("stat_value7");
            item.m_stat_type8 = rs.getShort("stat_type8");
            item.m_stat_value8 = rs.getShort("stat_value8");
            item.m_stat_type9 = rs.getShort("stat_type9");
            item.m_stat_value9 = rs.getShort("stat_value9");
            item.m_stat_type10 = rs.getShort("stat_type10");
            item.m_stat_value10 = rs.getShort("stat_value10");
            item.m_dmg_min = rs.getFloat("dmg_min");
            item.m_dmg_max = rs.getFloat("dmg_max");
            item.m_dmg_type = rs.getShort("dmg_type");
            item.m_armor = rs.getShort("armor");
            item.m_water_resistance = rs.getShort("water_res");
            item.m_fire_resistance = rs.getShort("fire_res");
            item.m_nature_resistance = rs.getShort("nature_res");
            item.m_frost_resistance = rs.getShort("frost_res");
            item.m_shadow_resistance = rs.getShort("shadow_res");
            item.m_arcane_resistance = rs.getShort("arcane_res");
            item.m_delay = rs.getFloat("delay");
            item.m_ammo_type = rs.getShort("ammo_type");
            item.m_range = rs.getFloat("attack_range");
            item.m_spellid = rs.getShort("spellid");
            item.m_spellTrigger = rs.getShort("spelltrigger");
            item.m_spellCharges = rs.getShort("spellcharges");
            item.m_quest_item = rs.getShort("quest_item");
            item.m_quest_id = rs.getShort("quest_id");
            item.m_block_amount = rs.getShort("block_amount");
            item.m_itemset = rs.getShort("itemset");
            item.m_maxDurability = rs.getShort("MaxDurability");
            item.m_requiredDisenchantSkill = rs.getShort("ReqDisenchantSkill");
            item.m_armorDamageModifier = rs.getShort("ArmorDamageModifier");
        } catch (SQLException ex) {
        }

        return item;
    }
}
