package test.com.camera360.auto.ci;

import com.camera360.auto.ci.Main;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Main Tester.
 *
 * @author <Authors name>
 * @version 1.0
 * @since <pre>十二月 22, 2014</pre>
 */
public class MainTest {

    @Before
    public void before() throws Exception {
    }

    @After
    public void after() throws Exception {
    }

    /**
     * Method: main(String[] args)
     */
    @Test
    public void testMain() throws Exception {
//TODO: Test goes here...
        Main.main(new String[]{"-h"});

        Main.main(new String[]{"-t"});

        Main.main(new String[]{"-t", "ALL"});
    }

    /**
     * Method: compare(JenkinsResult o1, JenkinsResult o2)
     */
    @Test
    public void testCompareForO1O2() throws Exception {
//TODO: Test goes here... 
    }


    /**
     * Method: doTask(String[] args)
     */
    @Test
    public void testDoTask() throws Exception {
//TODO: Test goes here... 
/* 
try { 
   Method method = Main.getClass().getMethod("doTask", String[].class); 
   method.setAccessible(true); 
   method.invoke(<Object>, <Parameters>); 
} catch(NoSuchMethodException e) { 
} catch(IllegalAccessException e) { 
} catch(InvocationTargetException e) { 
} 
*/
    }

} 
