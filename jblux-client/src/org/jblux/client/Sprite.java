/**
 * File: Sprite.java
 *
 * @author: Casey Jones
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

package org.jblux.client;

import org.jblux.client.gui.GameCanvas;
import org.jblux.client.gui.PlayerNameFontFactory;
import org.jblux.common.Relation;
import org.jblux.util.Coordinates;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.UnicodeFont;

public class Sprite {
    public final static int FACE_UP = 1;
    public final static int FACE_DOWN = 4;
    public final static int FACE_LEFT = 7;
    public final static int FACE_RIGHT = 10;

    /* This is where the feet are
     * Not the top left corner where it's rendered.
     * x is in the middle of the very bottom */
    protected Coordinates coords;

    protected int width;
    protected int height;
    
    protected SpriteSheet spriteSheet;
    protected Image image;
    protected String name;
    protected PlayerNameFontFactory pnff;
    protected UnicodeFont nameFont;
    protected GameCanvas canvas;

    public Sprite(String sheet) {
        width = 32;
        height = 39;
        name = "";
        coords = new Coordinates();
        canvas = GameCanvas.getInstance();

        try {
            pnff = PlayerNameFontFactory.getInstance();
            nameFont = pnff.getFont();
            spriteSheet = new SpriteSheet(sheet, width, height);
        } catch (SlickException ex) {
        }
    }

    public void draw() {
        Coordinates c = canvas.getMapCoords();
        c.x += coords.x;
        c.y += coords.y;
        image.draw(c.x - width/2, c.y - height);
        draw_name();
    }

    public void draw_name() {
        float x = coords.x - width/2;
        float y = coords.y - (height + 9);
        nameFont.drawString(x, y, name);
    }

    public void setImage(int x, int y) {
        image = spriteSheet.getSprite(x, y);
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public String getName() {
        return name;
    }

    public void setCoords(int x, int y) {
        setCoords(new Coordinates(x, y));
    }

    public void setCoords(Coordinates c) {
        coords = c;
    }

    public Coordinates getCoords() {
        return coords;
    }

    public void faceDirection(Relation r) {
        //x and y are coords on the sprite sheet
        int y = 0;
        int x = 0;
        
        switch(r) {
            case TOP:
                x = FACE_UP;
                break;
            case BOTTOM:
                x = FACE_DOWN;
                break;
            case LEFT:
                x = FACE_LEFT;
                break;
            case RIGHT:
                x = FACE_RIGHT;
                break;
        }

        setImage(x, y);
    }
}
