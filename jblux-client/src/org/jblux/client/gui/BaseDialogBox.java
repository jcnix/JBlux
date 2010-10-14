/**
 * File: BaseDialogBox.java
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

import java.util.ArrayList;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.gui.GUIContext;

public abstract class BaseDialogBox {
    protected int width;
    protected int height;

    public BaseDialogBox() {
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    protected ArrayList<String> getLines(String text, UnicodeFont ufont) {
        int num_lines = (ufont.getWidth(text) / width) + 1;
        ArrayList<String> lines = new ArrayList<String>();

        int prev_index = 0;
        int curr_index = 0;
        int line_length = text.length() / num_lines;
        for(int i = 1; i <= num_lines; i++) {
            curr_index = line_length * i;
            char curr_char = text.charAt(curr_index);

            /* if the string is in the middle of a word attempt to check the preceding
             * 15 characters to find a word break
             */
            if(curr_char != ' ') {
                for(int j = 1; j < 15; j++) {
                    char c = text.charAt(curr_index - i);
                    if(curr_char == ' ') {
                        curr_index = curr_index - i;
                        break;
                    }
                }
            }

            /* curr_index - 1 is because this method finds the ' ', we want
             * the character after the space.
             */
            String sub = text.substring(prev_index, curr_index-1);
            prev_index = curr_index+1;
            lines.add(sub);
        }

        if(curr_index != text.length()) {
            lines.add(text.substring(curr_index));
        }

        return lines;
    }

    public abstract void update(GUIContext gc);
    public abstract void render();
}
