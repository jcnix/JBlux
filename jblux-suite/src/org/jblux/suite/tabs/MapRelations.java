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

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import org.jblux.common.Map;
import org.jblux.common.Relation;
import org.jblux.sql.MapSqlTable;

public class MapRelations extends JPanel implements ActionListener {
    private MapSqlTable m_db;
    private JTable m_mapTable;
    private JButton m_newMapBtn;
    private JButton m_saveBtn;

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
        
        initGui();
    }

    private void initGui() {
        m_newMapBtn = new JButton("New Map");
        m_saveBtn = new JButton("Save");
        JScrollPane scrollPane = new JScrollPane(m_mapTable);

        JPanel btnPanel = new JPanel();
        GridLayout gLayout = new GridLayout(2, 1);
        btnPanel.setLayout(gLayout);
        btnPanel.add(m_newMapBtn);
        btnPanel.add(m_saveBtn);

        add(btnPanel);
        add(scrollPane);
    }

    private void save() {
        for(int i = 0; i < m_mapTable.getRowCount(); i++) {
            short map_id = Short.parseShort((String) m_mapTable.getValueAt(i, 0));
            String map_name = (String) m_mapTable.getValueAt(i, 1);
            short left = Short.parseShort((String) m_mapTable.getValueAt(i, 2));
            short right = Short.parseShort((String) m_mapTable.getValueAt(i, 3));
            short above = Short.parseShort((String) m_mapTable.getValueAt(i, 4));
            short below = Short.parseShort((String) m_mapTable.getValueAt(i, 5));
            m_db.saveMap(map_id, map_name, left, right, above, below);
        }
    }

    public void actionPerformed(ActionEvent e) {
        Object action = e.getSource();

        if(action == m_saveBtn) {
            save();
        }
    }
}
