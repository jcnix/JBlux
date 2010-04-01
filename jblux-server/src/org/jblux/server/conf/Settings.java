/**
 * File: Settings.java
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

package org.jblux.server.conf;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Settings {
    private static final String dbConf = "db.conf";

    private static BufferedReader initReader() {
        BufferedReader reader = null;

        try {
            reader = new BufferedReader(new FileReader(dbConf));
        } catch(IOException ex) {
        }

        return reader;
    }

    private static String readConf(String opt) {
        String option = "";
        BufferedReader reader = initReader();

        try {
            String line = "";
            while((line = reader.readLine()) != null) {
                if(line.startsWith(opt)) {
                    String[] s = line.split("=");
                    option = s[1];
                    break;
                }
            }
        } catch(IOException ex) {
            return null;
        }

        return option;
    }

    public static String getDbServer() {
        String server = readConf("server");
        

        return server;
    }

    public static String getDbUser() {
        String user = readConf("user");

        return user;
    }

    public static String getDbPass() {
        String pass = readConf("password");
        
        return pass;
    }
}
