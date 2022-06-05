package com.UnitTestAutomation;
import java.awt.event.*;
import java.io.*;
import java.applet.*;
import java.util.*;
import java.math.*;
import com.UnitTestAutomation.ExternalClass1;
import com.UnitTestAutomation.ExternalClass2;

public class TestCode{

    public void TestFunction1(ExternalClass1 obj1,ExternalClass2 obj2) throws IndexOutOfBoundsException{
        obj1.ExternalClass1Function1();
        obj1.ExternalClass1Function2();
        obj2.ExternalClass2Function2();
        return;
    }

    public String TestFunction2(ExternalClass1 obj1) throws IOException{
        obj1.ExternalClass1Function3();
        return "";
    }

    private void NonTestFunction() throws InstantiationError{
        return;
    }

    public static void main(String[] args){

        System.out.println("Hello");
    }

}
