/**
 * File: RelationUtil.java
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

package org.jblux.common;

public class RelationUtil {
    public static Relation fromString(String r) {
        Relation rel;

        if(r.equals("left"))
            rel = Relation.LEFT;
        else if(r.equals("right"))
            rel = Relation.RIGHT;
        else if(r.equals("bottom"))
            rel = Relation.BOTTOM;
        else if(r.equals("top"))
            rel = Relation.TOP;
        else
            rel = null;

        return rel;
    }

    public static Relation getOpposite(Relation r) {
        Relation r2 = null;

        if(r == Relation.LEFT)
            r2 = Relation.RIGHT;
        else if(r == Relation.RIGHT)
            r2 = Relation.LEFT;
        else if(r == Relation.TOP)
            r2 = Relation.BOTTOM;
        else if(r == Relation.BOTTOM)
            r2 = Relation.TOP;

        return r2;
    }
}
