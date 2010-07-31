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

import org.jblux.client.GameMap;
import org.jblux.client.Player;
import org.jblux.client.Sprite;
import org.jblux.client.gui.GUI;
import org.jblux.client.gui.GameCanvas;
import org.jblux.client.network.ResponseWaiter;
import org.jblux.client.network.ServerCommunicator;
import org.jblux.common.client.PlayerData;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

public class GameplayState extends BasicGameState {
    private int stateID = -1;
    private GameMap map;
    private Player player;
    private PlayerData player_data;
    private GameCanvas canvas;
    private ServerCommunicator server;
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

    public void setPlayer(PlayerData data) {
        System.out.printf("data null? %b\n", data.race == null);
        player_data = data;
        player = new Player(data, server);
        canvas.setPlayer(player);
    }

    @Override
    public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
        String map_test = "residential";
        map = new GameMap(map_test);
        canvas = GameCanvas.getInstance();
        canvas.init(map_test);
        //canvas = new GameCanvas(player, map_test);
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
        player.update(gc);
        gui.update();
    }
}
