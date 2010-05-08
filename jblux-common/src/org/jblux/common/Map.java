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

    /* Ids of adjacent maps for convenience */
    private short map_left;
    private short map_right;
    private short map_above;
    private short map_below;

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

    public void set_adjacent_maps(short left, short right, short above,
            short below) {
        map_left = left;
        map_right = right;
        map_above = above;
        map_below = below;
    }

    public short get_adjacent_map(Relation r) {
        short id = 0;

        if(r == Relation.LEFT)
            id = map_left;
        else if(r == Relation.RIGHT)
            id = map_right;
        else if(r == Relation.ABOVE)
            id = map_above;
        else if(r == Relation.BELOW)
            id = map_below;

        return id;
    }
}
