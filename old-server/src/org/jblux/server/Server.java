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

package org.jblux.server;

import com.google.gson.Gson;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import org.jblux.common.ServerInfo;
import org.jblux.common.client.PlayerData;
import org.jblux.common.error.FatalError;
import org.jblux.server.maps.Map;
import org.jblux.server.maps.Maps;
import org.jblux.sql.MapSqlTable;
import org.jblux.sql.UserTable;

public class Server {
    private ServerSocket serv;
    private Clients clients;
    private GameWorld m_gameworld;

    public Server() {
        System.out.printf("JBlux Server 0.0.1 -- running\n");
        System.out.printf("Running on %s:%d\n", ServerInfo.LOCAL_IP, ServerInfo.PORT);
        clients = Clients.getInstance();
        
        try {
            //This is binding my local IP address.
            serv = new ServerSocket(ServerInfo.PORT, 0, InetAddress.getByName(ServerInfo.LOCAL_IP));
        } catch (IOException ex) {
            FatalError.die(ex);
        }

        m_gameworld = new GameWorld();
        m_gameworld.start();

        Maps maps = Maps.getInstance();
        Map m = maps.getMap(1);
        Gson gson = new Gson();
        System.out.printf("%s\n", gson.toJson(m));

        while(true) {
            try {
                Socket socket = serv.accept();
                ClientThread client = new ClientThread(socket);
                
                clients.addClient(client);
            } catch (IOException ex) {
                System.out.printf("Accept failed: %d.\n", ServerInfo.PORT);
            }
        }
    }

    public static void main(String args[]) {
        new Server();
    }
}
