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
import org.newdawn.slick.CanvasGameContainer;
import org.newdawn.slick.SlickException;

public class MainWindow extends JFrame {
    private GamePreview preview;

    public MainWindow() {
        super("JBlux Editor Suite");

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
        init();
        this.pack();
    }

    private void init() {
        try {
            preview = new GamePreview(null);
            CanvasGameContainer cgc = new CanvasGameContainer(preview);
            this.add(cgc);
            cgc.start();
        } catch (SlickException ex) {
        }

        MenuBar menubar = new MenuBar(preview);
        this.setJMenuBar(menubar);
    }
}
