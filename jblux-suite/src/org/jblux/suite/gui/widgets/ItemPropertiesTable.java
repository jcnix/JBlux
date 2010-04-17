/**
 * File: ItemPropertiesTable.java
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

package org.jblux.suite.gui.widgets;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import org.jblux.common.items.Item;
import org.jblux.sql.ItemSqlTable;

public class ItemPropertiesTable extends JTable {
    private String m_itemName;
    private ItemSqlTable m_db;
    private DefaultTableModel m_tableModel;

    public ItemPropertiesTable(String name, ItemSqlTable db) {
        m_itemName = name;
        m_db = db;

        fill();
    }

    private void fill() {
        Vector<Vector> rowData = new Vector<Vector>();
        Vector<String> row;

        Vector<String> columns = new Vector<String>();
        columns.add("Properties");
        columns.add("Values");

        ResultSet item_rs = m_db.getAllValues(m_itemName);
        try {
            item_rs.next();
        } catch (SQLException ex) {
        }

        try {
            ResultSet rs = m_db.getColumnNames();
            while(rs.next()) {
                row = new Vector<String>();
                String columnName = rs.getString("COLUMN_NAME");
                row.add(columnName);

                if(item_rs != null) {
                    String value = item_rs.getString(columnName);
                    row.add(value);
                }

                rowData.add(row);
            }
        } catch(SQLException ex) {
        }

        m_tableModel = new DefaultTableModel();
        m_tableModel.setDataVector(rowData, columns);
        setModel(m_tableModel);

        setFillsViewportHeight(true);
    }

    public void save() {
        Item item = new Item();
        //This is gonna be ugly
        //TODO: Look for possible nicer ways to do this
        //id = (0,1);
        item.m_class = getShort(1);
        item.m_subclass = getShort(2);
        item.m_name = getString(3);
        item.m_description = getString(4);
        item.m_image = getString(5);
        item.m_quality = getShort(6);
        item.m_sellPrice = getInt(7);
        item.m_buyPrice = getInt(8);
        item.m_allowableClass = getShort(9);
        item.m_allowableRace = getShort(10);
        item.m_itemLevel = getShort(11);
        item.m_requiredLevel = getShort(12);
        item.m_requiredFaction = getShort(13);
        item.m_requiredFactionStanding = getShort(14);
        item.m_isUnique = getShort(15);
        item.m_maxStack = getShort(16);
        item.m_itemStatsCount = getShort(17);
        item.m_stat_type1 = getShort(18);
        item.m_stat_value1 = getShort(19);
        item.m_stat_type2 = getShort(20);
        item.m_stat_value2 = getShort(21);
        item.m_stat_type3 = getShort(22);
        item.m_stat_value3 = getShort(23);
        item.m_stat_type4 = getShort(24);
        item.m_stat_value4 = getShort(25);
        item.m_stat_type5 = getShort(26);
        item.m_stat_value5 = getShort(27);
        item.m_stat_type6 = getShort(28);
        item.m_stat_value6 = getShort(29);
        item.m_stat_type7 = getShort(30);
        item.m_stat_value7 = getShort(31);
        item.m_stat_type8 = getShort(32);
        item.m_stat_value8 = getShort(33);
        item.m_stat_type9 = getShort(34);
        item.m_stat_value9 = getShort(35);
        item.m_stat_type10 = getShort(36);
        item.m_stat_value10 = getShort(37);
        item.m_dmg_min = getFloat(38);
        item.m_dmg_max = getFloat(39);
        item.m_dmg_type = getShort(40);
        item.m_armor = getShort(41);
        item.m_water_resistance = getShort(42);
        item.m_fire_resistance = getShort(43);
        item.m_nature_resistance = getShort(44);
        item.m_frost_resistance = getShort(45);
        item.m_shadow_resistance = getShort(46);
        item.m_arcane_resistance = getShort(47);
        item.m_delay = getShort(48);
        item.m_ammo_type = getShort(49);
        item.m_range = getFloat(50);
        item.m_spellid = getShort(51);
        item.m_spellTrigger = getShort(52);
        item.m_spellCharges = getShort(53);
        item.m_spellCooldown = getShort(54);
        item.m_quest_item = getShort(55);
        item.m_quest_id = getShort(56);
        item.m_block_amount = getShort(57);
        item.m_itemset = getShort(58);
        item.m_maxDurability = getShort(59);
        item.m_requiredDisenchantSkill = getShort(60);
        item.m_armorDamageModifier = getShort(61);
        
        m_db.saveItem(item);
    }

    private int getInt(int row) {
        int i = 0;
        try {
            i = Integer.parseInt(getString(row));
        } catch(NumberFormatException ex) {
            i = 0;
        }

        return i;
    }

    private short getShort(int row) {
        short s = 0;
        try {
            s = Short.parseShort(getString(row));
        } catch(NumberFormatException ex) {
            s = 0;
        }

        return s;
    }

    private float getFloat(int row) {
        float f = 0.0f;
        try {
            f = Float.parseFloat(getString(row));
        } catch(NumberFormatException ex) {
            f = 0.0f;
        }

        return f;
    }

    private String getString(int row) {
        String string = "";
        try {
            string = m_tableModel.getValueAt(row, 1).toString();
        } catch(NullPointerException ex) {
            string = "";
        }

        return string;
    }
}
