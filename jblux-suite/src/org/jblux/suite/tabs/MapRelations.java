/**
 * File: MapRelations.java
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

package org.jblux.suite.tabs;

import java.util.Vector;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import org.jblux.common.Map;
import org.jblux.common.Relation;
import org.jblux.sql.MapSqlTable;

public class MapRelations extends JPanel {
    private MapSqlTable m_db;
    private JTable m_mapTable;

    public MapRelations() {
        m_db = new MapSqlTable();
        Vector<Map> maps = m_db.getAllMaps();
        
        Vector<String> columns = new Vector<String>();
        Vector<Vector> rows = new Vector<Vector>();

        columns.add("id");
        columns.add("name");
        columns.add("left");
        columns.add("right");
        columns.add("above");
        columns.add("below");

        for(int i = 0; i < maps.size(); i++) {
            Vector<String> row = new Vector<String>();
            Map m = maps.get(i);
            String id = String.valueOf(m.getID());
            String name = m.getName();
            String left = String.valueOf(m.get_adjacent_map(Relation.LEFT));
            String right = String.valueOf(m.get_adjacent_map(Relation.RIGHT));
            String above = String.valueOf(m.get_adjacent_map(Relation.ABOVE));
            String below = String.valueOf(m.get_adjacent_map(Relation.BELOW));

            row.add(id);
            row.add(name);
            row.add(left);
            row.add(right);
            row.add(above);
            row.add(below);
            rows.add(row);
        }

        m_mapTable = new JTable(rows, columns);
        JScrollPane scrollPane = new JScrollPane(m_mapTable);
        add(scrollPane);
    }
}
