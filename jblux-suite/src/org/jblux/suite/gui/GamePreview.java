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

import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.tiled.TiledMap;

public class GamePreview extends BasicGame {
    private TiledMap map;
    private String map_file;

    public GamePreview(String file) {
        super("JBlux Editor Suite");

        setMap(file);
    }

    public void setMap(String file) {
        try {
            if(file != null) {
                map_file = file;
                String path = file.substring(0, file.lastIndexOf('/'));
                System.out.println(path);
                map = new TiledMap(file, path);
            }
        } catch (SlickException ex) {
        }
    }

    @Override
    public void init(GameContainer gc) throws SlickException {
    }

    @Override
    public void update(GameContainer gc, int i) throws SlickException {
    }

    public void render(GameContainer gc, Graphics grphcs) throws SlickException {
        if(map != null) {
            try {
                map.render(0, 0, 0);
                map.render(0, 0, 1);
                map.render(0, 0, 2);
            } catch(IndexOutOfBoundsException ex) {
            }
        }
    }
}
