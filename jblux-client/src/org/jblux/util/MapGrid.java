/**
 * File: MapGrid.java
 *
 * @author Casey Jones
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

package org.jblux.util;

import org.jblux.client.data.NpcData;

/**
 * Given a set of Coordinates, find which tile that point is in.
 *
 * @author casey
 */
public class MapGrid {
    public final static int tile_size = 32;

    private MapGrid() {
    }

    public static Coordinates getTile(int x, int y) {
        return getTile(new Coordinates(x, y));
    }

    public static Coordinates getTile(Coordinates coords) {
        Coordinates tile_coords = new Coordinates();

        int diff_x = coords.x % tile_size;
        int diff_y = coords.y % tile_size;
        tile_coords.x = coords.x - diff_x;
        tile_coords.y = coords.y - diff_y;
        
        return tile_coords;
    }

    public static boolean is_in_range(Coordinates object, Coordinates target, int tiles) {
        int a = object.x - target.x;
        int b = object.y - target.y;
        int c = (int) Math.sqrt(Math.pow(a, 2) + Math.pow(b, 2));
        
        int t = c / tile_size;
        if(t < tiles) {
            return true;
        }
        else {
            return false;
        }
    }
}
