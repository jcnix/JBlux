/**
 * File:   MainMenuState.java
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
import org.jblux.client.JBlux;
import java.awt.Color;
import java.awt.Font;
import java.util.Observer;
import org.jblux.client.network.PlayerDataFactory;
import org.jblux.client.network.ResponseWaiter;
import org.jblux.client.network.ServerCommunicator;
import org.jblux.util.Commands;
import org.jblux.client.data.PlayerData;
import org.newdawn.slick.AppletGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;
import org.newdawn.slick.gui.TextField;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

public class MainMenuState extends BasicGameState implements Observer {
    private Image background;
    private Image loginButton;
    private TextField txtUsername;
    private TextField txtPassword;
    private ResponseWaiter response;
    private boolean received_data;
    private PlayerData player_data;
    
    private float startGameScale = 1;
    private float exitScale = 1;
    private int stateID = -1;
    private ServerCommunicator server;
    
    public MainMenuState(int stateID, ServerCommunicator server) {
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
                // get the parameters by casting container and getting the applet instance
                Applet applet = ((AppletGameContainer.Container) gc).getApplet();
                username = applet.getParameter("user");
                password = applet.getParameter("password");
                character_name = applet.getParameter("character");

                response = ResponseWaiter.get_new_waiter(this);
                server.authenticate(response, username, password, character_name);
            }
            else {
//                username = "casey-test";
//                password = "5baa61e4c9b93f3f0682250b6cf8331b7ee68fd8";
//                character_name = "mychar";
                username = "casey";
                password = "81b2f040df6152242feb966d071fe58977dab12e";
                character_name = "pdude";
                response = ResponseWaiter.get_new_waiter(this);
                server.authenticate(response, username, password, character_name);
            }
        }

        background = new Image("img/menu.png");
        loginButton = new Image("img/login.png");

        UnicodeFont font = new UnicodeFont(new Font("Serif", Font.PLAIN, 12));
        font.getEffects().add(new ColorEffect(Color.GREEN));
        font.addAsciiGlyphs();
        font.loadGlyphs();
        
        txtUsername = new TextField(gc, font, 333, 225, 150, 20);
        txtUsername.setCursorVisible(true);
        
        txtPassword = new TextField(gc, font, 333, 255, 150, 20);
        txtPassword.setCursorVisible(true);
    }
    
    @Override
    public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
        background.draw(0, 0);
        loginButton.draw(375, 300, startGameScale);
        txtUsername.render(gc, g);
        txtPassword.render(gc, g);
    }
 
    @Override
    public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
        if(received_data) {
            received_data = false;
            GameplayState gps = (GameplayState) sbg.getState(JBlux.GAMEPLAYSTATE);
            gps.setPlayer(player_data);
            sbg.enterState(JBlux.GAMEPLAYSTATE);
        }

        Input input = gc.getInput();

        int mouseX = input.getMouseX();
        int mouseY = input.getMouseY();

        boolean insideStartGame = false;

        if( (mouseX >= 375 && mouseX <= 375 + loginButton.getWidth()) &&
            (mouseY >= 300 && mouseY <= 300 + loginButton.getHeight()))
        {
            insideStartGame = true;
        }
        
        if(insideStartGame){
            if(input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON)) {
                server.authenticate(response, txtUsername.getText(), txtPassword.getText(), "");
                sbg.enterState(JBlux.GAMEPLAYSTATE);
            }
        }
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
