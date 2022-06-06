import java.awt.event.*;
import java.io.*;
import java.applet.*;
import java.util.*;
import java.math.*;
import com.UnitTestAutomation.ExternalClass1;
import com.UnitTestAutomation.ExternalClass2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
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

@Mock
recordUtils mockrecordUtils;

@Mock
recordService mockrecordService;

@Mock
labelService mocklabelService;

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
public void testgetRecordInfoById(){

}

@Test
public void testsaveRecord(){

}

@Test
public void testExternalClass1Function3(){

}

@Test
public void testgetEntityKeyPrefix(){

}

@Test
public void testgetLabel(){

}

}