/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package picoblazerules;

import org.junit.*;
import static org.junit.Assert.*;

/**
 *
 * @author 4m0ni4c
 */
public class PicoblazeRulesTest {
    
    public PicoblazeRulesTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of main method, of class PicoblazeRules.
     */
    @Test
    public void testMain() {
        System.out.println("main");
        String[] args = {"rules.txt"};
        PicoblazeRules.main(args);
    }
}
