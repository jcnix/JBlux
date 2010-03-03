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

package com.jblux.client.gui;

import com.jblux.client.GameMap;
import com.jblux.client.Player;
import com.jblux.client.Players;
import com.jblux.client.Sprite;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

public class GameCanvas {
    private static GameCanvas gc;
    private Player player;
    private Players players;
    private GameMap map;

    protected GameCanvas() {
    }

    public void init(Player player, String map_name) {
        this.player = player;
        players = Players.getInstance();

        try {
            map = new GameMap(map_name);
        } catch (SlickException ex) {
        }
    }

    public static GameCanvas getInstance() {
        if(gc == null)
            gc = new GameCanvas();

        return gc;
    }

    public void render(GameContainer gc, Graphics g) throws SlickException {
        map.render(0,0,0); //Ground Layer
        map.render(0,0,1); //Objects Layer

        player.draw();
        for(int i = 0; i < players.size(); i++) {
            Sprite s = players.getPlayer(i);
            s.draw();
        }
        map.render(0,0,2);  //Fringe Layer
    }

    public void setMap(String name) {
        try {
            map = new GameMap(name);
        } catch (SlickException ex) {
        }
    }
}
