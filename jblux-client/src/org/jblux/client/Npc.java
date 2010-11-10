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
    private boolean available_quests;
    private Image available_quest_icon;
    private PlayerData player;

    public Npc(NpcData data, GameCanvas gc, PlayerData player) {
        super(data, gc);
        this.data = data;
        this.player = player;
        
        Relation r = RelationUtil.upDownRelation(data.direction);
        this.faceDirection(r);

        available_quests = false;
        try {
            available_quest_icon = new Image("img/star.gif");
        } catch (SlickException ex) {
        }

        for(int i = 0; i < data.quests.size(); i++) {
            Quest q = data.quests.get(i);
            if(q.min_level <= player.level) {
                available_quests = true;
                break;
            }
        }

        check_quests(player);
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

    /**
     * Check if the player has any quests to turn in here
     */
    public void check_quests(PlayerData p) {
        for(int i = 0; i < p.quests.size(); i++) {
            Quest q = p.quests.get(i);
            if(q.complete == 1 && q.end_npc_id == data.npc_id) {
                available_quests = true;
                break;
            }
        }
    }

    public NpcData getData() {
        return data;
    }

    public void update() {
    }
}
