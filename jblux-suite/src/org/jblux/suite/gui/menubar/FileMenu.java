/**
 * File: FileMenu.java
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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.filechooser.FileFilter;
import org.jblux.suite.gui.GamePreview;

public class FileMenu extends JMenu implements ActionListener {
    private JMenuItem openItm;
    private JMenuItem exitItm;
    private GamePreview preview;
    private String m_openDir;

    public FileMenu(GamePreview preview) {
        super("File");

        m_openDir = System.getProperty("user.home");
        this.preview = preview;
        init();
    }

    private void init() {
        openItm = new JMenuItem("Open");
        exitItm = new JMenuItem("Exit");

        openItm.addActionListener(this);
        exitItm.addActionListener(this);

        this.add(openItm);
        this.addSeparator();
        this.add(exitItm);
    }

    public void actionPerformed(ActionEvent e) {
        Object action = e.getSource();

        if(action == exitItm) {
            System.exit(0);
        }
        if(action == openItm) {
            JFileChooser jfc = new JFileChooser(m_openDir);
            jfc.setFileFilter(new JBluxFileFilter());
            int returnVal = jfc.showOpenDialog(null);

            if(returnVal == JFileChooser.APPROVE_OPTION) {
                File file = jfc.getSelectedFile();
                String f = file.getAbsolutePath();
                m_openDir = f.substring(0, f.lastIndexOf('/'));
                preview.setMap(file.getAbsolutePath());
            }
        }
    }
}

class JBluxFileFilter extends FileFilter {
    public JBluxFileFilter() {
        super();
    }

    @Override
    public boolean accept(File f) {
        if(f.isDirectory()) {
            return true;
        }
        else if(f.getName().endsWith(".tmx")) {
            return true;
        }
        //TODO: Add else ifs for other files (Quests, items, etc)
        else {
            return false;
        }
    }

    @Override
    public String getDescription() {
        return "JBlux Files (.tmx)";
    }
}
