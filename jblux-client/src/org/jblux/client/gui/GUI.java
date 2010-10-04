/**
 * File: GUI.java
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

import org.jblux.client.network.ServerCommunicator;
import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.LinkedList;
import org.jblux.client.data.Quest;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;
import org.newdawn.slick.gui.GUIContext;

public class GUI {
    private ChatBox cb;
    private ChatInputBox inputBox;
    private QuestDialogBox dialog_box;
    private GUIContext gc;

    public GUI(GUIContext gc, ServerCommunicator s) {
        this.gc = gc;

        try {
            UnicodeFont uf = new UnicodeFont(new Font("Serif", Font.BOLD, 16));
            uf.getEffects().add(new ColorEffect(Color.WHITE));
            uf.addAsciiGlyphs();
            uf.loadGlyphs();

            cb = new ChatBox(gc, uf);
            inputBox = new ChatInputBox(gc, uf, s);
            inputBox.setCursorVisible(true);
        } catch(SlickException ex) {
        }
    }

    public void openQuestDialogBox(LinkedList<Quest> quests) {
        dialog_box = new QuestDialogBox(this);
        dialog_box.setQuests(quests);
    }

    public void closeDialogbox() {
        dialog_box = null;
    }

    public void update() {
        if(dialog_box != null) {
            dialog_box.update(gc);
        }
    }
    
    public void render(Graphics g) {
        cb.render(g);
        inputBox.render(g);

        if(dialog_box != null) {
            dialog_box.render();
        }
    }
}
