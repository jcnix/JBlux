/**
 * File: TabCloseButton.java
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

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.plaf.basic.BasicButtonUI;

public class TabPane extends JTabbedPane {
    public TabPane() {
        super();
    }

    public void addTab(String name, JPanel tab) {
        add(name, tab);

        int i = indexOfTab(name);
        if(i != -1) {
            setTabComponentAt(i, new TabComponent(this, name));
        }
    }

    public void addTab_noClose(String name, JPanel tab) {
        add(name, tab);
    }
}

class TabComponent extends JPanel {
    public TabComponent(final JTabbedPane pane, String name) {
        setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
        setOpaque(false);

        JLabel label = new JLabel(name);
        add(label);
        //add more space between the label and the button
        label.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 5));
        TabCloseButton button = new TabCloseButton(pane, name);
        add(button);
        //add more space to the top of the component
        setBorder(BorderFactory.createEmptyBorder(2, 0, 0, 0));
    }
}

class TabCloseButton extends JButton implements ActionListener, MouseListener {
    private JTabbedPane m_pane;
    private String m_name;

    public TabCloseButton(final JTabbedPane pane, String name) {
        m_pane = pane;
        m_name = name;

        int size = 17;
        setPreferredSize(new Dimension(size, size));
        setUI(new BasicButtonUI());
        setContentAreaFilled(false);
        //No need to be focusable
        setFocusable(false);
        setBorder(BorderFactory.createEtchedBorder());
        setBorderPainted(false);

        //Mouse Listener is for rollover
        addMouseListener(this);
        addActionListener(this);
    }

    public void actionPerformed(ActionEvent e) {
        int i = m_pane.indexOfTab(m_name);
        if(i != -1) {
            m_pane.remove(i);
        }
    }

    //we don't want to update UI for this button
    @Override
    public void updateUI() {
    }

    //paint the X
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        //shift the image for pressed buttons
        if(getModel().isPressed()) {
            g2.translate(1, 1);
        }
        g2.setStroke(new BasicStroke(2));
        g2.setColor(Color.BLACK);
        if(getModel().isRollover()) {
            g2.setColor(Color.MAGENTA);
        }
        int delta = 6;
        g2.drawLine(delta, delta, getWidth() - delta - 1, getHeight() - delta - 1);
        g2.drawLine(getWidth() - delta - 1, delta, delta, getHeight() - delta - 1);
        g2.dispose();
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        Component component = e.getComponent();
        if (component instanceof AbstractButton) {
            AbstractButton button = (AbstractButton) component;
            button.setBorderPainted(true);
        }
    }

    @Override
    public void mouseExited(MouseEvent e) {
        Component component = e.getComponent();
        if (component instanceof AbstractButton) {
            AbstractButton button = (AbstractButton) component;
            button.setBorderPainted(false);
        }
    }

    public void mouseClicked(MouseEvent e) {
    }

    public void mousePressed(MouseEvent e) {
    }

    public void mouseReleased(MouseEvent e) {
    }
}
