/**
 * File: Npc.java
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

package org.jblux.client;

import java.util.LinkedList;
import org.jblux.client.gui.GameCanvas;
import org.jblux.util.Relation;
import org.jblux.util.RelationUtil;
import org.jblux.client.data.NpcData;
import org.jblux.client.data.PlayerData;
import org.jblux.client.data.Quest;
import org.jblux.util.Coordinates;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class Npc extends Sprite {
    private NpcData data;
    private LinkedList<Quest> player_quests;

    private boolean available_quests;
    private Image available_quest_icon;

    public Npc(NpcData data, GameCanvas gc, LinkedList<Quest> player_quests) {
        super(data, gc);
        this.data = data;
        this.player_quests = player_quests;
        
        Relation r = RelationUtil.upDownRelation(data.direction);
        this.faceDirection(r);

        available_quests = false;
        try {
            available_quest_icon = new Image("img/star.gif");
        } catch (SlickException ex) {
        }
    }

    @Override
    public void draw() {
        super.draw();
        
        if(available_quests) {
            Coordinates c = canvas.getMapCoords();
            c.x += coords.x - available_quest_icon.getWidth()/2;
            c.y += coords.y - (height + available_quest_icon.getHeight() + 3);
            available_quest_icon.draw(c.x, c.y);
        }
    }

    /**
     * Don't draw names for Npcs
     */
    @Override
    public void draw_name() {
        return;
    }

    public NpcData getData() {
        return data;
    }

    public void update(PlayerData player) {
        for(int i = 0; i < data.quests.size(); i++) {
            Quest q = data.quests.get(i);
            if(q.min_level <= player.level) {
                available_quests = true;
                break;
            }
        }
        //Check if the player has any quests to turn in here
        for(int i = 0; i < player_quests.size(); i++) {
            Quest q = player_quests.get(i);
            if(q.end_npc_id == data.npc_id) {
                available_quests = true;
                break;
            }
        }
    }
}
