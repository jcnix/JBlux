/**
 * File: CharacterBar.java
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

import org.jblux.client.data.PlayerData;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;

public class CharacterBar {
    private PlayerData player;
    private Image barImg;
    private UnicodeFont font;

    private int name_size;

    public CharacterBar() {
        try {
            barImg = new Image("img/charbar.png");
        } catch (SlickException ex) {
        }

        font = FontFactory.getDefaultFont();
    }

    public void setPlayer(PlayerData player) {
        this.player = player;
        name_size = font.getWidth(player.character_name);
    }

    public void render() {
        barImg.draw(0, 575);
        if(player != null) {
            font.drawString(5, 580, player.character_name);
            font.drawString(50, 580, "HP: " + String.valueOf(player.hp) + " / " +
                    String.valueOf(player.max_hp));
        }
    }
}
