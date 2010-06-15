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

import java.util.Vector;
import org.jblux.common.Relation;
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
    private TiledMap m_map;
    private Tool m_tool;
    private boolean mouseReleased;
    private Vector<Entity> m_entities;

    public GamePreview() {
        super("JBlux Editor Suite");
        mouseReleased = true;
        m_entities = new Vector<Entity>();
    }

    public void setMap(String file) {
        try {
            if(file != null) {
                String path = file.substring(0, file.lastIndexOf('/'));
                System.out.println(file);
                m_map = new TiledMap(file, path);
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
            if(input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON) && (m_tool != null)) {
                mouseReleased = false;
                draw_with_tool(input);
            }
            else if(!input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON)
                    && !mouseReleased && (m_tool != null))
            {
                mouseReleased = true;
                Entity e = m_tool.getEntity();
                m_entities.add(e);
                e.save();
            }
        }
    }

    public void render(GameContainer gc, Graphics g) throws SlickException {
        if(m_map != null) {
            try {
                m_map.render(0, 0, 0);
                m_map.render(0, 0, 1);
                m_map.render(0, 0, 2);
            } catch(IndexOutOfBoundsException ex) {
            }
        }

        for(int i = 0; i < m_entities.size(); i++) {
            Entity e = m_entities.get(i);
            e.render(gc, g);
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

    public Vector<Entity> getEntities() {
        return m_entities;
    }
}
