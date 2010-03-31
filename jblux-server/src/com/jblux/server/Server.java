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

import com.jblux.common.ServerInfo;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Server {
    private ServerSocket serv;
    private Clients clients;

    public Server() {
        System.out.printf("JBlux Server 0.0.1 -- running\n");
        clients = Clients.getInstance();
        
        try {
            //This is binding my local IP address.
            serv = new ServerSocket(ServerInfo.PORT, 0, InetAddress.getByName(ServerInfo.LOCAL_IP));
        } catch (IOException ex) {
            System.out.printf("Could not bind socket %d.\n", ServerInfo.PORT);
            System.exit(1);
        }

        try {
            Class.forName("org.postgresql.Driver");
        } catch(ClassNotFoundException ex) {
            System.err.println("Couldn't find PostgreSQL Driver.");
            System.out.println("Fatal Error: Exiting.");
            System.exit(1);
        }

        Connection conn = null;
        try {
            conn = DriverManager.getConnection("jdbc:postgresql://127.0.0.1/tmuo",
                    "postgres", "mypass"); //This isn't my password
        } catch(SQLException ex) {
            System.err.println(ex.getMessage());
            System.err.println("Couldn't connect to Database.");
            System.out.println("Fatal Error: Exiting");
            System.exit(1);
        }

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
