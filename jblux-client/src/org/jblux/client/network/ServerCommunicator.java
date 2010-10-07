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
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Observable;
import org.jblux.client.Player;
import org.jblux.client.Players;
import org.jblux.client.Sprite;
import org.jblux.client.gui.observers.ChatBoxObserver;
import org.jblux.client.gui.observers.NewPlayerObserver;
import org.jblux.util.Commands;
import org.jblux.util.Relation;
import org.jblux.util.ServerInfo;
import org.jblux.client.data.PlayerData;
import org.jblux.common.items.Item;
import org.jblux.util.Base64;
import org.jblux.util.Coordinates;

/*
 * Sends data to the server.
 */
public class ServerCommunicator {
    private Socket socket;
    private OutputStream netOut;
    private ServerListener sl;
    public Player player;

    public ServerCommunicator() {
        try {
            socket = new Socket(ServerInfo.SERVER, ServerInfo.PORT);
            if(socket.isConnected()) {
                netOut = socket.getOutputStream();
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

    public void add_observable(ResponseWaiter ro) {
        sl.add_observable(ro);
    }
    
    public void rm_observable(Observable o) {
        sl.rm_observable((ResponseWaiter) o);
    }

    public void move(int x, int y) {
        String command = String.format("%s %d %d", Commands.MOVE, x, y);
        writeString(command);
    }

    public void sendChat(String message) {
        String command = String.format("%s %s", Commands.CHAT, message);
        writeString(command);
    }

    public void goto_map(ResponseWaiter ro, Relation r, String map_name) {
        String map = "";
        String command = String.format("%s goto %s %s", Commands.MAP, r, map_name);
        sl.add_observable(ro);
        writeString(command);
    }

    public void getMapInfo(ResponseWaiter ro) {
        String command = String.format("%s info", Commands.MAP);
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

    public void pickup_item(Coordinates coords, ResponseWaiter response) {
        String command = String.format("%s %s %s", Commands.MAP, Commands.PICKUP, coords);
        sl.add_observable(response);
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
            netOut.write(s.getBytes());
        } catch (IOException ex) {
        }
    }
}

/*
 * Listens for data from the server.
 */
class ServerListener extends Thread {
    private Socket socket;
    private InputStreamReader netIn;
    private Players players;
    private ChatBoxObserver cbObserver;
    public Coordinates coords;
    private ArrayList<ResponseWaiter> observables;
    private NewPlayerObserver player_observer;
    private int msg_size;
    private int recv_bytes;
    private String recv_command;
    private String remaining_command;

    public ServerListener(Socket s) {
        socket = s;
        players = Players.getInstance();
        cbObserver = ChatBoxObserver.getInstance();
        coords = new Coordinates();
        observables = new ArrayList<ResponseWaiter>();
        player_observer = NewPlayerObserver.getInstance();
        msg_size = 0;
        recv_bytes = 0;
        recv_command = "";
        remaining_command = null;
    }

    @Override
    public void run() {
        try {
            netIn = new InputStreamReader(socket.getInputStream(), "UTF-8");
            
            char buf[] = new char[1024];
            int n;
            while(true) {
                String command = "";
                recv_bytes = 0;
                recv_command = "";
                
                if(remaining_command != null) {
                    command = remaining_command;
                    remaining_command = null;
                }
                else {
                    if((n = netIn.read(buf)) > 0) {
                        StringBuilder s = new StringBuilder();
                        s.append(buf, 0, n);
                        command = s.toString();
                    }
                    else {
                        break;
                    }
                }

                if(command.startsWith("size")) {
                    String[] c0 = command.split("\\s");
                    msg_size = Integer.parseInt(c0[1]);

                    /* if anything else is attached to the command, chop it off */
                    int chop_size = "size ".length() + c0[1].length() + 1;
                    recv_command = command.substring(chop_size);
                    recv_bytes = recv_command.length();

                    /* continue reading in the rest of the command */
                    while(recv_bytes < msg_size) {
                        int n1 = netIn.read(buf);
                        recv_bytes += n1;
                        StringBuilder s1 = new StringBuilder();
                        s1.append(buf, 0, n1);
                        recv_command += s1.toString();
                    }

                    /* if we got more than we need, leave the rest for the next command */
                    int rem_bytes = recv_bytes - msg_size;
                    if(rem_bytes > 0) {
                        int len = recv_command.length();
                        remaining_command = recv_command.substring(len - rem_bytes, len);
                        recv_command = recv_command.substring(0, len - rem_bytes);
                    }
                    
                    doCommand(recv_command);
                }
            }
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

    public synchronized void doCommand(String command) {
        String[] c0 = command.split("\\s");
        
        if(command.startsWith(Commands.MOVE)) {
            String name = c0[1];
            int x = Integer.parseInt(c0[2]);
            int y = Integer.parseInt(c0[3]);

            Sprite npc = players.getPlayer(name);
            npc.setCoords(x, y);
        }
        else if(command.startsWith(Commands.PLAYER)) {
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

                player_observer.receivedMessage(data);
            }
            else if(c0[1].equals("goto")) {
                String map = c0[2];
                coords.x = Integer.parseInt(c0[3]);
                coords.y = Integer.parseInt(c0[4]);
                String npcs = c0[6];
                System.out.printf("response: %s @ %s\n", map, coords);
                String response = String.format("map %s %s %s", map, coords, npcs);
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
