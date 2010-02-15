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
import com.jblux.util.ChatMessage;
import java.util.Observable;
import java.util.Observer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.gui.GUIContext;
import org.newdawn.slick.gui.TextField;

public class ChatBox extends TextField implements Observer {
    private GUIContext gc;
    private ChatBoxObserver cbObserver;

    public ChatBox(GUIContext gc, UnicodeFont uf) {
        super(gc, uf, 0, 0, 300, 200);

        cbObserver = ChatBoxObserver.getInstance();
        cbObserver.addObserver(this);
        this.gc = gc;
        setAcceptingInput(false);
    }

    public void render(Graphics g) {
        super.render(gc, g);
    }

    public void update(Observable o, Object obj) {
        if(obj instanceof ChatMessage) {
            String text = getText();
            ChatMessage cm = (ChatMessage) obj;
            text += cm.getChatBoxString();

            setText(text);
        }
    }
}
