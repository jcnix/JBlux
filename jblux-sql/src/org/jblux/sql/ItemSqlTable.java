/**
 * File: ItemTable.java
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
import java.sql.Statement;
import org.jblux.common.items.Item;

public class ItemSqlTable {
    private DBManager m_db;
    private Connection m_conn;

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

    public ResultSet getAllValues(String name) {
        m_db.connect();

        ResultSet item_rs = null;
        if(name != null) {
            item_rs = m_db.query_select("SELECT * FROM items WHERE name='" + name + "';");
        }

        m_db.close();
        return item_rs;
    }

    public ResultSet getAllItemNames() {
        m_db.connect();
        ResultSet rs = m_db.query_select("SELECT name FROM items");
        m_db.close();
        return rs;
    }

    public void saveItem(Item item) {
        m_db.connect();
        
        try {
            String query = "";
            m_conn = m_db.getConnection();
            Statement stmt = m_conn.createStatement();

            boolean exists = false;
            if(doesItemExist(item.m_name)) {
                exists = true;
                query = "DELETE FROM items WHERE name='"+item.m_name+"';";
                System.out.println(query);
                stmt.execute(query);
            }

            String id_sql = "";
            if(exists) {
                id_sql = "'" + item.m_id + "',";
            } else {
                id_sql = "nextval('items_id_seq'),";
            }

            query = "INSERT INTO items VALUES(" +
                    id_sql +
                    "'" + item.m_class + "'," +
                    "'" + item.m_subclass + "'," +
                    "'" + item.m_name + "'," +
                    "'" + item.m_description + "'," +
                    "'" + item.m_image + "'," +
                    "'" + item.m_quality + "'," +
                    "'" + item.m_buyPrice + "'," +
                    "'" + item.m_sellPrice + "'," +
                    "'" + item.m_allowableClass + "'," +
                    "'" + item.m_allowableRace + "'," +
                    "'" + item.m_itemLevel + "'," +
                    "'" + item.m_requiredLevel + "'," +
                    "'" + item.m_requiredFaction + "'," +
                    "'" + item.m_requiredFaction + "'," +
                    "'" + item.m_isUnique + "'," +
                    "'" + item.m_maxStack + "'," +
                    "'" + item.m_itemStatsCount + "'," +
                    "'" + item.m_stat_type1 + "'," +
                    "'" + item.m_stat_value1 + "'," +
                    "'" + item.m_stat_type2 + "'," +
                    "'" + item.m_stat_value2 + "'," +
                    "'" + item.m_stat_type3 + "'," +
                    "'" + item.m_stat_value3 + "'," +
                    "'" + item.m_stat_type4 + "'," +
                    "'" + item.m_stat_value4 + "'," +
                    "'" + item.m_stat_type5 + "'," +
                    "'" + item.m_stat_value5 + "'," +
                    "'" + item.m_stat_type6 + "'," +
                    "'" + item.m_stat_value6 + "'," +
                    "'" + item.m_stat_type7 + "'," +
                    "'" + item.m_stat_value7 + "'," +
                    "'" + item.m_stat_type8 + "'," +
                    "'" + item.m_stat_value8 + "'," +
                    "'" + item.m_stat_type9 + "'," +
                    "'" + item.m_stat_value9 + "'," +
                    "'" + item.m_stat_type10 + "'," +
                    "'" + item.m_stat_value10 + "'," +
                    "'" + item.m_dmg_min + "'," +
                    "'" + item.m_dmg_max + "'," +
                    "'" + item.m_dmg_type + "'," +
                    "'" + item.m_armor + "'," +
                    "'" + item.m_water_resistance + "'," +
                    "'" + item.m_fire_resistance + "'," +
                    "'" + item.m_nature_resistance + "'," +
                    "'" + item.m_frost_resistance + "'," +
                    "'" + item.m_shadow_resistance + "'," +
                    "'" + item.m_arcane_resistance + "'," +
                    "'" + item.m_delay + "'," +
                    "'" + item.m_ammo_type + "'," +
                    "'" + item.m_range + "'," +
                    "'" + item.m_spellid + "'," +
                    "'" + item.m_spellTrigger + "'," +
                    "'" + item.m_spellCooldown + "'," +
                    "'" + item.m_quest_item + "'," +
                    "'" + item.m_quest_id + "'," +
                    "'" + item.m_block_amount + "'," +
                    "'" + item.m_itemset + "'," +
                    "'" + item.m_maxDurability + "'," +
                    "'" + item.m_requiredDisenchantSkill + "'," +
                    "'" + item.m_armorDamageModifier + "'" +
                    ");";

            System.out.println(query);

            stmt.execute(query);
        } catch (SQLException ex) {
        }

        m_db.close();
    }

    private boolean doesItemExist(String name) {
        boolean exists = false;
        
        try {
            String query = "SELECT id FROM items WHERE name='"+name+"';";
            Statement stmt = m_conn.createStatement();
            exists = stmt.execute(query);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        
        return exists;
    }
}
