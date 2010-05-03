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

import org.jblux.common.Map;
import org.jblux.common.Relation;
import org.jblux.sql.MapSqlTable;

/**
 * This class reads maps/layout.txt to find out the relationship
 * between maps.  So if the player moves off the left side of a \
 * map, they proceed to the correct map.
 */

public class Maps {
    public MapSqlTable m_mapsTable;

    public Maps() { 
        m_mapsTable = new MapSqlTable();
    }

    public Map getMap(Relation rel, String current_map) {
        String mapName = m_mapsTable.getMap(rel, current_map);
        return Map(mapName);
    }
}
