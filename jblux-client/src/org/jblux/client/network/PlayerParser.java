/**
 * File: PlayerParser.java
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

package org.jblux.client.network;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import org.jblux.common.client.PlayerData;
import org.jblux.util.Base64;

public class PlayerParser {
    public static void parse(String[] command, ServerListener server) {
        if(command[1].equals("self")) {
            try {
                byte[] bytes = Base64.decode(command[2]);
                ObjectInputStream is = new ObjectInputStream(new ByteArrayInputStream(bytes));
                PlayerData data = (PlayerData) is.readObject();
                server.data = data;
                System.out.println(data.character_name);
            } catch(IOException ex) {
            } catch(ClassNotFoundException ex) {
            }
        }
    }
}
