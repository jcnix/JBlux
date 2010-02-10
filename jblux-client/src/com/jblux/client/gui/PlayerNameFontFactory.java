/**
 * File: PlayerNameFont.java
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
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;

public class PlayerNameFontFactory {
    private static PlayerNameFontFactory pnff;
    private UnicodeFont font;

    protected PlayerNameFontFactory() {
        font = new UnicodeFont(new Font("Serif", Font.PLAIN, 12));

        try {
            font.getEffects().add(new ColorEffect(Color.BLACK));
            font.addAsciiGlyphs();
            font.loadGlyphs();
        } catch(SlickException ex) {
            ex.printStackTrace();
        }
    }

    public static PlayerNameFontFactory getInstance() {
        if(pnff == null) {
            pnff = new PlayerNameFontFactory();
        }

        return pnff;
    }

    public UnicodeFont getFont() {
        return font;
    }
}
