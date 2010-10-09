/**
 * File: Inventory.java
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

package org.jblux.client.items;

import java.util.Vector;


public class Inventory {
    //Player's unequiped "Bag" items
    private Vector<Item> m_bag;

    //Equipped Items
    public Item head;
    public Item neck;
    public Item chest;
    public Item back;
    public Item waist;
    public Item hands;
    public Item finger1;
    public Item finger2;
    public Item trinket1;
    public Item trinket2;
    public Item feet;
    public Item main_hand;
    public Item off_hand;
    public Item ranged;

    public Inventory() {
    }

    public Inventory(Vector<Item> inv) {
        m_bag = inv;
    }

    public void addItem(Item item) {
        m_bag.add(item);
    }

    public void rmItem(Item item) {
        m_bag.remove(item);
    }
}
