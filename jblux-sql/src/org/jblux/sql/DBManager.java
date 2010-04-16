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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBManager {
    private Connection m_conn;
    private String m_server;
    private String m_user;
    private String m_pass;

    public DBManager() {
        m_server = Settings.getDbServer();
        m_user = Settings.getDbUser();
        m_pass = Settings.getDbPass();
    }

    public boolean connect() {
        try {
            Class.forName("org.postgresql.Driver");
        } catch(ClassNotFoundException ex) {
            System.out.println("Could not connect to DB");
            return false;
        }

        try {
            String connString = String.format("jdbc:postgresql://%s/tmuo", m_server);
            m_conn = DriverManager.getConnection(connString, m_user, m_pass);
        } catch(SQLException ex) {
            System.out.println("Could not connect to DB");
            return false;
        }
        
        return true;
    }

    public void close() {
        try {
            m_conn.close();
        } catch (SQLException ex) {
        }
    }

    public Connection getConnection() {
        return m_conn;
    }

    public ResultSet query_select(String query) {
        ResultSet rs = null;

        try {
            Statement stmt = m_conn.createStatement();
            rs = stmt.executeQuery(query);
        } catch (SQLException ex) {
        }

        return rs;
    }
}
