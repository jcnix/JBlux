/**
 * File: NpcTable.java
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
import org.jblux.common.client.NpcData;

public class NpcTable {
    private DBManager m_db;
    private static final String NPC_TABLE = "jblux_npc";

    public NpcTable() {
        m_db = new DBManager();
    }

    public NpcData getNpc(int id) {
        NpcData npc = new NpcData();
        m_db.connect();

        try {
            String q = String.format("SELECT * from %s WHERE id=%d;",
                    NPC_TABLE, id);
            ResultSet rs = m_db.query_select(q);
            rs.next();
            npc.npc_id = id;
            npc.job = rs.getInt("job");
            npc.sprite_sheet = rs.getString("sprite_sheet");
            
            UserTable ut = new UserTable();
            npc.race = ut.getRace(rs.getInt("race_id"));
            npc.player_class = ut.getClass(rs.getInt("class_t_id"));
        } catch(SQLException ex) {
            ex.printStackTrace();
        }

        m_db.close();
        return npc;
    }
}
