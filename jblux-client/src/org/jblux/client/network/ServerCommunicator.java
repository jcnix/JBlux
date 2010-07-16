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

package org.jblux.client.network;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import org.jblux.client.Player;
import org.jblux.client.Players;
import org.jblux.client.Sprite;
import org.jblux.client.gui.observers.ChatBoxObserver;
import org.jblux.common.Commands;
import org.jblux.common.Relation;
import org.jblux.common.ServerInfo;
import org.jblux.common.items.Item;
import org.jblux.util.Base64;
import org.jblux.util.ChatMessage;
import org.jblux.util.Coordinates;

/*
 * Sends data to the server.
 */
public class ServerCommunicator {
    private Socket socket;
    private ObjectOutputStream netOut;
    private ServerListener sl;
    private String character_name;
    public Player player;

    public ServerCommunicator() {
        try {
            socket = new Socket(ServerInfo.SERVER, ServerInfo.PORT);
            if(socket.isConnected()) {
                netOut = new ObjectOutputStream(socket.getOutputStream());
                sl = new ServerListener(socket);
                sl.start();
            }
        } catch (UnknownHostException ex) {
        } catch (IOException ex) {
        }
    }

    public boolean isConnected() {
        try {
            return socket.isConnected();
        } catch(NullPointerException ex) {
            return false;
        }
    }

    public void connect_player(String player, Coordinates coords) {
        System.out.println("Connecting...");
        character_name = player;
        String command = String.format("%s %s %d %d", Commands.CONNECT, character_name, coords.x, coords.y);
        writeString(command);
    }

    public void move(int x, int y) {
        String command = String.format("%s %s %d %d", Commands.MOVE, character_name, x, y);
        writeString(command);
    }

    public void sendChat(String message) {
        String command = String.format("%s %s %s", Commands.CHAT, character_name, message);
        writeString(command);
    }

    public String goto_map(Relation r, String map_name, Player p) {
        String map = "";
        String command = String.format("%s goto %s %s", Commands.MAP, r, map_name);
        writeString(command);

        while(sl.response == null) {
            try {
                Thread.sleep(20);
            } catch(InterruptedException ex) {
            }
        }

        if(sl.response.equals("stay")) {
            return map_name;
        }

        map = sl.response;
        p.setCoords(sl.coords);        
        sl.response = null;
        
        return map;
    }

    public boolean authenticate(String username, String password, String character_name) {
        String command = String.format("%s %s %s %s", Commands.AUTH, username, password,
                character_name);
        System.out.println(command);
        writeString(command);
        boolean auth = false;

        while(sl.response == null) {
            try {
                Thread.sleep(20);
            } catch(InterruptedException ex) {
            }
        }
        
        if(sl.response.equals("true")) {
            auth = true;
        }

        sl.response = null;
        return auth;
    }

    public void close() {
        try {
            socket.close();
            netOut.close();
        } catch(IOException ex) {
        }
    }

    public void writeString(String s) {
        try {
            String command = Base64.encodeObject(s);
            netOut.writeObject(command);
        } catch (IOException ex) {
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
    public String response;
    public Coordinates coords;

    public ServerListener(Socket s) {
        socket = s;
        players = Players.getInstance();
        cbObserver = ChatBoxObserver.getInstance();
        response = null;
        coords = new Coordinates();
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
        } catch(IOException ex) {
            ex.printStackTrace();
        }
    }

    public synchronized void doCommand(String c) {
        String command = "";
        String[] c0 = null;
        try {
            String[] c_enc = c.split("\\s");
            command = c_enc[0];
            command = (String) Base64.decodeToObject(command);
            c0 = command.split("\\s");
        } catch (IOException ex) {
        } catch (ClassNotFoundException ex) {
        }

        if(command.startsWith(Commands.MOVE)) {
            String name = c0[1];
            int x = Integer.parseInt(c0[2]);
            int y = Integer.parseInt(c0[3]);

            Sprite npc = players.getPlayer(name);
            npc.setCoords(x, y);
        }
        else if(command.startsWith(Commands.AUTH)) {
            response = c0[1];
        }
        else if(command.startsWith(Commands.CONNECT)) {
            String name = c0[1];
            int x = Integer.parseInt(c0[2]);
            int y = Integer.parseInt(c0[3]);

            Sprite npc = new Sprite("img/koopa.png");
            npc.setName(name);
            npc.setCoords(x, y);
            npc.setImage(0, 0);
            players.addPlayer(npc);
        }
        else if(command.startsWith(Commands.DISCONNECT)) {
            String name = c0[1];
            players.removePlayer(name);
        }
        else if(command.startsWith(Commands.CHAT)) {
            String name = c0[1];

            //TODO: make this less ugly
            String message = "";
            for(int i = 2; i < c0.length; i++) {
                message += c0[i] + " ";
            }

            cbObserver.recievedMessage(new ChatMessage(name,message));
        }
        else if(command.startsWith(Commands.MAP)) {
            if(c0[1].equals("rm")) {
                String name = c0[2];
                players.removePlayer(name);
            }
            else if(c0[1].equals("add")) {
                String name = c0[2];
                int x = Integer.parseInt(c0[3]);
                int y = Integer.parseInt(c0[4]);

                Sprite npc = new Sprite("img/koopa.png");
                npc.setName(name);
                npc.setCoords(x, y);
                npc.setImage(0, 0);
                players.addPlayer(npc);
            }
            else if(c0[1].equals("goto")) {
                response = c0[2];
                coords.x = Integer.parseInt(c0[3]);
                coords.y = Integer.parseInt(c0[4]);
                System.out.printf("response: %s @ %s\n", response, coords);
            }
            else if(c0[1].equals("stay")) {
                response = "stay";
            }
        }
        else if(command.startsWith("put")) {
            try {
                Item item = (Item) Base64.decodeToObject(c0[2]);
            } catch (IOException ex) {
            } catch (ClassNotFoundException ex) {
            }
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
