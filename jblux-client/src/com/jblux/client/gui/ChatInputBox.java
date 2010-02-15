/**
 * File: InputBox.java
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

import com.jblux.client.network.ServerCommunicator;
import org.newdawn.slick.Font;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.gui.GUIContext;
import org.newdawn.slick.gui.TextField;

public class ChatInputBox extends TextField {
    private ServerCommunicator server;
    private GUIContext gc;

    public ChatInputBox(GUIContext gc, Font font, ServerCommunicator server) {
        super(gc, font, 0, 200, 300, 25);

        this.gc = gc;
        input.disableKeyRepeat();
        setMaxLength(140);
        this.server = server;
    }

    @Override
    public void keyPressed(int key, char c) {
        if(hasFocus() && key == Input.KEY_ENTER) {
            System.out.println("Sending message...");
            server.sendChat(getText());
            setText("");
        }
        else {
            super.keyPressed(key, c);
        }
    }

    public void render(Graphics g) {
        super.render(gc, g);
    }
}
