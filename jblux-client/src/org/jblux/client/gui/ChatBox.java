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

package org.jblux.client.gui;

import java.util.Observable;
import java.util.Observer;
import org.jblux.client.gui.observers.ChatBoxObserver;
import org.jblux.util.ChatMessage;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.gui.GUIContext;
import org.newdawn.slick.gui.TextField;

public class ChatBox extends TextField implements Observer {
    private GUIContext gc;
    private ChatBoxObserver cbObserver;
    private final int MAX_LINES = 10;

    public ChatBox(GUIContext gc, UnicodeFont uf) {
        super(gc, uf, 0, 0, 300, 200);

        cbObserver = ChatBoxObserver.getInstance();
        cbObserver.addObserver(this);
        this.gc = gc;
        setAcceptingInput(false);

        String blankLines = "";
        for(int i = 0; i < MAX_LINES; i++) {
            //We need this space so something is left after splitting
            blankLines += " \n";
        }
        setText(blankLines);
    }

    public void render(Graphics g) {
        super.render(gc, g);
    }

    public void update(Observable o, Object obj) {
        if(obj instanceof ChatMessage) {
            String text = getText();
            String[] lines = text.split("\\n");

            //Get rid of the first line, keep the remaining.
            //Really basic scrolling.
            String newText = "";
            for(int i = 1; i < MAX_LINES; i++) {
                newText += lines[i] + "\n";
            }

            ChatMessage cm = (ChatMessage) obj;
            newText += cm.getChatBoxString();

            setText(newText);
        }
    }
}
