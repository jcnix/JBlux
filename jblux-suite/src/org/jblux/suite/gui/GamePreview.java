/**
 * File: GamePreview.java
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

import org.jblux.suite.tools.Entity;
import org.jblux.suite.tools.Tool;
import org.jblux.util.Coordinates;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.tiled.TiledMap;

public class GamePreview extends BasicGame {
    private TiledMap map;
    private Tool m_tool;
    private boolean mouseReleased;

    public GamePreview() {
        super("JBlux Editor Suite");
        mouseReleased = true;
    }

    public void setMap(String file) {
        try {
            if(file != null) {
                String path = file.substring(0, file.lastIndexOf('/'));
                System.out.println(file);
                map = new TiledMap(file, path);
            }
        } catch (SlickException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void init(GameContainer gc) throws SlickException {
    }

    @Override
    public void update(GameContainer gc, int i) throws SlickException {
        Input input = gc.getInput();

        if(gc.hasFocus()) {
            if(input.isMousePressed(Input.MOUSE_LEFT_BUTTON ) && (m_tool != null)) {
                mouseReleased = false;
                draw_with_tool(input);
            }
            else if(!input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON)
                    && !mouseReleased && (m_tool != null)) {
                mouseReleased = true;
                draw_with_tool(input);
                Entity e = m_tool.getEntity();
            }
        }
    }

    public void render(GameContainer gc, Graphics g) throws SlickException {
        if(map != null) {
            try {
                map.render(0, 0, 0);
                map.render(0, 0, 1);
                map.render(0, 0, 2);
            } catch(IndexOutOfBoundsException ex) {
            }
        }
    }

    public void setTool(Tool t) {
        m_tool = t;
    }

    public void draw_with_tool(Input input) {
        if(m_tool != null) {
            Coordinates coords = new Coordinates();
            coords.x = input.getMouseX();
            coords.y = input.getMouseY();
            m_tool.draw(coords);
        }
    }
}
