/**
 * File: MenuBar.java
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

package org.jblux.suite.gui.menubar;

import javax.swing.JMenuBar;
import org.jblux.suite.gui.GamePreview;
import org.jblux.suite.tabs.TabPane;

public class MenuBar extends JMenuBar {
    private TabPane m_pane;
    private GamePreview m_preview;

    public MenuBar(TabPane pane, GamePreview preview) {
        m_pane = pane;
        m_preview = preview;
        
        init();
    }

    private void init() {
        this.add(new FileMenu(m_preview));
        this.add(new EditMenu());
        this.add(new ToolsMenu(m_pane));
        this.add(new HelpMenu());
    }
}
