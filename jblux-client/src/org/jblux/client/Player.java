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

package org.jblux.client;

import java.util.Calendar;
import org.jblux.client.gui.GameCanvas;
import org.jblux.client.network.ServerCommunicator;
import org.jblux.common.Relation;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

public class Player extends Sprite {
    private boolean switch_walk;  //Just means switch to other walk sprite
    private ServerCommunicator server;
    private Image walk_area;
    private int move_size;
    private String map_name;

    private Calendar cal;
    private long lastMove;

    public Player(String username, ServerCommunicator server) {
        //TODO: Replace this when accounts are set up.
        super("img/koopa.png");

        this.server = server;
        setName(username);
        move_size = 7;
        image = spriteSheet.getSubImage(FACE_DOWN, 0);
        
        coords.x = 352;
        coords.y = 384;
        switch_walk = false;

        //TODO: get this from the server
        map_name = "residential";

        cal = Calendar.getInstance();
        lastMove = cal.getTimeInMillis();

        try {
            walk_area = new Image("maps/residential/residentialbw.png");
        } catch (SlickException ex) {
        }

        server.connect_player(username, coords);
    }

    public void update(GameContainer gc) {
        Input input = gc.getInput();

        if(gc.hasFocus() && can_move()) {
            if(input.isKeyDown(Input.KEY_LEFT)) {
                if(coords.x > 0) {
                    if(switch_walk)
                        image = spriteSheet.getSprite(Sprite.FACE_LEFT-1, 0);
                    else
                        image = spriteSheet.getSprite(Sprite.FACE_LEFT+1, 0);

                    switch_walk = !switch_walk;
                    move(-move_size, 0);
                }
            }
            if(input.isKeyDown(Input.KEY_RIGHT)) {
                if(coords.x < 800 - 32) {
                    if(switch_walk)
                        image = spriteSheet.getSprite(Sprite.FACE_RIGHT-1, 0);
                    else
                        image = spriteSheet.getSprite(Sprite.FACE_RIGHT+1, 0);

                    switch_walk = !switch_walk;
                    move(move_size, 0);
                }
            }
            if(input.isKeyDown(Input.KEY_UP)) {
                if(coords.y > 0) {
                    if(switch_walk)
                        image = spriteSheet.getSprite(Sprite.FACE_UP-1, 0);
                    else
                        image = spriteSheet.getSprite(Sprite.FACE_UP+1, 0);

                    switch_walk = !switch_walk;
                    move(0, -move_size);
                }
            }
            if(input.isKeyDown(Input.KEY_DOWN)) {
                if(coords.y < 600 - 32) {
                    if(switch_walk)
                        image = spriteSheet.getSprite(Sprite.FACE_DOWN-1, 0);
                    else
                        image = spriteSheet.getSprite(Sprite.FACE_DOWN+1, 0);

                    switch_walk = !switch_walk;
                    move(0, move_size);
                }
            }
        }
    }

    public boolean can_move() {
        cal = Calendar.getInstance();
        long time = cal.getTimeInMillis();
        long diff_time = time - lastMove;

        if(diff_time < 100) {
            return false;
        }
        else {
            lastMove = time;
            return true;
        }
    }

    //parameters are deltas
    public void move(int dx, int dy) {
        coords.x += dx;
        coords.y += dy;

        boolean blocked = false;
        Color c = walk_area.getColor(coords.x, coords.y);

        //Check to see if we need to change maps
        changeMap();

        if(c.getRed() == 0) {
            blocked = true;
        }

        if(blocked) {
            coords.x -= dx;
            coords.y -= dy;
        }
        else {
            server.move(coords.x, coords.y);
        }
    }

    public void changeMap() {
        int x = walk_area.getWidth();
        int y = walk_area.getHeight();
        boolean change = false;
        Relation relation = null;

        if(coords.x <= 0) {
            change = true;
            relation = Relation.LEFT_OF;
        }
        else if(coords.x >= x) {
            change = true;
            relation = Relation.RIGHT_OF;
        }
        else if(coords.y <=0) {
            change = true;
            relation = Relation.ABOVE;
        }
        else if(coords.y >= y) {
            change = true;
            relation = Relation.BELOW;
        }

        if(change) {
            //TODO: Don't hardcode this
            coords.x = 350;
            coords.y = 250;
            map_name = server.ask_for_map(relation, map_name);
            GameCanvas gc = GameCanvas.getInstance();
            gc.setMap(map_name);
            walk_area = gc.getMap().getWalkArea();
        }
    }
}
