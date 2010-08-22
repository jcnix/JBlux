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

package org.jblux.maptester.states;

import java.io.File;
import org.jblux.maptester.GameMap;
import org.jblux.maptester.Player;
import org.jblux.maptester.gui.GameCanvas;
import org.jblux.util.Coordinates;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

public class GameplayState extends BasicGameState {
    private int stateID = -1;
    private GameMap map;
    private Player player;
    private GameCanvas canvas;
    
    public GameplayState(int stateID)
    {
        this.stateID = stateID;
    }
 
    @Override
    public int getID() {
        return stateID;
    }

    public void setMap(GameMap map, int x, int y) {
        canvas.setPlayer(new Coordinates(x, y));
        canvas.setMap(map);
    }

    @Override
    public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
        canvas = GameCanvas.getInstance();
    }
 
    @Override
    public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
        //The canvas renders the map and all players
        canvas.render(gc, g);
    }
 
    @Override
    public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
        canvas.update(gc);
    }
}
