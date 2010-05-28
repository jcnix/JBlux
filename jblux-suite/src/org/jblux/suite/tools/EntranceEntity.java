/**
 * File: EntranceEntity.java
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
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;

public class EntranceEntity implements Entity {
    private Vector<Rectangle> m_rects;
    
    public EntranceEntity() {
        m_rects = new Vector<Rectangle>();
    }

    public void addTile(Rectangle r) {
        if(!m_rects.contains(r)) {
            m_rects.add(r);
        }
    }

    public Vector<Rectangle> getTiles() {
        return m_rects;
    }

    public void save() {
    }

    public Color getColor() {
        Rectangle r;
        return Color.yellow;
    }

    public void render(GameContainer gc, Graphics g) {
        for(int i = 0; i < m_rects.size(); i++) {
            Rectangle r = m_rects.get(i);
            g.fill(r);
        }
    }
}
