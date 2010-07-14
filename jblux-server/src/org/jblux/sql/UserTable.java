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
    public boolean authenticate(String username, String password) {
        boolean auth = false;
        m_db.connect();
        try {
            String query = String.format("SELECT username, password from %s where username='%s' "
                    + "and password='%s';", USER_TABLE, username, password);
            ResultSet rs = m_db.query_select(query);
            auth = rs.next();
        } catch(SQLException ex) {
            auth = false;
        }

        m_db.close();
        return auth;
    }
}
