/**
 * File: EntranceSideDialog.java
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

package org.jblux.suite.gui;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import org.jblux.common.Relation;

public class EntranceSideDialog extends JDialog implements ActionListener {
    private ButtonGroup m_btnGroup;
    private JRadioButton m_leftBtn;
    private JRadioButton m_rightBtn;
    private JRadioButton m_topBtn;
    private JRadioButton m_bottomBtn;
    private JButton m_okBtn;
    private JButton m_cancelBtn;
    private Relation m_relation;

    public EntranceSideDialog(JFrame parent) {
        super(parent, "Select a side", true);

        if (parent != null) {
          Dimension parentSize = parent.getSize();
          Point p = parent.getLocation();
          setLocation(p.x + parentSize.width / 4, p.y + parentSize.height / 4);
        }
    }

    public Relation showDialog() {
        initDialog();

        return m_relation;
    }

    public void initDialog() {
        m_btnGroup = new ButtonGroup();
        m_leftBtn = new JRadioButton("Left");
        m_rightBtn = new JRadioButton("Right");
        m_topBtn = new JRadioButton("Top");
        m_bottomBtn = new JRadioButton("Bottom");
        m_okBtn = new JButton("OK");
        m_cancelBtn = new JButton("Cancel");

        m_okBtn.addActionListener(this);
        m_cancelBtn.addActionListener(this);

        m_btnGroup.add(m_leftBtn);
        m_btnGroup.add(m_rightBtn);
        m_btnGroup.add(m_topBtn);
        m_btnGroup.add(m_bottomBtn);

        GridLayout gLayout = new GridLayout(2, 1);
        setLayout(gLayout);

        JPanel radioPanel = new JPanel();
        radioPanel.add(m_leftBtn);
        radioPanel.add(m_rightBtn);
        radioPanel.add(m_topBtn);
        radioPanel.add(m_bottomBtn);

        JPanel btnPanel = new JPanel();
        btnPanel.add(m_okBtn);
        btnPanel.add(m_cancelBtn);

        add(radioPanel);
        add(btnPanel);
        pack();
        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        Object obj = e.getSource();

        if(obj == m_okBtn) {
            if(m_leftBtn.isSelected())
                m_relation = Relation.LEFT;
            else if(m_rightBtn.isSelected())
                m_relation = Relation.RIGHT;
            else if(m_topBtn.isSelected())
                m_relation = Relation.ABOVE;
            else if(m_bottomBtn.isSelected())
                m_relation = Relation.BELOW;

            this.setVisible(false);
            this.dispose();
        }
        else if(obj == m_cancelBtn) {
            this.setVisible(false);
            this.dispose();
        }
    }
}
