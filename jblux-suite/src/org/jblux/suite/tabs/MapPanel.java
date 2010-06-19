/**
 * File: MapPanel.java
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

package org.jblux.suite.tabs;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import org.jblux.suite.gui.GamePreview;
import org.jblux.suite.tools.EntranceEntity;
import org.jblux.suite.tools.EraserTool;
import org.jblux.suite.tools.Tool;
import org.newdawn.slick.CanvasGameContainer;
import org.newdawn.slick.SlickException;

public class MapPanel extends JPanel implements ActionListener {
    private GamePreview m_preview;
    private JPanel toolPanel;
    private ButtonGroup m_btnGroup;
    private JToggleButton entranceBtn;
    private JToggleButton eraserBtn;
    private Tool active_tool;

    boolean m_entity_tool;
    boolean m_eraser_tool;

    public MapPanel(GamePreview preview) {
        m_preview = preview;
        m_entity_tool = false;
        init();
    }

    private void init() {
        GridBagLayout gb = new GridBagLayout();
        setLayout(gb);
        GridBagConstraints c = new GridBagConstraints();
        m_btnGroup = new ButtonGroup();

        try {
            c.gridx = 0;
            c.gridy = 0;
            CanvasGameContainer cgc = new CanvasGameContainer(m_preview);
            cgc.setSize(800, 600);
            add(cgc, c);
            cgc.start();
        } catch (SlickException ex) {
        }

        c.gridx = 0;
        c.gridy = 1;
        toolPanel = new JPanel();

        entranceBtn = new JToggleButton("Ent");
        eraserBtn = new JToggleButton("Eraser");

        entranceBtn.addActionListener(this);
        eraserBtn.addActionListener(this);

        m_btnGroup.add(entranceBtn);
        m_btnGroup.add(eraserBtn);

        toolPanel.add(entranceBtn);
        toolPanel.add(eraserBtn);
        add(toolPanel, c);
    }

    public void actionPerformed(ActionEvent e) {
        Object obj = e.getSource();

        if(obj == entranceBtn) {
            active_tool = new Tool();
            active_tool.setEntity(new EntranceEntity());
        }
        else if(obj == eraserBtn) {
            active_tool = new EraserTool();
        }
        else {
            active_tool = null;
        }

        active_tool.setGamePreview(m_preview);
        m_preview.setTool(active_tool);
    }
}
