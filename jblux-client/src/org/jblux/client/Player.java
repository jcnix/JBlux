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

import org.jblux.util.MapGrid;
import java.util.Observable;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.Observer;
import org.jblux.client.gui.GameCanvas;
import org.jblux.client.network.ItemFactory;
import org.jblux.client.network.ResponseWaiter;
import org.jblux.client.network.ServerCommunicator;
import org.jblux.util.Relation;
import org.jblux.client.data.NpcData;
import org.jblux.client.data.NpcJob;
import org.jblux.client.data.PlayerData;
import org.jblux.client.data.Quest;
import org.jblux.client.items.Item;
import org.jblux.client.network.PlayerDataFactory;
import org.jblux.util.Coordinates;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Input;

public class Player extends Sprite implements Observer {
    private int switch_walk;  //Just means switch to other walk sprite
    private ServerCommunicator server;
    private int move_size;
    private String map_name;
    private PlayerData player_data;
    private ResponseWaiter response;

    private Calendar cal;
    private long lastMove;
    private boolean left_foot;
    private boolean wait_pressed_action;

    public Player(PlayerData data, ServerCommunicator server, GameCanvas gc) {
        super(data, gc);

        this.player_data = data;
        if(player_data.quests == null)
            player_data.quests = new LinkedList<Quest>();

        this.server = server;
        response = ResponseWaiter.getInstance();
        response.addObserver(this);
        
        setImage(FACE_DOWN, 0);
        move_size = 7;
        coords = data.coords;
        map_name = data.map;
        switch_walk = 0;
        left_foot = false;

        cal = Calendar.getInstance();
        lastMove = cal.getTimeInMillis();
    }

    public PlayerData getData() {
        return player_data;
    }
    
    public LinkedList<Quest> getQuests() {
        return player_data.quests;
    }
    
    public void setMapName(String map) {
        map_name = map;
    }

    public void update(GameContainer gc) {
        Input input = gc.getInput();
        boolean moved = false;

        if(gc.hasFocus()) {
            //Can only move once every 100ms
            if(can_perform_action(100)) {
                if(input.isKeyDown(Input.KEY_LEFT) || input.isKeyDown(Input.KEY_A)) {
                    image = spriteSheet.getSprite(Sprite.FACE_LEFT+switch_walk, 0);
                    moved = true;
                    move(-move_size, 0);
                }
                if(input.isKeyDown(Input.KEY_RIGHT) || input.isKeyDown(Input.KEY_D)) {
                    image = spriteSheet.getSprite(Sprite.FACE_RIGHT+switch_walk, 0);
                    moved = true;
                    move(move_size, 0);
                }
                if(input.isKeyDown(Input.KEY_UP) || input.isKeyDown(Input.KEY_W)) {
                    image = spriteSheet.getSprite(Sprite.FACE_UP+switch_walk, 0);
                    moved = true;
                    move(0, -move_size);
                }
                if(input.isKeyDown(Input.KEY_DOWN) || input.isKeyDown(Input.KEY_S)) {
                    image = spriteSheet.getSprite(Sprite.FACE_DOWN+switch_walk, 0);
                    moved = true;
                    move(0, move_size);
                }
                /* Animate player's walking */
                if(moved) {
                    if(switch_walk == 1) {
                        switch_walk = 0;
                    }
                    else if(switch_walk == -1) {
                        switch_walk = 0;
                    }
                    else if(left_foot)
                    {
                        left_foot = false;
                        switch_walk = 1;
                    }
                    else
                    {
                        left_foot = true;
                        switch_walk = -1;
                    }
                }

                //Action key
                if(input.isKeyDown(Input.KEY_SPACE)) {
                    //TODO: Check in front of the player
                    //Only checking below the player for now
                    wait_pressed_action = true;
                    Coordinates tile = MapGrid.getTile(coords);
                    server.pickup_item(tile);
                }
                if(input.isKeyDown(Input.KEY_L)) {
                    canvas.openQuestLog(this.player_data);
                }
                if(input.isKeyDown(Input.KEY_F1)) {
                    canvas.toggle_developer_mode();
                }
                if(input.isMousePressed(Input.MOUSE_RIGHT_BUTTON)) {
                    NpcData npc = canvas.isNpcAt(input.getMouseX(), input.getMouseY());
                    if(npc != null) {
                        if(npc.job == NpcJob.HOSTILE)
                        {
                            canvas.attackNpc(npc);
                        }
                        else {
                            canvas.talkToNpc(npc);
                        }
                    }
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
        changeMap(end);

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
        server.move(coords.x, coords.y);
    }

    /**
     * Since this is checked before moving the player and whether or not
     * they can move, we're going to give this function the location of
     * where the player would be assuming they are able to move.
     *
     * @param c     Where the player would be
     */
    public void changeMap(Coordinates c) {
        int x = canvas.getWalkArea().getWidth();
        int y = canvas.getWalkArea().getHeight();
        boolean change = false;
        Relation relation = null;

        if(c.x <= 0) {
            change = true;
            relation = Relation.LEFT;
        }
        else if(c.x >= x) {
            change = true;
            relation = Relation.RIGHT;
        }
        else if(c.y <= 0) {
            change = true;
            relation = Relation.TOP;
        }
        else if(c.y >= y) {
            change = true;
            relation = Relation.BOTTOM;
        }

        if(change) {
            canvas.getNewMap(relation, map_name);
        }
    }

    @Override
    public void draw() {
        //Player must be drawn in the center of the screen
        int x = 400 - width/2;
        int y = 300 - (height);
        image.draw(x, y);
        draw_name();
    }

    @Override
    public void draw_name() {
        int w = nameFont.getWidth(player_data.character_name);
        int h = nameFont.getHeight(player_data.character_name);
        int x = 400 - (w/2);
        int y = 300 - (height + h);
        nameFont.drawString(x, y, player_data.character_name);
    }
    
    public void update(Observable o, Object arg) {
        String sarg = (String) arg;
        String[] args = sarg.split("\\s");
        if(wait_pressed_action) {
            if(args[0].equals("item")) {
                if(!args[1].equals("null")) {
                    Item item = ItemFactory.getItemFromBase64(args[1]);
                }
            }
        }
        if(args[0].equals("player")) {
            if(args[1].equals("self")) {
                this.player_data = PlayerDataFactory.getDataFromBase64(args[2]);
                canvas = GameCanvas.getInstance(server);
                canvas.updateNpcs();
            }
            else if(args[2].equals("hp")) {
                int player_id = Integer.parseInt(args[1]);
                if(player_id == player_data.character_id) {
                    int hp = Integer.parseInt(args[3]);
                    player_data.hp = hp;
                }
            }
        }
    }
}
