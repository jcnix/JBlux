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

package org.jblux.server.maps;

import java.util.HashMap;
import org.jblux.common.Relation;
import org.jblux.common.client.NpcData;
import org.jblux.common.items.Item;
import org.jblux.util.Coordinates;

public class Map {
    private int m_id;
    private String m_name;
    private HashMap<Coordinates, Item> m_items;
    private HashMap<Coordinates, NpcData> m_npcs;

    /* Ids of adjacent maps for convenience */
    private int map_left;
    private int map_right;
    private int map_above;
    private int map_below;

    public Map(int id, String name) {
        m_id = id;
        m_name = name;
    }

    public int getID() {
        return m_id;
    }

    public String getName() {
        return m_name;
    }

    public HashMap<Coordinates, Item> getItems() {
        return m_items;
    }

    public Item getItemAt(Coordinates c) {
        return m_items.get(c);
    }

    public void setItems(HashMap<Coordinates, Item> items) {
        m_items = items;
    }

    public HashMap<Coordinates, NpcData> getNpcs() {
        return m_npcs;
    }

    public NpcData getNpcAt(Coordinates c) {
        return m_npcs.get(c);
    }

    public void setNpcs(HashMap<Coordinates, NpcData> npcs) {
        m_npcs = npcs;
    }

    public void set_adjacent_maps(short left, short right, short above,
            short below) {
        map_left = left;
        map_right = right;
        map_above = above;
        map_below = below;
    }

    public int get_adjacent_map(Relation r) {
        int id = 0;

        if(r == Relation.LEFT)
            id = map_left;
        else if(r == Relation.RIGHT)
            id = map_right;
        else if(r == Relation.TOP)
            id = map_above;
        else if(r == Relation.BOTTOM)
            id = map_below;

        return id;
    }
}
