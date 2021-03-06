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
import org.jblux.client.JBlux;
import org.jblux.client.Players;
import org.jblux.client.Sprite;
import org.jblux.client.data.NpcData;
import org.jblux.client.gui.observers.ChatBoxObserver;
import org.jblux.client.gui.observers.NewPlayerObserver;
import org.jblux.util.Commands;
import org.jblux.util.Relation;
import org.jblux.util.ServerInfo;
import org.jblux.client.data.PlayerData;
import org.jblux.client.data.Quest;
import org.jblux.client.items.Item;
import org.jblux.util.Base64;
import org.jblux.util.Coordinates;
import org.newdawn.slick.state.StateBasedGame;

/*
 * Sends data to the server.
 */
public class ServerCommunicator {
    private Socket socket;
    private OutputStream netOut;
    private ServerListener sl;

    public ServerCommunicator(StateBasedGame sbg) {
        try {
            socket = new Socket(ServerInfo.SERVER, ServerInfo.PORT);
            if(socket.isConnected()) {
                netOut = socket.getOutputStream();
                sl = new ServerListener(socket, sbg);
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
    
    public void move(int x, int y) {
        String command = String.format("%s %d %d", Commands.MOVE, x, y);
        writeString(command);
    }

    public void sendChat(String message) {
        String command = String.format("%s %s", Commands.CHAT, message);
        writeString(command);
    }

    public void goto_map(Relation r, String map_name) {
        String command = String.format("%s goto %s %s", Commands.MAP, r, map_name);
        writeString(command);
    }

    public void attackNpc(NpcData npc) {
        String command = String.format("%s %d", Commands.ATTACK, npc.map_id);
        writeString(command);
    }

    public void getMapInfo() {
        String command = String.format("%s info", Commands.MAP);
        writeString(command);
    }

    public void authenticate(String username, String password, String character_name) {
        String command = String.format("%s %s %s %s", Commands.AUTH, username, password,
                character_name);
        writeString(command);
    }

    public void pickup_item(Coordinates coords) {
        String command = String.format("%s %s %s", Commands.MAP, Commands.PICKUP, coords);
        writeString(command);
    }

    public void acceptQuest(Quest quest) {
        String command = String.format("%s accept %d", Commands.QUEST, quest.id);
        writeString(command);
    }

    public void completeQuest(Quest quest) {
        String command = String.format("%s complete %d", Commands.QUEST, quest.id);
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
    private NewPlayerObserver player_observer;
    private ResponseWaiter response;
    private StateBasedGame sbg;
    private int msg_size;
    private int recv_bytes;
    private String recv_command;
    private String remaining_command;

    private boolean invalid_login;

    public ServerListener(Socket s, StateBasedGame sbg) {
        socket = s;
        this.sbg = sbg;
        players = Players.getInstance();
        cbObserver = ChatBoxObserver.getInstance();
        coords = new Coordinates();
        player_observer = NewPlayerObserver.getInstance();
        response = ResponseWaiter.getInstance();
        msg_size = 0;
        recv_bytes = 0;
        recv_command = "";
        remaining_command = null;
        invalid_login = false;
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
                        if(!invalid_login)
                            sbg.enterState(JBlux.SERVERDOWNSTATE);
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

    private void notify_observers(String s) {
        response.responseReceived(s);
    }

    public synchronized void doCommand(String command) {
        String[] c0 = command.split("\\s");

        if(command.startsWith(Commands.AUTH)) {
            invalid_login = true;
            sbg.enterState(JBlux.INVALIDLOGINSTATE);
        }
        else if(command.startsWith(Commands.MOVE)) {
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
                String r = String.format("map %s %s", map, coords);
                this.notify_observers(r);
            }
            else if(c0[1].equals("info")) {
                String npcs = c0[3];
                this.notify_observers(String.format("info npcs %s", npcs));
            }
            else if(c0[1].equals("stay")) {
                //Don't do anything
            }
        }
        else if(command.startsWith(Commands.NPC)) {
            notify_observers(command);
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
