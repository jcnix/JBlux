/**
 * File:   Server.java
 *
 * @author: Casey Jones
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

package com.jblux.server;

import java.util.LinkedList;

public class Clients {
    private LinkedList<ClientThread> clients;
    //List of clients that have changed
    private LinkedList<ClientThread> dirtyClients;

    private static Clients c;

    protected Clients() {
        clients = new LinkedList<ClientThread>();
        dirtyClients = new LinkedList<ClientThread>();
    }

    public static Clients getInstance() {
        if(c == null)
            c = new Clients();

        return c;
    }

    public void addClient(ClientThread client) {
        clients.add(client);
    }

    public void removeClient(ClientThread client) {
        clients.remove(client);
    }

    public LinkedList<ClientThread> getClients() {
        return clients;
    }

    public void addDirtyClient(ClientThread client) {
        dirtyClients.add(client);
    }

    public void removeDirtyClient(ClientThread client) {
        dirtyClients.remove(client);
    }

    public LinkedList<ClientThread> getDirtyClients() {
        return dirtyClients;
    }
}