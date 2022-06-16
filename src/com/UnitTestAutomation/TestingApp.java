package com.UnitTestAutomation;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import javax.swing.*;
import java.util.List;
import static javax.lang.model.SourceVersion.*;

public class TestingApp extends JFrame implements ActionListener {

    public static boolean check = false;
    String s1;

    String s2;

    public static int numKeys;

    public static int beforeFlag = 0;

    FileWriter output = new FileWriter("TestCodeTester.java");

    //Name of the class being tested
    public static String nameOfClassBeingTested;

    //Name of current function being parsed. Helps to associate the lines being read in the function body with the function
    public static String currFunction;

    //The absolute filepath taken as input from the user via the Java Swing GUI
    public static String inputFilePath;

    //Stores the external objects being referenced in a class which would be mocked
    public static Vector<String> externalObjectList = new Vector<>();

    //Stores the functions to be used in @Test
    public static Vector<String> functionsToBeTested = new Vector<>();

    public static Vector<String> autowiredObjectList = new Vector<>();

    //Hashmap storing information about the public functions being declared in a class, what external objects these functions use and what functions fo these external objects call
    public static HashMap<String, HashMap<String, List<String>>> functionData = new LinkedHashMap();

    //Hashmap to store what dummy value will be returned for the mocked functions

    public static HashMap<String, Vector<String>> whenReturnThisFunctions = new LinkedHashMap();

    public static Vector<String> beforeData = new Vector<>();

    public static String t1;

    public static String t2;

    public static String t3;

    public static String t4;

    //Flag for registering that previous line had an @Autowired object
    public static int autowiredFlag = 0;

    //Java Swing GUI Components
    // JFrame
    static JFrame f;

    static JPanel p;

    // JButton
    static JButton b;

    static JButton before;

    static JButton test;

    // label to display text
    static JLabel l;

    static JLabel l1;

    static JLabel l2;

    static JLabel l3;

    //static JLabel functionName;

    // text area
    static JTextArea jt;

    static JTextArea objName;

    static JTextArea funcName;

    static JTextArea funcParameters;

    static JTextArea funcReturnVal;

    static JTextArea jtParameters;

    static JTextArea jtReturnVal;

    static JTextArea jtPara1;

    static JTextArea jtPara2;

    //static JList li;

    //static JList li1;

    //static JList list;

    //To display the hashmap created
    static JTable table;

    static JCheckBox checkbox;

    // default constructor
    public TestingApp() throws IOException {
    }

    // main class
    public static void main(String[] args) throws IOException {
        // create a new frame to store text field and button
        f = new JFrame("Testing Application");

        // create a new button
        b = new JButton("Choose as filepath");

        l = new JLabel("Function data extracted from the file:\n");

        table = new JTable(20, 3);
        table.getColumnModel().getColumn(0).setPreferredWidth(400);
        table.getColumnModel().getColumn(1).setPreferredWidth(400);
        table.getColumnModel().getColumn(2).setPreferredWidth(420);

        // create an object of the text class
        TestingApp te = new TestingApp();

        // addActionListener to button
        b.addActionListener(te);

        l1 = new JLabel("@Before generation:");

        // create a text area, specifying the rows and columns
        jt = new JTextArea("Enter the absolute file path here", 4, 8);

        objName = new JTextArea("Object name", 2, 6);
        funcName = new JTextArea("Function name", 2, 6);
        funcParameters = new JTextArea("Parameters name", 2, 6);
        funcReturnVal = new JTextArea("Return value", 2, 6);

        before = new JButton("Place in @Before");
        before.addActionListener(te);

        //functionName=new JLabel();
        l2 = new JLabel("@Test generation\n");

        l3 = new JLabel();

        jtParameters = new JTextArea("Parameters for mock function", 2, 6);
        jtReturnVal = new JTextArea("Return Value for mock function", 2, 6);
        jtPara1 = new JTextArea("Parameters for function under test", 2, 6);
        jtPara2 = new JTextArea("Expected output for function under test", 2, 6);

        test = new JButton("Place in @Test");
        test.addActionListener(te);

        p = new JPanel();

        // add the components to the panel
        p.add(jt);
        p.add(b);
        p.add(l);
        p.add(table);
        //p.add(functionName);
        p.add(l1);
        p.add(objName);
        p.add(funcName);
        p.add(funcParameters);
        p.add(funcReturnVal);
        p.add(before);
        p.add(l2);
        p.add(l3);
        p.add(jtParameters);
        p.add(jtReturnVal);
        p.add(jtPara1);
        p.add(jtPara2);
        p.add(test);

        f.add(p);
        // set the size of frame
        f.setSize(1300, 1000);

        f.show();
    }

    // to be executed when the button is pressed
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == b) {
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
        } else if (e.getSource() == before) {
            String s = e.getActionCommand();
            if (s.equals("Place in @Before")) {
                try {
                    generateBefore();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        } else if (e.getSource() == test) {
            String s = e.getActionCommand();
            if (s.equals("Place in @Test")) {
                try {
                    generateTest();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        }
    }

    //Function that parses the code to be tested
    public void readUsingFileReader(String filePath) throws IOException {

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
        //FileWriter output = new FileWriter("TestCodeTester.java");

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
            if (line.contains("import")) {
                //System.out.println("Hi in imp");
                output.write(line);
                output.write("\n");
            }
            //Detects the class which is being tested
            else if (line.contains("public class")) {
                //System.out.println("Hi in pc");
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
                int i = line.indexOf("public class") + 13;
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

                //System.out.println("Hi in throw");
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
                HashMap<String, List<String>> temp = new LinkedHashMap<>();
                functionData.put(functionName, temp);
                //System.out.println(functionName);

                //To extract the parameters of the function
                int startIndex = line.indexOf('(') + 1;
                int endIndex = line.indexOf(')');

                if (startIndex != endIndex) {
                    List<String> parameters = Arrays.asList(line.substring(startIndex, endIndex).split("[ ,]"));
                    //System.out.println(parameters);
                    Vector<String> objectsReferencedInThisFunction = new Vector<>();
                    for (int i = 0; i < parameters.size(); i += 2) {
                        if (!isKeyword(parameters.get(i)) && parameters.get(i) != nameOfClassBeingTested && !parameters.contains("String")){
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

            } else if (line.contains("@Autowired")) {
                //System.out.println("In autowire");
                autowiredFlag = 1;
            } else if (autowiredFlag == 1) {
                //System.out.println("In autowire body");
                String temp = line.substring(line.indexOf("private") + 8, line.indexOf(";")); //Abc abc
                String objName = temp.substring(temp.indexOf(" ") + 1);
                autowiredObjectList.add(objName);
                autowiredFlag = 0;
            } else {
                //Logic for extracting the functions that are being called by the external objects

                /*if(functionData.size()!=0){
                    for (Map.Entry<String, HashMap<String, List<String>>> entry : functionData.entrySet()) {
                        if (entry.getKey() == currFunction) {
                            for (Map.Entry<String, List<String>> entry2 : entry.getValue().entrySet()) {
                                if (line.contains((String) entry2.getKey() + ".")) {
                                    int startIndex = line.indexOf(".") + 1;
                                    int endIndex = line.indexOf("(") + 1;
                                    entry2.getValue().add(line.substring(startIndex, endIndex) + ")");

                                    line = line.trim();
                                    List<String> temp1 = Arrays.asList(line.split(" "));
                                    String startWord = temp1.get(0);
                                    //Not returning a value
                                    if (startWord.contains(".")) {
                                        continue;
                                    }

                                    if (whenReturnThisFunctions.containsKey(currFunction)) {
                                        whenReturnThisFunctions.get(currFunction).add(line.substring(line.indexOf((String) entry2.getKey()), line.indexOf("(") + 1));
                                    } else {
                                        Vector<String> temp2 = new Vector<>();
                                        temp2.add(line.substring(line.indexOf((String) entry2.getKey()), line.indexOf("(") + 1));
                                        whenReturnThisFunctions.put(currFunction, temp2);
                                    }
                                    break;
                                }
                            }
                        }
                    }
                }*/

                for (int i = 0; i < autowiredObjectList.size(); i++) {
                    if (line.contains(autowiredObjectList.get(i) + ".")) {
                        //int startIndex = line.indexOf(autowiredObjectList.get(i)) + autowiredObjectList.get(i).length();
                        int startIndex=line.indexOf(autowiredObjectList.get(i));
                        int endIndex=startIndex+1;
                        while(endIndex<line.length()){
                            if(line.charAt(endIndex)=='('){
                                endIndex++;
                                break;
                            }
                            endIndex++;
                        }

                        if(!functionData.get(currFunction).containsKey(autowiredObjectList.get(i))){
                            List<String> temp = new ArrayList<>();
                            temp.add(line.substring(startIndex,endIndex)+")");
                            functionData.get(currFunction).put(autowiredObjectList.get(i),temp);
                        }
                        else{
                            functionData.get(currFunction).get(autowiredObjectList.get(i)).add(line.substring(startIndex, endIndex) + ")");
                        }

                        line = line.trim();
                        List<String> temp1 = Arrays.asList(line.split(" "));
                        String startWord = temp1.get(0);
                        //Not returning a value
                        if (startWord.contains(".")) {
                          continue;
                        }
                        break;
                    }
                }
            }
                //System.out.println(whenReturnThisFunctions);
                numKeys=functionData.size();
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

            //System.out.println(functionData);

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
        output.write("MockitoAnnotations.initMocks(this)\n");
        br.close();
        fr.close();
}

        public void generateBefore() throws IOException {
            t1=objName.getText();
            t2=funcName.getText();
            t3=funcParameters.getText();
            t4=funcReturnVal.getText();

            beforeData.add(t1+"."+t2);
            System.out.println(beforeData);

            output.write("when("+t1+"."+t2+"("+t3+").thenReturn("+t4+");\n");
        }

    public void generateTest() throws IOException {

        if(beforeFlag==0){
            beforeFlag=1;
            output.write("}\n\n");
        }

        for(Map.Entry<String,HashMap<String,List<String>>> entry:functionData.entrySet()){
            s1=entry.getKey();
            for(Map.Entry<String,List<String>> entry2:entry.getValue().entrySet()) {
                s2=entry2.getKey();
                String testString = functionData.get(s1).get(s2).get(0);
                if (beforeData.contains(testString)) {
                    functionData.get(s1).get(s2).remove(0);
                    if (functionData.get(s1).get(s2).size() == 0) {
                        functionData.get(s1).remove(s2);
                    }
                    return;
                }
                break;
            }
            break;
        }

        if(numKeys>functionData.size() || !check){
            for(Map.Entry<String, HashMap<String,List<String>>> entry:functionData.entrySet()){
                s1=entry.getKey();
                output.write("@Test\n");
                output.write("public void " + "test" + s1 + "{\n\n");
                check=true;
                numKeys=functionData.size();
                break;
            }
        }

            t1=jtParameters.getText();
            t2=jtReturnVal.getText();
            t3=jtPara1.getText();
            t4=jtPara2.getText();

            //l3.setText(whenReturnThisFunctions.get(s1).get(0));
            String temp=functionData.get(s1).get(s2).get(0);
            output.write("when("+temp.substring(0,temp.indexOf("(")+1)+t1+")).thenReturn("+t2+");\n");

            if(functionData.get(s1).size()==1 && functionData.get(s1).get(s2).size()==1){
                output.write("Assert.assertEquals("+nameOfClassBeingTested+"."+s1.substring(0,s1.indexOf("("))+"("+t3+"),"+t4+");\n");
                output.write("}\n");
                functionData.remove(s1);
            }
            else if(functionData.get(s1).get(s2).size()==1){
                functionData.get(s1).remove(s2);
                if(functionData.get(s1).size()==0){
                    functionData.remove(s1);
                }
            }
            else{
                functionData.get(s1).get(s2).remove(0);
            }

            if(functionData.size()==0){
                output.write("}");
                output.close();
            }
    }
}