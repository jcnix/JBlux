/**
 * File: PlayerDataFactory.java
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
import java.io.IOException;
import org.jblux.common.client.PlayerData;
import org.jblux.util.Base64;

public class PlayerDataFactory {
    private PlayerDataFactory() {
    }

    public static PlayerData getDataFromBase64(String p) {
        PlayerData data = null;
        Gson gson = new Gson();

        try {
            String s = new String(Base64.decode(p));
            data = gson.fromJson(s, PlayerData.class);
        } catch(IOException ex) {
        }

        return data;
    }
}
