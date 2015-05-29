/*
 *  Copyright (C) 2013-2015 Karl R. Wurst
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

package com.w_sts.karl;

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
import java.awt.Font;

/**
 * Reads a list of students from a file and assigns random lab partners.
 * 
 * @author Karl R. Wurst
 */
public class RandomLabPartners extends JFrame implements ActionListener
{
    private ArrayList<String> studentList = new ArrayList<String>();
    private ArrayList<JCheckBox> checkBoxList = new ArrayList<JCheckBox>();
    private JTextArea outputArea = new JTextArea(20,50);

    public RandomLabPartners(String title) {
        super(title);
	studentList = readStudentsFromFile();
	setUpWindow(studentList);	
    }

    private ArrayList<String> readStudentsFromFile() {
	ArrayList<String> students = new ArrayList<String>();
	String filename = getNamesFileFromUser();
        
        FileReader file = null;
        try {
            file = new FileReader(filename);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
	    System.exit(-1);
        }

        Scanner scan = new Scanner(file);
	scan.nextLine();
	
        while (scan.hasNextLine()) {
	    String line = scan.nextLine();
	    String[] parts = line.split(",");
            students.add( parts[1].substring(1, parts[1].length()-1) + " " + parts[0].substring(1, parts[0].length()-1));
        }
	
	return students;
    }
    
    private String getNamesFileFromUser() {
	JFileChooser chooser = new JFileChooser();
        chooser.showOpenDialog(null);
        return chooser.getSelectedFile().getAbsolutePath();
    }

    private void setUpWindow(ArrayList<String> studentList) {
	Font font = new Font("Verdana", Font.BOLD, 16);
	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel panel = new JPanel();
        setContentPane(panel);
        panel.setLayout(new BorderLayout());

        JPanel boxPanel = new JPanel();
        boxPanel.setLayout(new GridLayout(0,1));
        for (String student:studentList) {
            JCheckBox box = new JCheckBox(student, true);
	    box.setFont(font);
            boxPanel.add(box);
            checkBoxList.add(box);
        }
        panel.add(boxPanel, BorderLayout.WEST);
        
        JButton button = new JButton("Assign Lab Partners");
        button.addActionListener(this);
        panel.add(button, BorderLayout.SOUTH);
        
	outputArea.setFont(font);
        panel.add(outputArea, BorderLayout.CENTER);

        pack();
        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        ArrayList<String> checkedStudents = new ArrayList<String>();
        String partners;

	checkedStudents = getCheckedStudents();
	partners = assignPartners(checkedStudents);
        outputArea.setText("Randomly assigned lab partners:\n");
	outputArea.append(partners);	
    }

    private ArrayList<String> getCheckedStudents() {
	ArrayList<String> current = new ArrayList<String>();
	
        for (int i = 0; i < studentList.size(); i++) {
            if (checkBoxList.get(i).isSelected()) {
                current.add(checkBoxList.get(i).getText());
            }
        }

	return current;
    }

    private String assignPartners(ArrayList<String> current) {
	String partners = "";

	Collections.shuffle(current);
	
        int i = 0;
        int count = 1;
        while (i < current.size()) {
            partners = partners + count + ". " + current.get(i) + " and " + current.get(i+1);
            if (i+2 == current.size()-1) {
                partners = partners + " and " + current.get(i+2);
                i++;
            }

            partners = partners + "\n";
            i = i + 2;
            count++;
        }

	return partners;
    }

    public static void main(String[] args) 
    {
        new RandomLabPartners("Lab Partner Assigner");
    }
}
