package com.UnitTestAutomation;

import java.awt.event.*;
import java.io.*;
import java.util.*;
import javax.swing.*;
import javax.swing.table.JTableHeader;
import java.util.HashMap.*;

import static javax.lang.model.SourceVersion.*;

public class TestingApp extends JFrame implements ActionListener {

    public static String currFunction;
    public static String inputFilePath;
    public static Vector<String> functionList = new Vector<>();

    public static Vector<String> externalObjectList = new Vector<>();

    public static String nameOfFunctionBeingTested;

    public static HashMap<String, HashMap<String, List<String>>> functionData = new LinkedHashMap();

    // JFrame
    static JFrame f;

    // JButton
    static JButton b;

    // label to display text
    static JLabel l;

    static JLabel l2;

    // text area
    static JTextArea jt;

    static JList li;

    static JList li1;

    static JTable table;

    public static JTableHeader header;

    // default constructor
    TestingApp() {
    }

    // main class
    public static void main(String[] args) throws IOException {
        // create a new frame to store text field and button
        f = new JFrame("Testing Application");

        // create a label to display text
        l = new JLabel("The file path chosen is:\n");

        l2 = new JLabel("The file path chosen is:\n");

        //l3=new JLabel("The external objects referenced by these functions are:\n");

        // create a new button
        b = new JButton("Choose as filepath");

        li = new JList(functionList);

        li1 = new JList(externalObjectList);

        table = new JTable(100, 3);
        table.getColumnModel().getColumn(0).setPreferredWidth(400);
        table.getColumnModel().getColumn(1).setPreferredWidth(400);
        table.getColumnModel().getColumn(2).setPreferredWidth(400);

        // create an object of the text class
        TestingApp te = new TestingApp();

        // addActionListener to button
        b.addActionListener(te);

        // create a text area, specifying the rows and columns
        jt = new JTextArea(25, 25);

        JPanel p = new JPanel();

        // add the text area and button to panel
        p.add(jt);
        p.add(b);
        p.add(l2);
        p.add(l);
        p.add(li);
        p.add(li1);
        p.add(table);

        f.add(p);
        // set the size of frame
        f.setSize(1300, 1000);

        f.show();
    }

    // if the button is pressed
    public void actionPerformed(ActionEvent e) {
        String s = e.getActionCommand();
        if (s.equals("Choose as filepath")) {
            // set the text of the label to the text of the field
            inputFilePath = jt.getText();
            l.setText(inputFilePath);
            try {
                readUsingFileReader(inputFilePath);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    public static void readUsingFileReader(String filePath) throws IOException {

        File file = new File(filePath);
        FileReader fr = null;
        try {
            fr = new FileReader(file);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        BufferedReader br = new BufferedReader(fr);
        String line;

        File file1 = new File("TestCodeTester.java");
        FileWriter output = new FileWriter("TestCodeTester.java");

        try {
            // create a new file with name specified
            // by the file object
            boolean value = file.createNewFile();
            if (value) {
                System.out.println("New Java File is created.");
            } else {
                System.out.println("The file already exists.");
            }
        } catch (Exception e) {
            e.getStackTrace();
        }

        while (true) {
            try {
                if (!((line = br.readLine()) != null)) break;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            // process the line
            System.out.println(line);

            if (line.indexOf("import") == 0) {
                output.write(line);
                output.write("\n");

            }

            if (line.indexOf("public class") == 0) {
                output.write("import static org.junit.Assert.assertEquals;\n");
                output.write("import static org.junit.Assert.assertFalse;\n");
                output.write("import static org.junit.Assert.assertNotNull;\n");
                output.write("import static org.junit.Assert.assertTrue;\n");
                output.write("import org.junit.Before;\n");
                output.write("import org.junit.Rule;\n");
                output.write("import org.junit.Test;\n");
                output.write("import org.junit.mockito2.*;\n");
                output.write("import static org.mockito2.Mockito.mock;\n");
                output.write("import static org.mockito2.Mockito.spy;\n");
                output.write("import static org.mockito2.Mockito.when;\n");
                output.write("import static org.mockito2.Mockito.doNothing;\n");
                output.write("\n");
                output.write("public class TestCodeTester{\n\n");
                output.write("@Inject Mocks\n");
                int i = 13;
                String temp = "";
                while (i < line.length()) {
                    if (line.charAt(i) == '{' || line.charAt(i) == ' ') {
                        break;
                    }
                    temp += line.charAt(i);
                    i++;
                }
                nameOfFunctionBeingTested = temp;
                output.write(temp + " " + temp.toLowerCase() + ";\n\n");
            }

            if (line.contains("throws") && line.contains("public")) {

                int index = line.indexOf("public") + 7;
                int flag = 0;

                while (flag != 1) {
                    if (line.charAt(index) == ' ') {
                        flag = 1;
                    }
                    index++;
                }

                String functionName = line.substring(index, line.indexOf(')') + 1);
                functionList.add(functionName);
                HashMap<String, List<String>> temp = new LinkedHashMap<>();
                functionData.put(functionName, temp);

                int startIndex = line.indexOf('(') + 1;
                int endIndex = line.indexOf(')');

                if (startIndex != endIndex) {
                    List<String> parameters = Arrays.asList(line.substring(startIndex, endIndex).split("[ ,]"));
                    Vector<String> objectsReferencedInThisFunction = new Vector<>();
                    for (int i = 0; i < parameters.size(); i += 2) {
                        if (!isKeyword(parameters.get(i)) && parameters.get(i) != nameOfFunctionBeingTested) {
                            objectsReferencedInThisFunction.add(parameters.get(i + 1));
                            if (!externalObjectList.contains(parameters.get(i)))
                                externalObjectList.add(parameters.get(i));
                        }
                    }

                    //System.out.println(objectsReferencedInThisFunction);

                    for (int i = 0; i < objectsReferencedInThisFunction.size(); i++) {
                        List<String> temp1 = new ArrayList<>();
                        functionData.get(functionName).put(objectsReferencedInThisFunction.get(i), temp1);
                    }
                }
                currFunction = functionName;
            } else {
                for (Map.Entry<String, HashMap<String, List<String>>> entry : functionData.entrySet()) {
                    if (entry.getKey() == currFunction) {
                        for (Map.Entry<String, List<String>> entry2 : entry.getValue().entrySet()) {
                            if (line.indexOf((String) entry2.getKey() + ".") != -1) {
                                int startIndex = line.indexOf(".") + 1;
                                int endIndex = line.indexOf(")") + 1;
                                entry2.getValue().add(line.substring(startIndex, endIndex));
                            }
                        }
                        break;
                    }
                }
            }
        }

            for (int i = 0; i < externalObjectList.size(); i++) {
                output.write("@Mock\n");
                output.write(externalObjectList.get(i) + " mock" + externalObjectList.get(i) + ";\n\n");
            }

            //li.setListData(functionList);

            table.setValueAt("Public functions being called", 0, 0);
            table.setValueAt("External objects being referenced", 0, 1);
            table.setValueAt("Functions being called by external objects", 0, 2);

            int row = 1;
            for (Map.Entry<String, HashMap<String, List<String>>> entry : functionData.entrySet()) {
                table.setValueAt(entry.getKey(), row, 0);
                int count = 0;
                for (Map.Entry<String, List<String>> entry2 : entry.getValue().entrySet()) {
                    table.setValueAt(entry2.getKey(), row + count, 1);
                    table.setValueAt(entry2.getValue(), row + count, 2);
                    count++;
                }
                row += count;
            }

            System.out.println(functionData);

            output.write("}");
            br.close();
            fr.close();
            output.close();
        }
    }