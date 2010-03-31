/**
 * File: Maps.java
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

package org.jblux.server;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import org.jblux.common.Relation;

/**
 * This class reads maps/layout.txt to find out the relationship
 * between maps.  So if the player moves off the left side of a \
 * map, they proceed to the correct map.
 */

public class Maps {
    private final String LAYOUT = "layout.txt";
    private BufferedReader reader;

    public Maps() { 
        try {
            reader = new BufferedReader(new FileReader(LAYOUT));
        } catch(IOException ex) {
        }
    }

    public String getMap(Relation rel, String current_map) {
        String name = "";
        
        try {
            int index;
            String last_line = "";
            String line = "";

            while ((line = reader.readLine()) != null) {
                if(line.contains(current_map)) {
                    String[] s = line.split("\\s");
                    index = getMapIndex(s, current_map);
                    name = getMap(rel, line, last_line, index);
                }
                else {
                    last_line = line;
                }
            }
        } catch (IOException ex) {
        }

        return name;
    }

    private int getMapIndex(String[] s, String current_map) {
        int index = -1;

        for(int i = 0; i < s.length; i++) {
            if(s[i].equals(current_map)) {
                index = i;
                break;
            }
        }

        return index;
    }

    // last_line is just a quick hack because Java can't read files backwards
    private String getMap(Relation rel, String line, String last_line, int index) {
        String name = "";
        
        if(rel == Relation.LEFT_OF) {
            String[] s = line.split("\\s");
            name = s[--index];
        }
        else if(rel == Relation.RIGHT_OF) {
            String[] s = line.split("\\s");
            name = s[++index];
        }
        else if(rel == Relation.ABOVE) {
            if(last_line == null) {
                return null;
            }

            String[] s = last_line.split("\\s");
            name = s[index];
        }
        else if(rel == Relation.BELOW) {
            try {
                //Read one line down
                String l = reader.readLine();

                if(l == null) {
                    return null;
                }

                String[] s = l.split("\\s");
                name = s[index];
            } catch (IOException ex) {
            }
        }

        return name;
    }
}
