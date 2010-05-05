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
import org.jblux.common.Relation;
import org.jblux.common.items.Item;

public class MapSqlTable {
    private DBManager m_db;
    private Connection m_conn;

    public MapSqlTable() {
        m_db = new DBManager();
    }

    /**
     * Find out which map is next to the current map
     * @param   rel - also the name of the SQL column
     * @param   mapName - the name of the map the player is currently on
     * @return  Returns the name of the new map, adjacent to the old map.
     */
    public String getAdjacentMap(Relation rel, String mapName) {
        String newMap = "";
        m_db.connect();

        try {
            String column = "";
            if(rel != null)
                column = rel.toString();
            else
                column = "name";

            String q = String.format("SELECT %s FROM maps WHERE name='%s';",
                    column, mapName);
            ResultSet rs = m_db.query_select(q);
            rs.next();
            newMap = rs.getString(rel.toString());
        } catch(SQLException ex) {
        }

        m_db.close();
        return newMap;
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
