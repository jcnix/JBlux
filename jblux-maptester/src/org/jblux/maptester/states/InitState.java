/**
 * File: InitState.java
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

package org.jblux.maptester.states;

import java.io.File;
import org.jblux.maptester.GameMap;
import org.jblux.maptester.Test;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

public class InitState extends BasicGameState {
    private int stateID = -1;
    private GameMap map;

    public InitState(int stateID)
    {
        this.stateID = stateID;
    }

    @Override
    public int getID() {
        return stateID;
    }

    @Override
    public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
        String map_file = "";
        File f = new File(".");
        String[] files = f.list();
        for(int i = 0; i < files.length; i++) {
            String file = files[i];
            if(file.endsWith(".tmx")) {
                file = file.substring(0, file.length() - 4);
                map_file = file;
            }
        }

        try {
            map = new GameMap(map_file);
        } catch (SlickException ex) {
        }
    }

    @Override
    public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
        map.render(0, 0);
    }

    @Override
    public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
        Input input = gc.getInput();
        if(input.isMousePressed(Input.MOUSE_LEFT_BUTTON)) {
            int x = input.getMouseX();
            int y = input.getMouseY();

            GameplayState gps = (GameplayState) sbg.getState(Test.GAMEPLAYSTATE);
            gps.setMap(map, x, y);
            sbg.enterState(Test.GAMEPLAYSTATE);
        }
    }
}
