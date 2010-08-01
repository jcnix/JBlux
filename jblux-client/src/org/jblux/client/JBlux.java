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

package org.jblux.client;

import org.jblux.client.network.ServerCommunicator;
import org.jblux.client.states.GameplayState;
import org.jblux.client.states.MainMenuState;
import org.jblux.client.states.ServerDownState;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;

public class JBlux extends StateBasedGame{
    public static final int MAINMENUSTATE = 0;
    public static final int GAMEPLAYSTATE = 1;
    public static final int SERVERDOWNSTATE = 2;
    private ServerCommunicator server;
    
    public JBlux() {
        super("JBlux");
        server = new ServerCommunicator();
    }

    public void init() {
    }

    @Override
    public void initStatesList(GameContainer gc) throws SlickException {
         if(!server.isConnected()) {
            this.addState(new ServerDownState(SERVERDOWNSTATE));
            this.enterState(SERVERDOWNSTATE);
        }
        else {            
             MainMenuState mms = new MainMenuState(MAINMENUSTATE, server);
             this.addState(mms);
             mms.init(gc, this);
             
             GameplayState gps = new GameplayState(GAMEPLAYSTATE, server);
             this.addState(gps);
             gps.init(gc, this);
             
             this.enterState(MAINMENUSTATE);
        }
    }

    public static void main(String[] args) {
        try
        {
            JBlux game = new JBlux();
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
