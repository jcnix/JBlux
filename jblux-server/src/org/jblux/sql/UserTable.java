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

public class UserTable {
    private DBManager m_db;
    private static final String USER_TABLE = "jblux_user";
    private static final String CHARACTER_TABLE="jblux_character";

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
            ex.printStackTrace();
            auth = false;
        }

        m_db.close();
        return auth;
    }
}
