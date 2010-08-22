/**
 * File:   JBlux.java
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

package org.jblux.maptester;

import org.jblux.maptester.states.GameplayState;
import org.jblux.maptester.states.InitState;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;

public class Test extends StateBasedGame{
    public static final int GAMEPLAYSTATE = 0;
    public static final int INITSTATE = 1;
    
    public Test() {
        super("JBlux-MapTester");
    }

    @Override
    public void initStatesList(GameContainer gc) throws SlickException {
        InitState is = new InitState(INITSTATE);
        this.addState(is);
        is.init(gc, this);

        GameplayState gps = new GameplayState(GAMEPLAYSTATE);
        this.addState(gps);
        gps.init(gc, this);

        this.enterState(INITSTATE);
    }

    public static void main(String[] args) {
        try
        {
            Test game = new Test();
            AppGameContainer app = new AppGameContainer(game);
            app.setShowFPS(false);
            app.setDisplayMode(800, 600, false);
            app.start();
        }
        catch ( SlickException e )
        {
            e.printStackTrace();
        }
    }
}
