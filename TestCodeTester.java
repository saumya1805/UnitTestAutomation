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

when(recordUtils.getEntityKeyPrefix(1).thenReturn(key1);
when(recordUtils.getEntityKeyPrefix(2).thenReturn(key2);
}

@Test
public void testFunction1(ExternalClass1 obj1,ExternalClass2 obj2,int id){

when(obj1.ExternalClass1Function1(1)).thenReturn(1);
when(recordUtils.getRecordInfoById(1)).thenReturn(1);
when(recordService.saveRecord(1)).thenReturn(1);
Assert.assertEquals(TestCode.Function1(1),1);
}
@Test
public void testFunction2(ExternalClass1 obj1){

when(labelService.getLabel(1)).thenReturn(1);
Assert.assertEquals(TestCode.Function2(1),1);
}
}