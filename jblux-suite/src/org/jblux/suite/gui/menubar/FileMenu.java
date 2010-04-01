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

public class FileMenu extends JMenu implements ActionListener {
    public JMenuItem openItm;
    public JMenuItem exitItm;

    public FileMenu() {
        super("File");
        init();
    }

    private void init() {
        openItm = new JMenuItem("Open");
        exitItm = new JMenuItem("Exit");

        openItm.addActionListener(this);
        exitItm.addActionListener(this);

        this.add(openItm);
        this.addSeparator();
        this.add(exitItm);
    }

    public void actionPerformed(ActionEvent e) {
        Object action = e.getSource();

        if(action == exitItm) {
            System.exit(0);
        }
    }
}
