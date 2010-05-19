/**
 * File: Maps.java
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

package org.jblux.server.maps;

import java.util.HashMap;
import java.util.Vector;
import org.jblux.common.Map;
import org.jblux.common.Relation;
import org.jblux.sql.MapSqlTable;

public class Maps {
    private static Maps m_self;
    private MapSqlTable m_mapsTable;
    private HashMap<Short, Map> m_maps;

    protected Maps() {
        m_mapsTable = new MapSqlTable();
        m_maps = new HashMap<Short, Map>();
        init_maps();
    }

    public static Maps getInstance() {
        if(m_self == null) {
            m_self = new Maps();
        }
        return m_self;
    }

    private void init_maps() {
        Vector<Map> vmaps = m_mapsTable.getAllMaps();
        for(int i = 0; i < vmaps.size(); i++) {
            Map m = vmaps.get(i);
            m_maps.put(m.getID(), m);
        }
    }

    /**
     * Gets the map object based on the String name
     * 
     * @param   mapId - the Name of the map to get an object for
     * @return  The object of the Map based on the supplied name.
     */
    public Map getMap(short mapId) {
        return m_maps.get(mapId);
    }

    /**
     * Gets map adjacent to given map.
     *
     * @param rel           Which direction to go
     * @param current_map   The id of the current map
     * @return              The Map object of the adjacent map
     */
    public Map getAdjacentMap(Relation rel, short current_map) {
        short id = m_mapsTable.getAdjacentMap(rel, current_map);
        System.out.println(id);
        return getMap(id);
    }

    /**
     * Get the id of the map with the given name
     *
     * @param name  The name of the map to get the id of
     * @return      The id of the map
     */
    public short getID(String name) {
        short id = m_mapsTable.getIdForName(name);
        return id;
    }
}
