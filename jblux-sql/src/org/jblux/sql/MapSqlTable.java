/**
 * File: MapSqlTable.java
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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;
import org.jblux.common.Map;
import org.jblux.common.Relation;
import org.jblux.common.items.Item;

public class MapSqlTable {
    private DBManager m_db;
    private Connection m_conn;

    public MapSqlTable() {
        m_db = new DBManager();
    }

    public Vector<Map> getAllMaps() {
        Vector<Map> map_list = new Vector<Map>();
        ItemSqlTable item_table = new ItemSqlTable();
        m_db.connect();

        try {
            String q = String.format("SELECT * FROM maps;");
            ResultSet map_rs = m_db.query_select(q);
            
            while(map_rs.next()) {
                short id = map_rs.getShort("id");
                String name = map_rs.getString("name");
                Vector<Item> items = new Vector<Item>();

                q = String.format("SELECT * FROM map_items;");
                ResultSet items_rs = m_db.query_select(q);
                while(items_rs.next()) {
                    short item_id = items_rs.getShort("item_id");
                    Item item = item_table.getItem(item_id);
                    items.add(item);
                }

                short left = getAdjacentMap(Relation.LEFT, name);
                short right = getAdjacentMap(Relation.RIGHT, name);
                short above = getAdjacentMap(Relation.ABOVE, name);
                short below = getAdjacentMap(Relation.BELOW, name);

                Map m = new Map(id, name, items);
                m.set_adjacent_maps(left, right, above, below);
                map_list.add(m);
            }
        } catch(SQLException ex) {
        }

        return map_list;
    }

    /**
     * Find out which map is next to the current map
     * @param   rel - also the name of the SQL column
     * @param   mapName - the name of the map the player is currently on
     * @return  Returns the id of the new map, adjacent to the old map.
     */
    public short getAdjacentMap(Relation rel, String mapName) {
        short newMap = 0;
        m_db.connect();

        try {
            String col = "";
            if(rel != null)
                col = rel.toString();
            else
                col = "id";

            String q = String.format("SELECT %s FROM maps WHERE name='%s';",
                    col, mapName);
            ResultSet rs = m_db.query_select(q);
            rs.next();
            newMap = rs.getShort(rel.toString());
        } catch(SQLException ex) {
        }

        m_db.close();
        return newMap;
    }

    public String getNameForId(short id) {
        String name = "";
        m_db.connect();

        try {
            String q = String.format("SELECT name FROM maps WHERE id=", id);
            ResultSet rs = m_db.query_select(q);
            rs.next();
            name = rs.getString("name");
        } catch(SQLException ex) {
        }

        return name;
    }

    public Vector<Item> getItemsOnMap(String name) {
        Vector<Item> items = new Vector<Item>();
        m_db.connect();

        try {
            String q = String.format("SELECT id FROM maps WHERE NAME='%s'", name);
            ResultSet rs = m_db.query_select(q);
            rs.next();
            short map_id = rs.getShort("id");

            q = String.format("SELECT item_id FROM map_items WHERE map_id='%d';",
                    map_id);
            rs = m_db.query_select(q);

            ItemSqlTable ist = new ItemSqlTable();
            while(rs.next()) {
                short item_id = rs.getShort("item_id");
                Item item = ist.getItem(rs.getShort(item_id));
                items.add(item);
            }
        } catch(SQLException ex) {
        }

        return items;
    }
}
