/**
 * File: GameplayState.java
 *
 * @author: Casey Jones
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

package com.jblux.client.states;

import com.jblux.client.Player;
import com.jblux.client.Players;
import com.jblux.client.Sprite;
import com.jblux.client.gui.GUI;
import com.jblux.client.network.ServerCommunicator;
import java.applet.Applet;
import org.newdawn.slick.AppletGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.tiled.TiledMap;

public class GameplayState extends BasicGameState {
    private TiledMap map;

    private int stateID = -1;
    private Player player;
    private ServerCommunicator server;

    private Sprite npc;
    private Players players;
    private GUI gui;
    
    public GameplayState(int stateID, ServerCommunicator server)
    {
        this.stateID = stateID;
        this.server = server;

        players = Players.getInstance();
    }
 
    @Override
    public int getID() {
        return stateID;
    }
 
    @Override
    public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
        String username = "";
        if (gc instanceof AppletGameContainer.Container) {
            // get the parameters by casting container and getting the applet instance
            Applet applet = ((AppletGameContainer.Container) gc).getApplet();
            username = applet.getParameter("user");
        }
        else {
            username = "casey";
        }

        map = new TiledMap("maps/residential/residential.tmx", "maps/residential");
        player = new Player(server, username);
        npc = new Sprite("img/koopa.png");
        npc.setImage(Sprite.FACE_DOWN, 0);

        gui = new GUI(gc, server);
    }
 
    @Override
    public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
        map.render(0,0,0);
        map.render(0,0,1);

        player.draw();
        for(int i = 0; i < players.size(); i++) {
            Sprite s = players.getPlayer(i);
            s.draw();
        }
        map.render(0,0,2);

        gui.render(g);
    }
 
    @Override
    public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
        player.update(gc);
        gui.update();
    }
}
