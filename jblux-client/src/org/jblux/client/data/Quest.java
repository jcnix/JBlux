/**
 * File: Quest.java
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

package org.jblux.client.data;

public class Quest {    
    public Quest() {
    }

    public int id;
    public String name;
    public String details;
    public String objectives;
    public String completion_text;
    public int end_npc_id;
    public int min_level;
    public int type;
    public int quest_item_id;
    
    public int reward_xp;
    public int reward_money;
    public int reward_item1_id;
    public int reward_item2_id;
    public int reward_item1_count;
    public int reward_item2_count;

    public int required_item1_id;
    public int required_item2_id;
    public int required_item3_id;
    public int required_item1_count;
    public int required_item2_count;
    public int required_item3_count;

    public int required_npc1_id;
    public int required_npc2_id;
    public int required_npc3_id;
    public int required_npc1_count;
    public int required_npc2_count;
    public int required_npc3_count;

    public int current_item1_count;
    public int current_item2_count;
    public int current_item3_count;
    public int current_npc1_count;
    public int current_npc2_count;
    public int current_npc3_count;
}
