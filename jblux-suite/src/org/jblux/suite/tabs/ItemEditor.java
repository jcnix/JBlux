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
import javax.swing.JButton;
import javax.swing.JPanel;

public class ItemEditor extends JPanel {
    private TabPane pane;
    private ItemManagerTab managerTab;

    public ItemEditor() {
        pane = new TabPane();

        managerTab = new ItemManagerTab();
        pane.addTab_noClose("Items", managerTab);

        add(pane);
    }
}

class ItemManagerTab extends JPanel implements ActionListener {
    private JButton newItemBtn;

    public ItemManagerTab() {
        newItemBtn = new JButton("New Item");
        add(newItemBtn);
    }

    public void actionPerformed(ActionEvent e) {
        Object obj = e.getSource();
    }
}
