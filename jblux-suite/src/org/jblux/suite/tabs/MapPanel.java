/**
 * File: MapPanel.java
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

import javax.swing.JPanel;
import org.jblux.suite.gui.GamePreview;
import org.newdawn.slick.CanvasGameContainer;
import org.newdawn.slick.SlickException;

public class MapPanel extends JPanel {
    private GamePreview preview;

    public MapPanel() {
        init();
    }

    private void init() {
        try {
            preview = new GamePreview(null);
            CanvasGameContainer cgc = new CanvasGameContainer(preview);
            cgc.setSize(800, 600);
            this.add(cgc);
            cgc.start();
        } catch (SlickException ex) {
        }
    }
}
