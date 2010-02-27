/**
 * File: Map.java
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

package com.jblux.client;

import com.jblux.client.network.ServerCommunicator;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.tiled.TiledMap;

public class GameMap extends TiledMap {
    private String name;
    private Player player;
    private Players players;
    private ServerCommunicator server;

    public GameMap(String name, Player player, ServerCommunicator server) throws SlickException {
        super("maps/" + name + "/" + name +".tmx", "maps/" + name);

        this.server = server;
        server.setMap(name);

        this.name = name;
        this.player = player;
        players = Players.getInstance();
    }

    public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
        this.render(0,0,0);
        this.render(0,0,1);

        player.draw();
        for(int i = 0; i < players.size(); i++) {
            Sprite s = players.getPlayer(i);
            s.draw();
        }
        this.render(0,0,2);

    }
}
