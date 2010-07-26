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
import java.util.ArrayList;
import java.util.Observable;
import org.jblux.client.Player;
import org.jblux.client.Players;
import org.jblux.client.Sprite;
import org.jblux.client.gui.observers.ChatBoxObserver;
import org.jblux.common.Commands;
import org.jblux.common.Relation;
import org.jblux.common.ServerInfo;
import org.jblux.common.client.PlayerData;
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

    public void rm_observable(Observable o) {
        sl.rm_observable((ResponseWaiter) o);
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

    public void goto_map(ResponseWaiter ro, Relation r, String map_name) {
        String map = "";
        String command = String.format("%s goto %s %s", Commands.MAP, r, map_name);
        sl.add_observable(ro);
        writeString(command);
    }

    public void authenticate(ResponseWaiter ro, String username, String password, String character_name) {
        String command = String.format("%s %s %s %s", Commands.AUTH, username, password,
                character_name);
        System.out.println(command);
        sl.add_observable(ro);
        writeString(command);
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
    public Coordinates coords;
    private ArrayList<ResponseWaiter> observables;

    public ServerListener(Socket s) {
        socket = s;
        players = Players.getInstance();
        cbObserver = ChatBoxObserver.getInstance();
        coords = new Coordinates();
        observables = new ArrayList<ResponseWaiter>();
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

    public void add_observable(ResponseWaiter o) {
        observables.add(o);
    }

    public void rm_observable(ResponseWaiter o) {
        observables.remove(o);
    }

    private void notify_observers(Object o) {
        for(int i = 0; i < observables.size(); i++) {
            observables.get(i).responseReceived(o);
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
        else if(command.startsWith(Commands.PLAYER)) {
            System.err.println(command);
            notify_observers(command);
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

            cbObserver.receivedMessage(new ChatMessage(name,message));
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
                PlayerData data = PlayerDataFactory.getDataFromBase64(c0[5]);

                Sprite npc = new Sprite(data.race.sprite_sheet);
                npc.setName(name);
                npc.setCoords(x, y);
                npc.setImage(0, 0);
                players.addPlayer(npc);
            }
            else if(c0[1].equals("goto")) {
                String map = c0[2];
                coords.x = Integer.parseInt(c0[3]);
                coords.y = Integer.parseInt(c0[4]);
                System.out.printf("response: %s @ %s\n", map, coords);
                String response = String.format("%s %s", map, coords);
                this.notify_observers(response);
            }
            else if(c0[1].equals("stay")) {
                //Don't do anything
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
