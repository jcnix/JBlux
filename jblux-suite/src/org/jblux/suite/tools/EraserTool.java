/**
 * File: EraserTool.java
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

import java.util.Vector;
import org.jblux.util.Coordinates;
import org.newdawn.slick.geom.Rectangle;

public class EraserTool extends Tool {
    @Override
    /**
     * Erases entities from map
     *
     * @param coords    Where the user clicked on the map
     */
    public void draw(Coordinates coords) {
        if(m_gp == null)
            return;

        Rectangle clicked_tile = Grid.getTile(coords);
        Vector<Entity> entities = m_gp.getEntities();

        for(int i = 0; i < entities.size(); i++) {
            Entity e = entities.get(i);
            if(e.rmTile(clicked_tile)) {
                System.out.println("Erased tile");
                e.save();
            }
        }
    }
}
