/**
 * File: EntranceTool.java
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

import org.jblux.suite.gui.GamePreview;
import org.jblux.util.Coordinates;
import org.newdawn.slick.geom.Rectangle;

public class Tool {
    protected Entity m_entity;
    protected GamePreview m_gp;

    public Tool() {
    }

    public void setEntity(Entity e) {
        m_entity = e;
    }

    public void draw(Coordinates coords) {
        Rectangle r = Grid.getTile(coords);
        m_entity.addTile(r);
    }

    public Entity getEntity() {
        return m_entity;
    }

    public void setGamePreview(GamePreview gp) {
        m_gp = gp;
    }
}
