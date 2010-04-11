/**
 * File: ItemEditor.java
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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import org.jblux.sql.DBManager;

public class ItemEditor extends JPanel {
    private TabPane m_pane;
    private ItemManagerTab m_managerTab;
    private DBManager m_db;

    public ItemEditor() {
        m_pane = new TabPane();
        m_db = new DBManager();
        m_db.connect();

        m_managerTab = new ItemManagerTab(m_db, m_pane);
        m_pane.addTab_noClose("Items", m_managerTab);

        add(m_pane);
    }
}

class ItemManagerTab extends JPanel implements ActionListener {
    private JButton m_newItemBtn;
    private DBManager m_db;
    private JTable m_itemTable;
    private TabPane m_pane;

    public ItemManagerTab(DBManager db, TabPane pane) {
        m_db = db;
        m_pane = pane;
        m_newItemBtn = new JButton("New Item");
        m_newItemBtn.addActionListener(this);

        ResultSet rs = m_db.query_select("SELECT name FROM items");
        Vector<Vector> rowData = new Vector<Vector>();
        Vector<String> items;
        Vector<String> columns = new Vector<String>();
        columns.add("Item");

        try {
            while(rs.next()) {
                items = new Vector<String>();
                String name = rs.getString("name");
                items.add(name);
                rowData.add(items);
            }
        } catch(SQLException ex) {
        }
        //Just some test stuff
        //TODO: remove soon
        items = new Vector<String>();
        items.add("Test");
        rowData.add(items);
        items = new Vector<String>();
        items.add("Test2");
        rowData.add(items);

        m_itemTable = new JTable(rowData, columns);
        JScrollPane scrollPane = new JScrollPane(m_itemTable);
        m_itemTable.setFillsViewportHeight(true);

        add(m_newItemBtn);
        add(scrollPane);
    }

    public void actionPerformed(ActionEvent e) {
        Object action = e.getSource();
        
        if(action == m_newItemBtn) {
            m_pane.addTab("New Item", new ItemTab(m_db, null));
        }
    }
}

class ItemTab extends JPanel {
    private DBManager m_db;
    private JButton m_saveBtn;
    private JTable m_propertyTable;
    private String m_itemName;

    public ItemTab(DBManager db, String name) {
        m_db = db;
        m_itemName = name;
        m_saveBtn = new JButton("Save");

        Vector<Vector> rowData = new Vector<Vector>();
        Vector<String> properties;
        Vector<String> values = null;
        Vector<String> columns = new Vector<String>();
        columns.add("Property");
        columns.add("Value");

        ResultSet item_rs = null;
        if(m_itemName != null) {
            item_rs = m_db.query_select("SELECT * FROM items WHERE name='" + m_itemName + "';");
        }

        try {
            ResultSet rs = m_db.getColumnNames_items();
            while(rs.next()) {
                properties = new Vector<String>();
                String columnName = rs.getString("COLUMN_NAME");
                properties.add(columnName);

                if(item_rs != null) {
                    values = new Vector<String>();
                    item_rs.next();
                    String value = item_rs.getString(columnName);
                    values.add(value);
                }

                rowData.add(properties);
                if(item_rs != null) {
                    rowData.add(values);
                }
            }
        } catch(SQLException ex) {
        }

        m_propertyTable = new JTable(rowData, columns);
        JScrollPane scrollPane = new JScrollPane(m_propertyTable);
        m_propertyTable.setFillsViewportHeight(true);
        
        add(m_saveBtn);
        add(scrollPane);
    }
}
