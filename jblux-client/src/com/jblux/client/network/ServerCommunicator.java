/**
 * File: ServerCommunicator.java
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

package com.jblux.client.network;

import com.jblux.client.Players;
import com.jblux.client.Sprite;
import com.jblux.client.gui.observers.ChatBoxObserver;
import com.jblux.common.ServerInfo;
import com.jblux.util.ChatMessage;
import com.jblux.util.Commands;
import com.jblux.util.Coordinates;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

/*
 * Sends data to the server.
 */
public class ServerCommunicator {
    private Socket socket;
    private ObjectOutputStream netOut;
    private ServerListener sl;
    private String username;

    public ServerCommunicator() {
        try {
            socket = new Socket(ServerInfo.SERVER, ServerInfo.PORT);
            netOut = new ObjectOutputStream(socket.getOutputStream());
        } catch (UnknownHostException ex) {
        } catch (IOException ex) {
        }

        sl = new ServerListener(socket);
        sl.start();
    }

    public void connect_player(String player, Coordinates coords) {
        System.out.println("Connecting...");
        username = player;
        String command = String.format("%s %s %d %d", Commands.CONNECT, username, coords.x, coords.y);
        try {
            netOut.writeObject(command);
        } catch (IOException ex) {
        }
    }

    public void move(int x, int y) {
        try {
            String command = String.format("%s %s %d %d", Commands.MOVE, username, x, y);
            netOut.writeObject(command);
        } catch (IOException ex) {
        }
    }

    public void sendChat(String message) {
        try {
            String command = String.format("%s %s %s", Commands.CHAT, username, message);
            netOut.writeObject(command);
        } catch(IOException ex) {
        }
    }

    public void close() {
        try {
            socket.close();
            netOut.close();
        } catch(IOException ex) {
        }
    }
}

/*
 * Listens for data from the server.
 */
class ServerListener extends Thread {
    private Socket socket;
    private ObjectInputStream netIn;
    private Players players;
    private ChatBoxObserver cbObserver;

    public ServerListener(Socket s) {
        socket = s;
        players = Players.getInstance();
        cbObserver = ChatBoxObserver.getInstance();
    }

    @Override
    public void run() {
        try {
            netIn = new ObjectInputStream(socket.getInputStream());
            
            String command = "";
            while((command = (String) netIn.readObject()) != null) {
                doCommand(command);
            }
        } catch(ClassNotFoundException ex) {
            ex.printStackTrace();
        } catch(IOException ex) {
            ex.printStackTrace();
        }
    }

    public void doCommand(String c) {
        String[] c0 = c.split("\\s");
        System.out.println(c);

        if(c.startsWith(Commands.MOVE)) {          
            String name = c0[1];
            int x = Integer.parseInt(c0[2]);
            int y = Integer.parseInt(c0[3]);

            Sprite npc = players.getPlayer(name);
            npc.setCoords(x, y);
        }
        else if(c.startsWith(Commands.CONNECT)) {
            String name = c0[1];
            int x = Integer.parseInt(c0[2]);
            int y = Integer.parseInt(c0[3]);

            Sprite npc = new Sprite("img/koopa.png");
            npc.setName(name);
            npc.setCoords(x, y);
            npc.setImage(0, 0);
            players.addPlayer(npc);
        }
        else if(c.startsWith(Commands.DISCONNECT)) {
            String name = c0[1];
            players.removePlayer(name);
        }
        else if(c.startsWith(Commands.CHAT)) {
            String name = c0[1];

            //TODO: make this less ugly
            String message = "";
            for(int i = 2; i < c0.length; i++) {
                message += c0[i] + " ";
            }

            cbObserver.recievedMessage(new ChatMessage(name,message));
        }
    }

    public void endThread() {
        try {
            netIn.close();
        } catch(IOException ex) {
        }

        this.interrupt();
    }
}
