/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package picoblazerules;

import java.util.HashMap;
import java.util.Map;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author 4m0ni4c
 */
public class NodeTest {
    
    public NodeTest() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of getEndAddress method, of class Node.
     */
    @Test
    public void testGetEndAddress() {
        System.out.println("getEndAddress");
        Node instance = new Node("test");
        Integer expResult = 0;
        Integer result = instance.getEndAddress();
        assertEquals(expResult, result);
    }

    /**
     * Test of setEndAddress method, of class Node.
     */
    @Test
    public void testSetEndAddress() {
        System.out.println("setEndAddress");
        Integer endAddress = 0;
        Node instance = new Node("test");
        instance.setEndAddress(endAddress);
    }

    /**
     * Test of getStartAdress method, of class Node.
     */
    @Test
    public void testGetStartAdress() {
        System.out.println("getStartAdress");
        Node instance = new Node("test");
        Integer expResult = 0;
        Integer result = instance.getStartAdress();
        assertEquals(expResult, result);
    }

    /**
     * Test of setStartAdress method, of class Node.
     */
    @Test
    public void testSetStartAdress() {
        System.out.println("setStartAdress");
        Integer startAdress = null;
        Node instance = new Node("test");
        instance.setStartAdress(startAdress);
    }

    /**
     * Test of addNextNode method, of class Node.
     */
    @Test
    public void testAddNextNode() {
        System.out.println("addNextNode");
        Node _node = new Node("add");
        Node instance = new Node("test");
        instance.addNextNode(_node);
    }

    /**
     * Test of getNextNode method, of class Node.
     */
    @Test
    public void testGetNextNode() {
        System.out.println("getNextNode");
        String _nextNodeName = "";
        Node instance = new Node("test");
        Node expResult = null;
        Node result = instance.getNextNode(_nextNodeName);
        assertEquals(expResult, result);
    }

    /**
     * Test of getId method, of class Node.
     */
    @Test
    public void testGetId() {
        System.out.println("getId");
        Node instance = new Node("test");
        Integer expResult = 0;
        Integer result = instance.getId();
        assertEquals(expResult, result);
    }

    /**
     * Test of setId method, of class Node.
     */
    @Test
    public void testSetId() {
        System.out.println("setId");
        Integer id = 0;
        Node instance = new Node("test");
        instance.setId(id);
    }

    /**
     * Test of getInDictionary method, of class Node.
     */
    @Test
    public void testGetInDictionary() {
        System.out.println("getInDictionary");
        Node instance = new Node("test");
        Boolean expResult = false;
        Boolean result = instance.getInDictionary();
        assertEquals(expResult, result);
    }

    /**
     * Test of setInDictionary method, of class Node.
     */
    @Test
    public void testSetInDictionary() {
        System.out.println("setInDictionary");
        Boolean inDictionary = true;
        Node instance = new Node("test");
        instance.setInDictionary(inDictionary);
    }

    /**
     * Test of getName method, of class Node.
     */
    @Test
    public void testGetName() {
        System.out.println("getName");
        Node instance = new Node("test");
        String expResult = "test";
        String result = instance.getName();
        assertEquals(expResult, result);
    }

    /**
     * Test of setName method, of class Node.
     */
    @Test
    public void testSetName() {
        System.out.println("setName");
        String name = "toto";
        Node instance = new Node("test");
        instance.setName(name);
    }

    /**
     * Test of getNext method, of class Node.
     */
    @Test
    public void testGetNext() {
        System.out.println("getNext");
        Map<String, Node> next = new HashMap<String, Node>();
        Node instance = new Node("test");
        Map expResult = next;
        Map result = instance.getNext();
        assertEquals(expResult, result);
    }

    /**
     * Test of setNext method, of class Node.
     */
    @Test
    public void testSetNext() {
        System.out.println("setNext");
        Map<String, Node> next = new HashMap<String, Node>();
        Node instance = new Node("test");
        instance.setNext(next);
    }

    /**
     * Test of getSuffix method, of class Node.
     */
    @Test
    public void testGetSuffix() {
        System.out.println("getSuffix");
        Node instance = new Node("test");
        Node expResult = null;
        Node result = instance.getSuffix();
        assertEquals(expResult, result);
    }

    /**
     * Test of setSuffix method, of class Node.
     */
    @Test
    public void testSetSuffix() {
        System.out.println("setSuffix");
        Node suffix = null;
        Node instance = new Node("test");
        instance.setSuffix(suffix);
    }
}
