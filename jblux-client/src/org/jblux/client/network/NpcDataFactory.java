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
import java.util.ArrayList;
import java.util.Arrays;
import org.jblux.common.client.NpcData;
import org.jblux.util.Base64;

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
    public static ArrayList<NpcData> getArrayFromBase64(String p) {
        ArrayList<NpcData> data = new ArrayList<NpcData>();
        Gson gson = new Gson();
        NpcList list = new NpcList();
        try {
            byte[] bytes = Base64.decode(p);
            String json = new String(bytes);
            list = gson.fromJson(json, NpcList.class);
        } catch(IOException ex) {
        }
        
        data.addAll(Arrays.asList(list.npcs));
        return data;
    }
}

class NpcList {
    public NpcData npcs[];
}
