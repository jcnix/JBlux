/**
 * File: ChatBox.java
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

package com.jblux.client.gui;

import com.jblux.client.gui.observers.ChatBoxObserver;
import com.jblux.client.network.ServerCommunicator;
import com.jblux.util.ChatMessage;
import java.awt.Color;
import java.awt.Font;
import java.util.Observable;
import java.util.Observer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;
import org.newdawn.slick.gui.GUIContext;
import org.newdawn.slick.gui.TextField;

public class ChatBox implements Observer {
    private TextField chatBox;
    private TextField inputBox;
    private GUIContext gc;
    private ServerCommunicator server;
    private ChatBoxObserver cbObserver;

    public ChatBox(GUIContext gc, ServerCommunicator s) {
        server = s;
        cbObserver = ChatBoxObserver.getInstance();
        cbObserver.addObserver(this);

        try {
            this.gc = gc;
            UnicodeFont uf = new UnicodeFont(new Font("Serif", Font.BOLD, 16));
            uf.getEffects().add(new ColorEffect(Color.WHITE));
            uf.addAsciiGlyphs();
            uf.loadGlyphs();
            chatBox = new TextField(gc, uf, 0, 0, 300, 200);

            inputBox = new ChatInputBox(gc, uf, server, 0, 200, 300, 25);
            inputBox.setCursorVisible(true);
        } catch(SlickException ex) {
        }
    }

    public void render(Graphics g) {
        chatBox.render(gc, g);
        inputBox.render(gc, g);
    }

    public void update(Observable o, Object obj) {
        if(obj instanceof ChatMessage) {
            String text = chatBox.getText();
            ChatMessage cm = (ChatMessage) obj;
            text += cm.getMessage();

            chatBox.setText(text);
        }
    }
}
