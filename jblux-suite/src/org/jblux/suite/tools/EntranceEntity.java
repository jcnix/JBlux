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

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import org.jblux.common.Relation;
import org.jblux.sql.MapSqlTable;
import org.jblux.suite.gui.EntranceSideDialog;
import org.jblux.util.Coordinates;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;

public class EntranceEntity implements Entity {
    private HashMap<Coordinates, Rectangle> m_rects;
    private Relation side;
    private String m_map_name;
    
    public EntranceEntity() {
        m_rects = new HashMap<Coordinates, Rectangle>();
        m_map_name = "";
    }

    public void addTile(Rectangle r) {
        Coordinates c = new Coordinates();
        c.x = (int) r.getX();
        c.y = (int) r.getY();

        if(!m_rects.containsKey(c)) {
            m_rects.put(c, r);
        }
    }

    public boolean rmTile(Rectangle r) {
        Coordinates c = new Coordinates();
        c.x = (int) r.getX();
        c.y = (int) r.getY();
        Rectangle d = m_rects.remove(c);
        if(d != null)
            return true;
        else
            return false;
    }

    public void setMap(String map_name) {
        m_map_name = map_name;
    }

    public Collection<Rectangle> getTiles() {
        return m_rects.values();
    }

    public void save() {
        Relation r;
        if(side == null) {
            EntranceSideDialog esd = new EntranceSideDialog(null);
            r = esd.showDialog();
            setSide(r);
        }
        else {
            r = side;
        }
        
        MapSqlTable map_table = new MapSqlTable();
        Collection<Rectangle> coords = m_rects.values();
        Iterator i = coords.iterator();

        //Find min and max
        short min = 0;
        short max = 0;

        Rectangle rect = (Rectangle) i.next();
        if(r == Relation.LEFT || r == Relation.RIGHT)
            min = (short) rect.getY();
        else
            min = (short) rect.getX();
        max = min;

        while(i.hasNext()) {
            rect = (Rectangle) i.next();
            Coordinates c = new Coordinates();
            c.x = (int) rect.getX();
            c.y = (int) rect.getY();

            short coord = 0;
            if(r == Relation.LEFT || r == Relation.RIGHT)
                coord = (short) rect.getY();
            else
                coord = (short) rect.getX();

            if(coord < min)
                min = coord;
            if(coord > max)
                max = coord;
        }

        short id = map_table.getIdForName(m_map_name);
        if(r == Relation.LEFT)
            map_table.setEntrance_left(id, max, min);
        else if(r == Relation.RIGHT)
            map_table.setEntrance_right(id, max, min);
        else if(r == Relation.ABOVE)
            map_table.setEntrance_top(id, max, min);
        else if(r == Relation.BELOW)
            map_table.setEntrance_bottom(id, max, min);
    }

    private void setSide(Relation r) {
        side = r;
    }

    public Color getColor() {
        Rectangle r;
        return Color.yellow;
    }

    public void render(GameContainer gc, Graphics g) {
        g.setColor(getColor());
        Collection<Rectangle> vr = m_rects.values();
        Iterator i = vr.iterator();

        while(i.hasNext()) {
            Rectangle r = (Rectangle) i.next();
            g.fill(r);
        }
    }
}
