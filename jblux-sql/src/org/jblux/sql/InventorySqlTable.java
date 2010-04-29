/**
 * File: InventorySqlTable.java
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
import java.util.Vector;
import org.jblux.common.items.Item;

public class InventorySqlTable {
    private DBManager m_db;
    private Connection m_conn;

    public InventorySqlTable() {
        m_db = new DBManager();
    }

    public Vector<Item> getInventory(String player) {
        m_db.connect();
        m_conn = m_db.getConnection();
        Vector<Item> inv = new Vector<Item>();

        m_db.close();

        return inv;
    }
}
