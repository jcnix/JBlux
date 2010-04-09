/**
 * File: DBManager.java
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

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBManager {
    private Connection m_conn;
    private String m_server;
    private String m_user;
    private String m_pass;

    public DBManager(String server, String user, String pass) {
        m_server = server;
        m_user = user;
        m_pass = pass;
    }

    public boolean connect() {
        try {
            Class.forName("org.postgresql.Driver");
        } catch(ClassNotFoundException ex) {
            return false;
        }

        try {
            String connString = String.format("jdbc:postgresql://%s/tmuo", m_server);
            m_conn = DriverManager.getConnection(connString, m_user, m_pass);
        } catch(SQLException ex) {
            return false;
        }

        return true;
    }
}
