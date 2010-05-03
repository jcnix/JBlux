/**
 * File: GameWorld.java
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

package org.jblux.server;

import java.util.Vector;
import org.jblux.common.Map;
import org.jblux.common.items.Item;
import org.jblux.sql.ItemSqlTable;
import org.jblux.util.Coordinates;

public class GameWorld extends Thread {
    private Vector<Map> m_maps;

    public GameWorld() {
        m_maps = new Vector<Map>();
        //Fill Vector with maps
    }

    @Override
    public void run() {
        drop_item(null, "residential");
    }

    /* TODO: this probably isn't something we need
     * other than to test some stuff */
    public void drop_item(Item item, String map) {
        ItemSqlTable items = new ItemSqlTable();
        item = items.getItem("test");
        System.out.printf("item id: %d\n", item.m_id);
        
        Coordinates coords = new Coordinates();
        coords.x = 400;
        coords.y = 400;

        String command = String.format("put item %s at %s",
            item.m_name, coords);
        Clients clients = Clients.getInstance();
        clients.tell_all_clients_on_map(map, command, item);
    }
}
