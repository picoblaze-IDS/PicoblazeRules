/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package picoblazerules;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.*;
import static org.junit.Assert.*;

/**
 *
 * @author 4m0ni4c
 */
public class ParserTest {
    
    public ParserTest() {
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
     * Test of parseOption method, of class Parser.
     */
    @Test
    public void testParseOption() {
        System.out.println("parseOption");
        String options = "(content:\"his\";)";
        Parser instance = new Parser("rules.txt");
        Map expResult = new HashMap<String, String>();
        expResult.put("content", "his");
        Map result = instance.parseOption(options);
        assertEquals(expResult, result);
    }
}
