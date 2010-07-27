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

package org.jblux.client.states;

import java.applet.Applet;
import java.util.Observable;
import java.util.Observer;
import org.jblux.client.GameMap;
import org.jblux.client.Player;
import org.jblux.client.Sprite;
import org.jblux.client.gui.GUI;
import org.jblux.client.gui.GameCanvas;
import org.jblux.client.network.PlayerDataFactory;
import org.jblux.client.network.ResponseWaiter;
import org.jblux.client.network.ServerCommunicator;
import org.jblux.common.Commands;
import org.jblux.common.client.PlayerData;
import org.newdawn.slick.AppletGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

public class GameplayState extends BasicGameState implements Observer {
    private int stateID = -1;
    private GameMap map;
    private Player player;
    private PlayerData player_data;
    private GameCanvas canvas;
    private ServerCommunicator server;
    private ResponseWaiter response;
    private boolean received_data;

    private Sprite npc;
    private GUI gui;
    
    public GameplayState(int stateID, ServerCommunicator server)
    {
        this.stateID = stateID;
        this.server = server;
    }
 
    @Override
    public int getID() {
        return stateID;
    }
 
    @Override
    public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
        if(server.isConnected()) {
            String username = "";
            String password = "";
            String character_name = "";

            boolean authorized = false;
            if (gc instanceof AppletGameContainer.Container) {
                System.out.println("Applet");
                // get the parameters by casting container and getting the applet instance
                Applet applet = ((AppletGameContainer.Container) gc).getApplet();
                username = applet.getParameter("user");
                password = applet.getParameter("password");
                character_name = applet.getParameter("character");

                response = new ResponseWaiter();
                response.addObserver(this);
                server.authenticate(response, username, password, character_name);
            }
            else {
                username = "casey-test";
                password = "5baa61e4c9b93f3f0682250b6cf8331b7ee68fd8";
                character_name = "mychar";
                //username = "casey";
                //password = "81b2f040df6152242feb966d071fe58977dab12e";
                //password = "wrong password";
                //character_name = "pdude";
                response = new ResponseWaiter();
                response.addObserver(this);
                server.authenticate(response, username, password, character_name);
            }
        }

        String map_test = "residential";
        map = new GameMap(map_test);
        canvas = GameCanvas.getInstance();
        canvas.init(map_test);
        //canvas = new GameCanvas(player, map_test);

        npc = new Sprite("img/races/koopa.png");
        npc.setImage(Sprite.FACE_DOWN, 0);

        gui = new GUI(gc, server);
    }
 
    @Override
    public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
        //The canvas renders the map and all players
        canvas.render(gc, g);
        gui.render(g);
    }
 
    @Override
    public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
        if(received_data) {
            System.out.println("init player");
            received_data = false;
            player = new Player(player_data, server);
            canvas.setPlayer(player);
        }
        
        player.update(gc);
        gui.update();
    }

    public void update(Observable o, Object arg) {
        System.out.println("Received response");
        if(o == response) {
            server.rm_observable(o);
            String c = (String) arg;
            String[] command = c.split(" ");
            if(command[0].equals(Commands.PLAYER)) {
                if(command[1].equals("self")) {
                    player_data = PlayerDataFactory.getDataFromBase64(command[2]);
                    received_data = true;
                }
            }
        }
    }
}
