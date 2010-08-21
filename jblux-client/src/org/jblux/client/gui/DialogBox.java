/**
 * File: DialogBox.java
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

import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.gui.GUIContext;

public class DialogBox {
    private Image boxImage;
    private String text;
    private int text_lines;
    private UnicodeFont ufont;
    private GUI gui;
    private Image closeButton;

    public DialogBox(GUI gui) {
        this.gui = gui;
        text = "";
        text_lines = 0;
        
        try {
            boxImage = new Image("img/dialogbox.png");
            closeButton = new Image("img/closebutton.png");
        } catch(SlickException ex) {
        }

        PlayerNameFontFactory pnff = PlayerNameFontFactory.getInstance();
        ufont = pnff.getFont();
    }

    public void setText(String text) {
        this.text = text;

        int width = ufont.getWidth(text);
        text_lines = width / 300;
    }

    public void update(GUIContext gc) {
        Input input = gc.getInput();
        if(input.isMousePressed(Input.MOUSE_LEFT_BUTTON)) {
            int x = input.getMouseX();
            int y = input.getMouseY();

            if( (x >= 550 && x <= 550 + closeButton.getWidth()) &&
                (y >= 100 && y <= 100 + closeButton.getHeight()))
            {
                gui.closeDialogbox();
            }
        }
    }

    public void render() {
        boxImage.drawCentered(400, 300);
        closeButton.draw(550, 100);
        ufont.drawString(250, 125, text);
    }
}
