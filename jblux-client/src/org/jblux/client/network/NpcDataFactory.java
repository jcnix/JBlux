/**
 * File: NpcDataFactory.java
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

package org.jblux.client.network;

import com.google.gson.Gson;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.HashMap;
import org.jblux.common.client.NpcData;
import org.jblux.util.Base64;
import org.jblux.util.Coordinates;

public class NpcDataFactory {
    private NpcDataFactory() {
    }

    public static NpcData getDataFromBase64(String p) {
        NpcData data = null;
        Gson gson = new Gson();
        
        try {
            byte[] bytes = Base64.decode(p);
            String s = new String(bytes);
            data = gson.fromJson(s, NpcData.class);
        } catch(IOException ex) {
        }

        return data;
    }

    //TODO: Redo this.  Have the server send seperate Base64 strings for each element of the list
    public static HashMap<Coordinates, NpcData> getHashMapFromBase64(String p) {
        HashMap<Coordinates, NpcData> data = null;
        try {
            byte[] bytes = Base64.decode(p);
            ObjectInputStream is = new ObjectInputStream(new ByteArrayInputStream(bytes));
            data = (HashMap<Coordinates, NpcData>) is.readObject();
        } catch(IOException ex) {
        } catch(ClassNotFoundException ex) {
        }

        return data;
    }
}
