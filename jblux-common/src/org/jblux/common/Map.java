/**
 * File: Map.java
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

package org.jblux.common;

import java.util.Vector;
import org.jblux.common.items.Item;

public class Map {
    private short m_id;
    private String m_name;
    private Vector<Item> m_items;

    public Map(short id, String name, Vector<Item> items) {
        m_id = id;
        m_name = name;
        m_items = items;
    }

    public short getID() {
        return m_id;
    }

    public String getName() {
        return m_name;
    }

    public Vector<Item> getItems() {
        return m_items;
    }
}
