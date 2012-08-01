/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package picoblazerules;

import java.util.HashMap;
import java.util.Map;
import org.junit.*;
import static org.junit.Assert.*;

/**
 *
 * @author 4m0ni4c
 */
public class RuleTest {
    
    public RuleTest() {
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
     * Test of getActions method, of class Rule.
     */
    @Test
    public void testGetActions() {
        System.out.println("getActions");
        Rule instance = new Rule("alert", "udp", "1.2.3.4", "42", "->", "5.6.7.8", "80", new HashMap<String, String>());
        String expResult = "alert";
        String result = instance.getActions();
        assertEquals(expResult, result);
    }


    /**
     * Test of getDestsIp method, of class Rule.
     */
    @Test
    public void testGetDestsIp() {
        System.out.println("getDestsIp");
        Rule instance = new Rule("alert", "udp", "1.2.3.4", "42", "->", "5.6.7.8", "80", new HashMap<String, String>());
        String expResult = "5.6.7.8";
        String result = instance.getDestsIp();
        assertEquals(expResult, result);
    }


    /**
     * Test of getDestsPort method, of class Rule.
     */
    @Test
    public void testGetDestsPort() {
        System.out.println("getDestsPort");
        Rule instance = new Rule("alert", "udp", "1.2.3.4", "42", "->", "5.6.7.8", "80", new HashMap<String, String>());
        String expResult = "80";
        String result = instance.getDestsPort();
        assertEquals(expResult, result);
    }

    /**
     * Test of getDirections method, of class Rule.
     */
    @Test
    public void testGetDirections() {
        System.out.println("getDirections");
        Rule instance = new Rule("alert", "udp", "1.2.3.4", "42", "->", "5.6.7.8", "80", new HashMap<String, String>());
        String expResult = "->";
        String result = instance.getDirections();
        assertEquals(expResult, result);
    }

    /**
     * Test of getProtocols method, of class Rule.
     */
    @Test
    public void testGetProtocols() {
        System.out.println("getProtocols");
        Rule instance = new Rule("alert", "udp", "1.2.3.4", "42", "->", "5.6.7.8", "80", new HashMap<String, String>());
        String expResult = "udp";
        String result = instance.getProtocols();
        assertEquals(expResult, result);
    }

    /**
     * Test of getSourcesIp method, of class Rule.
     */
    @Test
    public void testGetSourcesIp() {
        System.out.println("getSourcesIp");
        Rule instance = new Rule("alert", "udp", "1.2.3.4", "42", "->", "5.6.7.8", "80", new HashMap<String, String>());
        String expResult = "1.2.3.4";
        String result = instance.getSourcesIp();
        assertEquals(expResult, result);
    }

    /**
     * Test of getSourcesPort method, of class Rule.
     */
    @Test
    public void testGetSourcesPort() {
        System.out.println("getSourcesPort");
        Rule instance = new Rule("alert", "udp", "1.2.3.4", "42", "->", "5.6.7.8", "80", new HashMap<String, String>());
        String expResult = "42";
        String result = instance.getSourcesPort();
        assertEquals(expResult, result);
    }

    /**
     * Test of getProtocolNumber method, of class Rule.
     */
    @Test
    public void testGetProtocolNumber() {
        System.out.println("getProtocolNumber");
        Rule instance = new Rule("alert", "tcp", "1.2.3.4", "42", "->", "5.6.7.8", "80", new HashMap<String, String>());
        int expResult = 6;
        int result = instance.getProtocolNumber();
        assertEquals(expResult, result);
    }
}
