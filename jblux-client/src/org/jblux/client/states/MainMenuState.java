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

import java.util.Observable;
import org.jblux.client.JBlux;
import java.awt.Color;
import java.awt.Font;
import java.util.Observer;
import org.jblux.client.Player;
import org.jblux.client.gui.GameCanvas;
import org.jblux.client.network.PlayerDataFactory;
import org.jblux.client.network.ResponseWaiter;
import org.jblux.client.network.ServerCommunicator;
import org.jblux.common.Commands;
import org.jblux.common.client.PlayerData;
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

public class MainMenuState extends BasicGameState {
    private Image background;
    private Image loginButton;
    private TextField txtUsername;
    
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
        background = new Image("img/menu.png");
        loginButton = new Image("img/login.png");

        UnicodeFont font = new UnicodeFont(new Font("Serif", Font.PLAIN, 12));
        font.getEffects().add(new ColorEffect(Color.GREEN));
        font.addAsciiGlyphs();
        font.loadGlyphs();
        
        txtUsername = new TextField(gc, font, 333, 225, 150, 25);
        txtUsername.setCursorVisible(true);
        txtUsername.setText("Testing");
    }
    
    @Override
    public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
        background.draw(0, 0);
        loginButton.draw(375, 300, startGameScale);
        txtUsername.render(gc, g);
    }
 
    @Override
    public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
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
            if(input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON))
                sbg.enterState(JBlux.GAMEPLAYSTATE);
        }
    }
}
