/**
 * File: HelpMenu.java
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

public class HelpMenu extends JMenu implements ActionListener {
    private JMenuItem aboutItm;

    public HelpMenu() {
        super("Help");
        init();
    }

    private void init() {
        aboutItm = new JMenuItem("About");
        aboutItm.addActionListener(this);

        this.add(aboutItm);
    }

    public void actionPerformed(ActionEvent e) {
    }
}
