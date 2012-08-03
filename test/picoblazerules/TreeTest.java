/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package picoblazerules;

import java.util.ArrayList;
import org.junit.*;
import static org.junit.Assert.*;

/**
 *
 * @author 4m0ni4c
 */
public class TreeTest {
    
    public TreeTest() {
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
     * Test of print method, of class Tree.
     */
    @Test
    public void testPrint() {
        System.out.println("print");
        ArrayList<Rule> rules = new ArrayList<Rule>();
        Tree instance = new Tree(rules);
        instance.print();
    }

    /**
     * Test of getTable method, of class Tree.
     */
    @Test
    public void testGetTable() {
        System.out.println("getTable");
        ArrayList<Rule> rules = new ArrayList<Rule>();
        Tree instance = new Tree(rules);
        String expResult = "";
        String result = instance.getTable();
        assertEquals(expResult, result);
    }

    /**
     * Test of printBinaryTable method, of class Tree.
     */
    @Test
    public void testPrintBinaryTable() {
        System.out.println("printBinaryTable");
        ArrayList<Rule> rules = new ArrayList<Rule>();
        Tree instance = new Tree(rules);
        instance.printBinaryTable();
    }

    /**
     * Test of getFormattedTable method, of class Tree.
     */
    @Test
    public void testGetFormattedTable() {
        System.out.println("getFormattedTable");
        ArrayList<Rule> rules = new ArrayList<Rule>();
        Tree instance = new Tree(rules);
        String expResult = "";
        String result = instance.getFormattedTable();
        assertEquals(expResult, result);
    }

    /**
     * Test of getInstructionTable method, of class Tree.
     */
    @Test
    public void testGetInstructionTable() {
        System.out.println("getInstructionTable");
        ArrayList<Rule> rules = new ArrayList<Rule>();
        Tree instance = new Tree(rules);
        String expResult = "";
        String result = instance.getInstructionTable();
        assertEquals(expResult, result);
    }

    /**
     * Test of getVhdlTable method, of class Tree.
     */
    @Test
    public void testGetVhdlTable() {
        System.out.println("getVhdlTable");
        ArrayList<Rule> rules = new ArrayList<Rule>();
        Tree instance = new Tree(rules);
        String expResult = "\"";
        String result = instance.getVhdlTable();
        assertEquals(expResult, result);
    }

    /**
     * Test of getHexaFormattedTable method, of class Tree.
     */
    @Test
    public void testGetHexaFormattedTable() {
        System.out.println("getHexaFormattedTable");
        ArrayList<Rule> rules = new ArrayList<Rule>();
        Tree instance = new Tree(rules);
        String expResult = "";
        String result = instance.getHexaFormattedTable();
        assertEquals(expResult, result);
    }
}
