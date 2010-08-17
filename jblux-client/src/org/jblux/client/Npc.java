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

import org.jblux.common.Relation;
import org.jblux.common.RelationUtil;
import org.jblux.common.client.NpcData;
import org.jblux.common.client.PlayerData;
import org.jblux.common.client.Quest;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class Npc extends Sprite {
    private NpcData data;

    private boolean available_quests;
    private Image available_quest_icon;

    public Npc(NpcData data) {
        super(data);
        this.data = data;
        
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
            available_quest_icon.draw(0, 0);
        }
    }

    /**
     * Don't draw names for Npcs
     */
    @Override
    public void draw_name() {
        return;
    }

    public void update(PlayerData player) {
        for(int i = 0; i < data.quests.size(); i++) {
            Quest q = data.quests.get(i);
            if(q.min_level <= player.level) {
                available_quests = true;
                break;
            }
        }
    }
}
