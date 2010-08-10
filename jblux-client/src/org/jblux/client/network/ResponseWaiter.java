/**
 * File: ResponseObservable.java
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

import java.util.Observable;
import java.util.Observer;

public class ResponseWaiter extends Observable {
    public ResponseWaiter() {
        super();
    }

    public void responseReceived(Object o) {
        this.setChanged();
        this.notifyObservers(o);
    }

    /**
     * Factory method that takes an observer
     * Sometimes I forget to do rw.addObserver(), so this
     * should help me not have to remember.
     *
     * @param ob    Observer
     * @return      ResponseWaiter
     */
    public static ResponseWaiter get_new_waiter(Observer ob) {
        ResponseWaiter rw = new ResponseWaiter();
        rw.addObserver(ob);
        return rw;
    }
}
