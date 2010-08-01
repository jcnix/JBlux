/**
 * File: NPCData.java
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

package org.jblux.common.client;

import java.io.Serializable;

public class NpcData extends CharacterData implements Serializable {
    private final long serialVersionUID = 1L;

    public int npc_id;
    
    //Job will be things like Vendor or Enemy
    public int job;
    
    /* Custom sprite sheet.  If not set, use the
     * default race sprite sheet. */
    public String sprite_sheet;
}
