/**
 * File: ServerInfo.java
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

package org.jblux.common;

/* This file will not include DB info, since this file is included
 * in both the client and the server, and we don't want DB
 * passwords sitting in the client jar.
 */
public class ServerInfo {
    public static final String SERVER = "casey-jones.org";
    public static final String LOCAL_IP = "98.158.185.149";
    public static final int PORT = 4000;
}
