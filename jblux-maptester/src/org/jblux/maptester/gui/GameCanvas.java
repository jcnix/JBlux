/**
 * File: GameCanvas.java
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

package org.jblux.maptester.gui;

import org.jblux.maptester.GameMap;
import org.jblux.maptester.Player;
import org.jblux.util.Coordinates;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class GameCanvas {
    private static GameCanvas gc;
    private Player player;
    private GameMap map;
    private Image walk_area;
    private Coordinates map_coords;
    private boolean developer_mode;
    private Image bw_location_sprite;

    protected GameCanvas() {
        init();
    }

    public void init() {
        map_coords = new Coordinates();
        developer_mode = false;
    }

    public static GameCanvas getInstance() {
        if(gc == null)
            gc = new GameCanvas();

        return gc;
    }

    public void setPlayer(Coordinates coords) {
        player = new Player(coords.x, coords.y);
        Coordinates c = player.getCoords();
        map_coords.x = c.x + coords.x;
        map_coords.y = c.y + coords.y;
    }

    /**
     * The Coords param sets the initial coords when a map is entered.
     * This will determine where to draw the top left
     * corner of the map in relation to the player
     *
     * @param c     The initial coordinates when entering a map.
     */
    public void setMap(GameMap map) {
        this.map = map;
        walk_area = map.getWalkArea();
    }

    public void setMap(String name) {
        try {
            setMap(new GameMap(name));
        } catch (SlickException ex) {
        }
    }

    public GameMap getMap() {
        return map;
    }

    public Image getWalkArea() {
        return walk_area;
    }

    public boolean is_walkable(Coordinates coords) {
        Color c = Color.black;
        try {
            c = walk_area.getColor(coords.x, coords.y);
        } catch(ArrayIndexOutOfBoundsException ex) {
            return false;
        }
        
        if(c.getRed() == 0) {
            return false;
        }
        else {
            return true;
        }
    }

    public Coordinates getMapCoords() {
        return map_coords.clone();
    }

    public void update(GameContainer gc) {
        player.update(gc);
    }

    public void render(GameContainer gc, Graphics g) throws SlickException {
        if(map == null || player == null)
            return;

        map_coords = player.getCoords().clone();
        // 400 and 300 need to be adjusted to account for the height of the sprite
        map_coords.x = 400 - map_coords.x;
        map_coords.y = 300 - map_coords.y;

        map.render(map_coords.x, map_coords.y, 0); //Ground Layer
        map.render(map_coords.x, map_coords.y, 1); //Objects Layer
        map.render(map_coords.x, map_coords.y, 2); //Objects Layer 2

        player.draw();

        map.render(map_coords.x, map_coords.y, 3); //Fringe layer
        map.render(map_coords.x, map_coords.y, 4); //Fringe layer 2

        if(developer_mode) {
            this.walk_area.setAlpha(0.6f);
            this.walk_area.draw(0, 0);
            Coordinates c = player.getCoords();
            bw_location_sprite.draw(c.x, c.y);
        }
    }

    public void toggle_developer_mode() {
        developer_mode = !developer_mode;

        if(developer_mode) {
            try {
                bw_location_sprite = new Image("test.png");
            } catch(SlickException ex) {
            }
        }
        else {
            bw_location_sprite = null;
        }
    }
}
