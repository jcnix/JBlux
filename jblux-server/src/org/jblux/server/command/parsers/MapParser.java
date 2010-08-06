/**
 * File: MapParser.java
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

package org.jblux.server.command.parsers;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jblux.common.Commands;
import org.jblux.common.Relation;
import org.jblux.common.RelationUtil;
import org.jblux.common.items.Item;
import org.jblux.server.ClientThread;
import org.jblux.server.maps.Map;
import org.jblux.server.maps.Maps;
import org.jblux.sql.MapSqlTable;
import org.jblux.util.Base64;
import org.jblux.util.Coordinates;

public class MapParser implements CommandParser {
    public MapParser() {
    }

    public void parse(String[] command, ClientThread client) {
        if(command[1].equals("goto")) {
            Relation r = RelationUtil.fromString(command[2]);
            String name = command[3];
            Maps maps = Maps.getInstance();
            int id = maps.getID(name);
            Map m = maps.getAdjacentMap(r, id);

            String cmd = "";
            if(m != null) {
                MapSqlTable mst = new MapSqlTable();
                String map_name = m.getName();
                Relation map_side = RelationUtil.getOpposite(r);
                Coordinates crd = mst.getEntrance(m.getID(), map_side);
                client.coords = crd;

                String npcs_enc = "";
                try {
                    npcs_enc = Base64.encodeObject(m.getNpcs());
                } catch (IOException ex) {
                }

                cmd = String.format("%s goto %s %s npcs %s",
                        Commands.MAP, map_name, crd, npcs_enc);
                client.go_to_map(m.getID(), crd);
            }
            else {
                cmd = String.format("%s stay", Commands.MAP);
            }
            
            //Respond to client
            client.writeString(cmd);
        }
        else if(command[1].equals(Commands.PICKUP)) {
            Coordinates c = new Coordinates();
            c.x = Integer.parseInt(command[2]);
            c.y = Integer.parseInt(command[3]);

            Maps maps = Maps.getInstance();
            int id = client.getMap();
            Map m = maps.getMap(id);
            Item item = m.getItemAt(c);

            String enc_item = "";
            if(item == null) {
                enc_item = "null";
            } else {
                client.add_to_inventory(item);
                try {
                    enc_item = Base64.encodeObject(item);
                } catch (IOException ex) {
                }
            }
            
            String cmd = String.format("%s %s", Commands.ITEM, enc_item);
            client.writeString(cmd);
        }
    }
}
