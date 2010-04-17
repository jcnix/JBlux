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

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import org.jblux.sql.ItemSqlTable;
import org.jblux.suite.gui.widgets.ItemPropertiesTable;

public class ItemEditor extends JPanel {
    private TabPane m_pane;
    private ItemManagerTab m_managerTab;
    private ItemSqlTable m_db;

    public ItemEditor() {
        m_pane = new TabPane();
        m_db = new ItemSqlTable();

        m_managerTab = new ItemManagerTab(m_db, m_pane);
        m_pane.addTab_noClose("Items", m_managerTab);

        add(m_pane);
    }
}

class ItemManagerTab extends JPanel implements ActionListener {
    private JButton m_newItemBtn;
    private JButton m_openItemBtn;
    private ItemSqlTable m_db;
    private JTable m_itemTable;
    private TabPane m_pane;

    public ItemManagerTab(ItemSqlTable db, TabPane pane) {
        m_db = db;
        m_pane = pane;
        m_newItemBtn = new JButton("New Item");
        m_openItemBtn = new JButton("Open");
        m_newItemBtn.addActionListener(this);
        m_openItemBtn.addActionListener(this);

        ResultSet rs = m_db.getAllItemNames();
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

        m_itemTable = new JTable(rowData, columns);
        JScrollPane scrollPane = new JScrollPane(m_itemTable);
        m_itemTable.setFillsViewportHeight(true);

        JPanel btnPanel = new JPanel();
        GridLayout glayout = new GridLayout(2,1);
        btnPanel.setLayout(glayout);
        btnPanel.add(m_newItemBtn);
        btnPanel.add(m_openItemBtn);

        add(btnPanel);
        add(scrollPane);
    }

    public void actionPerformed(ActionEvent e) {
        Object action = e.getSource();
        
        if(action == m_newItemBtn) {
            m_pane.addTab("New Item", new ItemTab(m_db, null));
        }
        else if(action == m_openItemBtn) {
            int r = m_itemTable.getSelectedRow();
            String name = (String) m_itemTable.getValueAt(r, 0);
            m_pane.addTab(name, new ItemTab(m_db, name));
        }
    }
}

class ItemTab extends JPanel implements ActionListener {
    private ItemSqlTable m_db;
    private JButton m_saveBtn;
    private ItemPropertiesTable m_propertyTable;
    private String m_itemName;

    public ItemTab(ItemSqlTable db, String name) {
        m_db = db;
        m_itemName = name;
        m_saveBtn = new JButton("Save");
        m_saveBtn.addActionListener(this);

        m_propertyTable = new ItemPropertiesTable(m_itemName, m_db);
        JScrollPane scrollPane = new JScrollPane(m_propertyTable);
        m_propertyTable.setFillsViewportHeight(true);
        
        add(m_saveBtn);
        add(scrollPane);
    }

    public void actionPerformed(ActionEvent e) {
        Object action = e.getSource();

        if(action == m_saveBtn) {
            m_propertyTable.save();
        }
    }
}
