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

        m_managerTab = new ItemManagerTab(m_db);
        m_pane.addTab_noClose("Items", m_managerTab);

        add(m_pane);
    }
}

class ItemManagerTab extends JPanel implements ActionListener {
    private JButton m_newItemBtn;
    private DBManager m_db;
    private JTable m_itemTable;

    public ItemManagerTab(DBManager db) {
        m_db = db;
        m_newItemBtn = new JButton("New Item");


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
        Object obj = e.getSource();
    }
}
