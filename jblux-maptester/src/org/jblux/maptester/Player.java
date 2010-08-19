/**
 * File: Player.java
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

package org.jblux.maptester;

import java.util.Calendar;
import org.jblux.maptester.gui.GameCanvas;
import org.jblux.common.client.PlayerData;
import org.jblux.util.Coordinates;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

public class Player {
    private int move_size;
    private Coordinates coords;
    private int width;
    private int height;
    private Image image;
    private GameCanvas canvas;

    private Calendar cal;
    private long lastMove;

    public Player(int x, int y) {
        width = 32;
        height = 32;
        coords = new Coordinates(x, y);
        canvas = GameCanvas.getInstance();
        move_size = 7;

        cal = Calendar.getInstance();
        lastMove = cal.getTimeInMillis();
        try {
            image = new Image("player.png");
        } catch (SlickException ex) {
        }
    }

    public Coordinates getCoords() {
        return coords;
    }

    public void update(GameContainer gc) {
        Input input = gc.getInput();

        if(gc.hasFocus()) {
            //Can only move once every 100ms
            if(can_perform_action(100)) {
                if(input.isKeyDown(Input.KEY_LEFT) || input.isKeyDown(Input.KEY_A)) {
                    move(-move_size, 0);
                }
                if(input.isKeyDown(Input.KEY_RIGHT) || input.isKeyDown(Input.KEY_D)) {
                    move(move_size, 0);
                }
                if(input.isKeyDown(Input.KEY_UP) || input.isKeyDown(Input.KEY_W)) {
                    move(0, -move_size);
                }
                if(input.isKeyDown(Input.KEY_DOWN) || input.isKeyDown(Input.KEY_S)) {
                    move(0, move_size);
                }
                if(input.isKeyDown(Input.KEY_F1)) {
                    canvas.toggle_developer_mode();
                }
            }
        }
    }

    /**
     *
     * @param delay_time    Can perform action once every delay_time milliseconds
     * @return              Can the action be performed?
     */
    public boolean can_perform_action(int delay_time) {
        cal = Calendar.getInstance();
        long time = cal.getTimeInMillis();
        long diff_time = time - lastMove;

        if(diff_time < delay_time) {
            return false;
        }
        else {
            lastMove = time;
            return true;
        }
    }

    /**
     * parameters are deltas
     */
    private void move(int dx, int dy) {
        //Check to see if we need to change maps
        Coordinates end = new Coordinates();
        end.x = coords.x + dx;
        end.y = coords.y + dy;

        boolean negative = (dx < 0 || dy < 0);
        int adx = Math.abs(dx);
        int ady = Math.abs(dy);

        while(adx > 0 || ady > 0) {
            int x = adx;
            int y = ady;
            if(negative) {
                x = -adx;
                y = -ady;
            }

            Coordinates c = new Coordinates(coords.x + x, coords.y + y);
            boolean walkable = canvas.is_walkable(c);
            if(!walkable) {
                return;
            }
            
            if(adx > 0)
                adx--;
            if(ady > 0)
                ady--;
        }

        coords.x += dx;
        coords.y += dy;
    }

    public void draw() {
        //Player must be drawn in the center of the screen
        int x = 400 - width/2;
        int y = 300 - (height);
        image.draw(x, y);
    }
}
