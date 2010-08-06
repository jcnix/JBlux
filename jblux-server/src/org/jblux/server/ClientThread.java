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

package org.jblux.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.LinkedList;
import org.jblux.common.Commands;
import org.jblux.common.client.PlayerData;
import org.jblux.common.items.Inventory;
import org.jblux.common.items.Item;
import org.jblux.server.command.parsers.AuthParser;
import org.jblux.server.command.parsers.MapParser;
import org.jblux.sql.MapSqlTable;
import org.jblux.sql.UserTable;
import org.jblux.util.Base64;
import org.jblux.util.Coordinates;

/*
 * Manages the client, and sends data back to the client.
 */
public class ClientThread {
    private Socket socket;
    private ObjectOutputStream netOut;
    private Inventory inv;
    private boolean authenticated;
    private String character_name;
    private int map_id;
    private PlayerData player_data;
    public Coordinates coords;

    private Clients clients;
    private ClientListener cl;

    public ClientThread(Socket s) {
        socket = s;
        clients = Clients.getInstance();
        authenticated = false;
        map_id = 0;
        coords = new Coordinates();

        try {
            netOut = new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException ex) {
        }

        cl = new ClientListener(this, socket);
        cl.start();

        //System.out.printf("Accepted Client: %s\n", socket.getInetAddress().getHostName());
    }

    public Coordinates getCoords() {
        return coords;
    }

    public String getUsername() {
        return cl.character_name;
    }

    public int getMap() {
        return map_id;
    }

    public void setCharacterName(String name) {
        this.character_name = name;
    }

    /*
     * Actually tells all clients except the client that is
     * sending the message.
     */
    public void tell_all_clients_on_map(String command) {
        LinkedList<ClientThread> c = clients.getClients();

        for(int i = 0; i < c.size(); i++) {
            ClientThread ct = c.get(i);
            if(ct == this || !is_on_same_map(ct)) {
                continue;
            }

            //Tell other client about the new player
            ct.writeString(command);
        }
    }

    public boolean isAuthenticated() {
        return authenticated;
    }

    public void auth(String name, boolean b) {
        if(b) {
            authenticated = true;
            setCharacterName(name);
        }
        else {
            authenticated = false;
        }

        if(authenticated) {
            sendPlayerData();
        }
    }

    public void sendPlayerData() {
        UserTable ut = new UserTable();
        player_data = ut.getPlayer(character_name);
        String player_enc = "";
        try {
            player_enc = Base64.encodeObject(player_data);
        } catch (IOException ex) {
        }

        String command = String.format("%s self %s", Commands.PLAYER, player_enc);
        writeString(command);
    }

    /*
     * Send's all players' coordinates back to the client so they can be displayed.
     */
    public void move(String username, Coordinates coords) {
        String command = String.format("%s %s %d %d", Commands.MOVE, username, coords.x, coords.y);
        tell_all_clients_on_map(command);
    }

    public void disconnect(String user) {
        String command = String.format("%s %s", Commands.DISCONNECT, user);
        tell_all_clients_on_map(command);
        clients.removeClient(this);
    }

    public void leave_map(String user) {
        String command = String.format("%s rm %s", Commands.MAP, user);
        tell_all_clients_on_map(command);
    }

    /* Put the player on a new map */
    public void go_to_map(int map_id, Coordinates coords) {
        System.out.printf("%s connected\n", character_name);

        this.map_id = map_id;
        UserTable ut = new UserTable();
        MapSqlTable mst = new MapSqlTable();        
        ut.setMap(player_data.character_id, map_id, getCoords());
        
        String encoded_player_data = "";
        try {
            encoded_player_data = Base64.encodeObject(player_data);
        } catch(IOException ex) {
        }
        
        String command = String.format("%s add %s %s %s",
                Commands.MAP, character_name, getCoords(), encoded_player_data);
        
        LinkedList<ClientThread> c = clients.getClients();
        for(int i = 0; i < c.size(); i++) {
            ClientThread ct = c.get(i);
            if(ct == this || !is_on_same_map(ct)) {
                continue;
            }

            //Tell the new player about the other clients
            String otherPlayer = String.format("%s add %s %s", Commands.MAP,
                    ct.getUsername(), ct.getCoords());
            writeString(otherPlayer);

            //Tell other client about the new player
            ct.writeString(command);
        }
    }

    public void sendChatMessage(String username, String message) {
        String command = String.format("%s %s %s", Commands.CHAT, username, message);
        tell_all_clients_on_map(command);
        writeString(command);
    }

    public boolean is_on_same_map(ClientThread ct) {
        return (ct.getMap() == this.getMap());
    }

    public void add_to_inventory(Item i) {
        inv.addItem(i);
    }

    public void writeString(String s) {
        writeObjects(s);
    }

    /* First argument should be a string that tells what the payload is
     * (If one is needed!)
     * Second argument and beyond are serializable objects.
     *
     * We'll just write the whole array
     */
    public void writeObjects(Object... o) {
        String command = "";
        int i = 1;
        for(Object obj : o) {
            i++;
            try {
                command += Base64.encodeObject((Serializable) obj);
            } catch(IOException ex) {
            }

            if(o.length != i)
                command += " ";
        }

        try {
            netOut.writeObject(command);
        } catch (IOException ex) {
        }
    }
}

class ClientListener extends Thread {
    private Socket clientSocket;
    private ObjectInputStream netIn;
    private ClientThread client;
    
    public String character_name;

    public ClientListener(ClientThread client, Socket s) {
        this.client = client;
        clientSocket = s;
        character_name = "";
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
        try {
            c = (String) Base64.decodeToObject(c);
        } catch (IOException ex) {
        } catch (ClassNotFoundException ex) {
        }
        String[] c1 = c.split("\\s");

        if(c.startsWith(Commands.AUTH)) {
            AuthParser ap = new AuthParser();
            ap.parse(c1, client);
        }

        //Ignore any other command if not authenticated.
        if(!client.isAuthenticated()) {
            return;
        }

        if(c.startsWith(Commands.MOVE)) {
            client.coords.x = Integer.parseInt(c1[2]);
            client.coords.y = Integer.parseInt(c1[3]);
            client.move(character_name, client.coords);
        }
        else if(c.startsWith(Commands.CONNECT)) {
            character_name = c1[1];
            client.coords.x = Integer.parseInt(c1[2]);
            client.coords.y = Integer.parseInt(c1[3]);
            UserTable ut = new UserTable();
            int id = ut.getMapForPlayer(character_name);
            client.go_to_map(id, client.coords);
        }
        else if(c.startsWith(Commands.CHAT)) {
            character_name = c1[1];

            //TODO: Make this less ugly
            String message = "";
            for(int i = 2; i < c1.length; i++) {
                message += c1[i] + " ";
            }
            client.sendChatMessage(character_name, message);
        }
        else if(c.startsWith(Commands.MAP)) {
            MapParser mp = new MapParser();
            mp.parse(c1, client);
        }
    }

    public void endThread() {
        System.out.printf("%s disconnected.\n", character_name);
        client.disconnect(character_name);

        try {
            netIn.close();
        } catch (IOException ex) {
        }

        this.interrupt();
    }
}
