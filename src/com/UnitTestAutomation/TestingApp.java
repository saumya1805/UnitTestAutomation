package com.UnitTestAutomation;

import java.awt.event.*;
import java.io.*;
import java.util.*;
import javax.swing.*;
import javax.swing.table.JTableHeader;
import java.util.HashMap.*;

import static javax.lang.model.SourceVersion.*;

public class TestingApp extends JFrame implements ActionListener {

    //Name of the class being tested
    public static String nameOfClassBeingTested;

    //Name of current function being parsed. Helps to associate the lines being read in the function body with the function
    public static String currFunction;

    //The absolute filepath taken as input from the user via the Java Swing GUI
    public static String inputFilePath;

    //Stores the public functions present within the class to be tested (These are the functions to be tested)
    public static Vector<String> functionList = new Vector<>();

    //Stores the external objects being referenced in a class which would be mocked
    public static Vector<String> externalObjectList = new Vector<>();

    //Stores the functions to be used in @Test
    public static Vector<String> functionsToBeTested=new Vector<>();

    public static Vector<String> autowiredObjectList=new Vector<>();

    //Hashmap storing information about the public functions being declared in a class, what external objects these functions use and what functions fo these external objects call
    public static HashMap<String, HashMap<String, List<String>>> functionData = new LinkedHashMap();

    //Flag for registering that previous file had an @Autowired

    public static int autowiredFlag=0;

    //Java Swing GUI Components
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

    //To display the hashmap created
    static JTable table;

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

        // add the components to the panel
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

    // to be executed when the button is pressed
    public void actionPerformed(ActionEvent e) {
        String s = e.getActionCommand();
        //Checks if button is pressed. String would be set to button value
        if (s.equals("Choose as filepath")) {
            // Sets the public variable filepath to the input that will be passed to the user in the textarea field
            inputFilePath = jt.getText();
            //Displays the file path chosen on the label field
            l.setText(inputFilePath);
            try {
                //calls the function that is responsible for reading code from the file whose path has been provided by user
                readUsingFileReader(inputFilePath);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    //Function that parses the code to be tested
    public static void readUsingFileReader(String filePath) throws IOException {

        File file = new File(filePath);
        FileReader fr = null;
        try {
            fr = new FileReader(file);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        //Reads the file line by line
        BufferedReader br = new BufferedReader(fr);
        //Stores the current line of code that was read
        String line;

        //Creates file that will store the unit test code corresponding to the code to be tested
        File file1 = new File("TestCodeTester.java");

        //To write into the unit test code file
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

            //This is where the actual parsing happens
            //Every line of code will be parsed to identify the tokens present in it


            //Line read was an import statement
            //Copy the import to the unit test file
            if (line.indexOf("import") == 0) {
                output.write(line);
                output.write("\n");

            }
            //Detects the class which is being tested
            else if (line.indexOf("public class") == 0) {
                //Importing the additional dependencies here (as to be imported only once)
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

                //Declares the public class which will hold the mocks and tests in the unit test code file
                output.write("public class TestCodeTester{\n\n");

                //@InjectMocks creates class instances which need to be tested in the test class
                output.write("@InjectMocks\n");

                //Logic to extract class name (Assumes starts with public class)
                int i = 13;
                String temp = "";
                while (i < line.length()) {
                    if (line.charAt(i) == '{' || line.charAt(i) == ' ') {
                        break;
                    }
                    temp += line.charAt(i);
                    i++;
                }
                nameOfClassBeingTested = temp;
                output.write(temp + " " + temp.toLowerCase() + ";\n\n");
            }

            //Public function has been detected in the line
            else if (line.contains("throws") && line.contains("public")) {

                //Logic to extract function name
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

                //To extract the parameters of the function
                int startIndex = line.indexOf('(') + 1;
                int endIndex = line.indexOf(')');

                if (startIndex != endIndex) {
                    List<String> parameters = Arrays.asList(line.substring(startIndex, endIndex).split("[ ,]"));
                    Vector<String> objectsReferencedInThisFunction = new Vector<>();
                    for (int i = 0; i < parameters.size(); i += 2) {
                        //External object detected
                        if (!isKeyword(parameters.get(i)) && parameters.get(i) != nameOfClassBeingTested) {
                            objectsReferencedInThisFunction.add(parameters.get(i + 1));
                            if (!externalObjectList.contains(parameters.get(i)))
                                externalObjectList.add(parameters.get(i));
                        }
                    }

                    //Updates the hashmap
                    for (int i = 0; i < objectsReferencedInThisFunction.size(); i++) {
                        List<String> temp1 = new ArrayList<>();
                        functionData.get(functionName).put(objectsReferencedInThisFunction.get(i), temp1);
                    }
                }
                currFunction = functionName;
                functionsToBeTested.add(functionName);
            }
            else if(line.contains("@Autowired")){
                autowiredFlag=1;
            }
            else if(autowiredFlag==1){
                String temp=line.substring(line.indexOf("private")+8,line.indexOf(";")); //Abc abc
                System.out.println(temp);
                String objName=temp.substring(temp.indexOf(" ")+1);
                autowiredObjectList.add(objName);
                autowiredFlag=0;
            }
            else {
                //Logic for extracting the functions that are being called by the external objects
                for (Map.Entry<String, HashMap<String, List<String>>> entry : functionData.entrySet()) {
                    if (entry.getKey() == currFunction) {
                        for (Map.Entry<String, List<String>> entry2 : entry.getValue().entrySet()) {
                            if (line.contains((String) entry2.getKey() + ".")) {
                                int startIndex = line.indexOf(".") + 1;
                                int endIndex = line.indexOf(")") + 1;
                                entry2.getValue().add(line.substring(startIndex, endIndex));
                                //To extract function without the parameter list for @Test
                                endIndex=line.indexOf("(")+1;
                                String functionWithoutParameters=line.substring(startIndex,endIndex)+")";
                                /*if(!functionsToBeTested.contains(functionWithoutParameters)){
                                    functionsToBeTested.add(functionWithoutParameters);
                                }*/
                            }
                        }
                        break;
                    }
                }
                for(int i=0;i<autowiredObjectList.size();i++){
                    if(line.contains(autowiredObjectList.get(i)+".")){
                        int startIndex = line.indexOf(".") + 1;
                        int endIndex = line.indexOf(")") + 1;
                        List<String> temp = new ArrayList<>();
                        functionData.get(currFunction).put(autowiredObjectList.get(i),temp);
                        functionData.get(currFunction).get(autowiredObjectList.get(i)).add(line.substring(startIndex,endIndex));

                        //To extract function without the parameter list for @Test
                        endIndex=line.indexOf("(")+1;
                        String functionWithoutParameters=line.substring(startIndex,endIndex)+")";

                        /*if(!functionsToBeTested.contains(functionWithoutParameters)){
                            functionsToBeTested.add(functionWithoutParameters);
                        }*/
                    }
                }
            }
        }
            //Generating @Mocks in the file
        for (int i = 0; i < externalObjectList.size(); i++) {
                output.write("@Mock\n");
                output.write(externalObjectList.get(i) + " mock" + externalObjectList.get(i) + ";\n\n");
        }

        for (int i = 0; i < autowiredObjectList.size(); i++) {
            output.write("@Mock\n");
            output.write(autowiredObjectList.get(i) + " mock" + autowiredObjectList.get(i) + ";\n\n");
        }

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

            output.write("@Before\n");
            output.write("public void beforeTest(){\n\n");
            output.write("}\n\n");

            for(int i=0;i<functionsToBeTested.size();i++){
                output.write("@Test\n");
                output.write("public void "+"test"+functionsToBeTested.get(i)+"{\n\n");
                output.write("}\n\n");
            }

            output.write("}");
            br.close();
            fr.close();
            output.close();
        }
    }