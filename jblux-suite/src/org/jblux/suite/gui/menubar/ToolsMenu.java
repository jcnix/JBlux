/**
 * File: FileMenu.java
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

package org.jblux.suite.gui.menubar;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import org.jblux.suite.gui.MainWindow;
import org.jblux.suite.tabs.ItemEditor;

public class ToolsMenu extends JMenu implements ActionListener {
    private JMenuItem m_itemEditorItm;
    private MainWindow m_window;

    public ToolsMenu(MainWindow mw) {
        super("Tools");

        m_window = mw;
        init();
    }
    
    private void init() {
        m_itemEditorItm = new JMenuItem("Item Editor");
        m_itemEditorItm.addActionListener(this);

        this.add(m_itemEditorItm);
    }
    
    public void actionPerformed(ActionEvent e) {
        Object action = e.getSource();

        if(action == m_itemEditorItm) {
            showItemEditor();
        }
    }

    private void showItemEditor() {
        m_window.addTab("Items", new ItemEditor());
    }
}
