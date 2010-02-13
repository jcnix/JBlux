/**
 * File: GUI.java
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

package com.jblux.client.gui;

import com.jblux.client.network.ServerCommunicator;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.gui.GUIContext;

public class GUI {
    private ChatBox cb;

    public GUI(GUIContext gc, ServerCommunicator s) {
        cb = new ChatBox(gc, s);
    }

    public void update() {
        cb.update();
    }
    
    public void render(Graphics g) {
        cb.render(g);
    }
}
