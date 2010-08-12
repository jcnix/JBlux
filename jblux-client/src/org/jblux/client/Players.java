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

package org.jblux.client;

import java.util.ArrayList;

/*
 * Represents all players on the current map.
 */
public class Players {
    private ArrayList<Sprite> players;
    private static Players plys;

    protected Players() {
        players = new ArrayList<Sprite>();
    }

    public static Players getInstance() {
        if(plys == null)
            plys = new Players();

        return plys;
    }

    public void addPlayer(Sprite player) {
        players.add(player);
    }

    public void removePlayer(String username) {
        for(int i = 0; i < players.size(); i++) {
            Sprite npc = players.get(i);
            if(npc.name.equals(username)) {
                players.remove(npc);
                break;
            }
        }
    }

    public Sprite getPlayer(int i) {
        return players.get(i);
    }

    public Sprite getPlayer(String username) {
        Sprite npc = null;
        for(int i = 0; i < players.size(); i++) {
            Sprite n = players.get(i);
            if(n.name.equals(username)) {
                npc = n;
                break;
            }
        }

        return npc;
    }

    public int size() {
        return players.size();
    }
}
