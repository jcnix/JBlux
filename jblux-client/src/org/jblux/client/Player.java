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

import org.jblux.common.MapGrid;
import java.util.Observable;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Observer;
import org.jblux.client.gui.GameCanvas;
import org.jblux.client.network.ItemFactory;
import org.jblux.client.network.NpcDataFactory;
import org.jblux.client.network.ResponseWaiter;
import org.jblux.client.network.ServerCommunicator;
import org.jblux.common.Relation;
import org.jblux.common.client.NpcData;
import org.jblux.common.client.PlayerData;
import org.jblux.common.items.Item;
import org.jblux.util.Coordinates;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Input;

public class Player extends Sprite implements Observer {
    private boolean switch_walk;  //Just means switch to other walk sprite
    private ServerCommunicator server;
    private int move_size;
    private String map_name;
    private PlayerData player_data;
    private HashMap<Coordinates, NpcData> npcs;
    private ResponseWaiter response;
    private GameCanvas canvas;
    private Coordinates bwcoords;

    private Calendar cal;
    private long lastMove;
    private boolean execute_change;

    private boolean wait_new_map;
    private boolean wait_pressed_action;

    public Player(PlayerData data, ServerCommunicator server) {
        super(data.race.sprite_sheet);

        this.player_data = data;
        this.server = server;
        setName(data.character_name);

        npcs = new HashMap<Coordinates, NpcData>();
        image = spriteSheet.getSubImage(FACE_DOWN, 0);
        move_size = 7;
        bwcoords = data.coords.clone();
        coords = data.coords;
        map_name = data.map;
        switch_walk = false;
        canvas = GameCanvas.getInstance();

        cal = Calendar.getInstance();
        lastMove = cal.getTimeInMillis();
        server.connect_player(data.character_name, coords);
    }

    public void update(GameContainer gc) {
        Input input = gc.getInput();

        if(gc.hasFocus()) {
            //Can only move once every 100ms
            if(can_perform_action(100)) {
                if(input.isKeyDown(Input.KEY_LEFT) || input.isKeyDown(Input.KEY_A)) {
                    if(coords.x > 0) {
                        if(switch_walk)
                            image = spriteSheet.getSprite(Sprite.FACE_LEFT-1, 0);
                        else
                            image = spriteSheet.getSprite(Sprite.FACE_LEFT+1, 0);

                        switch_walk = !switch_walk;
                        move(-move_size, 0);
                    }
                }
                if(input.isKeyDown(Input.KEY_RIGHT) || input.isKeyDown(Input.KEY_D)) {
                    if(coords.x < 800 - 32) {
                        if(switch_walk)
                            image = spriteSheet.getSprite(Sprite.FACE_RIGHT-1, 0);
                        else
                            image = spriteSheet.getSprite(Sprite.FACE_RIGHT+1, 0);

                        switch_walk = !switch_walk;
                        move(move_size, 0);
                    }
                }
                if(input.isKeyDown(Input.KEY_UP) || input.isKeyDown(Input.KEY_W)) {
                    if(coords.y > 0) {
                        if(switch_walk)
                            image = spriteSheet.getSprite(Sprite.FACE_UP-1, 0);
                        else
                            image = spriteSheet.getSprite(Sprite.FACE_UP+1, 0);

                        switch_walk = !switch_walk;
                        move(0, -move_size);
                    }
                }
                if(input.isKeyDown(Input.KEY_DOWN) || input.isKeyDown(Input.KEY_S)) {
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
            //Action key
            if(input.isKeyDown(Input.KEY_SPACE)) {
                //TODO: Check in front of the player
                //Only checking below the player for now
                if(can_perform_action(500)) {
                    wait_pressed_action = true;
                    response = new ResponseWaiter();
                    Coordinates tile = MapGrid.getTile(coords);
                    server.pickup_item(tile, response);
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
        bwcoords.x += dx;
        bwcoords.y += dy;
        Coordinates c = new Coordinates();
        c.x = bwcoords.x + (width/2);
        c.y = bwcoords.y + (height - 9);

        coords.x += dx;
        coords.y += dy;

        boolean walkable = canvas.is_walkable(c);
        //Check to see if we need to change maps
        changeMap();

        if(!walkable) {
            //move back
            coords.x -= dx;
            coords.y -= dy;
            bwcoords.x -= dx;
            bwcoords.y -= dy;
        }
        else {
            server.move(coords.x, coords.y);
        }
    }

    public void changeMap() {
        int x = canvas.getWalkArea().getWidth();
        int y = canvas.getWalkArea().getHeight();
        boolean change = false;
        Relation relation = null;

        if(coords.x <= 0) {
            change = true;
            relation = Relation.LEFT;
        }
        else if(coords.x >= x) {
            change = true;
            relation = Relation.RIGHT;
        }
        else if(coords.y <=0) {
            change = true;
            relation = Relation.TOP;
        }
        else if(coords.y >= y) {
            change = true;
            relation = Relation.BOTTOM;
        }

        if(change) {
            wait_new_map = true;
            response = new ResponseWaiter();
            response.addObserver(this);
            server.goto_map(response, relation, map_name);
        }

        if(execute_change) {
            execute_change = false;
            GameCanvas gc = GameCanvas.getInstance();
            gc.setMap(map_name, coords);
            gc.setNpcs(npcs);
        }
    }

    @Override
    public void draw() {
        //Player must be drawn in the center of the screen
        // 400 and 300 need to be adjusted to account for the height of the sprite
        int x = 400 - width/2;
        int y = 300 - (height - 9);
        image.draw(400, 300);
        draw_name();
    }

    @Override
    public void draw_name() {
        int x = 380;
        int y = 280;
        nameFont.drawString(x, y, name);
    }

    public void update(Observable o, Object arg) {
        if(response == o && wait_new_map) {
            server.rm_observable(o);
            String sarg = (String) arg;
            String[] args = sarg.split("\\s");
            map_name = args[0];
            Coordinates c = new Coordinates();
            c.x = Integer.parseInt(args[1]);
            c.y = Integer.parseInt(args[2]);
            setCoords(c);
            npcs = NpcDataFactory.getHashMapFromBase64(args[3]);
            execute_change = true;
        }
        if(response == o && wait_pressed_action) {
            server.rm_observable(o);
            String sarg = (String) arg;
            String[] args = sarg.split("\\s");
            if(!args[1].equals("null")) {
                Item item = ItemFactory.getItemFromBase64(args[1]);
            }
        }
    }
}
