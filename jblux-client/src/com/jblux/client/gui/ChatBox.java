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

import java.awt.Color;
import java.awt.Font;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;
import org.newdawn.slick.gui.GUIContext;
import org.newdawn.slick.gui.TextField;

public class ChatBox {
    private TextField chatBox;
    private TextField inputBox;
    private GUIContext gc;

    public ChatBox(GUIContext gc) {
        try {
            this.gc = gc;
            UnicodeFont uf = new UnicodeFont(new Font("Serif", Font.PLAIN, 12));
            uf.getEffects().add(new ColorEffect(Color.BLACK));
            uf.addAsciiGlyphs();
            uf.loadGlyphs();
            chatBox = new TextField(gc, uf, 0, 0, 300, 200);
        } catch(SlickException ex) {
        }
    }

    public void render(Graphics g) {
        chatBox.render(gc, g);
    }
}
