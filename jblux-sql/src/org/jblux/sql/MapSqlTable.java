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
import org.jblux.common.Relation;

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
     */
    public String getMap(Relation rel, String mapName) {
        String newMap = "";
        m_db.connect();

        try {
            String q = String.format("SELECT %s FROM maps WHERE name='%s';",
                    rel, mapName);
            ResultSet rs = m_db.query_select(q);
            rs.next();
            newMap = rs.getString(rel.toString());
        } catch(SQLException ex) {
        }

        m_db.close();
        return newMap;
    }
}
