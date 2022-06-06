import java.awt.event.*;
import java.io.*;
import java.applet.*;
import java.util.*;
import java.math.*;
import com.UnitTestAutomation.ExternalClass1;
import com.UnitTestAutomation.ExternalClass2;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.mockito2.*;
import static org.mockito2.Mockito.mock;
import static org.mockito2.Mockito.spy;
import static org.mockito2.Mockito.when;
import static org.mockito2.Mockito.doNothing;

public class TestCodeTester{

@InjectMocks
TestCode testcode;

@Mock
ExternalClass1 mockExternalClass1;

@Mock
ExternalClass2 mockExternalClass2;

@Before
public void beforeTest(){

}

@Test
public void testExternalClass1Function1(){

}

@Test
public void testExternalClass1Function2(){

}

@Test
public void testExternalClass2Function2(){

}

@Test
public void testExternalClass1Function3(){

}

}