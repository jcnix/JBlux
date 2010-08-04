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

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;
import org.jblux.client.GameMap;
import org.jblux.client.Npc;
import org.jblux.client.Player;
import org.jblux.client.Players;
import org.jblux.client.Sprite;
import org.jblux.common.client.NpcData;
import org.jblux.util.Coordinates;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

public class GameCanvas {
    private static GameCanvas gc;
    private Player player;
    private Players players;
    private Vector<Npc> npcs;
    private GameMap map;

    protected GameCanvas() {
    }

    public void init() {
        players = Players.getInstance();
        npcs = new Vector<Npc>();
    }

    public static GameCanvas getInstance() {
        if(gc == null)
            gc = new GameCanvas();

        return gc;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public void setMap(GameMap map) {
        this.map = map;
    }

    public void setNpcs(HashMap<Coordinates, NpcData> n) {
        npcs = new Vector<Npc>();

        Set<Coordinates> c_set = n.keySet();
        Iterator<Coordinates> it = c_set.iterator();
        while(it.hasNext()) {
            Coordinates c = it.next();
            System.out.println(c.toString());
            NpcData data = n.get(c);
            Npc npc = new Npc(data);
            npc.setCoords(c);
            npcs.add(npc);
        }
    }

    public void render(GameContainer gc, Graphics g) throws SlickException {
        if(map == null || player == null)
            return;

        map.render(0,0,0); //Ground Layer
        map.render(0,0,1); //Objects Layer
        map.render(0,0,2); //Objects Layer 2

        player.draw();
        for(int i = 0; i < players.size(); i++) {
            Sprite s = players.getPlayer(i);
            s.draw();
        }

        for(int i = 0; i < npcs.size(); i++) {
            Npc n = npcs.get(i);
            n.draw();
        }

        map.render(0,0,3); //Fringe layer
        map.render(0,0,4); //Fringe layer 2
    }

    public void setMap(String name) {
        try {
            map = new GameMap(name);
        } catch (SlickException ex) {
        }
    }

    public GameMap getMap() {
        return map;
    }
}
