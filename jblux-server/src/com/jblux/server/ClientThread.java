/**
 * File: ClientThread.java
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

import com.jblux.util.Commands;
import com.jblux.util.Coordinates;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.LinkedList;

/*
 * Manages the client, and sends data back to the client.
 */
public class ClientThread {
    private Socket socket;
    private ObjectOutputStream netOut;

    private Clients clients;
    private ClientListener cl;

    public ClientThread(Socket s) {
        socket = s;
        clients = Clients.getInstance();

        try {
            netOut = new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException ex) {
        }


        cl = new ClientListener(this, socket);
        cl.start();

        //System.out.printf("Accepted Client: %s\n", socket.getInetAddress().getHostName());
    }

    public Coordinates getCoords() {
        return cl.coords;
    }

    public String getUsername() {
        return cl.username;
    }

    /*
     * Actually tells all clients except the client that is
     * sending the message.
     */
    public void tell_all_clients(String command, boolean include_self) {
        LinkedList<ClientThread> c = clients.getClients();

        for(int i = 0; i < c.size(); i++) {
            ClientThread ct = c.get(i);
            if(!include_self && ct == this) {
                continue;
            }

            //Tell other client about the new player
            ct.writeString(command);
        }
    }

    /*
     * Send's all players' coordinates back to the client so they can be displayed.
     */
    public void move(String username, Coordinates coords) {
        String command = String.format("%s %s %d %d", Commands.MOVE, username, coords.x, coords.y);
        tell_all_clients(command, false);
    }

    //Tell the other clients that a player has connected.
    public void connect(String username, Coordinates coords) {
        String command = String.format("%s %s %s", Commands.CONNECT, username, coords);
        System.out.printf("%s connected\n", username);

        /* This can't use tell_all_clients yet because it sends data
         * back to the sender.
         */
        LinkedList<ClientThread> c = clients.getClients();
        for(int i = 0; i < c.size(); i++) {
            ClientThread ct = c.get(i);
            if(ct == this) {
                continue;
            }

            //The the new player about the other clients
            String otherPlayer = String.format("%s %s %s", Commands.CONNECT,
                    ct.getUsername(), ct.getCoords());
            writeString(otherPlayer);

            //Tell other client about the new player
            ct.writeString(command);
        }
    }

    public void disconnect(String user) {
        String command = String.format("%s %s", Commands.DISCONNECT, user);
        tell_all_clients(command, false);
        clients.removeClient(this);
    }

    public void sendChatMessage(String username, String message) {
        String command = String.format("%s %s %s", Commands.CHAT, username, message);
        tell_all_clients(command, true);
    }

    public void writeString(String s) {
        try {
            netOut.writeObject(s);
        } catch (IOException ex) {
        }
    }
}

class ClientListener extends Thread {
    private Socket clientSocket;
    private ObjectInputStream netIn;
    private ClientThread client;
    private Clients clients;

    public String username;
    public Coordinates coords;

    public ClientListener(ClientThread client, Socket s) {
        clients = Clients.getInstance();
        this.client = client;
        clientSocket = s;
        coords = new Coordinates();
        username = "";
    }

    @Override
    public void run() {
        try {
            netIn = new ObjectInputStream(clientSocket.getInputStream());
        } catch (IOException ex) {
        }

        try {
            String command = "";
            while((command = (String) netIn.readObject()) != null) {
                doCommand(command);
            }
        } catch(ClassNotFoundException ex) {
        } catch(NullPointerException ex) {
            ex.printStackTrace();
        } catch(IOException ex) {
            endThread();
        }
    }

    public void doCommand(String c) {
        if(c.startsWith(Commands.MOVE)) {
            String[] c1 = c.split("[ ]");
            coords.x = Integer.parseInt(c1[2]);
            coords.y = Integer.parseInt(c1[3]);
            client.move(username, coords);
        }
        else if(c.startsWith(Commands.CONNECT)) {
            String[] c1 = c.split("[ ]");
            username = c1[1];
            coords.x = Integer.parseInt(c1[2]);
            coords.y = Integer.parseInt(c1[3]);
            client.connect(username, coords);
        }
        else if(c.startsWith(Commands.CHAT)) {
            String[] c1 = c.split("[ ]");
            username = c1[1];

            //TODO: Make this less ugly
            String message = "";
            for(int i = 2; i < c1.length; i++) {
                message += c1[i] + " ";
            }
            client.sendChatMessage(username, message);
        }

        clients.addDirtyClient(client);
    }

    public void endThread() {
        System.out.printf("%s disconnected.\n", username);
        client.disconnect(username);

        try {
            netIn.close();
        } catch (IOException ex) {
        }

        this.interrupt();
    }
}
