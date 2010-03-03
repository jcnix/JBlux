/**
 * File: Relations.java
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

package com.jblux.common;

public enum Relation {
    LEFT_OF("LEFT_OF"),
    RIGHT_OF("RIGHT_OF"),
    ABOVE("ABOVE"),
    BELOW("BELOW");

    private final String str;

    private Relation(String s) {
        str = s;
    }

    @Override
    public String toString() {
        return str;
    }
}
