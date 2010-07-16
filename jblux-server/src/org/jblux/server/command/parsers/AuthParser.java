/**
 * File: AuthParser.java
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

import org.jblux.server.ClientThread;
import org.jblux.sql.UserTable;

public class AuthParser implements CommandParser {
    public AuthParser() {
    }

    public void parse(String[] command, ClientThread client) {
        String name = command[1];
        String pass = command[2];
        String character_name = command[3];
        UserTable ut = new UserTable();
        boolean b = ut.authenticate(name, pass, character_name);
        client.auth(character_name, b);
    }
}
