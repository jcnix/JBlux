/**
 * File: UserTable.java
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
import org.jblux.common.client.PlayerClass;
import org.jblux.common.client.PlayerData;
import org.jblux.common.client.Race;

public class UserTable {
    private DBManager m_db;
    private static final String USER_TABLE = "jblux_user";
    private static final String CHARACTER_TABLE="jblux_character";
    private static final String RACE_TABLE = "jblux_race";
    private static final String CLASS_TABLE = "jblux_class";

    public UserTable() {
        m_db = new DBManager();
    }

    /**
     * The password will already be a SHA-1 sum, so just compare to what is
     * in the database without any hashing.
     *
     * @param username  The user's account name
     * @param password  The user's password
     */
    public boolean authenticate(String username, String password, String character_name) {
        boolean auth = false;
        m_db.connect();
        String query;
        ResultSet rs;
        try {
            query = String.format("SELECT id, username, password FROM %s WHERE username='%s' "
                    + "and password='%s';", USER_TABLE, username, password);
            rs = m_db.query_select(query);
            auth = rs.next();
            int id = rs.getInt("id");

            //We're good so far
            if(auth) {
                query = String.format("SELECT user FROM %s WHERE name='%s' and user_id='%d';",
                        CHARACTER_TABLE, character_name, id);
                rs = m_db.query_select(query);
                auth = rs.next();
                //If we haven't SQLException'd, then we're good.
            }
        } catch(SQLException ex) {
            auth = false;
        }

        m_db.close();
        return auth;
    }

    public PlayerData getPlayer(String character_name) {
        PlayerData pdata = new PlayerData();
        m_db.connect();

        try {
            String query = String.format("SELECT * FROM %s WHERE name='%s'",
                    CHARACTER_TABLE, character_name);
            ResultSet rs = m_db.query_select(query);
            rs.next();
            
            pdata.user_id = rs.getInt("id");
            pdata.character_name = character_name;
            pdata.current_map = rs.getString("current_map");
            int race_id = rs.getInt("race_id");
            int class_id = rs.getInt("class_t_id");
            
            Race race = getRace(race_id);
            PlayerClass pclass = getClass(class_id);
            pdata.race = race;
            pdata.player_class = pclass;
        } catch(SQLException ex) {
        }
        
        m_db.close();
        return pdata;
    }

    public Race getRace(int id) {
        Race race = new Race();
        m_db.connect();

        try {
            String query = String.format("SELECT * FROM %s WHERE id='%d'", RACE_TABLE, id);
            ResultSet rs = m_db.query_select(query);
            rs.next();
            race.id = id;
            race.name = rs.getString("name");
            race.sprite_sheet = rs.getString("sprite_sheet");
        } catch(SQLException ex) {
        }

        m_db.close();
        return race;
    }

    public PlayerClass getClass(int id) {
        PlayerClass pclass = new PlayerClass();
        m_db.connect();

        try {
            String query = String.format("SELECT * FROM %s WHERE id='%d'", CLASS_TABLE, id);
            ResultSet rs = m_db.query_select(query);
            rs.next();
            pclass.id = id;
            pclass.name = rs.getString("name");
        } catch(SQLException ex) {
        }

        m_db.close();
        return pclass;
    }
}
