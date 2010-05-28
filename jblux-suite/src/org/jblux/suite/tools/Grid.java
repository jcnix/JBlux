/**
 * File: Grid.java
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

package org.jblux.suite.tools;

import org.jblux.util.Coordinates;
import org.newdawn.slick.geom.Rectangle;

public class Grid {
    public Grid() {
    }

    /**
     * Returns the tile the given coords are in.
     *
     * @param coords    Coordinates to get tile for.
     * @return          The tile the coordinates are in.
     */
    public static Rectangle getTile(Coordinates coords) {
        int diff_x = coords.x % 32;
        int diff_y = coords.y % 32;

        Coordinates new_coords = new Coordinates();
        new_coords.x = coords.x - diff_x;
        new_coords.y = coords.y - diff_y;
        
        return new Rectangle(new_coords.x, new_coords.y, 32, 32);
    }
}