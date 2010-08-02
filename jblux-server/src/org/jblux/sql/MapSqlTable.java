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

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Vector;
import org.jblux.common.Relation;
import org.jblux.common.client.NpcData;
import org.jblux.common.items.Item;
import org.jblux.server.maps.Map;
import org.jblux.util.Coordinates;

public class MapSqlTable {
    private DBManager m_db;
    private static final String MAP_TABLE = "jblux_map";
    private static final String ITEMS_TABLE = "jblux_mapitems";
    private static final String NPCS_TABLE = "jblux_mapnpcs";

    public MapSqlTable() {
        m_db = new DBManager();
    }

    /**
     * Initializes all maps and returns them.
     *
     * Make sure this is only called once.
     * Calling it twice would return the Maps as they appear in the database,
     * and not in the state they're really in.
     *
     * The dropped items, and players on the map would not be in the
     * returned data.
     *
     * @return List of all maps in the database.
     */
    public Vector<Map> getAllMaps() {
        Vector<Map> map_list = new Vector<Map>();
        ItemSqlTable item_table = new ItemSqlTable();
        m_db.connect();

        try {
            String q = String.format("SELECT * FROM %s;", MAP_TABLE);
            ResultSet map_rs = m_db.query_select(q);
            
            while(map_rs.next()) {
                short id = map_rs.getShort("id");
                String name = map_rs.getString("name");
                HashMap<Coordinates, Item> items = new HashMap<Coordinates, Item>();

                //Get items on map.
                q = String.format("SELECT * FROM %s WHERE map_id='%d';", ITEMS_TABLE, id);
                ResultSet items_rs = m_db.query_select(q);
                if(items_rs != null) {
                    while(items_rs.next()) {
                        short item_id = items_rs.getShort("item_id");
                        Coordinates c = new Coordinates();
                        c.x = items_rs.getInt("x_coord");
                        c.y = items_rs.getInt("y_coord");
                        Item item = item_table.getItem(item_id);
                        items.put(c, item);
                    }
                }

                short left = getAdjacentMap(Relation.LEFT, id);
                short right = getAdjacentMap(Relation.RIGHT, id);
                short above = getAdjacentMap(Relation.TOP, id);
                short below = getAdjacentMap(Relation.BOTTOM, id);

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
     * @param   mapId - the id of the map the player is currently on
     * @return  Returns the id of the new map, adjacent to the old map.
     */
    public short getAdjacentMap(Relation rel, short mapId) {
        short newMap = 0;
        m_db.connect();

        try {
            String col = "";
            if(rel != null)
                col = rel.toString();
            else
                col = "id";

            String q = String.format("SELECT map_%s_id FROM %s WHERE id='%d';",
                    col, MAP_TABLE, mapId);
            ResultSet rs = m_db.query_select(q);
            rs.next();
            newMap = rs.getShort("map_" + rel + "_id");
        } catch(SQLException ex) {
            ex.printStackTrace();
        }

        m_db.close();
        return newMap;
    }

    public String getNameForId(int id) {
        String name = "";
        m_db.connect();

        try {
            String q = String.format("SELECT name FROM %s WHERE id='%d';", MAP_TABLE, id);
            ResultSet rs = m_db.query_select(q);
            rs.next();
            name = rs.getString("name");
        } catch(SQLException ex) {
        }

        m_db.close();
        return name;
    }

    public short getIdForName(String name) {
        short id = 0;
        m_db.connect();

        try {
            String q = String.format("SELECT id FROM %s WHERE name='%s';", MAP_TABLE, name);
            ResultSet rs = m_db.query_select(q);
            rs.next();
            id = rs.getShort("id");
        } catch(SQLException ex) {
        }

        m_db.close();
        return id;
    }

    public Vector<Item> getItemsOnMap(int id) {
        Vector<Item> items = new Vector<Item>();
        m_db.connect();

        try {
            String q = String.format("SELECT * FROM %s WHERE map_t_id=%d;",
                    ITEMS_TABLE, id);
            ResultSet rs = m_db.query_select(q);

            ItemSqlTable ist = new ItemSqlTable();
            while(rs.next()) {
                short item_id = rs.getShort("item_id");
                Item item = ist.getItem(rs.getShort(item_id));
                items.add(item);
            }
        } catch(SQLException ex) {
        }

        m_db.close();
        return items;
    }

    public Vector<NpcData> getNpcsOnMap(int id) {
        Vector<NpcData> npcs = new Vector<NpcData>();
        m_db.connect();

        try {
            String q = String.format("SELECT * FROM %s WHERE map_t_id=%d;",
                    NPCS_TABLE, id);
            ResultSet rs = m_db.query_select(q);

            NpcTable npct = new NpcTable();
            while(rs.next()) {
                NpcData npc = npct.getNpc(rs.getInt("npc_id"));
                npcs.add(npc);
            }
        } catch(SQLException ex) {
        }

        m_db.close();
        return npcs;
    }

    public Coordinates getEntrance(short map_id, Relation r) {
        m_db.connect();
        Coordinates c = new Coordinates();

        String query = "";
        try {
            query = String.format("SELECT entrance_%s_x, entrance_%s_y FROM %s " +
                    "WHERE id='%d';", r, r, MAP_TABLE, map_id);

            ResultSet rs = m_db.query_select(query);
            rs.next();
            c.x = rs.getShort(String.format("entrance_%s_x", r));
            c.y = rs.getShort(String.format("entrance_%s_y", r));
        } catch(SQLException ex) {
        }

        m_db.close();
        return c;
    }

    public Coordinates getEntrance_left(short map_id) {
        return getEntrance(map_id, Relation.LEFT);
    }

    public Coordinates getEntrance_right(short map_id) {
        return getEntrance(map_id, Relation.RIGHT);
    }

    public Coordinates getEntrance_top(short map_id) {
        return getEntrance(map_id, Relation.TOP);
    }

    public Coordinates getEntrance_bottom(short map_id) {
        return getEntrance(map_id, Relation.BOTTOM);
    }
}
