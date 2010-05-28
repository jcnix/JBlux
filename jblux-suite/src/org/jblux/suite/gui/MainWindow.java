/**
 * File: MainWindow.java
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

package org.jblux.suite.gui;

import org.jblux.suite.gui.menubar.MenuBar;
import javax.swing.JFrame;
import org.jblux.suite.tabs.MapPanel;
import org.jblux.suite.tabs.TabPane;

public class MainWindow extends JFrame {
    private TabPane tabPane;
    private GamePreview preview;

    public MainWindow() {
        super("JBlux Editor Suite");

        preview = new GamePreview();
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
        init();
        this.pack();
    }

    private void init() {
        tabPane = new TabPane();
        tabPane.addTab_noClose("Map", new MapPanel(preview));

        this.add(tabPane);
        MenuBar menubar = new MenuBar(tabPane, preview);
        this.setJMenuBar(menubar);
    }
}
