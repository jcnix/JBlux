/**
 * File: QuestTable.java
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

package org.jblux.sql;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import org.jblux.common.client.Quest;

public class QuestTable {
    private DBManager m_db;
    private static final String QUEST_TABLE = "jblux_quest";

    public QuestTable() {
        m_db = new DBManager();
    }
    
    public ArrayList<Quest> getQuestsForNpc(int npc_id) {
        ArrayList<Quest> quests = new ArrayList<Quest>();
        m_db.connect();

        try {
            String q = String.format("SELECT * FROM %s WHERE npc_id=%d",
                    QUEST_TABLE, npc_id);
            ResultSet rs = m_db.query_select(q);
            while(rs.next()) {
                Quest quest = new Quest();
                quest.id = rs.getInt("id");
                quest.name = rs.getString("name");
                quest.details = rs.getString("details");
                quest.objectives = rs.getString("objectives");
                quest.completion_text = rs.getString("completion_text");
                quest.min_level = rs.getInt("min_level");
                quest.type = rs.getInt("flag");
                quest.quest_item_id = rs.getInt("quest_item_id");

                quest.reward_xp = rs.getInt("reward_xp");
                quest.reward_money = rs.getInt("reward_money");
                quest.reward_item1_id = rs.getInt("rewardItem1_id");
                quest.reward_item2_id = rs.getInt("rewardItem2_id");
                quest.reward_item1_count = rs.getInt("rewardItem1_count");
                quest.reward_item2_count = rs.getInt("rewardItem2_count");

                quest.required_item1_id = rs.getInt("reqItem1_id");
                quest.required_item2_id = rs.getInt("reqItem2_id");
                quest.required_item3_id = rs.getInt("reqItem3_id");
                quest.required_item1_count = rs.getInt("reqItem1_count");
                quest.required_item2_count = rs.getInt("reqItem2_count");
                quest.required_item3_count = rs.getInt("reqItem3_count");
                
                quest.required_npc1_id = rs.getInt("reqNpc1_id");
                quest.required_npc2_id = rs.getInt("reqNpc2_id");
                quest.required_npc3_id = rs.getInt("reqNpc3_id");
                quest.required_npc1_count = rs.getInt("reqNpc1_count");
                quest.required_npc2_count = rs.getInt("reqNpc2_count");
                quest.required_npc3_count = rs.getInt("reqNpc3_count");

                quests.add(quest);
            }
        } catch(SQLException ex) {
        }

        m_db.close();
        return quests;
    }
}
