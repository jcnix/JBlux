/**
 * File:  Coordinates.java
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

package org.jblux.util;

import java.io.Serializable;

public class Coordinates implements Serializable {
    public int x;
    public int y;

    public Coordinates() {
    }

    public Coordinates(int x, int y) {
        setX(x);
        setY(y);
    }

    @Override
    public String toString() {
        return String.format("%d %d", x, y);
    }

    public boolean equals(Coordinates c) {
        if((this.x == c.x) && (this.y == c.y))
            return true;
        else
            return false;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
