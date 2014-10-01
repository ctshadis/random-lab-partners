/*
 *  Copyright (C) 2013-2014 Karl R. Wurst
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301, USA
 */

import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
import java.io.*;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JFrame;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JTextArea;
import javax.swing.JFileChooser;

/**
 * Reads a list of students from a file and assigns random lab partners.
 * 
 * @author Karl R. Wurst
 * @version 7 February 2014
 */
public class RandomLabPartners extends JFrame implements ActionListener
{
    private ArrayList<String> students = new ArrayList<String>();
    private ArrayList<JCheckBox> boxes = new ArrayList<JCheckBox>();
    private JTextArea output = new JTextArea(20,50);

    public RandomLabPartners(String title) {
        super(title);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel panel = new JPanel();
        setContentPane(panel);
        panel.setLayout(new BorderLayout());
        JPanel boxPanel = new JPanel();
        boxPanel.setLayout(new GridLayout(0,1));

        JFileChooser chooser = new JFileChooser();
        chooser.showOpenDialog(null);
        String filename = chooser.getSelectedFile().getName();
        
        FileReader file = null;
        try {
            file = new FileReader(filename);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        Scanner scan = new Scanner(file);
        while (scan.hasNext()) {
            students.add(scan.next() + " " + scan.next());
        }

        for (String student:students) {
            JCheckBox box = new JCheckBox(student, true);
            boxPanel.add(box);
            boxes.add(box);
        }
        panel.add(boxPanel, BorderLayout.WEST);
        
        JButton button = new JButton("Assign Lab Partners");
        button.addActionListener(this);
        panel.add(button, BorderLayout.SOUTH);
        
        panel.add(output, BorderLayout.CENTER);

        pack();
        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        String partners = null;
        
        //ArrayList<String> current = new ArrayList<String>(students);
        ArrayList<String> current = new ArrayList<String>();

        for (int i = 0; i < students.size(); i++) {
            if (boxes.get(i).isSelected()) {
                current.add(boxes.get(i).getText());
            }
        }
        //System.out.println(current);
        
        
        boolean OK = false;
        while (!OK) {
            Collections.shuffle(current);

            OK = true;
            int i = 0;
            while (i < current.size()) {
                partners = current.get(i) + " and " + current.get(i+1);
                if (i+2 == current.size()-1) {
                    partners = partners + " and " + current.get(i+2);
                    i++;
                }

                //System.out.println(partners);
                i = i + 2;
            }
        }

        int i = 0;
        int count = 1;
        output.setText("Randomly assigned lab partners:\n");
        while (i < current.size()) {
            partners = count + ". " + current.get(i) + " and " + current.get(i+1);
            if (i+2 == current.size()-1) {
                partners = partners + " and " + current.get(i+2);
                i++;
            }

            output.append(partners + "\n");
            i = i + 2;
            count++;
        }
    }

    public static void main(String[] args) 
    {
        new RandomLabPartners("Lab Partner Assigner");
    }
}
