/**
 * File: Players.java
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

package com.jblux.client;

import java.util.Hashtable;
import java.util.LinkedList;

/*
 * Represents all players on the current map.
 */
public class Players {
    private LinkedList<Sprite> players;
    private LinkedList<Sprite> dirtyPlayers;
    //Gives the position in the player list for a given username
    private Hashtable<String, Integer> playerMap;
    private static Players plys;

    protected Players() {
        players = new LinkedList<Sprite>();
        dirtyPlayers = new LinkedList<Sprite>();
        playerMap = new Hashtable<String, Integer>();
    }

    public static Players getInstance() {
        if(plys == null)
            plys = new Players();

        return plys;
    }

    public void addPlayer(Sprite player) {
        playerMap.put(player.getName(), players.size());
        players.add(player);
    }

    public void removePlayer(String username) {
        players.remove(players.get(playerMap.get(username)));
        playerMap.remove(username);
    }

    public Sprite getPlayer(int i) {
        return players.get(i);
    }

    public void addDirtyPlayer(Sprite player) {
        dirtyPlayers.add(player);
    }

    public void removeDirtyPlayer(Sprite player) {
        dirtyPlayers.remove(player);
    }

    public Sprite getDirtyPlayer(int i) {
        return dirtyPlayers.get(i);
    }

    public Sprite getPlayer(String username) {
        return getPlayer(playerMap.get(username));
    }

    public int size() {
        return players.size();
    }
}
