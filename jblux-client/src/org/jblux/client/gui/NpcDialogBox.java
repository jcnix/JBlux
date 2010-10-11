/**
 * File: DialogBox.java
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

package org.jblux.client.gui;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Observable;
import java.util.Observer;
import org.jblux.client.data.NpcData;
import org.jblux.client.data.Quest;
import org.jblux.client.network.NpcDataFactory;
import org.jblux.client.network.ServerCommunicator;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.gui.GUIContext;

public class NpcDialogBox implements Observer {
    private Image boxImage;
    private LinkedList<Quest> quests;
    private UnicodeFont ufont;
    private GUI gui;
    private Image closeButton;
    private Image acceptImage;
    private Image declineImage;

    private boolean select_quest;
    private boolean display_quest;
    private ArrayList<Rectangle> quest_boxes;
    private Rectangle acceptButton;
    private Rectangle declineButton;
    private Quest selected_quest;
    private ServerCommunicator server;

    private boolean wait_map_info;
    private ArrayList<NpcData> npc_data;

    public NpcDialogBox(GUI gui, ServerCommunicator s) {
        this.gui = gui;
        server = s;
        
        try {
            boxImage = new Image("img/dialogbox.png");
            closeButton = new Image("img/closebutton.png");
            acceptImage = new Image("img/accept.png");
            declineImage = new Image("img/decline.png");
        } catch(SlickException ex) {
        }

        ufont = FontFactory.getDefaultFont();
        selected_quest = null;
        select_quest = false;
        display_quest = false;

        acceptButton = new Rectangle(0,0,0,0);
        declineButton = new Rectangle(0,0,0,0);

        wait_map_info = false;
    }

    public void setQuests(LinkedList<Quest> quests) {
        this.quests = quests;
        select_quest = true;
        quest_boxes = new ArrayList<Rectangle>();

        int x = 260;
        int y = 125;
        for(int i = 0; i < quests.size(); i++) {
            Quest q = quests.get(i);
            String quest_name = q.name;
            Rectangle r = new Rectangle(x, y, ufont.getWidth(quest_name), ufont.getHeight(quest_name));
            quest_boxes.add(r);
            y += ufont.getHeight(quest_name) + 15;
        }
        //int width = ufont.getWidth(text);
        //text_lines = width / 300;
    }

    public void update(GUIContext gc) {
        Input input = gc.getInput();
        if(input.isMousePressed(Input.MOUSE_LEFT_BUTTON)) {
            int x = input.getMouseX();
            int y = input.getMouseY();

            if( (x >= 550 && x <= 550 + closeButton.getWidth()) &&
                (y >= 100 && y <= 100 + closeButton.getHeight()))
            {
                gui.closeDialogbox();
            }

            if(select_quest) {
                for(int i = 0; i < quest_boxes.size(); i++) {
                    Rectangle r = quest_boxes.get(i);
                    if(r.contains(x, y)) {
                        selected_quest = quests.get(i);
                        select_quest = false;
                        display_quest = true;

                        acceptButton = new Rectangle(250, 450, acceptImage.getWidth(),
                                acceptImage.getHeight());
                        declineButton = new Rectangle(450, 450, declineImage.getWidth(),
                                acceptImage.getHeight());
                        break;
                    }
                }
            }

            else if(display_quest) {
                if(acceptButton.contains(x, y)) {
                    server.acceptQuest(selected_quest);
                    wait_map_info = true;
                    gui.closeDialogbox();
                }
                else if(declineButton.contains(x, y)) {
                    select_quest = true;
                    display_quest = false;
                }
            }
        }
    }

    public void render() {
        boxImage.drawCentered(400, 300);
        closeButton.draw(550, 100);

        int x = 260;
        int y = 125;
        if(select_quest) {
            for(int i = 0; i < quests.size(); i++) {
                Quest q = quests.get(i);
                String quest_name = q.name;
                ufont.drawString(x, y, quest_name);
                y += ufont.getHeight(quest_name) + 15;
            }
        }
        else if(display_quest) {
            ufont.drawString(x, y, selected_quest.name);
            ufont.drawString(x, y+25, selected_quest.details);
            ufont.drawString(x, y+50, selected_quest.objectives);
            acceptImage.draw(250, 450);
            declineImage.draw(450, 450);
        }
    }

    public void update(Observable o, Object arg) {
        if(arg instanceof String && wait_map_info) {
            String sarg = (String) arg;
            if(sarg.startsWith("npcs")) {
                String[] args = sarg.split("\\s");
                npc_data = NpcDataFactory.getArrayFromBase64(args[1]);
            }
        }
    }
}
