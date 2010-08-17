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

package org.jblux.client.gui;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;
import java.util.Vector;
import org.jblux.client.GameMap;
import org.jblux.client.Npc;
import org.jblux.client.Player;
import org.jblux.client.Players;
import org.jblux.client.Sprite;
import org.jblux.client.gui.observers.NewPlayerObserver;
import org.jblux.common.client.NpcData;
import org.jblux.common.client.PlayerData;
import org.jblux.util.Coordinates;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class GameCanvas implements Observer {
    private static GameCanvas gc;
    private Player player;
    private Players players;
    private Vector<Npc> npcs;
    private GameMap map;
    private Image walk_area;
    private Coordinates map_coords;
    private boolean developer_mode;
    private Image bw_location_sprite;
    private NewPlayerObserver player_observer;

    private boolean new_player;
    private PlayerData new_data;

    protected GameCanvas() {
        init();
    }

    public void init() {
        players = Players.getInstance();
        npcs = new Vector<Npc>();
        map_coords = new Coordinates();
        player_observer = NewPlayerObserver.getInstance();
        player_observer.addObserver(this);
        developer_mode = false;
        new_player = false;
        new_data = null;
    }

    public static GameCanvas getInstance() {
        if(gc == null)
            gc = new GameCanvas();

        return gc;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public void setNpcs(HashMap<Coordinates, NpcData> n) {
        npcs = new Vector<Npc>();

        Set<Coordinates> c_set = n.keySet();
        Iterator<Coordinates> it = c_set.iterator();
        while(it.hasNext()) {
            Coordinates c = it.next();
            NpcData data = n.get(c);
            Npc npc = new Npc(data);
            npc.setCoords(c);
            npcs.add(npc);
        }
    }

    /**
     * The Coords param sets the initial coords when a map is entered.
     * This will determine where to draw the top left
     * corner of the map in relation to the player
     *
     * @param c     The initial coordinates when entering a map.
     */
    public void setMap(GameMap map, Coordinates c) {
        this.map = map;
        walk_area = map.getWalkArea();
        map_coords = c;
    }

    public void setMap(String name, Coordinates c) {
        try {
            setMap(new GameMap(name), c);
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
        return map_coords;
    }

    public void update(GameContainer gc) {
        if(new_player) {
            new_player = false;
            Sprite npc = new Sprite(new_data);
            npc.setCoords(new_data.coords.x, new_data.coords.y);
            npc.setImage(0, 0);
            players.addPlayer(npc);
            new_data = null;
        }

        player.update(gc);
        for(int i = 0; i < npcs.size(); i++) {
            Npc npc = npcs.get(i);
            npc.update(player.getData());
        }
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
        for(int i = 0; i < players.size(); i++) {
            Sprite s = players.getPlayer(i);
            s.draw();
        }

        for(int i = 0; i < npcs.size(); i++) {
            Npc n = npcs.get(i);
            n.draw();
        }

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

    public void update(Observable o, Object arg) {
        if(arg instanceof PlayerData) {
            new_player = true;
            new_data = (PlayerData) arg;
        }
    }
}
